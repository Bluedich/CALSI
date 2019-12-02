package org.backend;

/**
 * General exception used to denote an application breaking bug in the backend.
 * @author Hugo
 *
 */
public class RipException extends BackEndException {
	public RipException(String errorMessage) {
		super(errorMessage);
	}
}
