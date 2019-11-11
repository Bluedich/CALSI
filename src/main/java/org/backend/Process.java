package org.backend;

import bsh.EvalError;
import bsh.Interpreter;

public class Process {
	static Variable[] sharedVars;
	String[] sourceCode;
	int currentLine;
	Interpreter inter;
	public boolean done;

	public Process(int index, PreTreatment preTreatment) throws EvalError, BadSourceCodeException {
		String[] varNames = new String[2];
		varNames[0] = "turn";
		varNames[1] = "flag";

		this.inter = new Interpreter();

		this.inter.set("i", index);
		try {
			this.inter.eval(preTreatment.getInitialisationBlock());
		} catch (EvalError e) {
			e.printStackTrace();
			throw new BadSourceCodeException("Error in initialization.");
		}

		String source = preTreatment.getPreTreatedSource();

		this.sourceCode = source.split("\\r?\\n");

		this.currentLine = 0;
		this.done = false;
	}

	public void treatFile() {

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

		for (int i = 0; i < Process.sharedVars.length; ++i) {
			this.inter.set(Process.sharedVars[i].getName(), Process.sharedVars[i].getObj());
		}

		// System.out.println(this.sourceCode[this.currentLine]);

		if (this.sourceCode[this.currentLine].indexOf("goto") >= 0) {
			this.treatGoto();
		} else {
			this.inter.eval(this.sourceCode[this.currentLine]);
			this.currentLine++;
		}

		for (int i = 0; i < Process.sharedVars.length; ++i) {
			Process.sharedVars[i].setObj(this.inter.get(Process.sharedVars[i].getName()));
		}

		if (this.currentLine >= this.sourceCode.length) {
			this.done = true;
		}
	}

	public static void setSharedVars(PreTreatment preTreatment) {
		Process.sharedVars = preTreatment.getSharedVars();
	}

}