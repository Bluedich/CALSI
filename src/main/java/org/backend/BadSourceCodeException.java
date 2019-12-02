package org.backend;

/**
 * Denotes that the source code provided does no have the correct format and thus can't be read by the backend.
 * @author Hugo
 *
 */
public class BadSourceCodeException extends BackEndException {
	public BadSourceCodeException(String errorMessage) {
		super(errorMessage);
	}
}
