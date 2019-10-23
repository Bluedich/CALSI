import bsh.EvalError;
import bsh.Interpreter;

public class Process {


	static int turn;
	static boolean[] flag;
	
	
	String[] sourceCode;	
	int currentLine;
	Interpreter inter;
	public boolean done;
	
	public Process(int i, String source) throws EvalError {
		
		this.inter = new Interpreter();
		
		Process.flag = new boolean[2];
		
		this.inter.set("i", i);
		
		this.inter.eval("int turn;");
		this.inter.eval("boolean[] flag;");

		this.inter.eval("int j;");
		this.inter.eval("boolean a;");
		this.inter.eval("int b;");
		
		this.inter.eval("flag = new boolean[2];");
		this.inter.eval("flag[0] = false;");
		this.inter.eval("flag[1] = false;");
		
		
		source = PreTreatment.preTreatWhile(source);
		this.sourceCode = source.split("\\r?\\n");
//		this.sourceCode = new String[16];
//
//		this.sourceCode[0] = new String("j = (i+1) % 2;");
//		this.sourceCode[1] = new String("flag[i] = true;");
//		this.sourceCode[2] = new String("turn = j;");
//		this.sourceCode[3] = new String("a = flag[j];");
//		this.sourceCode[4] = new String("b = turn;");
//		this.sourceCode[5] = new String("goto ( !(a == true && b == j) , 9);");
//		this.sourceCode[6] = new String("a = flag[j];");
//		this.sourceCode[7] = new String("b = turn;");
//		this.sourceCode[8] = new String("goto (true, 5);");
//		this.sourceCode[9] = new String("1");
//		this.sourceCode[10] = new String("1");
//		this.sourceCode[11] = new String("1");
//		this.sourceCode[12] = new String("1");
//		this.sourceCode[13] = new String("1");
//		this.sourceCode[14] = new String("1");
//		this.sourceCode[15] = new String("flag[i] = false;");
		

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
	
	public void oneStep() throws EvalError {
		
		if(this.done)
			return;
		
		this.inter.set("turn", Process.turn);
		this.inter.set("flag", Process.flag);

		//System.out.println(this.sourceCode[this.currentLine]);
		
		if(this.sourceCode[this.currentLine].indexOf("goto") >= 0) {
			this.treatGoto();
		}else {
			this.inter.eval(this.sourceCode[this.currentLine]);			
			this.currentLine++;
		}
		


		
		Process.turn = (int) this.inter.get("turn");
		Process.flag = (boolean[]) this.inter.get("flag");
		
		if(this.currentLine >= this.sourceCode.length) {
			this.done = true;
		}
	}
	
}