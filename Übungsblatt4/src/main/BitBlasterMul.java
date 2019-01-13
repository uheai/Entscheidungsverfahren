package main;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class BitBlasterMul {
	
	private int n; //array length
	private int[] a;
	private int[] b;
	
	private int var_index;
	
	private BufferedWriter out;
	private int numOfClauses;
	
	private List<int[]> shiftClauses;
	
	public BitBlasterMul(int a, int b, BufferedWriter out) throws IOException {
		setLength(a, b);
		var_index = 1;
		numOfClauses = 0;
		
		this.out = out;
		
		this.a = new int[n];
		this.b = new int[n];
		setArray(this.a, a);
		setArray(this.b, b);
		
		shiftClauses = new ArrayList<>();
		
		//System.out.println(Arrays.toString(this.a));
		//System.out.println(Arrays.toString(this.b));
	}
	
	public void makeFormula() throws IOException {
		createShiftClauses();
		createSumOfShiftClauses();
		System.out.println("\nnumber of clauses = " + numOfClauses);
	}
	
	private void setLength(int a, int b) {
		n = 0;
		int max = Math.max(a, b);
		while (max != 0) {
			max >>= 1;
			n++;
		}
		
		System.out.println("n = " + n);
	}
	
	private void setArray(int[] arr, int x) throws IOException {
		for (int i = 0; i < n; i++) {
			int bit = (x >> i) % 2;
			arr[i] = (-1 + 2 * bit) * var_index;
			out.write(arr[i] + " 0\n");
			var_index++;
			numOfClauses++;
		}
	}
	
	private void createShiftClauses() throws IOException {
		for (int i = 0; i < n; i++) {
			if (b[i] > 0) {
				createSingleShiftClause(a, i);
			}
		}
	}
	
	private void createSumOfShiftClauses() throws IOException {
		int[] sum = shiftClauses.get(0);
		
		for (int i = 1; i < shiftClauses.size(); i++) {
			sum = createSumOfShiftClauses(sum, shiftClauses.get(i));
		}
		
		System.out.println("vars containing result: " + Arrays.toString(sum));
		
	}
	
	private int[] createSumOfShiftClauses(int[] arr1, int[] arr2) throws IOException {
		int[] carry = newVarArr(2 * n); 
		int[] out = newVarArr(2 * n);
		
		this.out.write(-1 * carry[0] + " 0\n"); //inital carry is constant
		numOfClauses++;
		

		for (int i = 0; i < 2 * n; i++) {
			int a = arr1[i];
			int b = arr2[i];
			int c = carry[i];
			int res = out[i];
			
			//clauses for result, ie sum of a and b
			appendClause(-a, -b, -c, res);
			appendClause(-a, b, -c, -res);
			appendClause(a, -b, -c, -res);
			appendClause(a, b, -c, res);
			appendClause(-a, -b, c, -res);
			appendClause(-a, b, c, res);
			appendClause(a, -b, c, res);
			appendClause(a, b, c, -res);
			
			//clauses for carry
			if (i < 2 * n - 1) {
				int nextCary = carry[i + 1];
				
				appendClause(-a, -b, -res, nextCary);
				appendClause(-a, b, -res, -nextCary);
				appendClause(a, -b, -res, -nextCary);
				appendClause(a, b, -res, -nextCary);
				appendClause(-a, -b, res, nextCary);
				appendClause(-a, b, res, nextCary);
				appendClause(a, -b, res, nextCary);
				appendClause(a, b, res, -nextCary);
			}
			
		}
		
		return out;
	}
	
	private void appendClause(int a, int b, int c, int out) throws IOException {
		this.out.write(a + " " + b + " " + c + " " + out + " 0\n");
		numOfClauses++;
	}
	
	private void createSingleShiftClause(int[] arr, int c) throws IOException {
		int[] shifted = newVarArr(2 * n);
		shiftClauses.add(shifted);
		for (int i = 0; i < c; i++) {
			out.write(-1 * shifted[i] + " 0\n");
			numOfClauses++;
		}
		
		for (int i = 0; i < arr.length; i++) {
			//shifted[i + c] = arr[i];
			addEquivClause(arr[i], shifted[i + c]);
		}
		
		for (int i = arr.length + c; i < shifted.length; i++) {
			out.write(-1 * shifted[i] + " 0\n");
			numOfClauses++;
		}
	}
	
	private void addEquivClause(int x, int y) throws IOException {
		int a = Math.abs(x);
		int b = Math.abs(y);
		out.append(-a + " " + b + " 0\n");
		out.append(-b + " " + a + " 0\n");
		numOfClauses += 2;
	}
	
	private int[] newVarArr(int length) {
		int[] arr = new int[length];
		for (int i = 0; i < length; i++) {
			arr[i] = var_index;
			var_index++;
		}
		
		return arr;
	}
	
	

}
