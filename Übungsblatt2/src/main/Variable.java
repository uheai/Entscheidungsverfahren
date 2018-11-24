package main;

public class Variable {

	private int index;
	private int coefficient;
	
	public Variable(int index, int coefficient) {
		this.index = index;
		this.coefficient = coefficient;
	}
	
	public int getCoefficient() {
		return coefficient;
	}
	
	public int getIndex() {
		return index;
	}
}
