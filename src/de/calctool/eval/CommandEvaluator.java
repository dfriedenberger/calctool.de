package de.calctool.eval;

import de.calctool.cmd.MathCommand;
import de.calctool.parser.MathDefinitions;
import de.calctool.vm.MathResult;
import de.calctool.vm.MathRuntime;
import de.calctool.vm.MathTerm;

public class CommandEvaluator {

	private MathDefinitions definitions;
	private MathRuntime rt;

	public CommandEvaluator(MathDefinitions definitions, MathRuntime rt) {
		this.definitions = definitions;
		this.rt = rt;
	}

	public MathResult[] eval(MathTerm cterm) {
		
		if(!cterm.isCommand())
		 	return null;
		
		
		MathCommand cmd = cterm.getCommand();
		
		return cmd.eval(rt, cterm.child.toArray(new MathTerm[0]));
		
		
	}

}
