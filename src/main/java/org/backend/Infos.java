package org.backend;

public class Infos {
	private Simulation simulation;

	public Infos(Simulation simulation) {
		this.simulation = simulation;
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
