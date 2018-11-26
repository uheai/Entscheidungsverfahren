package main;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Solver {

	private EquationSystem system;

	public Solver(EquationSystem system) {
		this.system = system;
	}

	public int[] solve() throws UnsolvableException {
		while (system.getEqSystem().size() > 0) {
			
			
			// reduce equation system, find "simple" equations and substitute them into others
			boolean changed = false;
			do {
				changed = reduce();
			} while (changed);

			removeRedundant();
			
			if (system.getEqSystem().isEmpty()) {
				break;
			}
			// create new equation
			createNewEquationAndSubstitute();

			//system.printEquationSystem();;
		}

		// resolve substitutions
		Map<Integer, Equation> substitutions = system.getSubstitutions();

		
		//perform some sort of fix point iteration. Substitute equations into each other until solution is found.
		boolean changed = false;
		do {

			changed = false;
			for (int index = 1; index <= system.getNumOfVars(); index++) {
				Equation equation = substitutions.get(index);
				
				if (equation == null) {
					//set value arbitrarily
					equation = new Equation(index, 1, 0);
					substitutions.put(index, equation);
				}
				if (equation.getVars().size() == 1) {
					continue;
				}
				changed = true;
				
				Set<Integer> usedVars = new HashSet<>();
				for (int usedVar : equation.getVars().keySet()) {
					if (usedVar == index) {
						continue;
					}
					
					usedVars.add(usedVar);
				}
				
				for (int usedVar : usedVars) {
					Equation usedEquation = substitutions.get(usedVar);
					if (usedEquation == null) {
						usedEquation = new Equation(usedVar, 1, 0);
						substitutions.put(usedVar, usedEquation);
					}
					equation.substitute(usedEquation, usedVar);
				}
			}
		} while (changed);

		int[] result = new int[system.getNumOfVars()];
		
		for (int i = 0; i < result.length; i++) {
			Equation eq = substitutions.get(i + 1);
			result[i] = eq.getRhs() * eq.getVars().get(i + 1); //coefficient is +1 or -1
		}
		
		return result;

	}
	
	/**
	 * eliminates equation like 0 = 0
	 */
	private void removeRedundant() {
		Set<Equation> set = new HashSet<>();
		
		for (Equation eq : system.getEqSystem()) {
			if (eq.getVars().isEmpty()) {
				set.add(eq);
			}
		}
		
		for (Equation eq : set) {
			system.getEqSystem().remove(eq);
		}
	}

	private boolean reduce() throws UnsolvableException {

		boolean singleLhs = false;
		// find equation with coefficient 1 or -1
		// or find equation with only one variable on left hand side
		Equation equation = null;
		int index = 0;
		outer: for (Equation eq : system.getEqSystem()) {
			if (eq.getVars().isEmpty()) {
				continue;
			}
			if (eq.getVars().keySet().size() == 1) {
				// found equation with only one variable on left hand side
				equation = eq;
				index = eq.getVars().keySet().iterator().next(); // take first element
				singleLhs = true;
				break;
			}
			Map<Integer, Integer> vars = eq.getVars();
			for (int i : vars.keySet()) {
				if (Math.abs(vars.get(i)) == 1) {
					index = i;
					equation = eq;
					break outer;
				}
			}
		}

		if (equation == null) {
			return false;
		}

		//System.out.println("found equation for substitution");
		//System.out.println(equation.toString() + "\n");

		// substitute this equation into all others and remove equation from system.
		system.getEqSystem().remove(equation);

		if (singleLhs) {
			equation = new Equation(index, equation.getVars().get(index), equation.getRhs());
		}

		system.addSubstitution(index, equation);

		for (Equation eq : system.getEqSystem()) {
			eq.substitute(equation, index);
		}

		//System.out.println("resulting equation system after substitution");
		//system.printEquationSystem();

		return true;

	}

	private void createNewEquationAndSubstitute() throws UnsolvableException {
		// take first equation
		if (system.getEqSystem().isEmpty()) {
			return;
		}

		Equation eq = system.getEqSystem().get(0);
		Equation equation = new Equation();

		// find variable with minimal coefficient
		int index = eq.getVars().keySet().iterator().next();
		int m = Math.abs(eq.getVars().get(index));

		for (int i : eq.getVars().keySet()) {
			int v = Math.abs(eq.getVars().get(i));
			if (v < m) {
				index = i;
				m = v;
			}
		}

		if (eq.getVars().get(index) != m) {
			// coefficient is negative, invert equation
			eq.invert();
		}

		m++; // m = a_k + 1
		// create new equation
		for (int i : eq.getVars().keySet()) {
			int value = eq.getVars().get(i);
			equation.addVariable(i, mod(value, m));
		}
		// set rhs
		equation.setRhs(mod(eq.getRhs(), m));

		system.addEquationWithNewVar(index, m, equation);

		//System.out.println("created new equation");
		//System.out.println(equation.toString());
		//System.out.println();

		// substitute equation into all others
		for (Equation other : system.getEqSystem()) {
			other.substitute(equation, index);
		}

		//System.out.println("system after substitution");
		//system.printEquationSystem();
		//System.out.println("------------\n");
	}

	/**
	 * computes modulus as described in slides.
	 * @param a
	 * @param b
	 * @return
	 */
	public int mod(int a, int b) {
		return a - b * ((int) Math.floor((double) a / b + 0.5));
	}
}
