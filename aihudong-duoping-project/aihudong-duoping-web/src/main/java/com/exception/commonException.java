package com.exception;

public class commonException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public commonException(String message) {
		super(message);
	}
	
	public commonException(String message,Throwable cause) {
		super(message);
	}
}
