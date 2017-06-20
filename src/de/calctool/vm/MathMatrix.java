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

import de.calctool.image.MathImageCreator;

public class MathMatrix
{
    //cell[r(ow)][c(olumn)]
    private MathTerm cell[][] = new MathTerm[0][0];
    public MathMatrix()  {}
    public void addRow(MathTerm t[])
    {
	blowup(cell.length + 1,t.length);
	
	for(int i = 0;i < t.length;i++)
	    cell[cell.length - 1][i] = t[i];
    }
    public MathTerm getCell(int r,int c)
    {
	return cell[r][c];
    }
    public void setCell(MathTerm t,int r,int c)
    {
	blowup(r+1,c+1);
	cell[r][c] = t;
    }
    public int rows()
    {
	return cell.length;
    }
    public int columns()
    {
	if(cell.length == 0)
	    return 0;
	return cell[0].length;
    }
    public void addColumn(MathMatrix m)
    {
	int coff = columns();
	blowup(m.cell.length,columns() + m.columns());
	
	for(int r = 0;r < m.cell.length;r++)
	    for(int c = 0;c < m.columns();c++)
		cell[r][coff + c] = m.cell[r][c];
    }
    public MathImage toImage(MathImageCreator imageCreator)
    {
	//inner Images 
	MathImage img[][] = new MathImage[cell.length][columns()];
	for(int r = 0;r < cell.length;r++)
	    for(int c = 0;c < columns();c++)
		img[r][c] = imageCreator.createImage(cell[r][c]); 
	
	MathImage nimg = imageCreator.concat(img);
	
	int h = nimg.getHeight();
	MathImage left = imageCreator.createImage(h);
	left.clear(false);
	left.printLine(14,3,10,7);
	left.printLine(10,7,10,h-7);
	left.printLine(14,h - 3,10,h-7);
	left.setMaxwidth(15);
	MathImage right = imageCreator.createImage(h);
	right.clear(false);
	right.printLine(6,3,10,7);
	right.printLine(10,7,10,h-7);
	right.printLine(6,h-3,10,h-7);
	right.setMaxwidth(15);

	return imageCreator.concat(  new MathImage[]{ left , nimg , right } );
    }
    private void blowup(int rows,int col)
    {
	if(rows <= cell.length && col <= columns()) return;
	if(rows < cell.length) rows = cell.length;
	if(col < columns()) col  = columns();
	
	MathTerm n[][] = new MathTerm[rows][col];
	for(int r = 0;r < n.length;r++)
	    {
		if(r < cell.length)
		    {
			for(int c = 0;c < n[0].length;c++)
			    {
				if(c < cell[0].length)
				    n[r][c] = cell[r][c];
				else
				    {
					//debug: System.out.println("blowup ("+rows+","+col+") new ("+r+","+c+")");
					n[r][c] = new MathTerm(new MathNumber(0)); 
				    }
			    }
		    }
		else
		    for(int c = 0;c < n[0].length;c++) 
			{
			    //debug: System.out.println("blowup ("+rows+","+col+") new ("+r+","+c+")");
			    n[r][c] = new MathTerm(new MathNumber(0));
			} 
	    }
	cell = n;
    }
    public String toString()
    {
	String text = "";
	for(int r = 0;r < cell.length;r++)
	    {
		text += "[";

		for(int c = 0;c < cell[0].length;c++)
		    text += cell[r][c]+"  ";
		text += "] ";
	    }
	return text;
    }
}

