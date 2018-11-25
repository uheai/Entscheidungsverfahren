package main;

import java.io.FileNotFoundException;
import java.io.IOException;

public class Main {
	
	public static void main(String[] args) throws IOException {
		
		EquationSystem system = null;
		
		String path = args[0];
		try {
			system = new EquationSystem(path);
		} catch (FileNotFoundException e) {
			System.err.print("Could not read file");
			System.exit(-1);
		} catch (UnsolvableException e) {
			System.out.println("Equation system not solvable");
			System.exit(-1);
		}
		
		//system.printEquationSystem();
		
		Solver solver = new Solver(system);
		
		int[] result = null;
		try {
			result = solver.solve();
		} catch (UnsolvableException e) {
			System.out.println("UNSAT");
			System.exit(0);
		}
		
		System.out.println("SAT");
		for (int i = 0; i < result.length - 1; i++) {
			System.out.print(result[i] + " ");
		}
		
		System.out.println(result[result.length - 1]);
		
		
		
	}
	

}
