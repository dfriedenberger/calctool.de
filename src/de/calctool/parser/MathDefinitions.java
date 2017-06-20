package de.calctool.parser;

import java.util.Hashtable;

import de.calctool.cmd.ConfigCommand;
import de.calctool.cmd.IsCommand;
import de.calctool.cmd.MathCommand;
import de.calctool.cmd.TimeCommand;
import de.calctool.func.Addition;
import de.calctool.func.Binom;
import de.calctool.func.BuildMatrix;
import de.calctool.func.Compare;
import de.calctool.func.Division;
import de.calctool.func.Exponentiation;
import de.calctool.func.Factorial;
import de.calctool.func.Limes;
import de.calctool.func.Logarithm;
import de.calctool.func.MathFunction;
import de.calctool.func.Multiplication;
import de.calctool.func.Perm;
import de.calctool.func.Plot;
import de.calctool.func.SquareRoot;
import de.calctool.func.Subtraction;
import de.calctool.func.Sum;
import de.calctool.func.Trigonometric;

public class MathDefinitions {

	
	public static final MathDefinitions DefaultDefinitions = new MathDefinitions(new MathFunction[]{ 
    	        new Addition(),
    	        new Compare("<"),
    	        new Compare("="),
    	        new Compare(">"),
    	        new Exponentiation(),
    	        new Division(),
    	        new Multiplication(),
    	        new Subtraction(),
    	        new SquareRoot(),
    	        new Plot(),
    	        new BuildMatrix(),
    	        new Logarithm(),
    	        new Logarithm(2),
    	        new Logarithm(10),
    	        new Trigonometric("sin"),
    	        new Trigonometric("cos"),
    	        new Trigonometric("tan"),
    	        new Perm(),
    	        new Binom(),
    	        new Factorial(),
    	        new Sum(),
    	        new Limes() }, new MathCommand[]{ 
						new IsCommand() , 
						new TimeCommand() , 
						new ConfigCommand() 
						});

    	

	public MathDefinitions(MathFunction[] mathFunctions, MathCommand[] mathCommands)
	{
		
		for(MathFunction function : mathFunctions)
		    functions.put(function.getName(),function);
		
		for(MathCommand command : mathCommands)
			commands.put(command.getName(), command);
		
	}
	
	/* Commands */
	private Hashtable<String, MathCommand> commands = new Hashtable<String, MathCommand>();

	public MathCommand getCommand(String name) {
		MathCommand f = commands.get(name);
		return f;
	}
    /* functions */
	 private  Hashtable<String,MathFunction> functions = new  Hashtable<String,MathFunction>();
	    public  MathFunction getFunction(String name)
	    {
	        MathFunction f = functions.get(name);
	        return f;
	    }
		public MathFunction getSimpleOperation(char simpleoperation) {
			return getFunction(""+simpleoperation);
		} 
	   
	
}
