package org.backend;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class MainPretreat {

	public static void main(String[] args) throws BackEndException, IOException {

		// just reading a file and storing it to the String content.
		
		BufferedReader reader = new BufferedReader(new FileReader("tests/test1.txt"));
		StringBuilder stringBuilder = new StringBuilder();
		char[] buffer = new char[10];
		while (reader.read(buffer) != -1) {
			stringBuilder.append(new String(buffer));
			buffer = new char[10];
		}
		reader.close();
		String content = stringBuilder.toString();
		
		
		
		// The object that will be used to convert the source code
		BlocksConversion bc = new BlocksConversion(); 
	
		
		// Pre-treating a source code and saving the result in the object
		bc.preTreat(content);
		
		// the size of the new source code
		System.out.println("The number of lines in the new source code is:");
		System.out.println(bc.getNumberOfLines());
		System.out.println("");

		// the new source code as String
		System.out.println("The new source code is:");
		System.out.println(bc.getNewSourceCode());
		
		// the new source code as String[]
		String[] code = bc.getNewSourceCodeArray();
		
		// finding the original line number of the line number 11 in the new source code.
		System.out.println("The line number 14 maps to the following line number in the original source code:");
		System.out.println(bc.originalLineNumber(14));
		
	}

}
