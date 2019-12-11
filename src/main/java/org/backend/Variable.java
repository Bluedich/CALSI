package org.backend;

import java.util.Arrays;

public class Variable {
	protected String name;
	protected boolean isArray;
	protected Type type;
	protected Object obj;

	public enum Type {

		INTEGER, BOOLEAN, FLOAT, DOUBLE, NULL;

		static protected final Type getType(Object obj) {
			if (obj instanceof Integer || obj instanceof Integer[]) {
				return Type.INTEGER;
			} else if (obj instanceof Boolean || obj instanceof Boolean[]) {
				return Type.BOOLEAN;
			} else if (obj instanceof Float || obj instanceof Float[]) {
				return Type.FLOAT;
			} else if (obj instanceof Double || obj instanceof Double[]) {
				return Type.DOUBLE;
			} else {
				return Type.NULL;
			}
		}
	}

	public Variable(String name, Object obj) {
		this.name = name;
		this.obj = obj;
		this.type = Type.getType(obj);
		this.isArray = obj.getClass().isArray();
	}

	public Variable(String name) {
		this.name = name;
		this.obj = null;
		this.type = Type.NULL;
		this.isArray = false;
	}

	public void update(Object obj) {
		if (obj != null) {
			this.obj = obj;
			this.isArray = obj.getClass().isArray();
		}
		this.type = Type.getType(obj);
	}

	public final String getName() {
		return this.name;
	}

	public final boolean isArray() {
		return this.isArray;
	}

	public final Object getObj() {
		return this.obj;
	}

	public String getType() {
		String typeAsString = new String();
		switch (type) {
			case INTEGER:
				typeAsString += "Integer";
				break;
			case BOOLEAN:
				typeAsString += "Boolean";
				break;
			case FLOAT:
				typeAsString += "Float";
				break;
			case DOUBLE:
				typeAsString += "Double";
				break;
			case NULL:
				typeAsString += "NULL";
				break;
			default:
				typeAsString += "Unrecognized type";
		}
		
		if (isArray) {
			typeAsString += "[]";
		}
		
		return typeAsString;
	}
	
	public String getValue() {		
		if (isArray) {
			// Convert to a readable string potentially recursive arrays
			return Arrays.deepToString((Object[]) obj);
		}
		
		if (type == Type.NULL) {
			return "";
		}
		
		return obj.toString();
	}
}
