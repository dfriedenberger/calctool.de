package de.calctool.parser;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import de.calctool.eval.MathEvaluator;
import de.calctool.vm.MathRuntime;
import de.calctool.vm.MathTerm;

public class TestWorkflow {

	public void test(String input, String output) {

		MathDefinitions def = MathDefinitions.DefaultDefinitions;

		MathParser parser = new MathParser(def);
		MathTerm[] term = parser.parse(input, null);

		assertEquals(1, term.length);
		assertEquals(input, term[0].toString());

		MathRuntime rt = new MathRuntime("temp/tempruntime.dat");
		MathEvaluator evaluator = new MathEvaluator(def, rt);

		MathTerm result = evaluator.eval(term[0]);
		assertEquals(input + " = " + output, output, result.toString());

	}

	private boolean testSequence(String file) throws IOException {
		String[] lines = readFile(file);

		int cnt = 0;
		for (String line : lines) {
			int i = line.indexOf(":");
			String input = line.substring(0, i).trim();
			String output = line.substring(i + 1).trim();
			test(input, output);
			cnt++;
		}

		return cnt == lines.length && lines.length >= 1;

	}

	private String[] readFile(String filename) throws IOException {

		List<String> lines = new ArrayList<String>();

		BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(filename)));

		while (true) {
			String line = in.readLine();
			if (line == null)
				break;
			if (line.trim().startsWith("#"))
				continue;
			if (line.trim().isEmpty())
				continue;

			lines.add(line);
		}

		in.close();

		return lines.toArray(new String[0]);

	}

	@Test
	public void testAddition() {
		test("1 + 2", "3");
	}

	@Test
	public void testMultiplication() {
		test("1 * 2", "2");
	}

	@Test
	public void testSinus() {
		test("sin(-3 / 2 * pi)", "1");
	}

	@Test
	public void testLimes() {
		test("lim((1 - 1 / x) ^ x,x,infini)", "1 / e");
	}

	@Test
	public void testBinom() {
		test("binom(5,2)", "10");
	}

	@Test
	public void testDivision() {
		test("20 / 4", "5");
	}

	@Test
	public void testPlot() {
		test("plot(x ^ 2)", "x");
	}

	@Test
	public void testSequence() throws IOException {
		assertTrue(testSequence("testdata/sequence1.txt"));
	}

}
