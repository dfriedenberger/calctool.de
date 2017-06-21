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
import de.calctool.cmd.MathCommand;
import de.calctool.func.*;
import de.calctool.parser.MathParser;
import de.calctool.parser.MathToken;
import de.calctool.parser.MathTokenizer;

import java.util.*;

public class MathTerm
{
    private boolean virtuell = false;
    private boolean isascii = true;
    public String text = "";
    public String function = null;
    public boolean isnumber = false;
    public MathNumber number = null;
    private MathImage image = null;
    public MathMatrix matrix = null;
    public char simpleoperation = 0;
    public List<MathTerm> child = new ArrayList<MathTerm>();
    public int level = -1;
	private MathDataSet dataSet = null;
	private MathCommand command = null;
 
    //special Types
    public final static String INFINITY = "infini";


    public MathTerm(MathTerm org)  {
        this(org,null);

    }
    public MathTerm(MathTerm org,List<MathTerm> newchild)  {
    	
    	 virtuell        = org.virtuell;
         isascii         = org.isascii;
         text            = org.text;
         function        = org.function;
     
         number          = org.number;
         matrix          = org.matrix;
         image           = org.image;
         isnumber        = org.isnumber;
         simpleoperation = org.simpleoperation;
         child           = new ArrayList<MathTerm>();
         
         
         
         if(newchild != null)
             child.addAll(newchild);

         else
             child.addAll(org.child);

         
         level           = org.level;
    }
    

	public MathTerm()  {
        //noop
        virtuell = true;
        isascii = false;
    }


    public final static String IMAGINARYUNIT = "i";
    public MathTerm(String name)
    {
        //variable or function
	if(name.toLowerCase().startsWith(INFINITY))
	    function = INFINITY; 
	else
	    function = name;
        
	text     = function;
    }

    //can set an image 
   
    public void setImage(MathImage image)
    {
        this.image = image;
    }

    public boolean is(String key)
    {
        if(function != null)
            if(function.equals(key)) return true;
        return false;
    }
    
    
    public void add(MathTerm ch)
    {
        if(function == null && command == null)
            throw new RuntimeException("only for functions or commands "+text);
        child.add(ch);
    }
   
    public MathTerm(MathDataSet dataSet) {
         this.dataSet = dataSet;
    }
    
    public boolean isDataSet()
    {
    	return dataSet != null;
    }
    public MathDataSet getDataSet()
    {
    	return dataSet;
    }
    
    //MathTerm
	public MathTerm(MathCommand command) {
		this.command = command;
		text = command.getName();
	}

	public boolean isCommand() {
		return command != null;
	}

	public MathCommand getCommand() {
		return command;
	}
    
    public MathTerm(MathNumber number)
    {
        text = "" + number;
        this.number = number;
        isnumber = true;
    }
    public MathTerm(MathMatrix m)
    {
	this.matrix = m;
    }
    public boolean isMatrix()
    {
	return matrix != null;
    }
    public MathMatrix getMatrix()
    {
	return matrix;
    }
    public MathTerm(String op,MathTerm t1,MathTerm t2)
    {
        text  = op;
        level = -1;
        for(int lev = 0;lev < MathParser.operand.length;lev++)
        {
            for(int j = 0;j < MathParser.operand[lev].operand.length;j++)
                if(MathParser.operand[lev].operand[j].equals(op))
                    level = lev;
        }


        child = new ArrayList<MathTerm>();
        child.add(t1);
        child.add(t2);
        if(level >= 0 && op.length() == 1) simpleoperation = op.charAt(0);
    }
   
	
	public boolean isAscii()
    {
        for(MathTerm term : child)
            if(!term.isAscii()) return false;
        return isascii;
    }
    public boolean isVirtuell()
    {
        return virtuell;
    }
    public boolean isFunction()
    {
        return (function != null);
    }
    
    public final static String constants[] = { "pi" , "e" };
    private final static double constval[] = {
	/* 3.14159265358979323846264338327950 , /* pi */
    		Math.PI, /* pi */
    		2.71828182845904523536028747135266  /* e */
    };
    public boolean isNumber()
    {
        if(isnumber)
	    return true;
	return false;
    }
   
    public boolean isNumber(double nr)
    {
        return isnumber && number.isNumber(nr);
    }
    public MathNumber getNumber()
    {
        if(isnumber)
            return number;
	
	/* Constants */
	if(function != null)
	    {
		for(int i = 0;i < constants.length;i++)
		    if(constants[i].equals(function)) 
			{
			    return new MathNumber(constval[i]);
			}
	    }
	throw new RuntimeException("is not a number "+this);
    }
    public String getFuncname()
    {
        if(isOperation(":=")) return child.get(0).function;
        if(isFunction()) return function;
        throw new RuntimeException("not an assignment and not a function"+text);
    }
    public boolean isOperation(String op)
    {
        return (level >= 0 && text.equals(op));
    }
    public boolean hasChilds()
    {
        return child.size() > 0;
    }
    public int childCount()
    {
        return child.size();
    }
    public MathTerm getFirstChild()
    {
        return child.get(0);
    }
    public MathTerm getSecondChild()
    {
        return child.get(1);
    }
  
    private String bracket(MathTerm ch,boolean html)
    {
        if(withBracket(ch)) return "(" + ch.getString(html) + ")";
        return ch.getString(html);
    }
   
    public boolean withBracket(MathTerm ch)
    {
    	return ch.level < level && ch.level >= 0;
    }
  
 
    public String getString(boolean html)
    {
         if(function == null)
            switch(child.size())
            {
            case 0:
                if(isnumber)
                    return number.getString(html);
		if(isMatrix())
		    return "" + matrix;
                return text;
            case 1:
                return text + " " + bracket(child.get(0),html);
            case 2:
                if(html && simpleoperation == '^' && child.get(1).isnumber)
                {
                    if(child.get(1).number.isNumber(2))
                        return bracket(child.get(0),html) + "&amp;sup2;";
                    if(child.get(1).number.isNumber(3))
                        return bracket(child.get(0),html) + "&amp;sup3;";
                }
                if(html && simpleoperation == '*')
                    return bracket(child.get(0),html)+ "&amp;middot;" + bracket(child.get(1),html);
                return bracket(child.get(0),html)+ " " + text + " " + bracket(child.get(1),html);
            default:
                throw new RuntimeException(child.size()+" childs not supported");
            }
        else
        {
            //function
            String res = function;
            if(child.size() > 0)
            {
                res += "(";
                for(int i = 0;i < child.size();i++)
                    res += (i==0?"":",") + child.get(i).getString(html);
                res += ")";
            }
            else
            {
                if(html)
                {
                    //http://de.selfhtml.org/html/referenz/zeichen.htm
                    if(function.equals("pi")) res = "&amp;pi;";
                    if(function.equals("alpha")) res = "&amp;alpha;";
                    if(function.equals("beta")) res = "&amp;beta;";
                    if(function.equals("gamma")) res = "&amp;gamma;";
                    if(function.equals("delta")) res = "&amp;delta;";
                    if(function.equals("epsilon")) res = "&amp;epsilon;";
                    if(function.equals("lambda")) res = "&amp;lambda;";
                    if(function.equals("mu")) res = "&amp;mu;";
                    if(function.equals(INFINITY)) res =  "&amp;infin;";
                }
            }
            return res;
        }
    }
    public String toString()
    {
        return getString(false);
    }
    private static String[] add( String p1[]  , String p2[] )
    {
        int i;
        Hashtable<String,String> h = new Hashtable<String,String>();
        for(i = 0;i < p1.length;i++) h.put(p1[i],"");
        for(i = 0;i < p2.length;i++) h.put(p2[i],"");
        Enumeration<String> en = h.keys();
        String p[] = new String[h.size()];
        i = 0;
        while(en.hasMoreElements()) p[i++] = en.nextElement();
        return p;
    }
    
    public static String[] getNames(MathTerm term)
    {
        String n[] = new String[0];
        if(term.hasChilds())
            for(int i = 0;i < term.child.size();i++)
                n = add(getNames(term.child.get(i)),n);
        
        if(term.isFunction() && !term.hasChilds())
            n = add(new String[]{term.getFuncname()},n);
            
        return n;
    }
   
    
 
 
    
}

