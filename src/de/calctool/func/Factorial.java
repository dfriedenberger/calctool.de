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

public class Factorial extends MathFunction {
	public String getName() {
		return "fac";
	};

	public MathTerm eval(MathEvaluator evaluator, MathTerm[] term) {
		if (term.length != 1)
			throw new RuntimeException("Factorial with " + term.length + " Elements");

		if (evaluator.isNumber(term[0])) {
			MathNumber n0 = term[0].getNumber();
			if (n0.isLong()) {
				long n = n0.getNumerator();
				if (n > 0) {
					MathTerm t = new MathTerm(new MathNumber(1));
					for (long i = n; i > 0; i--) {
						t = evaluator.eval("*", new MathTerm[] { t, new MathTerm(new MathNumber(i)) });
					}
					return t;
				}
			}
		}
		MathTerm mt = new MathTerm("fac");
		mt.add(term[0]);
		return mt;
	}

	@Override
	public MathImage createImage(MathImageCreator imageCreator, MathTerm[] child) {
		// TODO Auto-generated method stub
		return null;
	}
}
