package de.calctool.eval;

import static org.junit.Assert.*;

import org.junit.Test;

import de.calctool.cmd.IsCommand;
import de.calctool.parser.MathDefinitions;
import de.calctool.vm.MathNumber;
import de.calctool.vm.MathResult;
import de.calctool.vm.MathRuntime;
import de.calctool.vm.MathTerm;

public class TestEvaluator {

	@Test
	public void test() {
		
		MathDefinitions def = MathDefinitions.DefaultDefinitions;

		MathTerm mterm = new MathTerm(new IsCommand());
		mterm.add(new MathTerm(new MathNumber(123)));
		
		
		MathRuntime rt = new MathRuntime("temp/tempruntime.dat");
		CommandEvaluator evaluator = new CommandEvaluator(def, rt);

		
		MathResult[] r = evaluator.eval(mterm);
		
		assertNotNull(r);
		assertEquals(1,r.length);

		
	}

}
