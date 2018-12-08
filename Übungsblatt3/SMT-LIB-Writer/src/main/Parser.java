package main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class Parser {
	
	private int[][] eqSystem;
	private int numOfVars;
	private int numOfEquations;
	
	public void readFile(String path) throws IOException {
		File file = new File(path);
		BufferedReader reader = new BufferedReader(new FileReader(file));
		
		//read file
		String line = reader.readLine();
		String[] description = line.split(" ");
		numOfEquations = Integer.parseInt(description[0]);
		numOfVars = Integer.parseInt(description[1]);

		eqSystem = new int[numOfEquations][numOfVars + 1];

		line = reader.readLine();
		
		int eqNum = 0;

		while (line != null) {
			
			String[] values = line.split(" ");
			int num = Integer.parseInt(values[0]);

			int i = 1;
			for (; i < 2 * num - 2; i = i + 2) {
				int coefficient = Integer.parseInt(values[i]);
				int index = Integer.parseInt(values[i + 1]);

				eqSystem[eqNum][index] = coefficient;
			}

			eqSystem[eqNum][0] = Integer.parseInt(values[i]);

			assert Integer.parseInt(values[i + 1]) == 0;
			
			eqNum++;
			line = reader.readLine();
		}
		
		
		reader.close();
		
		
	}
	
	public int[][] getEqSystem() {
		return eqSystem;
	}
	
	public String getEqSystemString() {
		StringBuilder sb = new StringBuilder();
		for (int[] eq : eqSystem) {
			int rhs = eq[0];
			for (int i = 1; i < eq.length; i++) {
				sb.append(eq[i]).append(" * ").append("x").append(i).append(" + ");
			}
			sb.substring(0, sb.length() - 3);
			sb.append(" = ").append(rhs);
			sb.append("\n");
		}
		
		return sb.toString();
	}

}
