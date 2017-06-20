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
package de.calctool.parser;


public class MathToken
{
    private String term;
    private int type = 0;
    private MathToken(String term)  {
        this.term = term;
    }
    private void setType(int type)
    {
        this.type = type;
    }
    public String toString()
    {
        return term;
    }
    public String getText()
    {
        return term;
    }
    public static MathToken singlechar(String term)
    {
        MathToken t = new MathToken(term);
        t.setType(3);
        return t;
    }
    public static MathToken doublechar(String term)
    {
        MathToken t = new MathToken(term);
        t.setType(4);
        return t;
    }
    public static MathToken number(String term)
    {
        MathToken t = new MathToken(term);
        t.setType(1);
        return t;
    }
    public static MathToken word(String term)
    {        
        MathToken t = new MathToken(term);
        t.setType(2);
        return t;
    }
    public boolean isChar()
    {
        return (type == 3 || type == 4);
    }
    public boolean isChar(String c)
    {
        if(type != 3 && type != 4) return false;
        return term.equals(c);
    }
    public boolean isNumber()
    {
        return (type == 1);
    }
    public boolean isWord()
    {
        return (type == 2);
    }
}

