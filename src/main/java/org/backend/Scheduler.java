package org.backend;
import java.util.ArrayList;
import java.util.Random;

public class Scheduler {
	private Simulation simulation;
	public Random random;
	
	public ArrayList<String> executionOrderHistory;
	
	public Scheduler(Simulation simulation) {
		this.simulation = simulation;
		this.random = new Random();
		this.executionOrderHistory = new ArrayList<String>();
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
		
		int line = procs[next].currentLine;
		String str = "Proc " + next + ": " + line + " => " + procs[next].sourceCode[procs[next].currentLine];
		
		System.out.println(str);
		
		return next;
	}
}
