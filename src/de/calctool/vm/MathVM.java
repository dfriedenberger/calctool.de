// A mathematic virtuell machine
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

import de.calctool.cmd.*;
import de.calctool.eval.CommandEvaluator;
import de.calctool.eval.MathEvaluator;
import de.calctool.parser.CommandParser;
import de.calctool.parser.MathDefinitions;
import de.calctool.parser.MathParser;

import java.util.*;
import java.util.List;

public class MathVM {
	private Hashtable<String, MathRuntime> runtimes = new Hashtable<String, MathRuntime>(); 
	private final MathDefinitions definitions;

	public MathVM() {
		definitions = MathDefinitions.DefaultDefinitions;
	}

	public MathDefinitions getDefinitions() {
		return definitions;
	}

	public MathRuntime getRuntime(String sid) {
		long runtimenr = 0;
		try {
			runtimenr = Long.parseLong(sid);
		} catch (Exception ignoredexc) {
		}

		MathRuntime rt = runtimes.get("" + runtimenr);
		if (rt == null) {
			// read from file
			String path = "DATA/rt" + runtimenr + ".dat";
			System.out.println(" create new Runtime ");
			rt = new MathRuntime(path);
			if (runtimenr > 0)
				runtimes.put("" + runtimenr, rt);
		}
		return rt;
	}

	public MathResult[] calculate(String command, MathRuntime rt) {
		// special commands
		try {

			
			CommandParser cparser = new CommandParser(definitions);
			CommandEvaluator cevaluator = new CommandEvaluator(definitions, rt);

			MathTerm cterm = cparser.parse(command);
			if(cterm != null)
				return cevaluator.eval(cterm);
			
			MathParser parser = new MathParser(definitions);
			MathEvaluator evaluator = new MathEvaluator(definitions, rt);
			
			MathTerm term[] = parser.parse(command, rt);
			List<MathResult> results = new ArrayList<MathResult>();
			for (int i = 0; i < term.length; i++) {
				MathTerm t = evaluator.eval(term[i]);
				results.add(new MathResult(t));
				if (evaluator.isNumber(t) && !t.getNumber().isFractal())
					continue;

				MathTerm t2 = evaluator.evalNumeric(t);
				if (!evaluator.isNumber(t2))
					continue;
				results.add(new MathResult(new MathTerm(new MathNumber(t2.getNumber().getDouble()))));

			}
			return results.toArray(new MathResult[0]);
		} catch (Exception e) {
			e.printStackTrace();
			rt.cleanup();
			return new MathResult[] { new MathResult(e.getMessage()) };
		}
	}

}
