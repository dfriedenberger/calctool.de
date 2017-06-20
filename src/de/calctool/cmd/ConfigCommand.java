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
import de.calctool.vm.*;

import java.util.Hashtable;

public class ConfigCommand extends MathCommand
{
    public String getName() { return "conf"; };
    public MathResult[] eval(MathRuntime rt,String par)
    {
        String p[] = split(par);
	String resp = "FAILED unknown parameter \""+par+"\"";
	
	for(int i = 0;i < p.length;i++)
	    {
		String k[] = p[i].split("=");
		String key = k[0].trim().toLowerCase();
		//now try to interpret the commands
		if(key.equals("numeric"))
		    {
			rt.set(rt.METHOD,rt.NUMERIC);
			resp = "set evalmethod to numeric";
		    }
		if(key.equals("symbolic"))
		    {
			rt.set(rt.METHOD,rt.SYMBOLIC);
			resp = "set evalmethod to symbolic";
		    }
	    }
	return new MathResult[]{ new MathResult(resp) };
    }
}

