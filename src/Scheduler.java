import java.util.ArrayList;
import java.util.Random;

public class Scheduler {

	public Random random;
	
	public ArrayList<String> order;
	
	public Scheduler() {
		this.random = new Random();
		this.order = new ArrayList<String>();
	}
	
	public int getNext(Process procs[]) {
		boolean allDone = true;
		
		for(int i=0; i < procs.length; ++i) {
			if(!procs[i].done) {
				allDone = false;
				break;
			}
		}
		if(allDone)
			return -1;
		
		int next = this.random.nextInt(procs.length);
		
		while (procs[next].done) {
			next = this.random.nextInt(procs.length);
		}
		
		int line = procs[next].currentLine;
		String str = "Proc " + next + ": " + line + " => " + procs[next].sourceCode[procs[next].currentLine];
		
		System.out.println(str);
		
		return next;
	}
}
