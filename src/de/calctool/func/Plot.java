// part of the mathematic virtuell machine
// Copyright (C) 2006  Dirk Friedenberger, projekte@frittenburger.de

// This program is free software; you can redistribute it and/or
// modify it under the terms of the GNU General Public License
// as published by the Free Software Foundation; either version 2
// of the License, or (at your option) any later version.

// This program is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU General Public License for more details.

// You should have received a copy of the GNU General Public License
// along with this program; if not, write to the Free Software
// Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
package de.calctool.func;

import de.calctool.eval.MathEvaluator;
import de.calctool.image.MathImageCreator;
import de.calctool.vm.*;

public class Plot extends MathFunction {
	public String getName() {
		return "plot";
	};

	public MathTerm eval(MathEvaluator evaluator, MathTerm[] term) {
		
		 //the follow strinking values should calculated for function 
		double x[] = new double[101]; double y[] = new double[x.length]; 
		for(int i = 0;i < x.length;i++)
		{
			x[i] = 0.1 * (i - (x.length - 1)/2);
			y[i] = Double.NaN;
		}
		
		
		 if(term.length != 1) throw new RuntimeException( "plot with more than 1 argument "+term.length); 
		 		
		 String names[] = MathTerm.getNames(term[0]);

		 
		 
		
			 
			 //String function = term[0].getFuncname();
			 //System.out.println(" search function "+function); 
			 // MathFunction f =  MathFunction.get(function,false); 		
			 //MathTerm erg = f.eval(rt,new MathTerm[]{num}); 
			 
			 
		 if(names.length != 1) throw new RuntimeException( "plot with more than 1 argument: "+names.length);
		 
		 //temp
		 evaluator.createVarStack(); 

		 for(int i = 0;i < x.length;i++) { 
			 MathTerm num = new MathTerm(new MathNumber(x[i])); 
			 evaluator.setVar(names[0],num);
			 MathTerm erg = evaluator.evalNumeric(term[0]); 
			 if(evaluator.isNumber(erg)) 
				 y[i] = erg.getNumber().getDouble();			 
			 
		 }
		 evaluator.removeVarStack(); 

		
		 //double u[] = getUnits(miny,maxy - miny); 
		 //getStr(u[i]),
		

	return new MathTerm(new MathDataSet(x,y));// virtuell
	
	}

	private String getStr(double d) {
		long vk = (long) d;
		double rest = d - vk;
		if (d < 0)
			d *= -1;

		String format = "" + vk;
		long nk = (-1) * ((long) (Math.log(d) / Math.log(10)) - 2);
		if (nk > 0)
			format += ".";
		for (int i = 0; i < nk; i++) {
			rest = rest * 10;
			long st = (long) rest;
			format += st;
			rest -= st;
		}
		return format;
	}

	public final static int base = 10;
	public final static int units = 11;

	private double[] getUnits(double s, double l) {
		long f = (long) (Math.log(l) / Math.log(base)) - 1;
		double part = Math.pow(base, f);
		long fac = 1;
		long ns = (long) ((s / part) + 0.5);
		long ne = (long) (((s + l) / part) + 0.5);

		while ((ns + (fac + 1) * units) * part <= (ne * part))
			fac += 1;

		System.out.println(" f = " + f + " part = " + part + " ns " + (ns * part) + " calcne "
				+ ((ns + fac * units) * part) + " ne " + (ne * part));

		double p[] = new double[units];
		for (int i = 0; i < p.length; i++)
			p[i] = (ns + fac * i) * part;
		return p;
	}

	@Override
	public MathImage createImage(MathImageCreator imageCreator, MathTerm[] child) {
		return null;
	}

}
