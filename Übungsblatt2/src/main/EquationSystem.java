package main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import javax.swing.plaf.synth.SynthSeparatorUI;

public class EquationSystem {

	private Set<Equation> eqSystem;
	private int numOfEquations;
	private int numOfVars;

	public EquationSystem(String path) throws IOException, UnsolvableException {
		File file = new File(path);
		System.out.println(System.getProperty("user.dir"));
		BufferedReader reader = new BufferedReader(new FileReader(file));
		readFile(reader);
		reader.close();

	}

	private void readFile(BufferedReader reader) throws IOException, UnsolvableException {
		String line = reader.readLine();
		String[] description = line.split(" ");
		numOfEquations = Integer.parseInt(description[0]);
		numOfVars = Integer.parseInt(description[1]);

		eqSystem = new HashSet<>(numOfEquations);

		line = reader.readLine();

		int eqNumber = 0;

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

			eqSystem.add(new Equation(equation));
			
			eqNumber++;
			line = reader.readLine();
		}
	}

	private void normalize(int[] equation) throws UnsolvableException {
		
		int i = 0;
		int gcd = equation[0];
		
		int rightSide = equation[equation.length - 1];

		System.out.println("got equation");
		printEquation(equation);
		System.out.println();

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
		for (; i < equation.length; i++) {
			int num = equation[i];
			if (num == 0) {
				continue;
			}

			gcd = computeGCD(gcd, num);
		}

		System.out.println("gcd = " + gcd + "\n");

		// right hand side has to be multiple of gcd
		if (rightSide % gcd != 0) {
			throw new UnsolvableException("right hand side is not a multiple of gcd");
		}

		if (gcd != 1) {
			// divide all values by gcd
			for (int j = 0; j < equation.length; j++) {
				equation[j] /= gcd;
			}

			equation[equation.length - 1] /= gcd;

		
		
		System.out.println("normalized equation");
		printEquation(equation);
		}
		System.out.println("---------\n");
		
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
}
