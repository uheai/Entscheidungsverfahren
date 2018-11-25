package main;

import java.util.Map;

public class Solver {

	private EquationSystem system;

	public Solver(EquationSystem system) {
		this.system = system;
	}

	public void solve() throws UnsolvableException {
		boolean changed = false;
		do {
			changed = reduce();
		} while (changed);
	}

	private boolean reduce() throws UnsolvableException {

		boolean singleLhs = false;
		// find equation with coefficient 1 or -1
		// or find equation with only one variable on left hand side
		Equation equation = null;
		int index = 0;
		outer: for (Equation eq : system.getEqSystem()) {
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

		System.out.println("found equation for substitution");
		System.out.println(equation.toString() + "\n");

		//substitute this equation into all others and remove equation from system.
		system.getEqSystem().remove(equation);
		
		if (singleLhs) {
			equation = new Equation(index, equation.getVars().get(index), equation.getRhs());
		}
		
		system.addSubstitution(index, equation);

		for (Equation eq : system.getEqSystem()) {
			eq.substitute(equation, index);
		}

		System.out.println("resulting equation system after substitution");
		system.printEquationSystem();

		return true;

	}

	public int mod(int a, int b) {
		return a - b * (int) Math.floor((double) a / b + 0.5);
	}
}
