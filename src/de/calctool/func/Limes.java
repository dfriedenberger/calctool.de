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

public class Limes extends MathFunction {
	public String getName() {
		return "lim";
	};

	public MathTerm eval(MathEvaluator evaluator, MathTerm[] term) {
		if (term.length != 3)
			throw new RuntimeException("sum with " + term.length + " != 4 Elements");

		MathTerm val = null;
		if (term[1].isFunction()) {
			String n = term[1].getFuncname();
			if (term[2].is(MathTerm.INFINITY)) {
				val = getValue(evaluator, term[0], n, null);
			} else if (evaluator.isNumber(term[2])) {
				val = getValue(evaluator, term[0], n, term[2].getNumber());
			}
		}

		if (val != null)
			return val;

		MathTerm mt = new MathTerm("lim");
		mt.add(term[0]);
		mt.add(term[1]);
		mt.add(term[2]);
		return mt;
	}

	private MathTerm getValue(MathEvaluator evaluator, MathTerm t, String var, MathNumber val) {
		double base = 8;
		double border = 4096; // < MaxSumLoops
		double i = base;
		int dl = 0;
		double lastd = 0, last = 0;
		int inf = 0;
		int sup = 0;
		MathTerm retval = null;
		
		while (i <= border) // calc time for Calculation of Limes
		{

			// Temporaer
			evaluator.createVarStack();
			double v = i;
			if (val != null)
				v = val.getDouble() - 1.0 / i;
			evaluator.setVar(var, new MathTerm(new MathNumber(v)));
			MathTerm erg = evaluator.evalNumeric(t);
			evaluator.removeVarStack();

			
			if (!evaluator.isNumber(erg))
				break;

			double z = erg.getNumber().getDouble();
			double d = Math.abs(z - last);
			System.out.println("limes: i= " + i + ") erg= " + erg.getNumber().getDouble());
			if (++dl > 2) {
				double lastd2 = 2.0 / 3.0 * lastd;

				if (d > lastd && z > last)
					inf++;
				else
					inf = 0;

				if (d < lastd2)
					sup--;
				else
					sup = 0;

				System.out.println("i " + i + " diff " + d + " last " + lastd + " lastd2 " + lastd2 + " inf/sup " + inf
						+ "/" + sup);

				if (inf > 3 && i * 2 > border) {
					retval = new MathTerm(MathTerm.INFINITY);
					break;
				}
				if (sup < 3 && i * 2 > border) {
					retval = interval(evaluator, z, lastd2);
					break;
				}
			}
			last = z;
			lastd = d;
			i = 2 * i;
		}

		if (retval != null)
			return evaluator.eval(retval);

		return retval;
	}

	private MathTerm interval(MathEvaluator evaluator, double val, double iv) {
		double a = val - iv;
		double b = val - iv;
		MathTerm spez[] = new MathTerm[] { new MathTerm(new MathNumber(0)), new MathTerm("e"),
				new MathTerm("/", new MathTerm(new MathNumber(1)), new MathTerm("e")), new MathTerm("pi"),
				new MathTerm("/", new MathTerm(new MathNumber(1)), new MathTerm("pi")) };
		String factor[] = new String[] { "+", "-", "/", "*", "^" };
		if (iv < 0.001) {
			for (int i = 0; i < spez.length; i++) {
				for (int f = 0; f < factor.length; f++) {
					for (long n = 1; n <= 6; n++) {
						MathTerm ts = new MathTerm(factor[f], spez[i], new MathTerm(new MathNumber(n)));
						MathTerm t = evaluator.evalNumeric(ts);

						if (!evaluator.isNumber(t))
							continue;

						System.out.println((val - iv) + " .. " + t.getNumber() + " .. " + (val + iv));

						if (evaluator.is("<", t.getNumber(), new MathNumber(val - iv)))
							continue;
						if (evaluator.is(">", t.getNumber(), new MathNumber(val + iv)))
							continue;
						return ts;
					}
				}
			}
		}
		return new MathTerm(new MathNumber(val));
	}

	@Override
	public MathImage createImage(MathImageCreator imageCreator, MathTerm[] child) {
		// TODO Auto-generated method stub
		return null;
	}
}
