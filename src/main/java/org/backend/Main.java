package org.backend;

public class Main {

	public static void main(String[] args) throws BackEndException {
		SimulationBuilder simulationBuilder = new SimulationBuilder();
		Simulation simulation = simulationBuilder
			.withSourceCodeFromFile("tests/test.txt")
			.withNumberOfProcesses(2)
			.withScheduler("random")
			.build();
		
		Infos infos = simulation.getInfos();
		
		while ( !infos.isDone() ) {
			simulation.nextStep();
		}
	}
}
