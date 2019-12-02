package org.backend;

import java.io.File;

public class SimulationBuilder {
	String sourceCodeFileName;
	int numberOfProcesses;
	String schedulerType;
	
	public SimulationBuilder() {
	}
	
	public SimulationBuilder withSourceCodeFromFile(String sourceCodeFileName) {
		this.sourceCodeFileName = sourceCodeFileName;
		
		return this;
	}
	
	public SimulationBuilder withNumberOfProcesses(int numberOfProcesses) {
		this.numberOfProcesses = numberOfProcesses;
		
		return this;
	}
	
	public SimulationBuilder withScheduler(String schedulerType) {
		this.schedulerType = schedulerType;
		
		return this;
	}
	
	public Simulation build() throws BackEndException {
		return new Simulation(this);
	}
}
