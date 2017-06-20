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
package de.calctool.vm;
import de.calctool.eval.MathEvaluator;
import de.calctool.func.MathFunction;

public class MathPolynom
{
    MathNumber factor;
    MathNumber exp;
    String name;
    private MathPolynom(MathNumber factor,String name,MathNumber exp)
    {
        this.factor = factor;
        this.name = name;
        this.exp = exp;
    }
    private boolean isZero()
    {
        return (factor.isNumber(0));
    }
    private boolean isConstant()
    {
        return (exp.isNumber(0));
    }
    
    private boolean isbigger(MathEvaluator evaluator,MathPolynom p)
    {
        //if this is bigger as p return true
        //compare names
        if(p.name.equals("") && this.name.length() > 0) return false;
        if(this.name.equals("")  && p.name.length() > 0) return true;
        int i = this.name.compareTo(p.name);
        if(i > 0) return true;

        if(this.name.equals(p.name))
        {
            if(evaluator.is("<",this.exp,p.exp)) return true;
            if(evaluator.is("=",this.exp,p.exp))
                if(evaluator.is("<",this.factor,p.factor)) return true;
        }
        return false;

    }
    private MathPolynom mp(MathEvaluator evaluator,MathPolynom p2)
    {
        //multiplicate
        if(this.isConstant()) return simplify(evaluator,new MathPolynom(evaluator.calc("*",factor,p2.factor),p2.name,p2.exp));
        if(p2.isConstant()) return simplify(evaluator,new MathPolynom(evaluator.calc("*",factor,p2.factor),name,exp));
        if(!name.equals(p2.name))
            throw new RuntimeException(name +" != "+p2.name);
        return simplify(evaluator,new MathPolynom(evaluator.calc("*",factor,p2.factor),name,evaluator.calc("+",exp,p2.exp)));
    }
    private MathPolynom div(MathEvaluator evaluator,MathPolynom p2)
    {
        //divide
        if(p2.isConstant()) return simplify(evaluator,new MathPolynom(evaluator.calc("/",factor,p2.factor),name,exp));
        if(evaluator.is(">",p2.exp,exp)) throw new RuntimeException("negative exponents not supported");
        if(!name.equals(p2.name))
            throw new RuntimeException(name +" != "+p2.name);
        return simplify(evaluator,new MathPolynom(evaluator.calc("/",factor,p2.factor),name,evaluator.calc("-",exp,p2.exp)));
    }
    /* Polynom  3 * x ^ 2 - 4 * x + 1/2

                        +
                /               \
               *                +
            /     \          /     \
           3      ^         *       *
                /   \     /    \  /   \
               x     2   -4    ^ 1/2   ^
                              / \     / \
                             x   1   x   1
    */
    
    private static MathPolynom[] createPoly(MathEvaluator evaluator,MathTerm term)
    {
        if(term.isFunction())
        {
            if(term.hasChilds()) throw new RuntimeException("is not a variable (has childs) "+term);
            String name = term.getFuncname();
            return new  MathPolynom[]{ new MathPolynom(new MathNumber(1),name,new MathNumber(1)) };
        }
        if(evaluator.isNumber(term))
            return new  MathPolynom[]{ new MathPolynom(term.getNumber(),"",new MathNumber(0)) }; 
        if(term.isOperation("+"))
            return add(createPoly(evaluator,term.getFirstChild()),createPoly(evaluator,term.getSecondChild()));
        if(term.isOperation("*"))
        {
            MathTerm t1 = term.getFirstChild();
            MathTerm t2 = term.getSecondChild();
            if(!evaluator.isNumber(t1)) throw new RuntimeException("is not a number "+t1);
            MathNumber factor =  t1.getNumber();
            //special
            if(t2.isFunction() && !t2.hasChilds())
                return new  MathPolynom[]{ new MathPolynom(factor,t2.getFuncname(),new MathNumber(1)) };

            if(!t2.isOperation("^")) throw new RuntimeException("not operation ^ "+t2);
            MathTerm t21 =  t2.getFirstChild();
            MathTerm t22 =  t2.getSecondChild();
            if(!t21.isFunction()) throw new RuntimeException("is not a variable "+t21);
            if(t21.hasChilds()) throw new RuntimeException("is not a variable (has childs) "+t21);
            String name = t21.getFuncname();
            MathNumber exp = t22.getNumber();
            if(!exp.isLong())  throw new RuntimeException(exp + " isn't long");
            return new  MathPolynom[]{ new MathPolynom(factor,name,exp) };
        }
        if(term.isOperation("^"))
        {
            MathTerm t1 =  term.getFirstChild();
            MathTerm t2 =  term.getSecondChild();
            if(!t1.isFunction()) throw new RuntimeException("is not a variable "+t1);
            if(t1.hasChilds()) throw new RuntimeException("is not a variable (has childs) "+t1);
            if(!evaluator.isNumber(t2)) throw new RuntimeException("is not a number "+t2);
            String name = t1.getFuncname();
            MathNumber exp = t2.getNumber();
            if(!exp.isLong())  throw new RuntimeException(exp + " isn't long");
            return new  MathPolynom[]{ new MathPolynom(new MathNumber(1),name,exp) };
        }
        throw new RuntimeException("unknown "+term);

    }
    private static MathPolynom[] add( MathPolynom p1[]  , MathPolynom p2[] )
    {
        MathPolynom p[] = new MathPolynom[p1.length + p2.length];
        for(int i = 0;i < p1.length || i < p2.length;i++)
        {
            if(i < p1.length) p[i] = p1[i];
            if(i < p2.length) p[i+p1.length] = p2[i];
        }
        return p;
    }

    private static void exchange (MathPolynom a[], int m, int n) {
	MathPolynom t = a[m];
	a[m] = a[n];
	a[n] = t;
    }
    private static int partition (MathEvaluator evaluator,MathPolynom a[], int m, int n) {
	MathPolynom x = a[m];  // Pivot-Element
	int j = n + 1;
	int i = m - 1;
		
	while (true) {
	    j--;
	    while (a[j].isbigger(evaluator,x)) j--;
	    i++;
	    while (x.isbigger(evaluator,a[i])) i++;
	    if (i < j) exchange (a, i, j);
	    else return j;
	}
    }



    private static void qsort (MathEvaluator evaluator,MathPolynom a[], int l, int r) {
	// nur absteigen, wenn Array mehr als 1 Element hat (l < r)
	if (l < r) {
	    int r2 = partition (evaluator ,a, l, r);
	    qsort(evaluator,a, l, r2);
	    qsort(evaluator,a, r2 + 1, r);
	}
    }

    private static MathPolynom[] combine(MathEvaluator evaluator,MathPolynom p[])
    {
        logln("qsort start: "+createMathTerm(p,0,p.length));
        qsort(evaluator,p,0,p.length - 1);
        logln("qsort ende: "+createMathTerm(p,0,p.length));
        int x = -1;
        int z = 0;
        for(int i = 0;i < p.length;i++)
        {
            if(p[i].isZero())
            {
                p[i] = null;
                continue;
            }
            if(-1 < x && x < i)
            {
                if(p[x].name.equals(p[i].name) &&  evaluator.is("=",p[x].exp,p[i].exp))
                {
                    
                    p[x] = simplify(evaluator,new MathPolynom(evaluator.calc("+",p[x].factor,p[i].factor),p[x].name,p[x].exp));
                    p[i] = null;
                    continue;
                }
            }
            z++;
            x = i;
        }
        
        MathPolynom c[] = new MathPolynom[z];
        x = 0;
        for(int i = 0;i < p.length;i++)
        {
            if(p[i] == null) continue;
            if(x < p.length)
                c[x] = p[i];
            x++;
        }
        if(x != z) throw new RuntimeException(x + " != "+z);

        if(p.length != c.length)
            return combine(evaluator,c);
        return c;
    }

    private static MathPolynom[] addition(MathEvaluator evaluator,MathPolynom pol1[],MathPolynom pol2[])
    {
        //add
        MathPolynom p[]  = add(pol1,pol2);
        //combine
        p = combine(evaluator,p);
        return p;
    }


    private static MathTerm addition(MathEvaluator evaluator,MathTerm pol1,MathTerm pol2)
    {
        //add
        MathPolynom p1[] = createPoly(evaluator,pol1);
        MathPolynom p2[] = createPoly(evaluator,pol2);
        MathPolynom p[]  = addition(evaluator,p1,p2);
        if(p == null) return null;
        return createMathTerm(p,0,p.length);
    }
    public static MathPolynom[] multiplicate(MathEvaluator evaluator,MathPolynom pol1[],MathPolynom pol2[])
    {
        //multiplicate
        
        MathPolynom p[] = new MathPolynom[pol1.length * pol2.length];
        for(int i = 0;i < pol1.length;i++)
            for(int j = 0;j < pol2.length;j++)
            {
                if(pol1[i].isConstant() || pol2[j].isConstant() || pol1[i].name.equals(pol2[j].name))
                    p[i * pol2.length + j] = pol1[i].mp(evaluator , pol2[j]);
                else
                    return null;
            }

        //combine
        p = combine(evaluator,p);
        return p;
    }

    public static MathTerm multiplicate(MathEvaluator evaluator,MathTerm pol1,MathTerm pol2)
    {
        //multiplicate
        MathPolynom p1[] = createPoly(evaluator,pol1);
        MathPolynom p2[] = createPoly(evaluator,pol2);
        MathPolynom p[]  = multiplicate(evaluator , p1,p2);
        if(p == null) return null;
        return createMathTerm(p,0,p.length);
    }

    public static MathTerm divide(MathEvaluator evaluator,MathTerm pol1,MathTerm pol2)
    {
        //divide
        MathPolynom erg[] = new MathPolynom[0];
        MathPolynom p1[] = createPoly(evaluator,pol1);
        MathPolynom p2[] = createPoly(evaluator,pol2);
        MathPolynom negone[] = createPoly(evaluator,create(evaluator,new MathTerm(new MathNumber(-1.0))));
       
        p1 = combine(evaluator,p1);
        p2 = combine(evaluator,p2);
        
        while(p1.length >= 1 && p2.length >= 1)
        {
            logln("div: p1 "+createMathTerm(p1,0,p1.length));
            logln("div: p2 "+createMathTerm(p2,0,p2.length));
            //device p1[0] / p2[0]
            if(evaluator.is("<",p1[0].exp,p2[0].exp)) return null;
            if(!p2[0].isConstant() && !p1[0].name.equals(p2[0].name)) return null;
            MathPolynom tmp[]   = new MathPolynom[]{ p1[0].div(evaluator , p2[0]) };
            erg = add(erg,tmp);
            logln("div: tmp "+createMathTerm(tmp,0,tmp.length));
            logln("div: erg "+createMathTerm(erg,0,erg.length));
            
            MathPolynom ptmp2[] = multiplicate(evaluator , tmp , p2 );
            logln("div: tmp2 "+createMathTerm(ptmp2,0,ptmp2.length));
            p1 = addition(evaluator , p1 , multiplicate( evaluator , negone , ptmp2 ));
            if(p1.length == 0)
            {
                erg = combine(evaluator,erg);
                return createMathTerm(erg,0,erg.length);
            }
        }
        return null;

    }
    public static MathTerm createMathTerm(MathPolynom p[],int s,int len)
    {
        if(len == 0) return new MathTerm(new MathNumber(0.0));
        if(len == 1) return createMathTerm(p[s].factor,p[s].name,p[s].exp);
        return new MathTerm("+"
                            ,createMathTerm(p,s,len -1)
                            ,createMathTerm(p,s + len - 1,1));
    }
    private static MathPolynom simplify(MathEvaluator evaluator,MathPolynom p)
    {
        MathTerm term1  = evaluator.eval("^",new MathTerm[]{ new MathTerm(p.name),new MathTerm(p.exp) });
        MathTerm term   = evaluator.eval("*",new MathTerm[]{ new MathTerm(p.factor), term1 });
        MathPolynom pl[] = createPoly(evaluator,term);
        if(pl == null) throw new RuntimeException(term+" is not a polynom");
        if(pl.length != 1) throw new RuntimeException(pl.length +" polynoms createt");
        return pl[0];
    }
    public static MathTerm createMathTerm(MathNumber factor,String name,MathNumber exponent)
    {
        return new MathTerm("*",new MathTerm(factor),
                            new  MathTerm("^",new MathTerm(name),
                                          new MathTerm(exponent)));  
    }
    public static void logln(Object o)
    {
        //System.out.println(o);
    }
    public static MathTerm create(MathEvaluator evaluator,MathTerm term)
    {
        /*
          Simple Polynoms (one variable)
        */
	if(evaluator.isNumber(term))
	{
	     logln("Pol(input): "+term);
	     logln("Pol Nr    : "+createMathTerm(term.getNumber(),"",new MathNumber(0)));
	     //is an simple polynom
	     return createMathTerm(term.getNumber(),"",new MathNumber(0));
	}
        if(term.isFunction() && !term.hasChilds())
	{
            //is an simple polynom
            logln("Pol(input): "+term);
            logln("Pol Var   : "+createMathTerm(new MathNumber(1),term.getFuncname(),new MathNumber(1)));
            return createMathTerm(new MathNumber(1),term.getFuncname(),new MathNumber(1));
        }
      
        if(term.isOperation("+"))
        {
            /* 5 * x ^ 7 + 3 * x + 1 */
            logln("Pol(input): "+term.getFirstChild()+" , "+term.getSecondChild());

            MathTerm pol1 = create(evaluator,term.getFirstChild());
            MathTerm pol2 = create(evaluator,term.getSecondChild());
            logln("Pol: "+pol1+" '+' "+pol2);

            if(pol1 != null && pol2 != null)
            {
                MathTerm t = addition(evaluator , pol1,pol2);
                logln("Pol: (erg) "+t);
                return t;
            }
        }
        if(term.isOperation("-"))
        {
            //is addition with negative factor
            logln("Pol(input): "+term.getFirstChild()+" , "+term.getSecondChild());

            MathTerm pol1 = create(evaluator,term.getFirstChild());
            MathTerm pol2 = create(evaluator,term.getSecondChild());
            logln("Pol: "+pol1+" '-' "+pol2);

            if(pol1 != null && pol2 != null)
            {
                MathTerm t = addition(evaluator , pol1, multiplicate(evaluator , create(evaluator , new MathTerm(new MathNumber(-1))),pol2));
                logln("Pol: (erg) "+t);
                return t;
            }


        }
        if(term.isOperation("*"))
        {
            /* ( x  + 1 ) * ( x + 2 )  * ( x^2  + 2 ) */
            logln("Pol(input): "+term.getFirstChild()+" , "+term.getSecondChild());

            MathTerm pol1 = create(evaluator , term.getFirstChild());
            MathTerm pol2 = create(evaluator , term.getSecondChild());
            logln("Pol: "+pol1+" '*' "+pol2);
            if(pol1 != null && pol2 != null)
            {
                MathTerm t = multiplicate(evaluator , pol1,pol2);
                logln("Pol: (erg) "+t);
                return t;
            }
        }
        if(term.isOperation("/"))
        {
            //division of polynoms 
            logln("Pol(input): "+term.getFirstChild()+" , "+term.getSecondChild());
            MathTerm pol1 = create(evaluator,term.getFirstChild());
            MathTerm pol2 = create(evaluator,term.getSecondChild());
            logln("Pol: "+pol1+" '/' "+pol2);
            if(pol1 != null && pol2 != null)
            {
                MathTerm t = divide(evaluator , pol1,pol2);
                logln("Pol: (erg) "+t);
                return t;
            }
        }
        if(term.isOperation("^"))
        {
            //simple 
            logln("Pol(input): "+term.getFirstChild()+" , "+term.getSecondChild());
            
            
            if(evaluator.isNumber(term.getSecondChild()))
            { 
                MathNumber exp = term.getSecondChild().getNumber();
                if(exp.isLong())
                {
                    if(term.getFirstChild().isFunction() && !term.getFirstChild().hasChilds())
                    {
                        return createMathTerm(new MathNumber(1),term.getFirstChild().getFuncname(),exp);
                    }
                    //check for (a + b) ^ 2
                    MathTerm pol = create(evaluator , term.getFirstChild());
                    if(pol != null && evaluator.is("<",new MathNumber(0),exp) && evaluator.is("<",exp,new MathNumber(9)))
                    {
                        MathTerm erg = createMathTerm(new MathNumber(1),"",new MathNumber(0));
                        for(int i = 0;evaluator.is("<",new MathNumber(i),exp);i++)
                            erg = multiplicate(evaluator , erg,pol);
                        return erg;
                    }
                }
            }
        }
        return null;

    }
  
}

