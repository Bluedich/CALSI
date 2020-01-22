package org.backend;

import java.util.Arrays;

import bsh.EvalError;
import bsh.Interpreter;

public class PreTreatment {
	private String source;
	private String preTreatedSource;
	private String initialisationBlock;
	private int endOfInitBlocks;

	private Variable[] sharedVars;
	private Variable[] localVars;
	

	static String[] blocks = { "while", "if", "for", "do" }; // possible blocks
	static Transformation trans;

	public PreTreatment(String source) throws BadSourceCodeException {
		this.source = source;
		preTreat();
	}

	private void preTreat() throws BadSourceCodeException {
		String lines[] = source.split("\\r?\\n");
		removeComments(lines); // If the code contains '// ...' comments, they are removed
		endOfInitBlocks = preTreatInitialisationBlocks(lines);
		preTreatedSource = preTreatment(source);
	}

	/**
	 * Removes '// ...' comments from the source code's lines. NOTE : lines that are
	 * only a comment are thus replaced by empty lines.
	 * 
	 * @param The source code's lines
	 */
	private void removeComments(String[] lines) {
		String line;
		int offset;
		for (int i = 0; i < lines.length; i++) {
			line = lines[i];
			offset = line.indexOf("//");
			if (-1 != offset) {
				lines[i] = line.substring(0, offset);
			}
		}
	}

	/**
	 * Parses the initialization blocks, and declares + initializes the variables
	 * declared within
	 * 
	 * @param lines
	 * @return 
	 * @return Returns the line number of the start of the code important for the
	 *         process
	 * @throws BadSourceCodeException
	 */
	private int preTreatInitialisationBlocks(String[] lines) throws BadSourceCodeException {
		// We extract the 4 initialization blocks from the source code
		int[] blockIndexes = getBlockIndexes(lines);
		String[] importBlock = Arrays.copyOfRange(lines, blockIndexes[0], blockIndexes[1]);
		String[] sharedVarDecBlock = Arrays.copyOfRange(lines, blockIndexes[2], blockIndexes[3]);
		String[] sharedVarInitBlock = Arrays.copyOfRange(lines, blockIndexes[4], blockIndexes[5]);
		String[] localVarDecBlock = Arrays.copyOfRange(lines, blockIndexes[6], blockIndexes[7]);

		// We omit shared variables initializations as it is done once during
		// pre-treatment.
		initialisationBlock = String.join("\n", importBlock) + "\n\n" + String.join("\n", sharedVarDecBlock) + "\n\n"
				+ String.join("\n", localVarDecBlock);

		// We create a bsh interpreter to help us evaluate the initialized shared
		// variables
		Interpreter interpreter = new Interpreter();

		declareSharedVars(blockIndexes[2], sharedVarDecBlock, interpreter);
		initSharedVars(blockIndexes[4], sharedVarInitBlock, interpreter);
		declareLocalVars(blockIndexes[6], localVarDecBlock, interpreter);
		
		int endOfInitBlocks = blockIndexes[7];
		
		return endOfInitBlocks;

	}

	/**
	 * Blocks are separated by empty lines. There are 4 blocks in the following
	 * order: imports, shared variables declaration, shared variable initialization,
	 * and local variable declaration
	 * 
	 * @param lines The lines of the code
	 * @return The indexes of the blocks' ends
	 * @throws BadSourceCodeException
	 */
	private int[] getBlockIndexes(String[] lines) throws BadSourceCodeException {
		int[] blockIndexes = new int[8];
		int blockNumber = 0;
		String line;
		String previousLine;

		// Import block starts on the first line
		blockIndexes[0] = 0;
		// Case where there is no import statements: import block also finishes on the
		// first line
		if (lines[0].isBlank()) {
			blockIndexes[1] = 0;
			blockNumber++;
		}
		// We skip the first line as it is either part of the import block or empty
		for (int i = 1; i < lines.length; i++) {
			line = lines[i];
			previousLine = lines[i - 1];
			// Entering a block
			if (!line.isBlank() && previousLine.isBlank()) {
				blockIndexes[blockNumber * 2] = i;
			}
			// Exiting a block
			if (line.isBlank() && !previousLine.isBlank()) {
				blockIndexes[blockNumber * 2 + 1] = i;
				blockNumber++;
			}
			// We only need the first four blocks
			if (blockNumber > 3) {
				break;
			}
		}
		if (blockNumber < 4)
			throw new BadSourceCodeException("Source code does not contain 4 distinct init blocks");
		return blockIndexes;
	}
	
	

	/**
	 * Iterates through the declared local variables and creates empty local
	 * Variables with the right name.
	 * 
	 * @param startOfBlock
	 * @param blockLines
	 * @param interpreter
	 * @throws BadSourceCodeException
	 */
	private void declareLocalVars(int startOfBlock, String[] blockLines, Interpreter interpreter)
			throws BadSourceCodeException {
		localVars = new Variable[blockLines.length];
		String line;
		String varName;
		for (int i = 0; i < blockLines.length; i++) {
			line = blockLines[i];
			varName = parseVarName(startOfBlock + i, line);
			localVars[i] = new Variable(varName);
		}
	}

	private void initSharedVars(int startOfBlock, String[] blockLines, Interpreter interpreter)
			throws BadSourceCodeException {
		String line;
		for (int i = 0; i < blockLines.length; i++) {
			line = blockLines[i];
			try {
				// Bsh interpreter is used to parse the line for us and init the variable
				interpreter.eval(line);
			} catch (EvalError e) {
				e.printStackTrace();
				throw new BadSourceCodeException(
						"Error in the initialisation of the shared variable on line " + (startOfBlock + i));
			}
		}
		for (int i = 0; i < sharedVars.length; i++) {
			try {
				// We can then extract the computed value of the initialized variable from the
				// interpreter
				sharedVars[i].update(interpreter.get(sharedVars[i].getName()));
			} catch (EvalError e) {
				e.printStackTrace();
				throw new BadSourceCodeException("Shared variable " + sharedVars[i].getName() + " not initialised");
			}
		}
	}

	/**
	 * Declares the program's shared variables by parsing the corresponding block
	 * and populating the sharedVars array with the right initialized variables.
	 * 
	 * @param blockLines  The first init block
	 * @param interpreter The bsh interpreter used to help parse the init blocks
	 * @throws BadSourceCodeException In the event that the block can't be parsed
	 */
	private void declareSharedVars(int startOfBlock, String[] blockLines, Interpreter interpreter)
			throws BadSourceCodeException {
		sharedVars = new Variable[blockLines.length];
		String line;
		String varName;
		for (int i = 0; i < blockLines.length; i++) {
			line = blockLines[i];
			varName = parseVarName(startOfBlock + i, line);
			try {
				// We init the variable in the bsh interpreter for
				interpreter.eval(line);
				sharedVars[i] = new Variable(varName);
			} catch (EvalError e) {
				e.printStackTrace();
				throw new BadSourceCodeException("Error in the declaration of the shared variable " + varName
						+ " on line " + (startOfBlock + i));
			}
		}
	}

	/**
	 * Parses the name of the variable declared in the given line of code.
	 * 
	 * @param string The line of code to parse
	 * @return The name of the declared variable
	 * @throws BadSourceCodeException
	 */
	private String parseVarName(int lineNumber, String line) throws BadSourceCodeException {
		// Eventual leading and trailing whitespaces are removed, in addition to the
		// ';'.
		line = line.trim();
		if (!line.substring(line.length() - 1).equals(";"))
			throw new BadSourceCodeException("Missing ';' in variable declaration on line " + lineNumber);
		line = line.substring(0, line.length() - 1);
		// We assume a format of "variableType variableName;"
		if (line.split("\\s+").length != 2)
			throw new BadSourceCodeException("Invalid variable declaration on line " + lineNumber);
		return line.split("\\s+")[1];
	}

	// TODO Remove test code
	public void testPreTreatment() {
		for (int i = 0; i < sharedVars.length; i++) {
			// System.out.println();
		}
		System.out.println(initialisationBlock);
		System.out.println(preTreatedSource);

	}

	public String getPreTreatedSource() {
		return preTreatedSource;
	}

	public String getInitialisationBlock() {
		return initialisationBlock;
	}

	public Variable[] getSharedVars() {
		return sharedVars;
	}

	public Variable[] getLocalVars() {
		return localVars.clone();
	}

	public static String preTreatment(String source) throws BadSourceCodeException {
		String sourceCode[];
		String block;

		sourceCode = source.split("\\r?\\n");

		trans = new Transformation(sourceCode.clone(), sourceCode.length);

		block = getBlockString(trans.code);

		while (block != "none") {

			int line = getBlockLine(trans.code);
			switch (block) {
			case "while":
				preTreatWhile(line);
				break;
			case "if":
				preTreatIf(line);
				break;
			case "for":

				break;
			case "do":
				preTreatDoWhile(line);
				break;
			}

			block = getBlockString(trans.code);
		}

		String result = "";

		for (int i = 0; i < trans.code.length; i++) {
			result = result + trans.code[i] + "\n";
		}
		return result;
	}

	private static int getBlockLine(String[] code) {
		String blockString;

		for (int i = 0; i < code.length; ++i) {

			blockString = containBlock(code[i]);
			if (blockString != "") {
				return i;
			}

		}
		return -1;
	}

	private static String getBlockString(String[] code) {
		String blockString;

		for (int i = 0; i < code.length; ++i) {

			blockString = containBlock(code[i]);
			if (blockString != "") {
				return blockString;
			}

		}
		return "none";
	}

	// returns the end line of a block
	private static Integer getBlockEnd(String[] code, Integer start_line) {

		Integer start = start_line;

		Integer p = 1;
		Integer i = start;

		while (p != 0 && i < code.length) {
			i++;
			if (code[i].contains("}"))
				p--;
			else if (code[i].contains("{"))
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

	public static void preTreatWhile(int line) {

		String lines[] = trans.code;

		int whileLine = line;
		int closeLine = getBlockEnd(lines, whileLine);

		String cond = lines[whileLine].substring(lines[whileLine].indexOf('(') + 1, lines[whileLine].lastIndexOf(')'));

		lines[whileLine] = "goto ( !(" + cond + "), " + Integer.toString(closeLine + 1) + ");";
		lines[closeLine] = "goto (true, " + Integer.toString(whileLine) + ");";

	}

	public static void preTreatIf(int line) throws BadSourceCodeException {

		String lines[] = trans.code;

		int ifLine = line;
		int elseLine = getBlockEnd(lines, ifLine);
		int closeLine = getBlockEnd(lines, elseLine);

		trans.code = mergeArrays(Arrays.copyOfRange(lines, 0, closeLine),
				Arrays.copyOfRange(lines, closeLine + 1, lines.length));

		String cond = lines[ifLine].substring(lines[ifLine].indexOf('(') + 1, lines[ifLine].lastIndexOf(')'));
		trans.code[ifLine] = "goto (" + cond + ", " + Integer.toString(elseLine + 1) + ");";
		trans.code[elseLine] = "goto (true, " + Integer.toString(closeLine) + ");";

		Integer[] mapping = new Integer[trans.code.length];
		for (int i = 0; i < closeLine; ++i) {
			mapping[i] = i;
		}
		for (int i = closeLine; i < trans.code.length; ++i) {
			mapping[i] = i + 1;
		}

		correctGotos(ifLine, closeLine, mapping);

		trans.mapping = mapping;

	}

	public static void preTreatDoWhile(int line) throws BadSourceCodeException {

		String lines[] = trans.code;

		int startLine = line;
		int closeLine = getBlockEnd(lines, startLine);

		String cond = lines[closeLine].substring(lines[closeLine].indexOf('(') + 1, lines[closeLine].lastIndexOf(')'));

		String before[] = Arrays.copyOfRange(lines, 0, startLine);
		String rest[] = Arrays.copyOfRange(lines, startLine + 1, trans.code.length);

		trans.code = mergeArrays(before, rest);
		trans.code[closeLine - 1] = "goto ( (" + cond + "), " + Integer.toString(startLine) + ");";

		Integer[] mapping = new Integer[trans.code.length];
		for (int i = 0; i < startLine; ++i) {
			mapping[i] = i;
		}
		for (int i = startLine; i < trans.code.length; ++i) {
			mapping[i] = i + 1;
		}

		correctGotos(startLine, closeLine, mapping);

		trans.mapping = mapping;

	}

	public static void correctGotos(int start, int end, Integer[] mapping) throws BadSourceCodeException {
		for (int i = 0; i < mapping.length; ++i) {
			if (i >= start && i < end)
				continue;

			if (!trans.code[i].contains("goto"))
				continue;

			Integer toLine = Integer.parseInt(
					trans.code[i].substring(trans.code[i].indexOf(',') + 1, trans.code[i].lastIndexOf(')')).strip());
			toLine = indexOf(mapping, toLine);
			trans.code[i] = trans.code[i].substring(0, trans.code[i].indexOf(',') + 1) + " " + toLine.toString() + ");";

		}
	}

	public static Integer indexOf(Integer[] tab, Integer a) throws BadSourceCodeException {
		for (int i = 0; i < tab.length; ++i) {
			if (tab[i] == a)
				return i;
		}
		throw new BadSourceCodeException("Badly formated source code.");
	}

	public static String[] mergeArrays(String[] array1, String[] array2) {

		int len1 = array1.length;
		int len2 = array2.length;

		String[] mergedArray = new String[len1 + len2];

		System.arraycopy(array1, 0, mergedArray, 0, len1);
		System.arraycopy(array2, 0, mergedArray, len1, len2);

		return mergedArray;
	}
	
	public int getEndOfInitBlocks() {
		return endOfInitBlocks;
	}

}
