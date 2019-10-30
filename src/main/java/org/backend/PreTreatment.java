package org.backend;

public class PreTreatment {

	public static String preTreatWhile(String source) {
		String lines[] = source.split("\\r?\\n");
		
		int whileLine = 0, closeLine = 0;
		String cond = null;
		
		for(int i=0; i<lines.length; ++i) {
			
			if(lines[i].contains("while")) {
				whileLine = i;				
				cond = lines[i].substring(lines[i].indexOf('(')+1, lines[i].indexOf(')'));
			}
				
			
			if(lines[i].contains("}"))
				closeLine = i;
		}
		
		lines[whileLine] = "goto ( !(" + cond + "), " + Integer.toString(closeLine+1) + ");";
		lines[closeLine] = "goto (true, " + Integer.toString(whileLine) + ");";
	
		
		String result = "";
		
		for(int i=0; i<lines.length; i++) {
			result = result + lines[i] + "\n";
		}
		return result;
	}
	
}
