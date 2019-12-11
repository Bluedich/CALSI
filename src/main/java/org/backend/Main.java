package org.backend;

public class Main {

	public static void main(String[] args) throws BackEndException {
		SimulationBuilder simulationBuilder = new SimulationBuilder();
		Simulation simulation = simulationBuilder.withSourceCodeFromFile("tests/test.txt").withNumberOfProcesses(2)
				.withScheduler("random").build();

		Infos infos = simulation.getInfos();

		while (!infos.isDone()) {
			simulation.nextStep();
			int executedProcessId = infos.getIdOfLastExecutedProcess();
			String executedLine = infos.getExecutedPreTreatedLine();
			VariableInfo sharedVariablesInfos[] = infos.getSharedVariables();

			System.out.println("[" + executedProcessId + "]: " + executedLine);
			System.out.print("Shared Variables:");
			for (int i = 0; i < sharedVariablesInfos.length; i++) {
				VariableInfo sharedVar = sharedVariablesInfos[i];
				System.out.print(" " + sharedVar.getType() + " " + sharedVar.getName() + ": " + sharedVar.getValue() + ";");
			}
			System.out.println();
		}
		System.out.println(simulation.getProcesses()[0].getVarLst());
	}
}
