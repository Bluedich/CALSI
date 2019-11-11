package org.backend;

public class Variable {
	public enum Type {

		INTEGER, BOOLEAN, FLOAT, DOUBLE, UNDEFINED;

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
				return Type.UNDEFINED;
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
	}
	public void update(Object obj) {
		this.obj = obj;
		this.type = Type.getType(obj);
		this.isArray = obj.getClass().isArray();
	}

	public final String getName() {
		return this.name;
	}

	public final boolean isArray() {
		return this.isArray;
	}

	public final Type getType() {
		return this.type;
	}

	public final Object getObj() {
		return this.obj;
	}

	public final void setObj(Object obj) {
		this.obj = obj;
	}

	protected String name;
	protected boolean isArray;
	protected Type type;
	protected Object obj;
}
