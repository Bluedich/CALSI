package org.backend;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import bsh.EvalError;

public class Main {

	public static void main(String[] args) throws EvalError, IOException, BadSourceCodeException {
		BufferedReader reader = new BufferedReader(new FileReader("tests/test.txt"));
		StringBuilder stringBuilder = new StringBuilder();
		char[] buffer = new char[10];
		while (reader.read(buffer) != -1) {
			stringBuilder.append(new String(buffer));
			buffer = new char[10];
		}
		reader.close();

		String content = stringBuilder.toString();
		
		PreTreatment preTreatment = new PreTreatment(content);
		Process.setSharedVars(preTreatment);
		
		Scheduler sch = new Scheduler();
		
		Process[] procs = new Process[2];
		procs[0] = new Process(0, preTreatment);
		procs[1] = new Process(1, preTreatment);

		while(! (procs[0].done && procs[1].done) ) {
			int i = sch.getNext(procs);
			procs[i].oneStep();
		}
	}

}
