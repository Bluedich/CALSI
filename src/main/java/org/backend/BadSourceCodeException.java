package org.backend;

public class BadSourceCodeException extends Exception {
	public BadSourceCodeException(String errorMessage) {
		super(errorMessage);
	}
}
