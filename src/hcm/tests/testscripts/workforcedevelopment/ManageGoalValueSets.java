package hcm.tests.testscripts.workforcedevelopment;

import static util.ReportLogger.logFailure;

import java.util.List;

import org.openqa.selenium.By;
import org.testng.annotations.Test;

import common.BaseTest;
import common.CustomRunnable;
import common.TaskUtilities;
import hcm.pageobjects.FuseWelcomePage;
import hcm.pageobjects.LoginPage;
import hcm.pageobjects.TaskListManagerTopPage;

public class ManageGoalValueSets extends BaseTest{
	private final static int MAX_TIME_OUT = 60; //in seconds....
	
	@Test
	public void a_test() throws Exception  {
		testReportFormat();
	
	try{
		manageGoalValueSets();
	  
	  	}
	
        catch (AssertionError ae)
        {
            //takeScreenshot();
            logFailure(ae.getMessage());

            throw ae;
        }
        catch (Exception e)
        {
            //takeScreenshot();
            logFailure(e.getMessage());

            throw e;
        }
    }

	public void manageGoalValueSets() throws Exception{
		
		LoginPage login = new LoginPage(driver);
		//takeScreenshot();
		login.enterUserID(5);
		login.enterPassword(6);
		login.clickSignInButton();
		
		FuseWelcomePage welcome = new FuseWelcomePage(driver);
		//takeScreenshot();
		welcome.clickNavigator("More...");
		clickNavigationLink("Setup and Maintenance");
		
		TaskListManagerTopPage task = new TaskListManagerTopPage(driver);
		//takeScreenshot();
		
		task.clickTask("Manage Implementation Projects");
		Thread.sleep(1000);
		
		int inputs = 2;
		int inputLabel = 1;
		int curTestData = 7;
		String labelLocator ="", labelLocatorPath="";
		String dataLocator, dataPath, rawDataLocPath, dataLocatorPath, gotoTaskPath, inputValue, type;
		List<String> tableInputs = null;
		
		task.waitForElementToBeClickable(MAX_TIME_OUT, "//button[text()='D']");
		Thread.sleep(2000);
		
		//Search Project Name
		String searchName = "Name";
		String searchData = getExcelData(inputs, curTestData, "text");
		System.out.println("Now Holding: "+searchData);
		String searchDataLink = "//a[text()='"+searchData+"']";
		String dataSearchPath = TaskUtilities.retryingSearchInput(searchName);
		TaskUtilities.jsFindThenClick(dataSearchPath);
		System.out.println("New search path: "+dataSearchPath);
		task.enterTextByXpath(dataSearchPath, searchData);
				
		task.waitForElementToBeClickable(MAX_TIME_OUT, "//button[text()='Search']");
		task.clickSearchButton();
		Thread.sleep(250);
		task.clickSearchButton();
		//takeScreenshot();
		
		TaskUtilities.customWaitForElementVisibility(searchDataLink, MAX_TIME_OUT);
		TaskUtilities.retryingFindClick(By.xpath("//h2[text()='Search Results']"));
		Thread.sleep(1000);
		System.out.println("Now Clicking: "+searchDataLink);
		driver.findElement(By.xpath(searchDataLink+"/..")).click();
		TaskUtilities.retryingFindClick(By.linkText(searchData));
		Thread.sleep(1000);
		//END Search for Project Name
		
		//Move cursor to folder name.....//START OF FOLDER EXPANSION
		curTestData += 1; //Expand Task Folder
		dataLocator = getExcelData(inputs, curTestData, "text");
		dataPath = "//div[text()='"+dataLocator+"']";
		TaskUtilities.customWaitForElementVisibility(dataPath, MAX_TIME_OUT);
		if(is_element_visible(dataPath+"/span/a[contains(@title,'Expand')]","xpath")){
			
			TaskUtilities.retryingFindClick(By.xpath(dataPath));	
			task.clickExpandFolder(dataPath);
			Thread.sleep(1000);
			
		}
		
		curTestData += 1; //Expand Task Folder
		dataLocator = getExcelData(inputs, curTestData, "text");
		dataPath = "//div[text()='"+dataLocator+"']";
		TaskUtilities.customWaitForElementVisibility(dataPath, MAX_TIME_OUT);
		if(is_element_visible(dataPath+"/span/a[contains(@title,'Expand')]","xpath")){
			
			TaskUtilities.retryingFindClick(By.xpath(dataPath));	
			task.clickExpandFolder(dataPath);
			Thread.sleep(1000);
			
		}
		//END OF FOLDER EXPANSION
		
		//Moving the cursor.....
		curTestData += 1;
		dataLocator = getExcelData(inputs, curTestData, "text");
		dataPath = "//div[text()='"+dataLocator+"']";
		gotoTaskPath = dataPath+"/../../td/a[@title='Go to Task']";
		TaskUtilities.customWaitForElementVisibility(gotoTaskPath, MAX_TIME_OUT);
		System.out.println("Now clicking Go to Task....."+ gotoTaskPath);
		TaskUtilities.retryingFindClick(By.xpath(gotoTaskPath));
		
		TaskUtilities.customWaitForElementVisibility("//h1[text()='"+dataLocator+"']", MAX_TIME_OUT);
		Thread.sleep(1000);
		takeScreenshot();
		
		//This is for L4 tasks;;
		curTestData += 1;
		
		//Check if action is ADD/DELETE.....
		curTestData += 1;
		dataLocator = getExcelData(inputs, curTestData, "text");
		
		if(dataLocator.contentEquals("ADD")){
				
				TaskUtilities.jsFindThenClick("//a/img[@title='Create']");
				TaskUtilities.customWaitForElementVisibility("//h1[text()='Create Value Set']", MAX_TIME_OUT);
				Thread.sleep(250);
			
				curTestData += 1;//Input all data...
				while(!labelLocator.contentEquals("Value") && getExcelData(inputLabel, curTestData, "text").length()>0){
					labelLocator = getExcelData(inputLabel, curTestData, "text");
					type = TaskUtilities.getdataLocatorType(labelLocator);
					labelLocatorPath = TaskUtilities.retryingSearchInput(labelLocator);
					dataLocator = getExcelData(inputs, curTestData, type);
					
					TaskUtilities.customWaitForElementVisibility(labelLocatorPath, MAX_TIME_OUT);
					
					if(!labelLocatorPath.contains("select")){
						
							TaskUtilities.consolidatedInputEncoder(task, labelLocatorPath, dataLocator);
							//TaskUtilities.jsScrollIntoView(labelLocatorPath);
							//TaskUtilities.retryingFindClick(By.xpath(labelLocatorPath));
							takeScreenshot();
							//driver.findElement(By.xpath(labelLocatorPath)).clear();
							//task.enterTextByXpath(labelLocatorPath, dataLocator);
						} else if(labelLocatorPath.contains("select")){
							
							TaskUtilities.consolidatedInputSelector(labelLocatorPath, dataLocator);
						}
					
					curTestData += 1;
				}
				
				TaskUtilities.customWaitForElementVisibility("//button[text()='Save']", MAX_TIME_OUT);
				Thread.sleep(250);
				TaskUtilities.jsFindThenClick("//button[text()='Save']");
				Thread.sleep(250);
				TaskUtilities.customWaitForElementVisibility("//button[text()='Manage Values']", MAX_TIME_OUT, new CustomRunnable() {
					
					@Override
					public void customRun() throws Exception {
						// TODO Auto-generated method stub
						TaskUtilities.jsCheckMessageContainer();
						TaskUtilities.jsCheckMissedInput();
					}
				});
				
				TaskUtilities.jsFindThenClick("//button[text()='Manage Values']");
				TaskUtilities.customWaitForElementVisibility("//h1[text()='Manage Values']", MAX_TIME_OUT);
				Thread.sleep(250);
				TaskUtilities.jsFindThenClick("//a/img[@title='Create']");
				TaskUtilities.customWaitForElementVisibility("//h1[text()='Create Value']", MAX_TIME_OUT);
				Thread.sleep(250);
				
				while(getExcelData(inputLabel, curTestData, "text").length()>0){
					
					if(!labelLocatorPath.contains("select")){
							
							TaskUtilities.consolidatedInputEncoder(task, labelLocatorPath, dataLocator);
							takeScreenshot();
						} else if(labelLocatorPath.contains("select")){
							
							TaskUtilities.consolidatedInputSelector(labelLocatorPath, dataLocator);
							takeScreenshot();
						}
					
					curTestData += 1;
				}
				Thread.sleep(250);
				TaskUtilities.jsFindThenClick("//button[text()='ave and Close']");
				TaskUtilities.customWaitForElementVisibility("//button[text()='Manage Values']", MAX_TIME_OUT, new CustomRunnable() {
					
					@Override
					public void customRun() throws Exception {
						// TODO Auto-generated method stub
						TaskUtilities.jsCheckMessageContainer();
						TaskUtilities.jsCheckMissedInput();
					}
				});
				
				TaskUtilities.customWaitForElementVisibility("//button[text()='D']", MAX_TIME_OUT);
				TaskUtilities.jsFindThenClick("//button[text()='D']");	
				TaskUtilities.customWaitForElementVisibility("//h1[text()='Manage Goal Value Set']", MAX_TIME_OUT);
				Thread.sleep(250);
				TaskUtilities.jsFindThenClick("//button[text()='ave and Close']");
			
			} else if(dataLocator.contentEquals("DEL")){
				
			}
		
		
		
	}
}
