package main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class EquationSystem {
	
	private int[][] eqSystem;
	private int[] rightSide;
	
	public EquationSystem (String path) throws IOException {
		File file = new File(path);
		System.out.println(System.getProperty("user.dir"));
		BufferedReader reader = new BufferedReader(new FileReader(file));
		readFile(reader);
		reader.close();
		
	}
	
	private void readFile(BufferedReader reader) throws IOException {
		String line = reader.readLine();
		String[] description = line.split(" ");
		int numOfLines = Integer.parseInt(description[0]);
		int numOfVars = Integer.parseInt(description[1]);
		
		eqSystem = new int[numOfLines][numOfVars];
		rightSide = new int[numOfLines];
		
		line = reader.readLine();
		
		int eqNumber = 0;
		
		while (line != null) {
			String[] values = line.split(" ");
			int num = Integer.parseInt(values[0]);
			
			int i = 1;
			for (; i < 2*num - 2; i = i + 2) {
				int coefficient = Integer.parseInt(values[i]);
				int index = Integer.parseInt(values[i + 1]) - 1;
				
				eqSystem[eqNumber][index] = coefficient;
			}
			
			rightSide[eqNumber] = Integer.parseInt(values[i]);
			
			assert Integer.parseInt(values[i + 1]) == 0;
			
			eqNumber++;
			line = reader.readLine();
		}
	}
	
	public void printEquationSystem() {
		StringBuilder builder = new StringBuilder();
		
		int numOfEquations = eqSystem.length;
		int numOfVars = eqSystem[0].length;
		
		//first line
		builder.append(numOfEquations + " " + numOfVars + "\n");
		
		for (int i = 0; i < numOfEquations; i++) {
			for (int j = 0; j < numOfVars; j++) {
			builder.append(eqSystem[i][j]);
			builder.append(" ");
			}
			builder.append(" = ");
			builder.append(rightSide[i]);
			builder.append("\n");
		}
		
		System.out.println(builder.toString());
	}
}
