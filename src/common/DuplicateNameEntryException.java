package common;

/**
 * A customized Exception which is
 * thrown when input related errors
 * occurs.
 * 
 * @author jerrick.m.falogme
 */
public class DuplicateNameEntryException extends Exception{

	public DuplicateNameEntryException(){}
	
	public DuplicateNameEntryException(String msg){
		super(msg);
	}
}