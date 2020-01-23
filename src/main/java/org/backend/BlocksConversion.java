package org.backend;

import java.util.ArrayList;

/**
 * @author Chaimaa & Issam
 */

public class BlocksConversion {

	static String[] blocks = { "while", "if", "for", "do" }; // possible blocks

	// list of all the lines of code where the index of a line is the same as its Id
	ArrayList<Line> lines;

	// the actual code composed of ordered line ids
	ArrayList<Integer> code;

	public BlocksConversion(String[] sourceCode) throws BackEndException {
		this.lines = new ArrayList<Line>();
		this.code = new ArrayList<Integer>();
		preTreat(sourceCode);
	}

	public int getNumberOfLines() {
		return code.size();
	}

	public int originalLineNumber(int lineNumber) throws IndexOutOfBoundsException {

		if (lineNumber >= this.getNumberOfLines()) {
			throw new IndexOutOfBoundsException();
		}

		int lineId = this.code.get(lineNumber);
		Line line = this.lines.get(lineId);
		return line.mapId;
	}

	public String getNewSourceCode() throws BackEndException {
		String result = "";

		for (int i = 0; i < this.code.size(); i++) {
			Line l = this.lines.get(code.get(i));
			result = result + Integer.toString(i) + ") " + l.getLineCode(code) + "\n";
		}
		return result;
	}
	
	public String[] getNewSourceCodeAsArray() throws BackEndException {
		String[] newSourceCode = new String[code.size()];
		
		for (int i = 0; i < this.code.size(); i++) {
			Line line = this.lines.get(code.get(i));
			newSourceCode[i] = line.getLineCode(code);
		}
	
		return newSourceCode;
	}
	
	public String[] getNewSourceCodeArray() throws BackEndException {
		String[] result = new String[this.code.size()];

		for(int i=0; i<this.code.size(); i++) {
			Line l = this.lines.get(code.get(i));
			result[i] = l.getLineCode(this.code);
		}
		return result;
	}

	/**
	 * 
	 * @param source Source code to pre-treat
	 * @throws BackEndException
	 */
	public void preTreat(String[] sourceCode) throws BackEndException {
		String block;

		this.lines.clear();
		this.code.clear();

		for (int i = 0; i < sourceCode.length; ++i) {
			this.lines.add(i, new LineString(i, sourceCode[i]));
			this.code.add(i, Integer.valueOf(i));
		}

		block = getFirstBlockString();

		while (block != "none") {
			int line = getFirstBlockLine();

			switch (block) {
			case "while":
				preTreatWhile(line);
				break;
			case "if":
				preTreatIf(line);
				break;
			case "for":
				preTreatFor(line);
				break;
			case "do":
				preTreatDoWhile(line);
				break;
			}

			block = getFirstBlockString();
		}

	}

	/**
	 * @return The number of the line containing the first occurrence of a block.
	 * @throws BackEndException
	 */
	private int getFirstBlockLine() throws BackEndException {
		String blockString;

		for (int i = 0; i < code.size(); ++i) {

			blockString = containBlock(lines.get(code.get(i)).getLineCode(code));
			if (blockString != "") {
				return i;
			}
		}
		return -1;
	}

	/**
	 * @return The first occurrence of block name if found, and "none" if not.
	 * @throws BackEndException
	 */
	private String getFirstBlockString() throws BackEndException {
		String blockString;

		for (int i = 0; i < code.size(); ++i) {

			blockString = containBlock(lines.get(code.get(i)).getLineCode(code));
			if (blockString != "") {
				return blockString;
			}

		}
		return "none";
	}

	/**
	 * Method used to find the closing of a block.
	 * 
	 * @param start_line Line where the block begins.
	 * @return Line where the block ends.
	 * @throws BackEndException
	 */
	private Integer getBlockEnd(Integer start_line) throws BackEndException {

		Integer start = start_line;

		Integer p = 1;
		Integer i = start;

		while (p != 0 && i < code.size()) {
			i++;
			String line = lines.get(code.get(i)).getLineCode(code);
			if (line.contains("}"))
				p--;
			if (p == 0)
				break;
			if (line.contains("{"))
				p++;
		}

		return i;
	}

	// if String line contains block, returns block type, else returns ""
	private static String containBlock(String line) {
		for (int i = 0; i < blocks.length; ++i) {
			if (line.contains(blocks[i]))
				return blocks[i];
		}
		return "";
	}

	private void preTreatWhile(int line) throws BackEndException {

		int whileLine = line;
		int closeLine = getBlockEnd(whileLine);

		String whileLineString = lines.get(code.get(whileLine)).getLineCode(code);
		String cond = whileLineString.substring(whileLineString.indexOf('(') + 1, whileLineString.lastIndexOf(')'));
		String notCond = "!(" + cond + ")";

		int startToId = closeLine + 1 < code.size() ? code.get(closeLine + 1) : Line.ID_END;
		int closeToId = code.get(whileLine);

		lines.set(code.get(whileLine), new LineGoto(code.get(whileLine), notCond, startToId));
		lines.set(code.get(closeLine), new LineGoto(code.get(closeLine), "true", closeToId));

	}

	private void preTreatIf(int line) throws BackEndException {

		int ifLine = line;
		int elseLine = getBlockEnd(ifLine);
		int closeLine = getBlockEnd(elseLine);

		int ifLineId = code.get(ifLine);
		int elseLineId = code.get(elseLine);

		String ifLineString = lines.get(ifLineId).getLineCode(code);
		String cond = ifLineString.substring(ifLineString.indexOf('(') + 1, ifLineString.lastIndexOf(')'));
		String notCond = "!(" + cond + ")";

		code.remove(closeLine);

		int ifToId = elseLine + 1 < code.size() ? code.get(elseLine + 1) : Line.ID_END;
		int elseToId = closeLine < code.size() ? code.get(closeLine) : Line.ID_END;

		lines.set(ifLineId, new LineGoto(ifLineId, notCond, ifToId));
		lines.set(elseLineId, new LineGoto(elseLineId, "true", elseToId));

	}

	private void preTreatDoWhile(int line) throws BackEndException {

		int startLine = line;
		int closeLine = getBlockEnd(startLine);

		String whileLineString = lines.get(code.get(closeLine)).getLineCode(code);
		String cond = whileLineString.substring(whileLineString.indexOf('(') + 1, whileLineString.lastIndexOf(')'));

		int startLineId = code.get(startLine);
		int closeLineId = code.get(closeLine);
		int afterDoId = code.get(startLine + 1);

		lines.set(closeLineId, new LineGoto(closeLineId, cond, startLineId));

		code.remove(startLine);

		Line lineAfterDo = lines.get(afterDoId);
		lineAfterDo.id = startLineId;

		lines.set(startLineId, lineAfterDo);
		code.set(startLine, startLineId);

	}

	private void preTreatFor(int line) throws BackEndException {

		int startLine = line;
		int endLine = getBlockEnd(startLine);

		int startLineId = code.get(startLine);
		int endLineId = code.get(endLine);
		int afterEndId = endLine + 1 < code.size() ? code.get(endLine + 1) : Line.ID_END;

		String forLineString = lines.get(code.get(startLine)).getLineCode(code);
		String innerFor = forLineString.substring(forLineString.indexOf('(') + 1, forLineString.lastIndexOf(')'));
		String[] forThings = innerFor.split(";");

		String i1 = forThings[0].strip();
		String cond = forThings[1].strip();
		String i2 = forThings[2].strip();
		String notCond = "!(" + cond + ")";

		Line inst1 = new LineString(startLineId, i1);
		Line startLineGoto = new LineGoto(lines.size(), notCond, afterEndId);
		Line inst2 = new LineString(lines.size() + 1, i2);
		Line endLineGoto = new LineGoto(endLineId, "true", startLineGoto.id);

		lines.add(startLineGoto);
		lines.add(inst2);
		lines.set(inst1.id, inst1);
		lines.set(endLineGoto.id, endLineGoto);

		code.add(startLine + 1, startLineGoto.id);
		code.add(endLine + 1, inst2.id);

	}

}
