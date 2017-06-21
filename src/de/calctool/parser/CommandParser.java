package de.calctool.parser;

import de.calctool.cmd.MathCommand;
import de.calctool.vm.MathNumber;
import de.calctool.vm.MathTerm;

public class CommandParser {

	private MathDefinitions definitions;

	public CommandParser(MathDefinitions definitions) {
		this.definitions = definitions;
	}

	public MathTerm parse(String term) {
		
		
		MathTokenizer scanner = new MathTokenizer(term);

		MathToken t = scanner.nextToken();
		
		if(!t.isWord()) return null;
		
		
		MathCommand command = definitions.getCommand(t.getText());
		if(command == null)
			return null;
		
		MathTerm mterm = new MathTerm(command);
		
		while (scanner.hasToken()) {		
			t = scanner.nextToken();
			
			if(t.isNumber())
			{
				MathNumber n = MathNumber.parse(t.getText());
				mterm.add(new MathTerm(n));
			}
			else
				throw new RuntimeException("Unknown type " + t);
			
		}
		
		return mterm;
	}

	 
	
	
}
