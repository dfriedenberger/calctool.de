package de.calctool.parser;


import de.calctool.vm.MathMatrix;
import de.calctool.vm.MathNumber;
import de.calctool.vm.MathRuntime;
import de.calctool.vm.MathTerm;

public class MathParser {

	
	
	private final MathDefinitions definitions;
	
	public MathParser(MathDefinitions definitions)
	{
		this.definitions = definitions;
	}
	
	
	/* Terms */
	public MathTerm[] parse(String term, MathRuntime rt) {
		MathTerm[] terms = new MathTerm[0];
		MathTokenizer scanner = new MathTokenizer(term);

		while (true) {
			MathToken t = scanner.lookup();
			if (t == null)
				break;
			System.out.println(t);

			// if(t.isNumber()) here we can except specials
			MathTerm tm = parseTerm(scanner, 0);
			if (tm != null) {
				System.out.println(" parsed: " + tm + " =>  isoperation ? " + tm.text + " = " + tm.level);
				if (tm.isOperation(":="))
					rt.setDefinition(tm);
				terms = add(terms, new MathTerm[] { tm });
			}
		}

		// if debug
		// terms = add(terms , new MathTerm[] { new MathTerm()} );
		return terms;
	}



	private MathTerm parseTerm(MathTokenizer tk, int level) {
		if (level < operand.length) {
			MathTerm term = parseTerm(tk, level + 1);
			MathToken tt = tk.lookup();
			if (tt == null) // ends
				return term;
			if (!tt.isChar())
				throw new RuntimeException("operation char expected " + tt);

			System.out.println("operation " + tt);

			int x = 0;
			while (x < operand[level].operand.length && tt != null) {
				for (x = 0; x < operand[level].operand.length; x++) {
					if (!operand[level].operand[x].equals(tt.getText()))
						continue;
					tk.nextToken();
					MathTerm nextterm = parseTerm(tk, level + 1);
					if (nextterm == null)
						throw new RuntimeException("no follow term on " + tt);

					// special for definition
					if (operand[level].operand[x].equals(":=")) {
						// left only variables and function in level 1
						if (!checkForFunction(term))
							throw new RuntimeException("not for function " + term);
					}

					term = new MathTerm(operand[level].operand[x], term, nextterm);
					x = 0;
					tt = tk.lookup();
					System.out.println("zwischenstand " + term + " next " + tt);
					break; // break inner loop
				}
			}
			return term;
		} else {
			MathToken tt = tk.nextToken();
			// ends
			// is variable or function or ...
			if (tt == null)
				return null;
			if (tt.isChar() && tt.getText().equals("-")) {
				if (tk.lookup().isNumber()) {
					MathNumber n = MathNumber.parse(tk.nextToken().getText());
					return new MathTerm(new MathNumber(-1 * n.getDouble()));
				}
			}

			if (tt.isNumber()) {
				MathNumber n = MathNumber.parse(tt.getText());
				return new MathTerm(n);
			}

			if (tt.isWord()) {
				// is variable or function
				if (tt.getText().equals("noop"))
					return new MathTerm();
				MathTerm term = new MathTerm(tt.getText());

				MathToken nt = tk.lookup();
				if (nt == null)
					return term;
				if (nt.isChar("(")) {
					// is functioncall (or definition)
					tk.nextToken();
					while (true) {
						MathTerm cterm = parseTerm(tk, 0);
						if (cterm == null)
							throw new RuntimeException("term == null " + nt);
						term.add(cterm);

						nt = tk.nextToken();
						if (nt.isChar(")"))
							break;
						else if (nt.isChar(","))
							continue;
						else
							throw new RuntimeException(") or , expected " + nt);
					}
				}
				return term;
			}
			if (tt.isChar("[")) {
				MathMatrix matrix = new MathMatrix();
				MathTerm term = new MathTerm(matrix);
				while (true) {
					MathTerm cterm = parseTerm(tk, 0);
					if (cterm == null)
						throw new RuntimeException("term == null");
					matrix.addRow(new MathTerm[] { cterm });

					MathToken nt = tk.nextToken();
					if (nt.isChar("]"))
						break;
					else if (nt.isChar(","))
						continue;
					else
						throw new RuntimeException("] or , expected " + nt);
				}
				System.out.println(matrix);
				return term;
			}
			if (tt.isChar("(")) {
				MathTerm term = parseTerm(tk, 0);
				tt = tk.nextToken();
				if (tt.isChar(")"))
					return term;
				throw new RuntimeException(") failed " + tt);
			}
			// single operation
			throw new RuntimeException("only numbers implemented " + tt);
		}

	}

	private static MathTerm[] add(MathTerm[] a, MathTerm[] b) {
		MathTerm c[] = new MathTerm[a.length + b.length];
		for (int i = 0; i < a.length || i < b.length; i++) {
			if (i < a.length)
				c[i] = a[i];
			if (i < b.length)
				c[i + a.length] = b[i];
		}
		return c;
	}

	public final static StringCollector operand[] = new StringCollector[] { new StringCollector(new String[] { ":=" }),
			new StringCollector(new String[] { "+", "-" }), new StringCollector(new String[] { "*", "/" }),
			new StringCollector(new String[] { "^" }) };

	private static boolean checkForFunction(MathTerm term) {
		if (!term.isFunction())
			return false;
		for (MathTerm t : term.child) {
			if (t.isFunction() && t.child.size() == 0)
				continue;
			return false;
		}
		return true;
	}

}
