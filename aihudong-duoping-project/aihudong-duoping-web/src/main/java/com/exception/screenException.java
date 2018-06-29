package com.exception;

public class screenException extends RuntimeException{
	
	public screenException(String message) {
		super(message);
	}
	
	public screenException(String message,Throwable cause) {
		super(message);
	}
}
