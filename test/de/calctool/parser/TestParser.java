package de.calctool.parser;

import static org.junit.Assert.*;

import org.junit.Assert;
import org.junit.Test;

import de.calctool.vm.MathTerm;

public class TestParser {

	@Test
	public void test() {
		
		CommandParser parser = new CommandParser(MathDefinitions.DefaultDefinitions);
		
		MathTerm t = parser.parse("is 1234 0x12345a 0b101");
		
		Assert.assertNotNull(t);
		Assert.assertEquals(3,t.childCount());

		
	}

}
