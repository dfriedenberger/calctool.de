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

public class BuildMatrix extends MathFunction {
	public String getName() {
		return "matrix";
	};

	public MathTerm eval(MathEvaluator evaluator, MathTerm[] term) {
		MathMatrix matrix = new MathMatrix();

		for (int i = 0; i < term.length; i++) {
			if (term[i].isMatrix())
				matrix.addColumn(term[i].getMatrix());
			else {
				MathMatrix tmp = new MathMatrix();
				tmp.addRow(new MathTerm[] { term[i] });
				matrix.addColumn(tmp);
			}
		}
		return new MathTerm(matrix);
	}

	@Override
	public MathImage createImage(MathImageCreator imageCreator, MathTerm[] child) {
		// TODO Auto-generated method stub
		return null;
	}
}
