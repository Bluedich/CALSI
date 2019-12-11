package org.backend;

/**
 * Data class storing information about a variable in easy to use formats.
 * @author Hugo
 *
 */
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
	 * @return the name of the variable
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Get the value of the variable.
	 * @return the value of the variable, converted to a String
	 */
	public String getValue() {
		return value;
	}
	
	/**
	 * Get the type of the variable.
	 * @return the type of the variable, converted to a String
	 */
	public String getType() {
		return type;
	}
}
