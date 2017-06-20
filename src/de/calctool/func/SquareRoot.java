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

public class SquareRoot extends MathFunction {
	public String getName() {
		return "sqrt";
	};

	public MathTerm eval(MathEvaluator evaluator, MathTerm[] term) {
		if (term.length != 1)
			throw new RuntimeException("SquareRoot with " + term.length + " Elements");

		if (evaluator.isNumber(term[0])) {
			MathNumber n0 = term[0].getNumber();
			if (n0.getDouble() < 0) {
				MathNumber num = evaluator.calc("*", new MathNumber(-1), n0);
				MathTerm psqrt = evaluator.eval(getName(), new MathTerm[] { new MathTerm(num) });
				return evaluator.eval("*", new MathTerm[] { psqrt, new MathTerm(MathTerm.IMAGINARYUNIT) });
			}

			if (n0.isFractal()) {
				MathNumber num = new MathNumber(Math.sqrt(n0.getNumerator()));
				MathNumber denom = new MathNumber(Math.sqrt(n0.getDemoninator()));
				if (num.isLong() && denom.isLong()) {
					MathNumber n = new MathNumber(num.getNumerator(), denom.getNumerator());
					return new MathTerm(n);
				}
			}
			return new MathTerm(new MathNumber(Math.sqrt(n0.getDouble())));
		}
		MathTerm t = new MathTerm(getName());
		t.add(term[0]);
		return t;
	}

	@Override
	public MathImage createImage(MathImageCreator imageCreator, MathTerm[] child) {
		// TODO Auto-generated method stub
		return null;
	}
}
