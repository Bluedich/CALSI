package org.backend;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.tools.Tools;

import bsh.EvalError;

public class Simulation {
	private Infos infos;

	// Simulation initialization parameters
	private String sourceCodeFileName;
	int numberOfProcesses;
	private String schedulerType;

	private Process processes[];
	private Scheduler scheduler;
	private PreTreatment preTreatment;

	public Simulation(SimulationBuilder simulationBuilder) throws BackEndException {
		this.sourceCodeFileName = simulationBuilder.sourceCodeFileName;
		this.numberOfProcesses = simulationBuilder.numberOfProcesses;
		this.schedulerType = simulationBuilder.schedulerType;

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
		try {
			processes[i].oneStep();
		} catch (EvalError e) {
			e.printStackTrace();
			throw new RipException("EvalError when executing next step");
		}
	}

	public void nextStep(int processId) {
		// TODO
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
		// TODO needs to be generalized
		processes = new Process[2];
		processes[0] = new Process(0, preTreatment);
		processes[1] = new Process(1, preTreatment);
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