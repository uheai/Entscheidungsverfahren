package main;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class Main {

	public static final String OUT_FILE = "out.txt";

	public static void main(String[] args) {
		
		if (args.length != 2) {
			System.out.println("Wrong number of arguments! Expected two integers");
			System.exit(0);
		}
		
		int a = 0;
		int b = 0;
		
		try {
			a = Integer.parseInt(args[0]);
			b = Integer.parseInt(args[1]);
		} catch (NumberFormatException e) {
			System.out.println("Invalid argument(s)!");
			System.exit(0);
		}
		
		BufferedWriter writer;
		try {
			writer = new BufferedWriter(new FileWriter(OUT_FILE));
			
			BitBlasterMul mul = new BitBlasterMul(a, b, writer);
			mul.makeFormula();
			
			writer.close();
		} catch (IOException e) {
			System.err.println("An error occured: " + e.getMessage());
		}
		
		
	}

}
