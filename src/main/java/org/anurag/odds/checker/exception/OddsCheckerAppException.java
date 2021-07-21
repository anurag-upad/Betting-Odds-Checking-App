package org.anurag.odds.checker.exception;

/**
 * 
 * Custom wrapper exception class to handle application's exceptions
 *
 */
public class OddsCheckerAppException extends Exception {
	
	
	private static final long serialVersionUID = 1L;

	
	public OddsCheckerAppException(String errorMessage) {
		super(errorMessage);
	}


	public OddsCheckerAppException(String errorMessage, Exception e) {
		super(errorMessage, e);
	}

}
