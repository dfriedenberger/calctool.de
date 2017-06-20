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

public abstract class MathCommand
{
    abstract public String getName();
    abstract public MathResult[] eval(MathRuntime rt,String par);
    
  
    protected String[] split(String line)
    {
	    if(line.trim().equals("")) return new String[0];
        while(true)
        {
            int i = line.indexOf("  ");
            if(i < 0) break;
            line = line.substring(0,i) + " " + line.substring(i+2);
        }
        return line.trim().split(" ");
    }
}

