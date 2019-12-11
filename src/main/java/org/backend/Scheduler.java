package org.backend;
import java.util.Random;

public class Scheduler {
	private Simulation simulation;
	public Random random;
	
	public Scheduler(Simulation simulation) {
		this.simulation = simulation;
		this.random = new Random();
	}
	
	public int getNext() {
		Process procs[] = simulation.getProcesses();
		boolean allDone = true;
		
		for(int i=0; i < procs.length; ++i) {
			if(!procs[i].isDone()) {
				allDone = false;
				break;
			}
		}
		if(allDone)
			return -1;
		
		int next = this.random.nextInt(procs.length);
		
		while (procs[next].isDone()) {
			next = this.random.nextInt(procs.length);
		}
		
		return next;
	}
}
