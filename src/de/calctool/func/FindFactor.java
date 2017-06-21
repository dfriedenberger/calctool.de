package de.calctool.func;

public class FindFactor {

	private double c;
	private int parts;

	public FindFactor(double c, int parts) {
		this.c = c;
		this.parts = parts;
	}

	public Factor getFactor(double d) {

		double n = (d * parts) / c;
		long i = Math.round(n);

		if (Math.abs(n - i) < 0.00000000001)
			return new Factor(i);
		return new Factor();

	}

}
