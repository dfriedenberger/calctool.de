package de.calctool.func;

public class Factor {

	private long p = 0;
	private boolean factor = false;

	public Factor(long i) {
		this.p = i;
		this.factor = true;
	}

	public Factor() {
		this.factor = false;
	}

	public Factor mod(int m) {
		if(!factor) return new Factor();
		return new Factor(p % m);
	}
	public boolean is(int i) {
		return factor && p == i;
	}

	public long get() {
		if(!factor) throw new RuntimeException("not a factor");
		return p;
	}

}
