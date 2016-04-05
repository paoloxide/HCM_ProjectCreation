package common;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.ElementNotVisibleException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

import common.BaseTest;
import static util.ReportLogger.log;

/**
 * This class consolidate all commonly used methods
 * by the task managers.
 * 
 * 
 * @author jerrick.m.falogme
 */

public class TaskUtilities{
	
	protected static WebDriver driver = BaseTest.driver;
	
	public static boolean is_element_visible(String locator, String locType) {
		try {

			if (locType.equalsIgnoreCase("id")) {
				driver.findElement(By.id(locator)).isDisplayed();

			} else if (locType.equalsIgnoreCase("name")) {
				driver.findElement(By.name(locator)).isDisplayed();

			} else {
				driver.findElement(By.xpath(locator)).isDisplayed();
			}
			return true;
		}

		catch (NoSuchElementException e) {
			return false;
		}

	}
	
	public static void clickNavLink(By by){

		WebDriverWait wait = new WebDriverWait(driver, 30L);

		wait.until(ExpectedConditions.presenceOfElementLocated(by));
		driver.findElement(by).click();
		System.out.println("Career Development" + " link has been clicked...");
		log("Career Development" + " link has been clicked...");
	}
	
	public static boolean scrollDownToElement(boolean isScrollingDown, String scrollType) throws Exception{
		
		System.out.println("Initializing scroll down....");
		int scrollValue;
		
		switch(scrollType){
			case "small":
				scrollValue = 50;
				break;
			case "normal":
				scrollValue = 150;
				break;
			case "big":
				scrollValue = 400;
				break;
			default:
				scrollValue = 150;
		}
		
		System.out.println("Scroll is now moving....");	
		JavascriptExecutor js = (JavascriptExecutor)driver;
		boolean scrollDownAgain = (boolean) js.executeScript(
				
			"taskFolderArray=[];"+
			"taskFolderInt = -255;"+
			"queryFolderName = [];"+
			"oldScrollerValue = 0;"+
			"queryFolderName = document.querySelectorAll('div');"+

			"for(var i=0; i<queryFolderName.length;i++){"+
			"	curFolderId = queryFolderName[i].id;"+
			"	curFolderId1 = queryFolderName[i].style.overflow;"+
			"	curFolderId2 = queryFolderName[i].style.position;"+
			"	if(taskFolderInt < 0)taskFolderInt = -1;"+
			"	if((curFolderId1 === 'auto' && curFolderId2 === 'absolute') || curFolderId.contains('scroller')){"+
			"		taskFolderInt += 1;	"+
			"		taskFolderArray[taskFolderInt] = [curFolderId, curFolderId1, curFolderId2];"+
			"}}"+
      
			"for(var j =0; j<taskFolderArray.length;j++){"+
			"	  newScroller = document.getElementById(taskFolderArray[j][0]);"+
			"	  if(newScroller.scrollTop != undefined && newScroller != 'null'){"+
			"			if("+isScrollingDown+") {"+
			"				if(taskFolderArray[j][0].contains('scroller')){"+
			"					oldScrollerValue = newScroller.scrollTop;}"+
			
			"				newScroller.scrollTop += "+scrollValue+";}"+
			"			else if(!"+isScrollingDown+") newScroller.scrollTop = 0;"+
			"			if(oldScrollerValue == newScroller.scrollTop"+
			"				&& taskFolderArray[j][0].contains('scroller')"+
			"					&& oldScrollerValue > 0)"+
			"					return false;"+
			"	  }"+
			"}return true;"
		);
		//SLOW INTERNET CONNECTION might REQUIRE -- Higher Wait time: Recommended(5*2)
		driver.manage().timeouts().implicitlyWait(500, TimeUnit.MILLISECONDS);
		fluentWaitForElementInvisibility("//div[text()='Fetching Data...']", "Fetching Data...", 10);
		
		return scrollDownAgain;
	}
	
	public static void fluentWaitForElementInvisibility(String xPath, String textValue, int waitTime) throws Exception{
		
		Thread.sleep(250); //Momentary pause.....
		Wait<WebDriver> waitLoadHandler = new FluentWait<WebDriver>(driver)
				.withTimeout(waitTime, TimeUnit.SECONDS)
				.pollingEvery(500, TimeUnit.MILLISECONDS)
				.ignoring(NoSuchElementException.class)
				.ignoring(StaleElementReferenceException.class);
		
		waitLoadHandler.until(ExpectedConditions.invisibilityOfElementWithText(By.xpath(xPath), textValue));
		System.out.println("Page loading has been finished.....");
		log("Page loading has been finished.....");
	}
	
	public static void customWaitForElementVisibility(String elemPath, int waitTime, CustomRunnable runner) throws Exception{
		
		long startTime = System.currentTimeMillis();
		waitTime = waitTime * 1000;
		while(!is_element_visible(elemPath, "xpath")){
			try{
					runner.customRun();
				} catch(StaleElementReferenceException e){
					//Skips...
				}
			
			if(System.currentTimeMillis() - startTime > waitTime){
				log(waitTime + " second/s has elapsed after waiting for: "+elemPath+"\nNow throwing error.....\n");
				throw new TimeoutException(waitTime/1000 + " second/s has elapsed after waiting for: "+elemPath);
			}
			
		}		
		System.out.println("Element is now visible after "+(System.currentTimeMillis() - startTime)/1000+" second/s.....");
		log("Element is now visible after "+(System.currentTimeMillis() - startTime)/1000+" second/s.....");
	}
	
	public static void customWaitForElementVisibility(String elemPath, int waitTime) throws Exception{
		
		long startTime = System.currentTimeMillis();
		waitTime = waitTime * 1000;
		
		while(!is_element_visible(elemPath, "xpath")){
			//Just wait here...
			if(System.currentTimeMillis() - startTime > waitTime){
				log(waitTime/1000 + " second/s has elapsed after waiting for: "+elemPath+"\nNow throwing error.....\n");
				throw new TimeoutException(waitTime/1000 + " second/s has elapsed after waiting for: "+elemPath);
			}
			
		}
		System.out.println("Element is now visible after "+(System.currentTimeMillis() - startTime)/1000+" second/s.....");
		log("Element is now visible after "+(System.currentTimeMillis() - startTime)/1000+" second/s.....");
	}
	
	/**
	 * A 3-in-1 customized method
	 * that finds, clicks, clears
	 * and inputs data on a given inputbox. 
	 * 
	 * @author jerrick.m.falogme
	 */
	public static void consolidatedInputEncoder(BasePage bpInstance, String labelLocatorPath, String dataLocator) throws Exception{
		jsScrollIntoView(labelLocatorPath);
		retryingFindClick(By.xpath(labelLocatorPath));
		//takeScreenshot();
		driver.findElement(By.xpath(labelLocatorPath)).clear();
		bpInstance.enterTextByXpath(labelLocatorPath, dataLocator);
	}
	
	/**
	 * A 3-in-1 customized method
	 * that finds, clicks
	 * and selects option on a given select box. 
	 * 
	 * @author jerrick.m.falogme
	 */
	public static void consolidatedInputSelector(String labelLocatorPath, String dataLocator) throws Exception{
		
		jsScrollIntoView(labelLocatorPath);
		String dataLocatorPath = labelLocatorPath+"/option[text()='"+dataLocator+"']";
		retryingFindClick(By.xpath(labelLocatorPath));
		if(!dataLocator.isEmpty() && !dataLocator.contentEquals("")){
				customWaitForElementVisibility(dataLocatorPath, 30);
				retryingFindClick(By.xpath(dataLocatorPath));
			} else{
				//Skips the sequence...
			}
	}

	//Attempt type functions....
	public static String retryingSearchInput(String dataLocator) throws Exception{

        int attempts = 0;
        String[] inputTypesArray = {
        		"//td/label[text()='"+dataLocator+"']/../../td/input",
        		"//td/label[text()='"+dataLocator+"']/../../td/span/input",
        		"//td/label[text()='"+dataLocator+"']/../../td/span/span/input",
        		"//td/label[text()='"+dataLocator+"']/../../td/select",
        		"//td/label[text()='"+dataLocator+"']/../../td/table/tbody/tr/td/table/tbody/tr/td/span/input"
        };
        
        System.out.println("Attempting to find known valid path for dataLocator: "+dataLocator);
        while(attempts < inputTypesArray.length) {
            try {
	                driver.findElement(By.xpath(inputTypesArray[attempts])).click();
	                System.out.println("Valid Input path has been found"+" after "+attempts+" tries...");
	                System.out.println("Assigned path: \n"+inputTypesArray[attempts]);
	                return inputTypesArray[attempts];
	                
	            } catch(StaleElementReferenceException e) {
	            
	            } catch(NoSuchElementException e){
	            
	            } catch(ElementNotVisibleException e){
	            
	            } catch(Exception e){
	            	
	            }
            attempts++;
        }
        System.out.println("No valid path can be assigned after "+attempts+" tries...");
        return null;
	}
	
	public static void retryingFindClick(By by) throws Exception{

        int attempts = 0;
        boolean scrollDown = true;
        
        System.out.println("Attempting to catch element.....");
        while(attempts < 11) {
            try {
                driver.findElement(by).click();
                System.out.println("Element has been refreshed after "+attempts+" tries.....");
                return ;
            } catch(StaleElementReferenceException e) {
            
            } catch(NoSuchElementException e){
            	scrollDown = scrollDownToElement(scrollDown, "");
            
            } catch(ElementNotVisibleException e){
            	scrollDown = scrollDownToElement(scrollDown, "");
            
            }
            attempts++;
        }
        
        System.out.println("Failed to find path: "+by+"");
        System.out.println("Throwing Error.....");
        log("Failed to find path: "+by+"");
        log("Throwing Error.....");
        throw new StaleElementReferenceException("The Element cannot be clicked...\n");
	}
	
	//JS functions
	public static void jsCheckMessageContainer() throws Exception{
		int attempts = 0;
		String container = "dummy";
		String errMsg = "";
		
		JavascriptExecutor js = (JavascriptExecutor)driver;

		System.out.println("Verifying container value.....");	
		while(attempts < 5 && (!container.isEmpty() || !container.contentEquals(""))){
			container = (String)js.executeScript(
			"var msgContainer = document.getElementById('d1::msgCtr');"+
			" return msgContainer.innerHTML;"
			);
		
		attempts += 1;
		Thread.sleep(100);
		}
		System.out.println("Last container value: '"+container+"'\nafter "+attempts+" tries.");
		String tempMsg = driver.findElement(By.id("d1::msgDlg")).getText();
		
		if((container.isEmpty() || container.contentEquals("")) && tempMsg.contains("Error")){
			errMsg = driver.findElement(By.id("d1::msgDlg")).getText().replaceAll("OK", "").replace("Error", "");
			log(errMsg);
			throw new DuplicateNameEntryException("Error FOUND: \n"+errMsg);
		}
		
	}
	
	public static void jsCheckMissedInput() throws Exception{
		int attempts = 0;
		String container = "dummy";
		String errMsg = "";
		String msgPath = "//td[contains(@class,'NoteWindow') and not (contains(@class,'Border'))]";
		
		JavascriptExecutor js = (JavascriptExecutor)driver;

		System.out.println("Verifying container value.....");	
		while(attempts < 5 && (!container.isEmpty() || !container.contentEquals(""))){
			container = (String)js.executeScript(

			"function getElementByXPath(xPath){"+
			"	return document.evaluate(xPath, document, null, XPathResult.FIRST_ORDERED_NODE_TYPE, null).singleNodeValue;"+
			"}"+
			"var msgContainer = getElementByXPath(\""+msgPath+"\");"+
			"if(msgContainer != null)"+		
			"{return msgContainer.textContent;}"+
			"else{return '';}"
			);
		
		attempts += 1;
		Thread.sleep(100);
		}
		System.out.println("Last container value: '"+container+"'\nafter "+attempts+" tries.");
		
		if(container.contains("Error")){
			errMsg = driver.findElement(By.xpath(msgPath)).getText().replaceAll("OK", "");
			log(errMsg);
			throw new DuplicateNameEntryException("Error FOUND: \n"+errMsg);
		}
		
	}
	
	public static void jsFindThenClick(String dataPath){
		JavascriptExecutor js = (JavascriptExecutor)driver;
		js.executeScript(
			"function getElementByXPath(xPath){"+
					"	return document.evaluate(xPath, document, null, XPathResult.FIRST_ORDERED_NODE_TYPE, null).singleNodeValue;"+
					"}"+
			"getElementByXPath(\""+dataPath+"\").click();"
		);
	}

	public static void jsScrollIntoView(String dataPath){
		JavascriptExecutor js = (JavascriptExecutor)driver;
		js.executeScript(
			"function getElementByXPath(xPath){"+
					"	return document.evaluate(xPath, document, null, XPathResult.FIRST_ORDERED_NODE_TYPE, null).singleNodeValue;"+
					"}"+
			"getElementByXPath(\""+dataPath+"\").scrollIntoView(true);"
		);
	}

	public static boolean jsSideScroll(boolean isScrollingLeft){
		
		int scrollValue = 50;
		boolean scrollLeftAgain = true;
		String scrollerPath = "//div[contains(@id,'scroller')]";
		List<WebElement> scrollers = driver.findElements(By.xpath(scrollerPath));
		String sID;
		
		JavascriptExecutor js = (JavascriptExecutor)driver;
		for(WebElement scroller: scrollers){
			sID = scroller.getAttribute("id");
			
			scrollLeftAgain = (boolean)js.executeScript(
				"scrollMove = document.getElementById(\""+sID+"\").scrollLeft;"+
				"oldScrollValue = scrollMove;"+
				"if(scrollMove != null || scrollMove != undefined){"+
				"	if("+isScrollingLeft+"){"+
				"		scrollMove += "+scrollValue+";"+
				"		newScrollValue = scrollMove;"+
				"	} else if(!"+isScrollingLeft+"){ scrollMove = 0; }"+
				
				"	if(oldScrollValue == newScrollValue){"+
				"		return false;}"+
				"}"+
				"return true;"
			);
		}
		
		return scrollLeftAgain;
	}
	
	//Input Box Utilities...
	public static String getdataLocatorType(String dataLocator){
		String type = "";
		
		if(dataLocator.contains("Date")){
			type = "date";
		}else if(dataLocator.contains("Time")){
			type = "time";
		}else{
			type = "text";
		}
		
		return type;
	}

	public static String getProjectName(){
		System.out.println("Obtaining project name...");
		String rawText = "";
		
        Map<String, String> env = System.getenv();
        for (String envName : env.keySet()) {
        	if(envName.contentEquals("HCM_PROJECT")){//PROJECT_NAME
                rawText = env.get(envName);
        	}
        }

        System.out.println(rawText);
        return rawText;
	}
	
	public static void setProjectName(String projectName, String ExcelFilePath, int inputRow) throws Exception{
		ExcelUtilities.setCellData(projectName, inputRow, BaseTest.defaultProjectNameValue, ExcelFilePath);
		System.out.println("Project Name has been updated.");
	}
}
