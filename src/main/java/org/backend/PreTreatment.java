package org.backend;

import java.util.Arrays;

import bsh.EvalError;
import bsh.Interpreter;

public class PreTreatment {
	// The original source code as inputed by the user
	private String source;
	// Treated lines of code to be executed by the processes
	private String preTreatedSource;
	// The code that needs to be run upon initialization of each process.
	// It is important that it does not contain shared variable initialization, as
	// the latter is done
	// during pre-treatment.
	private String initialisationBlock;
	private Variable[] sharedVars;
	private Variable[] localVars;

	public PreTreatment(String source) throws BadSourceCodeException {
		this.source = source;
		preTreat();
	}

	private void preTreat() throws BadSourceCodeException {
		String lines[] = source.split("\\r?\\n");
		removeComments(lines); // If the code contains '// ...' comments, they are removed
		int startOfCode = preTreatInitialisationBlocks(lines);
		// We don't want the initialization blocks in the code the processes will run
		String[] linesWithoutInitBlocks = Arrays.copyOfRange(lines, startOfCode, lines.length);
		preTreatedSource = preTreatWhile(linesWithoutInitBlocks);
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

		// Start of code is the line after the last empty line of the code blocks
		return blockIndexes[7] + 1;
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
	 */
	private void declareLocalVars(int startOfBlock, String[] blockLines, Interpreter interpreter) {
		localVars = new Variable[blockLines.length];
		String line;
		String varName;
		for (int i = 0; i < blockLines.length; i++) {
			line = blockLines[i];
			varName = parseVarName(line);
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
			varName = parseVarName(line);
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
	 */
	private String parseVarName(String line) {
		// Eventual leading and trailing whitespaces are removed, in addition to the
		// ';'.
		line = line.trim();
		line = line.substring(0, line.length() - 1);
		// We assume a format of "variableType variableName;"
		return line.split("\\s+")[1];
	}

	private String preTreatWhile(String[] lines) {

		int whileLine = 0, closeLine = 0;
		String cond = null;

		for (int i = 0; i < lines.length; ++i) {

			if (lines[i].contains("while")) {
				whileLine = i;
				cond = lines[i].substring(lines[i].indexOf('(') + 1, lines[i].indexOf(')'));
			}

			if (lines[i].contains("}"))
				closeLine = i;
		}

		lines[whileLine] = "goto ( !(" + cond + "), " + Integer.toString(closeLine + 1) + ");";
		lines[closeLine] = "goto (true, " + Integer.toString(whileLine) + ");";

		String result = "";

		for (int i = 0; i < lines.length; i++) {
			result = result + lines[i] + "\n";
		}
		return result;
	}

	public void testPreTreatment() {
		for (int i = 0; i < sharedVars.length; i++) {
			//System.out.println();
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

}
