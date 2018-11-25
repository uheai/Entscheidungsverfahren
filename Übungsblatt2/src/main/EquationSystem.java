package main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EquationSystem {

	private List<Equation> eqSystem;
	private int numOfEquations;
	private int numOfVars;

	/**
	 * for creation of new variables. This index is not used yet
	 */
	private int nextFreeIndex;

	/**
	 * maps indices to equations which define them, i.e. equation has to be resolved for variable
	 * to get substitution.
	 * So substitution x4 = x1 + x3 - 3 is represented as 4 -> x1 - x4 + x3 = 3
	 */
	private Map<Integer, Equation> substitutions;
	
	public EquationSystem(String path) throws IOException, UnsolvableException {
		File file = new File(path);
		//System.out.println(System.getProperty("user.dir"));
		BufferedReader reader = new BufferedReader(new FileReader(file));
		readFile(reader);
		reader.close();
	}

	private void readFile(BufferedReader reader) throws IOException, UnsolvableException {
		String line = reader.readLine();
		String[] description = line.split(" ");
		numOfEquations = Integer.parseInt(description[0]);
		numOfVars = Integer.parseInt(description[1]);

		nextFreeIndex = numOfVars + 1;

		eqSystem = new ArrayList<>(numOfEquations);
		substitutions = new HashMap<>(numOfVars);

		line = reader.readLine();

		while (line != null) {

			int[] equation = new int[numOfVars + 1];

			String[] values = line.split(" ");
			int num = Integer.parseInt(values[0]);

			int i = 1;
			for (; i < 2 * num - 2; i = i + 2) {
				int coefficient = Integer.parseInt(values[i]);
				int index = Integer.parseInt(values[i + 1]) - 1;

				equation[index] = coefficient;
			}

			equation[numOfVars] = Integer.parseInt(values[i]);

			assert Integer.parseInt(values[i + 1]) == 0;

			normalize(equation);

			if (num == 2) {
				// set substitution
				for (int j = 0; j < numOfVars; j++) {
					int coeff = equation[j];
					if (coeff != 0) {
						Equation eq = new Equation(j + 1, coeff, equation[numOfVars]);
						addSubstitution(j + 1, eq);
						break;
					}
				}
			} else {
				eqSystem.add(new Equation(equation));
			}

			line = reader.readLine();
		}
		
		if (!substitutions.isEmpty()) {
			substitueSolutions();
		}
	}
	
	private void substitueSolutions() throws UnsolvableException {
		for (int index : substitutions.keySet()) {
			for (Equation eq : eqSystem) {
				eq.substitute(substitutions.get(index), index);
			}
		}
	}

	private void normalize(int[] equation) throws UnsolvableException {

		int i = 0;
		int gcd = equation[0];

		int rightSide = equation[equation.length - 1];

		//System.out.println("got equation");
		//printEquation(equation);
		//System.out.println();

		while (i < equation.length - 1 && equation[i] == 0) {
			i++;
		}

		gcd = equation[i];

		// if ggT is 0 all coefficients are zero, so right side of
		// of equation has to be zero too in order to be solvable
		if (gcd == 0 && rightSide != 0) {
			throw new UnsolvableException();
		}

		i++;
		for (; i < equation.length - 1; i++) {
			int num = equation[i];
			if (num == 0) {
				continue;
			}

			gcd = computeGCD(gcd, num);
		}

		//System.out.println("gcd = " + gcd + "\n");

		// right hand side has to be multiple of gcd
		if (rightSide % gcd != 0) {
			throw new UnsolvableException("right hand side is not a multiple of gcd");
		}

		if (gcd != 1) {
			// divide all values by gcd
			for (int j = 0; j < equation.length; j++) {
				equation[j] /= gcd;
			}


			//System.out.println("normalized equation");
			//printEquation(equation);
		}
		//System.out.println("---------\n");

	}

	/**
	 * returns greatest common divisor of two non-zero numbers
	 * 
	 * @param a has to be non-zero
	 * @param b has to be non-zero
	 * @return ggT of a and b
	 */
	private int computeGCD(int a, int b) {
		int h = 0;

		do {
			h = a % b;
			a = b;
			b = h;
		} while (b != 0);

		return Math.abs(a);
	}

	public void addSubstitution(int index, Equation eq) {
		substitutions.put(index, eq);
	}
	
	public void addEquationWithNewVar(int index, int m, Equation eq) {
		eq.addVariable(nextFreeIndex, -m);
		substitutions.put(index, eq);
		
		nextFreeIndex++;
	}
	
	public void printEquationSystem() {
		StringBuilder builder = new StringBuilder();

		// first line
		builder.append(numOfEquations + " " + numOfVars + "\n");

		for (Equation eqation : eqSystem) {
			builder.append(eqation.toString()).append("\n");

		}
		System.out.println(builder.toString());
	}
	
	public void printEquation(int[] equation) {
				StringBuilder builder = new StringBuilder();
				for (int i = 0; i < numOfVars; i++) {
					builder.append(equation[i]).append(" ");
				}
				builder.append(" = ");
				builder.append(equation[numOfVars]);
				System.out.println(builder.toString());
			}
	
	
	public List<Equation> getEqSystem() {
		return eqSystem;
	}
	
	public Map<Integer, Equation> getSubstitutions() {
		return substitutions;
	}
	
	public int getNumOfVars() {
		return numOfVars;
	}
	
	public int getNextFreeIndex() {
		return nextFreeIndex;
	}

}
