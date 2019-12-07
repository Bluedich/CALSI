package org.backend;

import bsh.EvalError;
import bsh.Interpreter;

public class Process {
	static Variable[] sharedVars;
	Variable[] localVars;
	String[] sourceCode;
	int currentLine;
	Interpreter inter;
	private boolean done;

	public Process(int index, PreTreatment preTreatment) throws BackEndException {
		this.inter = new Interpreter();

		// Reserved variables
		try {
			this.inter.set("i", index);
		} catch (EvalError e1) {
			e1.printStackTrace();
			throw new RipException("EvalError when setting index variable (i).");
		}
		
		// Execution of the initialization block (imports, and variable declarations)
		try {
			this.inter.eval(preTreatment.getInitialisationBlock());
		} catch (EvalError e) {
			e.printStackTrace();
			throw new BadSourceCodeException("Error in initialization.");
		}

		// We receive a copy of the array of local variables
		localVars = preTreatment.getLocalVars();
		
		String source = preTreatment.getPreTreatedSource();

		this.sourceCode = source.split("\\r?\\n");

		this.currentLine = 0;
		this.done = false;
	}

	public void treatGoto() throws EvalError {
		// currentLine is a goto
		String instruction = this.sourceCode[this.currentLine];
		String condition = instruction.substring(6, instruction.indexOf(','));
		int targetLine = Integer
				.parseInt(instruction.substring(instruction.indexOf(',') + 1, instruction.length() - 2).trim());

		boolean test = (boolean) this.inter.eval(condition);

		if (test)
			this.currentLine = targetLine;
		else
			++this.currentLine;

	}

	public void oneStep() throws EvalError {

		if (this.done)
			return;

		// The shared variables in the interpreter (which might have been modified in an other process since) are 
		// updated from the array of shared variables.
		for (int i = 0; i < Process.sharedVars.length; ++i) {
			this.inter.set(Process.sharedVars[i].getName(), Process.sharedVars[i].getObj());
		}

		// System.out.println(this.sourceCode[this.currentLine]);

		// One line is executed
		if (this.sourceCode[this.currentLine].indexOf("goto") >= 0) {
			this.treatGoto();
		} else {
			this.inter.eval(this.sourceCode[this.currentLine]);
			this.currentLine++;
		}

		// After the step, the shared variables are updated so their value can be shared between the processes
		for (int i = 0; i < Process.sharedVars.length; ++i) {
			Process.sharedVars[i].setObj(this.inter.get(Process.sharedVars[i].getName()));
		}
		
		// Local variables are also updated for the GUI
		for (int i = 0; i < localVars.length; ++i) {
			localVars[i].update(this.inter.get(localVars[i].getName()));
		}
		
		System.out.print("Local vars :");
		for (int i = 0; i < localVars.length; ++i) {
			System.out.print(" name: " + localVars[i].getName() + " value: " + localVars[i].getObj());
		}
		System.out.println();
		System.out.print("Shared vars :");
		for (int i = 0; i < Process.sharedVars.length; ++i) {
			System.out.print(" name: " + Process.sharedVars[i].getName() + " value: " + Process.sharedVars[i].getObj());
		}

		if (this.currentLine >= this.sourceCode.length) {
			this.done = true;
		}
	}

	public static void setSharedVars(PreTreatment preTreatment) {
		Process.sharedVars = preTreatment.getSharedVars();
	}
	
	public Boolean isDone() {
		return done;
	}

}