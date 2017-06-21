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
package de.calctool.cmd;


import java.util.Date;

import de.calctool.vm.MathNumber;
import de.calctool.vm.MathResult;
import de.calctool.vm.MathRuntime;
import de.calctool.vm.MathTerm;

public class TimeCommand extends MathCommand {
	
	public String getName() {
		return "time";
	};

	public MathResult[] eval(MathRuntime rt, MathTerm[] p) {
		if (p.length == 0) {
			long now = new Date().getTime();
			String s = (now / 1000) + " " + (now % 1000) + " msec";
			return new MathResult[] { new MathResult(s), time(new MathTerm(new MathNumber(now))) };
		}
		MathResult r[] = new MathResult[p.length];
		for (int i = 0; i < p.length; i++)
			r[i] = time(p[i]);
		return r;
	}

	private MathResult time(MathTerm val) {
		MathNumber n = val.getNumber();
		if (n.isLong()) {
			long ui4_border = 4294967295L;
			String t = "";
			long l = n.getNumerator();
			System.out.println("time : " + l + "  " + (l < ui4_border) + " " + (0xffffffff));
			if (l < ui4_border)
				l *= 1000;
			System.out.println("time : " + l);

			t = "" + new Date(l);
			return new MathResult(t);
		}
		return new MathResult(new MathTerm(n));
	}
}
