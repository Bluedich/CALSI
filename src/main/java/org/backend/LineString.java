package org.backend;

import java.util.ArrayList;


/**
 * Class representing a simple line to be executed by the interpreter
 */
public class LineString extends Line {
	
	public LineString(int id, String code) {
		super(id);
		this.code = code;
	}



	@Override
	public String getLineCode(ArrayList<Integer> code) throws BackEndException {
		return this.code;
	}
	
	
	String code;
}
