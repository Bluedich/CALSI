package org.backend;

public class VariableInfo {
	private String name;
	private String value;
	private String type;
	
	public VariableInfo(String name, String value, String type) {
		this.name = name;
		this.value = value;
		this.type = type;
	}

	/**
	 * Get the name of the variable.
	 * @return The name of the variable
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Get the value of the variable
	 * @return The value of the variable, represented as a String
	 */
	public String getValue() {
		return value;
	}
	
	/**
	 * Get the type of the variable
	 * @return The type of the variable, represented as a String
	 */
	public String getType() {
		return type;
	}
}
