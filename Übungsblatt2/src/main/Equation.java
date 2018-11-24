package main;

import java.util.ArrayList;
import java.util.List;

public class Equation {
	
	private List<Variable> vars;
	private int rhs;
	
	public Equation() {
		vars = new ArrayList<>();
	}
	
	public Equation(int[] equation) {
		vars = new ArrayList<>(equation.length);
		
		for (int i = 0; i < equation.length - 1; i++) {
			vars.add(new Variable(i + 1, equation[i]));
		}
		
		rhs = equation[equation.length - 1];
	}
	
	public void addVariable(Variable var) {
		vars.add(var);
	}
	
	public void addVariable(int index, int coeff) {
		vars.add(new Variable(index, coeff));
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		for (Variable var : vars) {
			int coeff = var.getCoefficient();
			if (coeff == 0) {
				continue;
			}
			
			builder.append(coeff).append(" x").append(var.getIndex()).append(" + ");
		}
		
		builder.setLength(builder.length() - 2);
		builder.append("= ").append(rhs);
		
		return builder.toString();
	}
}
