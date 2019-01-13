package main;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BitBlasterMul {
	
	/**
	 * number of bits needed to represent a and b
	 */
	private int n;
	private int[] a;
	private int[] b;
	
	/**
	 * next free variable
	 */
	private int var_index;
	
	private BufferedWriter out;
	private int numOfClauses;
	
	/**
	 * clauses resulting from an shift operation
	 */
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
	}
	
	public void makeFormula() throws IOException {
		createShiftClauses();
		createSumOfShiftClauses();
		System.out.println("number of clauses = " + numOfClauses);
	}
	
	/**
	 * set {@link #n} to smallest integer such that a,b <= 2^n
	 * @param a factor
	 * @param b factor
	 */
	private void setLength(int a, int b) {
		n = 0;
		int max = Math.max(a, b);
		while (max != 0) {
			max >>= 1;
			n++;
		}
		
		//System.out.println("n = " + n);
	}
	
	/**
	 * create bit representation for given array
	 * @param arr target of bit representation
	 * @param x value to set
	 * @throws IOException 
	 */
	private void setArray(int[] arr, int x) throws IOException {
		for (int i = 0; i < n; i++) {
			int bit = (x >> i) % 2;
			arr[i] = (-1 + 2 * bit) * var_index;
			out.write(arr[i] + " 0\n");
			var_index++;
			numOfClauses++;
		}
	}
	
	/**
	 * shift {@link #a} by bits set in {@link #b}
	 * @throws IOException
	 */
	private void createShiftClauses() throws IOException {
		for (int i = 0; i < n; i++) {
			if (b[i] > 0) {
				createSingleShiftClause(a, i);
			}
		}
	}
	
	/**
	 * create clauses describing sum of created clauses in {@link #createShiftClauses()}
	 * @throws IOException
	 */
	private void createSumOfShiftClauses() throws IOException {
		int[] sum = shiftClauses.get(0);
		
		for (int i = 1; i < shiftClauses.size(); i++) {
			sum = createSumOfShiftClauses(sum, shiftClauses.get(i));
		}
		
		System.out.println("variables containing result: " + Arrays.toString(sum));
		
	}
	
	/**
	 * create clause describing sum of given arrays
	 * @param arr1
	 * @param arr2
	 * @return clause containing result variables
	 * @throws IOException
	 */
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
	
	/**
	 * Helper method used in {@link #createSumOfShiftClauses(int[], int[])}.
	 * Appends clause to file
	 * @param a
	 * @param b
	 * @param c
	 * @param out
	 * @throws IOException
	 */
	private void appendClause(int a, int b, int c, int out) throws IOException {
		this.out.write(a + " " + b + " " + c + " " + out + " 0\n");
		numOfClauses++;
	}
	
	/**
	 * shift arr by c
	 * @param arr
	 * @param c
	 * @throws IOException
	 */
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
	
	/**
	 * add cnf clauses for x <-> y
	 * @param x
	 * @param y
	 * @throws IOException
	 */
	private void addEquivClause(int x, int y) throws IOException {
		//both variables need to have same sign
		int a = Math.abs(x);
		int b = Math.abs(y);
		out.append(-a + " " + b + " 0\n");
		out.append(-b + " " + a + " 0\n");
		numOfClauses += 2;
	}
	
	/**
	 * create array containing new positive variables
	 * @param length length of array to create
	 * @return
	 */
	private int[] newVarArr(int length) {
		int[] arr = new int[length];
		for (int i = 0; i < length; i++) {
			arr[i] = var_index;
			var_index++;
		}
		
		return arr;
	}
	
	

}
