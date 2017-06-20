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

public class Multiplication extends MathFunction {
	public String getName() {
		return "*";
	};

	public MathTerm eval(MathEvaluator evaluator, MathTerm[] term) {
		if (term.length != 2)
			throw new RuntimeException("Multiplication with " + term.length + " Elements");

		if (evaluator.isNumber(term[0]) && term[0].getNumber().isNumber(1))
			return new MathTerm(term[1]);
		if (evaluator.isNumber(term[0]) && term[0].getNumber().isNumber(0))
			return new MathTerm(new MathNumber(0));
		if (evaluator.isNumber(term[1]) && term[1].getNumber().isNumber(1))
			return new MathTerm(term[0]);
		if (evaluator.isNumber(term[1]) && term[1].getNumber().isNumber(0))
			return new MathTerm(new MathNumber(0));

		if (evaluator.isNumber(term[0]) && evaluator.isNumber(term[1])) {
			MathNumber n0 = term[0].getNumber();
			MathNumber n1 = term[1].getNumber();

			if ((n0.isFractal() && n1.isLong()) || (n0.isLong() && n1.isFractal())
					|| (n0.isFractal() && n1.isFractal())) {
				System.out.println("multi: " + n0 + " * " + n1 + " " + n0.getNumerator() + " " + n0.getDemoninator()
						+ " " + n1.getNumerator() + " " + n1.getDemoninator());
				MathNumber n = new MathNumber(n0.getNumerator() * n1.getNumerator(),
						n0.getDemoninator() * n1.getDemoninator());
				return new MathTerm(n);
			}
			return new MathTerm(new MathNumber(n0.getDouble() * n1.getDouble()));
		}

		// scale Matrix
		if (evaluator.isNumber(term[0]) && term[1].isMatrix()) {
			// create empty result matrix
			MathMatrix ResultMatrix = new MathMatrix();
			for (int i = 0; i < term[1].getMatrix().rows(); i++) {

				for (int j = 0; j < term[1].getMatrix().columns(); j++) {
					// do multiplication
					MathTerm MCellRes = evaluator.eval("*",
							new MathTerm[] { term[0], term[1].getMatrix().getCell(i, j) });
					// copy result to result matrix, grows automaticly
					ResultMatrix.setCell(MCellRes, i, j);
				}
			}
			return new MathTerm(ResultMatrix);
		}
		// matrix multiplication
		if (term[0].isMatrix() && term[1].isMatrix()) {

			// is multiplication allowed?
			if (term[0].getMatrix().columns() == term[1].getMatrix().rows()) {
				MathMatrix ResultMatrix = new MathMatrix();

				for (int i = 0; i < term[0].getMatrix().rows(); i++) {
					for (int j = 0; j < term[1].getMatrix().columns(); j++) {
						MathTerm MCellRes = new MathTerm(new MathNumber(0));
						for (int k = 0; k < term[0].getMatrix().columns(); k++) {
							MathTerm MulRes = evaluator.eval("*", new MathTerm[] { term[0].getMatrix().getCell(i, k),
									term[1].getMatrix().getCell(k, j) });
							// MCellRes += MulRes
							MCellRes = evaluator.eval("+", new MathTerm[] { MCellRes, MulRes });
						}
						ResultMatrix.setCell(MCellRes, i, j);
					}
				}
				return new MathTerm(ResultMatrix);
			}
		}
		return new MathTerm("*", term[0], term[1]);
	}

	@Override
	public MathImage createImage(MathImageCreator imageCreator, MathTerm[] child) {
		return imageCreator.createOperationImage(getName(), child[0], child[1]);
	}
}
