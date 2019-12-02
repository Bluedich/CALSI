package org.backend;

/**
 * Parent class for our custom backend exceptions.
 * @author Hugo
 *
 */
public class BackEndException extends Exception {
	public BackEndException(String errorMessage) {
		super(errorMessage);
	}
}
