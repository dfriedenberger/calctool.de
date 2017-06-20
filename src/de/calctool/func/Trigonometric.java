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

public class Trigonometric extends MathFunction {
	private String trig = "";
	private FindFactor findFactor;

	public String getName() {
		return trig;
	}

	public Trigonometric(String trig) {
		this.trig = trig;
		this.findFactor = new FindFactor(Math.PI,2);
	}

	public MathTerm eval(MathEvaluator evaluator, MathTerm[] term) {
		if (term.length != 1)
			throw new RuntimeException("SquareRoot with " + term.length + " Elements");

		
		//Spezielle Werte ???
		MathTerm zerg = evaluator.evalNumeric(term[0]);
		if (evaluator.isNumber(zerg))
		{
			MathNumber n0 = zerg.getNumber();

			Factor f = findFactor.getFactor(n0.getDouble());
			if (trig.equals("sin"))
			{
				if(f.mod(2).is(0))  /* 0 und pi */
					return new MathTerm(new MathNumber(0));
				if(f.mod(4).is(1) || f.mod(4).is(-3)) /* 1/2 pi und -3/2 pi*/
					return new MathTerm(new MathNumber(1));
				if(f.mod(4).is(3) || f.mod(4).is(-1)) /* 3/2 pi und -1/2 pi */
					return new MathTerm(new MathNumber(-1));
			}
			if (trig.equals("cos"))
			{
				if(f.mod(4).is(0))  /* 0 */
					return new MathTerm(new MathNumber(1));
				if(f.mod(4).is(2) || f.mod(4).is(-2))  /* pi / -pi */
					return new MathTerm(new MathNumber(-1));
				if(f.mod(4).is(1) || f.mod(4).is(-3)) /* 1/2 pi und -3/2 pi*/
					return new MathTerm(new MathNumber(0));
				if(f.mod(4).is(3) || f.mod(4).is(-1)) /* 3/2 pi und -1/2 pi */
					return new MathTerm(new MathNumber(0));
			}
		}
		
		
		
		if (evaluator.isNumber(term[0])) {
						
			MathNumber n0 = term[0].getNumber();

			
			if (trig.equals("sin"))
				return new MathTerm(new MathNumber(Math.sin(n0.getDouble())));
			if (trig.equals("cos"))
				return new MathTerm(new MathNumber(Math.cos(n0.getDouble())));
			if (trig.equals("tan"))
				return new MathTerm(new MathNumber(Math.tan(n0.getDouble())));

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
