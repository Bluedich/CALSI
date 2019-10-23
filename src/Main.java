
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import bsh.EvalError;
import bsh.Interpreter;


public class Main {

	public static void main(String[] args) throws EvalError, IOException {
		// TODO Auto-generated method stub

		BufferedReader reader = new BufferedReader(new FileReader("tests/test.txt"));
		StringBuilder stringBuilder = new StringBuilder();
		char[] buffer = new char[10];
		while (reader.read(buffer) != -1) {
			stringBuilder.append(new String(buffer));
			buffer = new char[10];
		}
		reader.close();

		String content = stringBuilder.toString();
		
		Scheduler sch = new Scheduler();
		
		Process[] procs = new Process[2];
		procs[0] = new Process(0, content);
		procs[1] = new Process(1, content);

		while(! (procs[0].done && procs[1].done) ) {
			int i = sch.getNext(procs);
			procs[i].oneStep();
		}
		System.out.println(Process.turn);
	}

}
