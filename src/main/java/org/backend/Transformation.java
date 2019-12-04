package org.backend;

public class Transformation {
	
	String[] code;
	Integer[] mapping;
	
	
	public Transformation() {
		
	}
	
	public Transformation(String[] c, int n) {

		this.code = c;
		
		Integer[] tab = new Integer[n];
		for(int i=0; i<n; ++i) {
			tab[i] = i;
		}
		
		this.mapping = tab;
	}
	
}
