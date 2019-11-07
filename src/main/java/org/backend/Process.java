package org.backend;
import PreTreatment;
import Variable;
import bsh.EvalError;
import bsh.Interpreter;

public class Process {

	
	static Variable[] sharedVars;
	

	String[] sourceCode;	
	int currentLine;
	Interpreter inter;
	public boolean done;
	
	public Process(int index, String source) throws Exception {
		
		String[] varNames = new String[2];
		varNames[0] = "turn";
		varNames[1] = "flag";
		
		this.inter = new Interpreter();

		
		
		this.inter.set("i", index);
		
		this.inter.eval("Integer turn;");
		this.inter.eval("Boolean[] flag;");

		this.inter.eval("int j;");
		this.inter.eval("boolean a;");
		this.inter.eval("int b;");
		
		this.inter.eval("turn = new Integer(0);");
		this.inter.eval("flag = new Boolean[2];");
		this.inter.eval("flag[0] = false;");
		this.inter.eval("flag[1] = false;");
		
		
		source = PreTreatment.preTreatWhile(source);
		this.sourceCode = source.split("\\r?\\n");

		this.sharedVars = new Variable[varNames.length];

		for(int i=0; i<varNames.length; ++i) {
			this.sharedVars[i] = new Variable(varNames[i], this.inter.get(varNames[i]));
		}
		
		
		this.currentLine = 0;
		this.done = false;		
	}
	
	public void treatFile() {
		
	}
	
	public void treatGoto() throws EvalError {
		//currentLine is a goto
		String instruction = this.sourceCode[this.currentLine];
		String condition = instruction.substring(6, instruction.indexOf(','));
		int targetLine = Integer.parseInt(instruction.substring(instruction.indexOf(',')+1, instruction.length()-2).trim());

		
		boolean test = (boolean) this.inter.eval(condition);

		if(test)
			this.currentLine = targetLine;
		else
			++this.currentLine;
		
	}
	
	public void oneStep() throws Exception {
		
		if(this.done)
			return;
		
		for(int i=0; i<this.sharedVars.length; ++i) {
			this.inter.set(this.sharedVars[i].getName(), this.sharedVars[i].getObj());
		}
		
		
		//System.out.println(this.sourceCode[this.currentLine]);
		
		if(this.sourceCode[this.currentLine].indexOf("goto") >= 0) {
			this.treatGoto();
		}else {
			this.inter.eval(this.sourceCode[this.currentLine]);			
			this.currentLine++;
		}
		

		for(int i=0; i<this.sharedVars.length; ++i) {
			this.sharedVars[i].setObj(this.inter.get(this.sharedVars[i].getName()));
		}
		
		

		
		if(this.currentLine >= this.sourceCode.length) {
			this.done = true;
		}
	}
	
}