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
		}
		
		system.printEquationSystem();
		
	}
	

}
