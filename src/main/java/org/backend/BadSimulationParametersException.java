package org.backend;

/**
 * Denotes that a new simulation has missing, incorrect or incompatible parameters.
 * @author Hugo
 *
 */
public class BadSimulationParametersException extends BackEndException {
	public BadSimulationParametersException(String errorMessage) {
		super(errorMessage);
	}
}
