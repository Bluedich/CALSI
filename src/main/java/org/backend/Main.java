package org.backend;

public class Main {

	public static void main(String[] args) throws BackEndException {
		SimulationBuilder simulationBuilder = new SimulationBuilder();
		Simulation simulation = simulationBuilder.withSourceCodeFromFile("tests/test.txt").withNumberOfProcesses(2)
				.withScheduler("random").build();

		Infos infos = simulation.getInfos();

		// Test simulation with prints to see what's going on as well as test the Infos class.
		while (!infos.simulationIsDone()) {
			simulation.nextStep();
			int executedProcessId = infos.getIdOfLastExecutedProcess();
			String executedLine = infos.getExecutedPreTreatedLine();
			VariableInfo sharedVariablesInfos[] = infos.getSharedVariables();
			VariableInfo localVariablesInfos[] = infos.getLocalVariables(executedProcessId);
			int originalLine = infos.getOriginalSourceLinesExecutedDuringLastStep(executedProcessId).get(0);
			
			System.out.println("[" + executedProcessId + "]: " + executedLine);
			System.out.println("Original Line : " + Integer.toString(originalLine));
			System.out.print("Shared Variables:");
			for (int i = 0; i < sharedVariablesInfos.length; i++) {
				VariableInfo sharedVar = sharedVariablesInfos[i];
				System.out.print(" " + sharedVar.getType() + " " + sharedVar.getName() + ": " + sharedVar.getValue() + ";");
			}
			System.out.println();
			
			System.out.print("Local Variables:");
			for (int i = 0; i < localVariablesInfos.length; i++) {
				VariableInfo sharedVar = localVariablesInfos[i];
				System.out.print(" " + sharedVar.getType() + " " + sharedVar.getName() + ": " + sharedVar.getValue() + ";");
			}
			System.out.println();
		}
	}
}
