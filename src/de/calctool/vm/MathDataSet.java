package de.calctool.vm;

public class MathDataSet {

	public double[] x;
	public double[] y;

	public MathDataSet(double[] x, double[] y) {
		this.x = x;
		this.y = y;
	}

	public double getMinX() {
		return min(x);
	}

	public double getMaxX() {
		return max(x);
	}
	
	public double getMinY() {
		return min(y);
	}

	public double getMaxY() {
		return max(y);
	}
	

	private double min(double[] v) {
		double min = Double.NaN;
		for(int i = 0;i < v.length;i++)
		{
			if(Double.isNaN(v[i])) continue;
			if(Double.isNaN(min)) 
				min = v[i];
			if(min > v[i]) min = v[i];
		}
		return min;
	}

	
	private double max(double[] v) {
		double max = Double.NaN;
		for(int i = 0;i < v.length;i++)
		{
			if(Double.isNaN(v[i])) continue;
			if(Double.isNaN(max)) 
				max = v[i];
			if(max < v[i]) max = v[i];
		}
		return max;
	}

	
}
