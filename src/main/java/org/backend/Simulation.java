package org.backend;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import org.tools.Tools;
import bsh.EvalError;

public class Simulation {
	private Infos infos;

	// Simulation initialization parameters
	private String sourceCodeFileName;
	private int numberOfProcesses;
	private String schedulerType;

	private Process processes[];
	private Scheduler scheduler;
	private PreTreatment preTreatment;
	private ArrayList<String> executionOrderHistory;

	public Simulation(SimulationBuilder simulationBuilder) throws BackEndException {
		this.sourceCodeFileName = simulationBuilder.sourceCodeFileName;
		this.numberOfProcesses = simulationBuilder.numberOfProcesses;
		this.schedulerType = simulationBuilder.schedulerType;

		this.executionOrderHistory = new ArrayList<String>();

		initSimulation();

		// infos gets a reference to simulation being created
		this.infos = new Infos(this);
	}

	public void changeScheduler(String newSchedulerType) {
		// TODO
	}

	public void crashProcess(int processId) {
		// TODO
	}

	public void nextStep() throws BackEndException {
		int i = scheduler.getNext();
		nextStep(i);
	}

	public void nextStep(int processId) throws RipException {
		try {
			processes[processId].oneStep();
		} catch (EvalError e) {
			e.printStackTrace();
			throw new RipException("EvalError when executing next step");
		}
	}

	public Infos getInfos() {
		return infos;
	}

	private void initSimulation() throws BackEndException {
		String sourceCode = readSourceCode();
		this.preTreatment = new PreTreatment(sourceCode);

		Process.setSharedVars(preTreatment);

		// TODO Scheduler needs to be chosen according to specified type
		scheduler = new Scheduler(this);

		initProcesses();
	}

	private void initProcesses() throws BackEndException {
		processes = new Process[numberOfProcesses];
		for (int i = 0; i < numberOfProcesses; i++) {
			processes[i] = new Process(i, preTreatment);
		}
	}

	private String readSourceCode() throws BackEndException {
		try {
			return Tools.getContentOfFile(sourceCodeFileName);
		} catch (FileNotFoundException e) {
			throw new BadSimulationParametersException("File specified for source code not found.");
		} catch (IOException e) {
			e.printStackTrace();
			throw new RipException("IO error while attempting to read from source code file.");
		}
	}

	public Process[] getProcesses() {
		return processes;
	}

}