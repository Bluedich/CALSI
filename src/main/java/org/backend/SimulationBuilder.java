package org.backend;

/**
 * Builder pattern for creating a new simulation.
 * @author Hugo
 *
 */
public class SimulationBuilder {
	String sourceCodeFileName;
	int numberOfProcesses;
	String schedulerType;

	public SimulationBuilder() {
	}

	/**
	 * Set the source file for the processes'
	 * 
	 * @param sourceCodeFileName the name of the source file to be read from
	 * @return itself
	 */
	public SimulationBuilder withSourceCodeFromFile(String sourceCodeFileName) {
		this.sourceCodeFileName = sourceCodeFileName;

		return this;
	}

	/**
	 * Set how many processes should be simulated.
	 * 
	 * @param numberOfProcesses the number of processes to be simulated
	 * @return itself
	 */
	public SimulationBuilder withNumberOfProcesses(int numberOfProcesses) {
		this.numberOfProcesses = numberOfProcesses;

		return this;
	}

	/**
	 * Set the type of scheduler to use with the simulation.
	 * 
	 * @param schedulerType the type of scheduler to use
	 * @return itself
	 */
	public SimulationBuilder withScheduler(String schedulerType) {
		this.schedulerType = schedulerType;

		return this;
	}

	/**
	 * Creates the new simulation with the parameters that where provided to the builder.
	 * @return the new simulation
	 * @throws BackEndException if there is a problem with the simulation's
	 *                          parameters or with the specified source code
	 */
	public Simulation build() throws BackEndException {
		return new Simulation(this);
	}
}
