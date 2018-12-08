package main;

import java.io.FileWriter;
import java.io.IOException;

public class SMT_LIB_Writer {

	private int[][] eqSystem;
	
	public SMT_LIB_Writer(int[][] eqSystem) {
		this.eqSystem = eqSystem;
	}
	
	public void write(String fileName) throws IOException {
		FileWriter writer = new FileWriter(fileName);
		writer.write("(set-option :produce-models true)\n");
		writer.write("(set-logic QF_LIA)\n");
		
		//create variables
		for (int i = 1; i <= eqSystem.length; i++) {
			writer.write("(declare-const x" + i + " Int)\n");
		}
		
		//create equations
		writer.write("(assert\n");
		writer.write("\t(and\n");
		
		for (int i = 0; i < eqSystem.length; i++) {
			int[] eq = eqSystem[i];
			int rhs = eq[0];
			
			writer.write("\t\t(= (+ ");
			for (int j = 1; j < eq.length; j++) {
				if (eq[j] == 0) {
					continue;
				}
				writer.write("(* " + eq[j] + " x" + j + ") ");
			}
			
			writer.write(") " + rhs + ")\n");
		}
		
		writer.write("))\n");
		writer.write("(check-sat)\n");
		writer.write("(get-model)\n");
		
		writer.close();
	}
}
