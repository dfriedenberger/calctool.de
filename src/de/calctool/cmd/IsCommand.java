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

public class IsCommand extends MathCommand
{
    public String getName() { return "is"; };
    public MathResult[] eval(MathRuntime rt,String par)
    {
        String p[] = split(par);
        MathResult r[] = new MathResult[p.length];
        for(int i = 0;i < p.length;i++)
            r[i] = is(p[i]);
        return r;
    }
    private MathResult is(String val)
    {
        MathNumber n = MathNumber.parse(val);
        if(n.isLong())
        {
            String number = "";
            long l = n.getNumerator();
            String out = ""+l+" 0x"+Long.toHexString(l);

            if(l <= 0xffff) out += " 0b"+Long.toBinaryString(l);

            return new MathResult(out);
        }
        return new MathResult(new MathTerm(n));
    } 
}

