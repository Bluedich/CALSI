package org.backend;

import java.util.ArrayList;

/**
 * Class with several method giving various information on the
 * current and past state of the simulation. Mainly for use by the GUI/front
 * end, and intended as the only interface between the front end and back end
 * data.
 * 
 * @author Hugo
 *
 */
public class Infos {
	private Simulation simulation;

	public Infos(Simulation simulation) {
		this.simulation = simulation;
	}

	/**
	 * Get the id of the last process that was executed.
	 * 
	 * @return an int corresponding to the id of the last executed process
	 */
	public int getIdOfLastExecutedProcess() {
		ArrayList<Integer> executionOrderHistory = simulation.getExecutionOrderHistory();
		return executionOrderHistory.get(executionOrderHistory.size() - 1);
	}

	/**
	 * Get the line of the last executed pre-treated line of code. For debugging
	 * purposes, not intended to be displayed to the user.
	 * 
	 * @return a string of the executed line
	 */
	public String getExecutedPreTreatedLine() {
		Process executedProcess = simulation.getProcesses()[getIdOfLastExecutedProcess()];
		String sourceCode[] = executedProcess.getSourceCode();
		return sourceCode[executedProcess.getCurrentLine() - 1];
	}

	/**
	 * Get info about the shared variables.
	 * 
	 * @return infos about the shared variables.
	 */
	public VariableInfo[] getSharedVariables() {
		Variable sharedVars[] = Process.getSharedVars();
		
		return VariableArrayToVariableInfoArray(sharedVars);
	}
	
	/**
	 * Get infos about the local variables of the specified process
	 * @param processId the id of the specified process
	 * @return infos about the local variables
	 */
	public VariableInfo[] getLocalVariables(int processId) {
		Process processes[] = simulation.getProcesses();
		
		if ( processId > processes.length ) {
			// If invalid processId, return empty array
			return new VariableInfo[0];
		}
		
		Variable localVars[] = processes[processId].getLocalVars();
		
		return VariableArrayToVariableInfoArray(localVars);
	}

	// Accessible from package only
	void updateInfos() {
		// TODO
	}

	/**
	 * Check whether the simulation is finished (ie that all processes are done)
	 * 
	 * @return true if done, false if not
	 */
	public boolean simulationIsDone() {
		Process processes[] = simulation.getProcesses();
		for (int i = 0; i < processes.length; i++) {
			if (!processes[i].isDone()) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * Check if the specified process has terminated. 
	 * @param processId the id of the process to be checked for termination
	 * @return true if terminated, false otherwise
	 * @throws RipException if the process id specified does not correspond to a process
	 */
	public boolean processIsDone(int processId) throws RipException {
		return getProcess(processId).isDone();
	}
	
	/**
	 * Check if the specified process has crashed. 
	 * @param processId the id of the process to be checked for termination
	 * @return true if terminated, false otherwise
	 * @throws RipException if the process id specified does not correspond to a process
	 */
	public boolean processIsCrashed(int processId) throws RipException {
		return getProcess(processId).isCrashed();
	}
	
	public ArrayList<Integer> getOriginalSourceLinesExecutedDuringLastStep(int processId) throws RipException {
		Process process = getProcess(processId);
		return process.getOriginalSourceLinesExecutedDuringLastStep();
	}
	
	private Process getProcess(int processId) throws RipException {
		Process processes[] = simulation.getProcesses();
		if (processId >= processes.length) {
			throw new RipException("Can't get termination status for process " + processId + " as it does not exist.");
		}
		return processes[processId];
	}
	
	private VariableInfo[] VariableArrayToVariableInfoArray(Variable[] variableArray) {
		VariableInfo variablesInfos[] = new VariableInfo[variableArray.length];

		for (int i = 0; i < variableArray.length; i++) {
			Variable sharedVar = variableArray[i];
			variablesInfos[i] = new VariableInfo(sharedVar.getName(), sharedVar.getValue(), sharedVar.getType());
		}
		return variablesInfos;
	}
}
