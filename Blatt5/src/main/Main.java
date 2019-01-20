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
		
		if (solution == null) {
			System.out.println("UNSAT");
		} else {
			for (int x : solution) {
				System.out.println(x);
			}
		}
	}

}
