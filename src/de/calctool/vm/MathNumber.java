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
package de.calctool.vm;

public class MathNumber
{
    public  final static int DOUBLE   = 0;
    public  final static int FRACTAL  = 1;
    public  final static int BOOLEAN  = 2;
    private int type = -1;
    private double d;
    private boolean b;
    public MathNumber(double d)
    {
        this.d = d;
        type = DOUBLE;
    }
    public MathNumber(long l)
    {
        this.d = l;
        type = DOUBLE;
    }
    public MathNumber(long n,long d)
    {
        long bcm    = bigestComonMeasure(n,d);
        numerator   = n/bcm;
        denominator = d/bcm;
        type = FRACTAL;

        if(denominator == 1)
        {
        	 this.d = numerator;
             type = DOUBLE;
        }
    }
    public MathNumber(boolean b)
    {
        this.b = b;
        type = BOOLEAN;
    }
  
    public boolean isBool()
    {
        return (type == BOOLEAN);
    }
    public boolean getBool()
    {
        switch(type)
        {
        case BOOLEAN:
            return b;
        }
        throw new RuntimeException("not a bool "+this);
    }
    public boolean isLong()
    {
        long t = (long)d;
        return (t == d);
    }
    private long numerator   = 0;
    private long denominator = 0;
    public long getNumerator()
    {
        if(isFractal()) return numerator;
        if(isLong()) return (long)d;
        throw new RuntimeException("not fractal or long");
    }
    public double getDouble()
    {
        switch(type)
        {
        case DOUBLE:
            return d;
        case FRACTAL:
            return ((double)numerator)/denominator;
	}
        throw new RuntimeException("canot convert to double");
    }
    public long getDemoninator()
    {
        if(isFractal()) return denominator;
        if(isLong()) return 1;
        throw new RuntimeException("not fractal or long");
    }
    public boolean isFractal()
    {
        return type == FRACTAL;
    }
    private long bigestComonMeasure(long n,long m)
    {
        if(n < 0) n *= -1;
        if(m < 0) m *= -1;
        long z;
        while (n > 0) 
        {
            z = n;
            n = m % n;
            m = z;
        }
        z = m;
        return z;
    }

    public boolean isNumber(double d)
    {
        return (getDouble() == d);
    }
    public String toString()
    {
        return getString(false);
    }
    public String getString(boolean html)
    {
        switch(type)
        {
        case DOUBLE:
            //formated output
            if(isLong()) return ""+(long)d;
            return ""+d;
        case FRACTAL:
            return numerator+"/"+denominator;
        case BOOLEAN:
            return ""+b;
	default:
            return "MathNumber type = "+type;
        }
    }
    public static MathNumber parse(String format)
    {
        String zahl = format.trim().toLowerCase();
        
        if(zahl.startsWith("0x"))
        {
            char[] hex = zahl.substring(2).toCharArray();
            long z = 0;
            for(int i = 0;i < hex.length;i++)
            {
                long d = 0;
                if('0' <= hex[i] && hex[i] <= '9') d = hex[i] - '0';
                if('a' <= hex[i] && hex[i] <= 'f') d = hex[i] - 'a' + 10;
                z = (z << 4) + d;
            }
            return new MathNumber(z);
        }
        if(zahl.startsWith("0b"))
        {
            char[] bin = zahl.substring(2).toCharArray();
            long z = 0;
            for(int i = 0;i < bin.length;i++)
            {
                long d = 0;
                if(bin[i] == '1') d = 1;
                z = (z << 1) + d;
            }
            return new MathNumber(z);
        }
        return new MathNumber(Double.parseDouble(zahl));
    }
}

