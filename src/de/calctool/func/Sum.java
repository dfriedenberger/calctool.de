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

public class Sum extends MathFunction {
	
	public String getName() {
		return "sum";
	};

	public MathTerm eval(MathEvaluator evaluator, MathTerm[] term) {
		if (term.length != 4)
			throw new RuntimeException("sum with " + term.length + " != 4 Elements");

		long start = 0, ende = 0;
		int par = 0;

		if (term[0].isFunction())
			par++;

		if (evaluator.isNumber(term[1])) {
			MathNumber n1 = term[1].getNumber();
			if (n1.isLong()) {
				start = n1.getNumerator();
				par++;
			}
		}
		if (evaluator.isNumber(term[2])) {
			MathNumber n2 = term[2].getNumber();
			if (n2.isLong()) {
				ende = n2.getNumerator();
				par++;
			}
		}

		MathTerm erg = null;

		if (par == 3) {

			// Varstack
			evaluator.createVarStack();
			if (ende - start < MathRuntime.MaxSumLoops)
				for (long i = start; i <= ende; i++) {
					evaluator.setVar(term[0].getFuncname(), new MathTerm(new MathNumber(i)));
					MathTerm efunc = evaluator.eval(term[3]);
					if (erg == null)
						erg = efunc;
					else
						erg = evaluator.eval(evaluator.eval("+", new MathTerm[] { erg, efunc }));
				}
			evaluator.removeVarStack();
			if (erg != null)
				return erg;
		}
		MathTerm mt = new MathTerm("sum");
		mt.add(term[0]);
		mt.add(term[1]);
		mt.add(term[2]);
		mt.add(term[3]);
		return mt;
	}

	@Override
	public MathImage createImage(MathImageCreator imageCreator, MathTerm[] child) {

		int hh = (imageCreator.getRuntimeHP());  //Buchstaben vor und ueber summe
		int h = (2 * hh + 30);
		MathImage sum = imageCreator.createImage(h + 15); // rt,h/rt.HP + 1
		int o = sum.off;

		sum.clear(false);
		//Summenzeichen
		sum.printLine(o + 3, hh, o + 20, hh);
		sum.printLine(o + 20, hh, o + 23, hh + 3);
		sum.printLine(o + 3, hh, o + 10, h / 2);
		sum.printLine(o + 3, h - hh, o + 10, h / 2);
		sum.printLine(o + 20, h - hh, o + 23, h - hh - 3);
		sum.printLine(o + 3, h - hh, o + 20, h - hh);
		sum.setMaxwidth(o + 26);

		if (child.length != 4)
			return sum;

		MathImage i0 = imageCreator.createImage(child[0]);
		MathImage eq = imageCreator.createImage(1);
		eq.println("=");
		MathImage i1 = imageCreator.createImage(child[1]);

		MathImage iu = imageCreator.concat(new MathImage[] { i0, eq, i1 });
		sum.add(iu, 0, h - hh + 1, hh);

		MathImage io = imageCreator.createImage(child[2]);
		sum.add(io, 10, 0, hh);

		MathImage i3 = imageCreator.createImage(child[3]);
		return imageCreator.concat(new MathImage[] { sum, i3 });
	}

}
