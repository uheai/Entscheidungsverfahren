package main;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class Main {
	
	public static final String OUT_FILE = "out.txt";

	public static void main(String[] args) throws IOException {
		int a = Integer.parseInt(args[0]);
		int b = Integer.parseInt(args[1]);
		
		BufferedWriter writer = new BufferedWriter(new FileWriter(OUT_FILE));
		
		BitBlasterMul mul = new BitBlasterMul(a, b, writer);
		mul.makeFormula();
		
		writer.close();
	}

}
