package main;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Hensel {

	private int k;
	private int[] coeff;
	private Hensel deriv;

	public Hensel(int k, int[] coeff) {
		this.k = k;
		this.coeff = coeff;
	}

	public void derive() {
		if (deriv != null) {
			throw new RuntimeException("can only derive once");
		}

		int[] deriv_arr = new int[coeff.length - 1];
		for (int i = 1; i < coeff.length; i++) {
			deriv_arr[i - 1] = coeff[i] * i;
		}

		deriv = new Hensel(k, deriv_arr);
	}

	public Hensel getDeriv() {
		return deriv;
	}

	public int eval(int x) {
		int res = 0;
		for (int i = 0; i < coeff.length; i++) {
			res += Math.pow(x, i) * coeff[i];
		}

		return res;
	}

	public int derivEval(int x) {
		return deriv.eval(x);
	}

	public Set<Integer> solve() {
		HashSet<Integer> result = new HashSet<>();

		// k = 1
		if (eval(0) % 2 == 0) {
			result.add(0);
		}
		if (eval(1) % 2 == 0) {
			result.add(1);
		}

		if (result.isEmpty()) {
			return null; // no solution
		}

		// lift
		for (int i = 2; i <= k; i++) {
			HashSet<Integer> newRes = new HashSet<>();
			for (int x : result) {
				if (derivEval(x) % 2 == 0) {
					if (eval(x) % (int) (Math.pow(2, i)) != 0) {
						continue; // x cannot be lifted
					} else {
						int pow = (int) Math.pow(2, i - 1);
						int x1 = x + pow;

						newRes.add(x);
						newRes.add(x1);
					}
				} else { // derivEval(x) == 1 (mod 2)
					int x1 = (x - eval(x)) % (int) Math.pow(2, i);
					newRes.add(x1);
				}
			}

			result = newRes;
		}
		
		return result;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < coeff.length; i++) {
			sb.append(coeff[i] + "x^" + i + " + ");
		}

		sb.setLength(sb.length() - 2);
		sb.append(" = 0 (mod 2^" + k + ")");

		return sb.toString();
	}

}
