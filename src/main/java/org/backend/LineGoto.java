package org.backend;

import java.util.ArrayList;


/**
 * Class representing goto lines
 */
public class LineGoto extends Line {
	
	public LineGoto(int id, String cond, int toid) {
		super(id);
		this.condition = cond;
		this.toId = toid;
		
	}
	
	
	
	/**
	 *	This function returns the final code of the line as a string.
	 *	It uses the parameter code to find the number of the line to which the goto is pointing.
	 *  This is because the goto only stores the id of the line, and the interpreter will need the actual number of
	 *  the line to be executed next.
	 */
	@Override
	public String getLineCode(ArrayList<Integer> code) throws BackEndException {
		
		int idLine = ID_NOT_FOUND;
		
		if(this.toId == ID_BEGIN) {
			idLine = 0;
		}else if(this.toId == ID_END){
			idLine = code.size();
		}else {
			for(int i = 0; i < code.size(); ++i) {
				if(code.get(i) == this.toId) {
					idLine = i;
					break;
				}
			}			
		}
		
		if(idLine == ID_NOT_FOUND) {
			throw new BackEndException("Internal Error: LineGoto.getLineCode : toId not found in code");
		}
		
		return "goto (" + this.condition + ", " + Integer.toString(idLine) + ");";
	}
	
	String condition;
	int toId;
}
