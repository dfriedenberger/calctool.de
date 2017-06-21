package de.calctool.eval;

import java.util.ArrayList;
import java.util.List;

import de.calctool.func.MathFunction;
import de.calctool.parser.MathDefinitions;
import de.calctool.vm.MathMatrix;
import de.calctool.vm.MathNumber;
import de.calctool.vm.MathPolynom;
import de.calctool.vm.MathRuntime;
import de.calctool.vm.MathTerm;

public class MathEvaluator {
    
	private final MathDefinitions definitions;
	private final MathRuntime rt;
	
	public MathEvaluator(MathDefinitions definitions,MathRuntime rt)
	{
		this.definitions = definitions;
		this.rt = rt;
	}
	
	  public MathTerm eval(MathTerm term)
	  {
		  return eval(term,true);
	  }
	  public MathTerm evalNumeric(MathTerm term)
	  {
		  String mode = rt.get(MathRuntime.METHOD);
		  
		  rt.set(MathRuntime.METHOD, MathRuntime.NUMERIC);
		  
		  MathTerm t = eval(term,true);
		  
		  rt.set(MathRuntime.METHOD, mode);
		  return t;
	  }
	  private MathTerm eval(MathTerm term,boolean topolynom)
	    {
	        if(term.isOperation(":="))
		    {
			String def = term.child.get(0).getFuncname();
			rt.lock(def);
			MathTerm evaled = eval(term.child.get(1),topolynom);
			rt.unlock(def);
			List<MathTerm> childs = new ArrayList<MathTerm>();
			childs.add(term.child.get(0));
			childs.add(evaled);
			return new MathTerm(term,childs);
		    }
	        if(topolynom)
		    {   
			MathTerm pol = MathPolynom.create(this,term);
			if(pol != null)
			    {
				//System.out.println("Polynom: "+this+" => "+pol);
				return eval(pol,false);
				//here we can calc with polynoms
			    }
		    }
	        
	        List<MathTerm> newchild = new ArrayList<MathTerm>();
	        for(MathTerm t : term.child)
	            newchild.add(eval(t,topolynom));


	        //here comes the artificial inteligence
		if(term.isMatrix())
		    {
			MathMatrix nmatrix = new MathMatrix();
			for(int r = 0;r < term.matrix.rows();r++)
			    for(int c = 0;c < term.matrix.columns();c++)
				{
				    MathTerm t = term.matrix.getCell(r,c);
				    t = eval(t,topolynom);
				    nmatrix.setCell(t,r,c);
				}
			return new MathTerm(nmatrix);
		    }
		
	        if(term.function == null)
	        {
	            switch(newchild.size())
	            {
	            case 2:
	                if(term.simpleoperation > 0)
	                {
	                    MathFunction func = definitions.getSimpleOperation(term.simpleoperation);
	                    return func.eval(this,newchild.toArray(new MathTerm[0]));
	                }
	            }
	        }
	        else
	        {
	            MathFunction f = definitions.getFunction(term.function);
	            if(f != null)
	                return f.eval(this,newchild.toArray(new MathTerm[0]));

	            MathTerm mterm = rt.getDefinition(term.function);
	            if(mterm != null)
	            {
	                if(!mterm.isOperation(":=") || mterm.child.size() != 2) 
	                    throw new RuntimeException("not a definition");
	                MathTerm func = mterm.child.get(0);
	                if(func.child.size() != newchild.size())
	                    throw new RuntimeException("differnt numbers of funcarguments "+func+" != "+mterm);
	                rt.createVarStack();
	                for(int i = 0;i < func.child.size();i++)
	                    rt.setVar(func.child.get(i).getFuncname(),newchild.get(i));
	                
	                MathTerm efunc = eval(mterm.child.get(1),topolynom);
	                rt.removeVarStack();
	                return efunc;
	            }
	            
	            MathTerm val = rt.getVar(term.function);
	            if(val != null) return val;
	        }
	        return new MathTerm(term,newchild);
	    }


	  //Workaround
	public boolean is(String funcname, MathNumber t1, MathNumber t2) {
		
		MathFunction func = definitions.getFunction(funcname);
		
		
		  MathTerm term = func.eval(this,new MathTerm[]{new MathTerm(t1),new MathTerm(t2)});
	        //is term boolean?
	        if(!isNumber(term)) throw new RuntimeException("not a bool number"+term);
	        if(!term.getNumber().isBool()) throw new RuntimeException("number not a bool "+term.getNumber());
	        return term.getNumber().getBool();
		
		
		
		
	}


	public MathNumber calc(String funcname, MathNumber t1, MathNumber t2) {
		MathFunction func = definitions.getFunction(funcname);
		
		
		MathTerm term = func.eval(this,new MathTerm[]{new MathTerm(t1),new MathTerm(t2)});

	        System.out.println("calc: "+t1+" ("+func.getName()+")"+t2+" = "+term.getNumber());

	        if(isNumber(term))
	            return term.getNumber();
	        throw new RuntimeException(term+ " is not a number");
		
	
	}


	public MathTerm eval(String funcname, MathTerm[] mathTerms) {
		MathFunction func = definitions.getFunction(funcname);
		return func.eval(this,mathTerms);
	}


	public boolean isNumber(MathTerm term) {

		if (term.isnumber)
			return true;

		/* Constants */
		if (rt.get(MathRuntime.METHOD).equals(MathRuntime.NUMERIC))
			if (term.function != null) {
				for (int i = 0; i < MathTerm.constants.length; i++)
					if (MathTerm.constants[i].equals(term.function))
						return true;
			}

		return false;
	}


	public boolean isSymbolic() {
		return rt.get(MathRuntime.METHOD).equals(MathRuntime.SYMBOLIC);
	}



	public void log(MathFunction func, Object o) {
		rt.log(func, o);		
	}


	public void createVarStack() {
		rt.createVarStack();		
	}


	public void setVar(String var, MathTerm mathTerm) {
		rt.setVar(var,mathTerm);
	}


	public void removeVarStack() {
		rt.removeVarStack();		
	}


	
	
}
