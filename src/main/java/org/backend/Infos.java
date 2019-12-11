package org.backend;

import java.util.ArrayList;

public class Infos {
	private Simulation simulation;

	public Infos(Simulation simulation) {
		this.simulation = simulation;
	}
	
	/**
	 * Get the id of the last process that was executed.
	 * @return an int corresponding to the id of the last executed process
	 */
	public int getIdOfLastExecutedProcess() {
		ArrayList<Integer> executionOrderHistory = simulation.getExecutionOrderHistory();
		return executionOrderHistory.get(executionOrderHistory.size() - 1); 
	}
	
	/**
	 * Get the line of the last executed pre-treated line of code. For debugging purposes, not intended to be displayed to the user.
	 * @return a string of the executed line
	 */
	public String getExecutedPreTreatedLine() {
		Process executedProcess = simulation.getProcesses()[getIdOfLastExecutedProcess()];
		String sourceCode[] = executedProcess.getSourceCode();
		return sourceCode[executedProcess.getCurrentLine() - 1];
	}
	
	/**
	 * Get info about the shared variables.
	 * @return infos about the shared variables.
	 */
	public VariableInfo[] getSharedVariables() {
		Variable sharedVars[] = Process.getSharedVars();
		VariableInfo sharedVariablesInfos[] = new VariableInfo[sharedVars.length];
		
		for (int i=0; i<sharedVars.length; i++) {
			Variable sharedVar = sharedVars[i];
			sharedVariablesInfos[i] = new VariableInfo(sharedVar.getName(), sharedVar.getValue(), sharedVar.getType());
		}
		return sharedVariablesInfos;
	}

	// Accessible from package only
	void updateInfos() {
		// TODO
	}

	/**
	 * Check whether the simulation is finished (ie that all processes are done)
	 * @return true if done, false if not
	 */
	public boolean isDone() {
		Process processes[] = simulation.getProcesses();
		for (int i = 0; i < processes.length; i++) {
			if (!processes[i].isDone()) {
				return false;
			}
		}
		return true;
	}
}
