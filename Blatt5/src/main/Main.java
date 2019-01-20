package main;

import java.util.Set;

public class Main {

	public static void main(String[] args) {

		int n = args.length;
		int k = 0;
		int[] arr = new int[n - 1];

		try {
			k = Integer.parseInt(args[0]);
			for (int i = n - 1; i > 0; i--) {
				arr[n - 1 - i] = Integer.parseInt(args[i]);
			}
		} catch (NumberFormatException e) {
			System.err.println("Invalid input");
			System.exit(0);
		}
		
		Hensel hensel = new Hensel(k, arr);
		hensel.derive();
		Set<Integer> solution = hensel.solve();
		
		if (solution.isEmpty()) {
			System.out.println("UNSAT");
		} else {
			int pow = (int) Math.pow(2, k);
			for (int x : solution) {
				//output positive solution
				int s = x;
				while (s < 0) {
					s += pow;
				}
				System.out.println(s);
			}
		}
	}

}
