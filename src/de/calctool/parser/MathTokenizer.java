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
import java.util.Vector;
public class MathTokenizer
{
    private final static int WORD       = 1;
    private final static int NUMBER     = 2;
    private final static int DOUBLECHAR = 3;
    private Vector<MathToken> v = new Vector<MathToken>();
    private String doublechar[] = new String[]{ "<=" , "=>" , ":=" };
    private String singlechar[] = new String[]{ "+" , "-" , "*" ,"/" , "^" , "(" , ")" , "," ,"[" , "]" };
    public MathTokenizer(String term)  {
        
        int i = 0;
        int x;

        int state = 0;
        String text = null;
        term += " ";
        while(i < term.length())
        {
            char c = term.charAt(i);
            switch(state)
            {
            case 0:
                if(c == ' ' || c == '\t' || c == '\r' ||  c == '\n') break;
                
                if( ('a' <= c && c <= 'z') ||
                    ('A' <= c && c <= 'Z') ||
                    (c == '_'))
                {
                    state = WORD;
                    text = ""+c;
                    break;
                }

                if( ('0' <= c && c <= '9') ||
                    (c == '.'))
                {
                    state = NUMBER;
                    text = ""+c;
                    break;
                }

                for(x = 0;x < doublechar.length;x++)
                {
                    if(doublechar[x].charAt(0) == c)
                    {
                        state = DOUBLECHAR;
                        text = ""+c;
                        break;
                    }
                }
                if( x < doublechar.length ) break;

                for(x = 0;x < singlechar.length;x++)
                {
                    if(singlechar[x].charAt(0) == c)
                    {
                        v.add(MathToken.singlechar(singlechar[x]));
                        break;
                    }
                }
                if( x < singlechar.length ) break;
                throw new RuntimeException(" unknown char "+c+" on pos "+i);
            case WORD:
                if( ('a' <= c && c <= 'z') ||
                    ('A' <= c && c <= 'Z') ||
                    ('0' <= c && c <= '9') ||
                    (c == '_'))
                {
                    text += c;
                    break;
                }
                
                v.add(MathToken.word(text));
                state = 0;
                text = null;
                continue;
            case NUMBER:
                if( ('a' <= c && c <= 'f') ||
                    ('A' <= c && c <= 'F') ||
                    ('0' <= c && c <= '9') ||
                    (c == 'e') ||
                    (c == 'x') ||
                    (c == 'X') ||
                    (c == '.') )
                {
                    text += c;
                    break;
                }
                v.add(MathToken.number(text));
                state = 0;
                text = null;
                continue;
            case DOUBLECHAR:
                for(x = 0;x < doublechar.length;x++)
                {
                    if(doublechar[x].charAt(0) == text.charAt(0))
                    {
                        if(doublechar[x].charAt(1) == c)
                        {
                            v.add(MathToken.doublechar(doublechar[x]));
                            state = 0;
                            text  = null;
                            break;
                        }
                        break;
                    }
                }
                if( x < doublechar.length ) break;

                for(x = 0;x < singlechar.length;x++)
                {
                    if(singlechar[x].charAt(0) == text.charAt(0))
                    {
                        v.add(MathToken.singlechar(singlechar[x]));
                        state = 0;
                        text  = null;
                        break;
                    }
                }
                if( x < singlechar.length ) continue; /* spec same char again */
                throw new RuntimeException(" unknown char "+text+" on pos "+(i-1));
            default:
                throw new RuntimeException(" unknown state "+state);
            }
            i++;
        }
    }
    
    private int pos = -1;
    public MathToken nextToken()
    {
        pos++;
        if(pos < v.size()) return v.elementAt(pos);
        return null;
    }
    public MathToken lookup()
    {
        if((pos + 1) < v.size()) return v.elementAt(pos + 1);
        return null;
    }
}

