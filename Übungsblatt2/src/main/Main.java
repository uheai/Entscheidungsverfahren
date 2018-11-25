package main;

import java.io.FileNotFoundException;
import java.io.IOException;

public class Main {

	public static final String path = "./lin-eq-ex/ex-5-5-1.leq";
	
	public static void main(String[] args) throws IOException {
		
		EquationSystem system = null;
		
		try {
			system = new EquationSystem(path);
		} catch (FileNotFoundException e) {
			System.err.print("Could not read file");
			System.exit(-1);
		} catch (UnsolvableException e) {
			System.out.println("Equation system not solvable");
			System.exit(-1);
		}
		
		system.printEquationSystem();
		
		Solver solver = new Solver(system);
		try {
			solver.solve();
		} catch (UnsolvableException e) {
			System.err.println("System not solvable");
		}
		
	}
	

}
