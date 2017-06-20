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


public class Exponentiation extends MathFunction
{
    public String getName() { return "^"; };
    public MathTerm eval(MathEvaluator evaluator,MathTerm[] term)
    {
        if(term.length != 2)
            throw new RuntimeException("Exponentiation with "+term.length+" Elements");


        if(!term[0].isMatrix() && evaluator.isNumber(term[1]) && term[1].getNumber().isNumber(0)) return new MathTerm(new MathNumber(1));
        if(!term[0].isMatrix() && evaluator.isNumber(term[1]) && term[1].getNumber().isNumber(1)) return new MathTerm(term[0]);
        
        if(evaluator.isNumber(term[0]) && evaluator.isNumber(term[1]))
        {
            MathNumber n0 = term[0].getNumber();
            MathNumber n1 = term[1].getNumber();
            if(n1.isLong())
            {
                if(n0.isFractal())
                {
                    MathNumber n = new MathNumber((long)Math.pow(n0.getNumerator(),n1.getDouble())
                                                  ,(long)Math.pow(n0.getDemoninator() ,n1.getDouble()));
                    return new MathTerm(n);
                }
                else
                {
                    MathNumber n = new MathNumber( Math.pow(n0.getDouble() , n1.getDouble()));
                    return new MathTerm(n);
                }
            }
        }
        if(term[0].is(MathTerm.IMAGINARYUNIT) && evaluator.isNumber(term[1]))
        {
            MathNumber n1 = term[1].getNumber();
            if(n1.isLong() && n1.getDouble() >= 0)
            {
                long f = n1.getNumerator();
                switch((int)(f%4))
                {
                case 0:
                    return new MathTerm(new MathNumber(1));
                case 1:
                    return new MathTerm(MathTerm.IMAGINARYUNIT);
                case 2:
                    return new MathTerm(new MathNumber(-1));
                case 3:
                    return evaluator.eval("*",new MathTerm[]{ 
                                                          new MathTerm(new MathNumber(-1)) , 
                                                          new MathTerm(MathTerm.IMAGINARYUNIT)});
                }
            }
        }
        
        
        //only NxN marices are allowed to potenciate!
        if(term[0].isMatrix() && evaluator.isNumber(term[1]) && (term[0].getMatrix().rows() == term[0].getMatrix().columns()))
        {
        	MathNumber n1 = term[1].getNumber(); 
        	
        	
        	if(n1.isLong())	//only integer exponents!
        	{
        		long pow = n1.getNumerator();
        		if(pow >= 0)
        		{
        			//create id-matrix to fullfill A^0 = E
        			MathMatrix MIdentity = new MathMatrix();
        			for(int i = 0;i < term[0].getMatrix().rows(); i++) 
        			{
        				MIdentity.setCell(new MathTerm(new MathNumber(1)), i, i);
        			}
        			
        			MathTerm ResultMatrix = new MathTerm(MIdentity);
        			
		        	for(int i = 0;i < pow ;i++) 
		        	{
		        		ResultMatrix =  evaluator.eval("*", new MathTerm[] { ResultMatrix , term[0] });
		        	}
		        	return new MathTerm(ResultMatrix);
        		}
        	}
        }
        return new MathTerm("^",term[0],term[1]);
    }

    public MathImage createImage(MathImageCreator imageCreator,MathTerm child[])
    {
	if(child.length != 2) return null;
	

	MathImage i0 = imageCreator.createImage(child[0]);
	MathImage i1 = imageCreator.createImage(child[1]);

	int w = i0.filledWidth();
	int h = i0.getHeight();
	
	i0.add(i1,w,0,(2 * h)/3);

	return i0;
    }

}

