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

public class Division extends MathFunction {
	public String getName() {
		return "/";
	};

	public MathTerm eval(MathEvaluator evaluator, MathTerm[] term) {
		if (term.length != 2)
			throw new RuntimeException("Division with " + term.length + " Elements");

		if (evaluator.isNumber(term[0]) && term[0].getNumber().isNumber(0))
			return new MathTerm(new MathNumber(0));
		if (evaluator.isNumber(term[1]) && term[1].getNumber().isNumber(1))
			return new MathTerm(term[0]);
		if (evaluator.isNumber(term[1]) && term[1].getNumber().isNumber(0))
			return new MathTerm(MathTerm.INFINITY);

		evaluator.log(this, "division " + term[0] + "  /   " + term[1]);

		if (evaluator.isNumber(term[0]) && evaluator.isNumber(term[1])) {
			MathNumber n0 = term[0].getNumber();
			MathNumber n1 = term[1].getNumber();

			if ((n0.isFractal() && n1.isLong()) || (n0.isLong() && n1.isFractal()) || (n0.isFractal() && n1.isFractal())
					|| (n0.isLong() && n1.isLong())) {
				if (evaluator.isSymbolic()) {
					MathNumber n = new MathNumber(n0.getNumerator() * n1.getDemoninator(),
							n0.getDemoninator() * n1.getNumerator());
					return new MathTerm(n);
				}
			}
			return new MathTerm(new MathNumber(n0.getDouble() / n1.getDouble()));
		}
		return new MathTerm("/", term[0], term[1]);
	}

	public MathImage createImage(MathImageCreator imageCreator, MathTerm child[]) {
		if (child.length != 2)
			return null;
		MathImage div[][] = new MathImage[2][1];
		div[0][0] = imageCreator.createImage(child[0]);
		div[1][0] = imageCreator.createImage(child[1]);

		MathImage i = imageCreator.concat(div);

		int h = i.getHeight();
		int w = i.filledWidth();
		i.printLine(0, h / 2, w, h / 2);
		return i;
	}

}
