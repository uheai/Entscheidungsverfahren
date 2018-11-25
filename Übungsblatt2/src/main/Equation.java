package main;

import java.util.HashMap;
import java.util.Map;

public class Equation {
	
	/**
	 * map indices to coefficients, i.e. for expression 4 * x3
	 * this map contains entry 3 -> 4
	 */
	private Map<Integer, Integer> vars;
	private int rhs;
	
	public Equation() {
		vars = new HashMap<>();
	}
	
	public Equation(int[] equation) {
		vars = new HashMap<>(equation.length);
		
		for (int i = 0; i < equation.length - 1; i++) {
			if (equation[i] == 0) {
				continue;
			}
			vars.put(i + 1, equation[i]);
		}
		
		rhs = equation[equation.length - 1];
	}
	
	/**
	 * create equation coeff * x_index = rhs. 
	 * Equation will be transformed to x_index = rhs / coeff 
	 * @param index variable
	 * @param coeff coefficient
	 * @param rhs right hand side
	 * @throws UnsolvableException 
	 */
	public Equation(int index, int coeff, int rhs) throws UnsolvableException {
		vars = new HashMap<>(1);
		vars.put(index, 1);
		if (rhs % coeff != 0) {
			throw new UnsolvableException();
		}
		this.rhs = rhs / coeff;
	}
	
	public void addVariable(int index, int coeff) {
		assert !vars.containsKey(index) : "variable already declared";
		
		vars.put(index, coeff);
	}
	
	public void invert() {
		vars.forEach(((Integer index, Integer value) -> {
			vars.put(index, -value);
		}));
		
		rhs = -rhs;
	}

	
	/**
	 * solve subst for variable with given index, and substitute in 
	 * this equation
	 * @param subst substitution
	 * @param index variable
	 */
	public void substitute(Equation subst, int index) {
		if (!vars.containsKey(index)) {
			return; //equation does not change
		}
		
		int coeff = vars.get(index);
		vars.remove(index);
		
		//add coefficients for left hand side
		for (int i : subst.vars.keySet()) {
			if (i == index) {
				continue;
			}
			
			int varCoeff = vars.getOrDefault(i, 0);
			varCoeff -= coeff * subst.vars.get(i);
			if (varCoeff == 0) {
				vars.remove(i);
			} else {
				vars.put(i, varCoeff);
			}
		}
		
		//add value to right hand side
		rhs -= coeff * subst.rhs;
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		vars.forEach((Integer index, Integer coeff) -> {
			if (coeff != 0) {
			builder.append(coeff).append(" x").append(index).append(" + ");
			}
			
		});

		builder.setLength(builder.length() - 2);
		builder.append("= ").append(rhs);
		
		return builder.toString();
	}
	
	public Map<Integer, Integer> getVars() {
		return vars;
	}
	
	public int getRhs() {
		return rhs;
	}
}
