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

import java.awt.image.*;
import java.awt.geom.*;
import java.awt.*;

public class MathImage {
	
	public int bgcolor[] = new int[] { 0xFF, 0xFF, 0xFF };
	public int off = 4;

	
	
	private BufferedImage image;
	private String name;
	private int parts;
	private Graphics g;
	public int width;
	public int height;
	public int hpart;
	public int line;
	private int maxwidth = 0;
	private int[] color;
	MathRuntime rt = null;

	public MathImage(BufferedImage image, String name, int parts, MathRuntime rt) {
		this.image = image;
		this.name = name;
		this.parts = parts;
		this.width = image.getWidth(null);
		this.height = image.getHeight(null);
		this.hpart = this.height / parts;
		this.g = image.createGraphics();
		this.color = bgcolor;
		this.line = 0;
		this.rt = rt;
		clear(false);
	}

	public void clear(boolean frame) {
		int off = 0;
		// 9BD1FA
		Color c = new Color(color[0], color[1], color[2]);
		;
		if (frame) {
			g.setColor(Color.black);
			g.fillRect(0, 0, width, height);
			off = 2;
			c = Color.white;
		}
		g.setColor(c);
		g.fillRect(off, off, width - (2 * off), height - (2 * off));
	}

	public void println(Object o) {
		printString(o.toString(), off, line * hpart, hpart);
		line++;
	}

	public void printString(String text, int xpos, int ypos, int s) {
		int size = (int) (s * 0.75);
		g.setColor(Color.black);
		Font nf = new Font(g.getFont().getFamily(), g.getFont().getStyle(), size);
		g.setFont(nf);
		Rectangle2D sr = g.getFontMetrics().getStringBounds(text, g);
		int len = (int) (2 * sr.getCenterX());
		g.drawString(text, xpos, ypos + size);
		setMaxwidth(xpos + len);
	}

	public void setMaxwidth(int w) {
		if (w < maxwidth)
			return;
		if (w > width)
			maxwidth = width;
		else
			maxwidth = w;
	}

	public void printLine(double x1, double y1, double x2, double y2) {
		System.out.println("(" + x1 + "," + y1 + "/" + x2 + "," + y2 + ")");
		//if (x1 < 0 || width < x2 || y1 < 0 || height < y2)
		//	throw new RuntimeException("(" + x1 + "," + y1 + "/" + x2 + "," + y2 + ") out of Range");
		g.setColor(Color.black);
		g.drawLine((int) x1, (int) y1, (int) x2, (int) y2);
	}

	public void add(MathImage i, int x, int y, int h) {
		Image img = i.getImage();
		int w = (i.filledWidth() * h) / img.getHeight(null);
		setMaxwidth(x + w);
		g.drawImage(img, x, y, x + w, y + h, 0, 0, i.filledWidth(), i.getHeight(), null);
	}

	public String getName() {
		return name;
	}

	public BufferedImage getImage() {
		return image;
	}

	public Graphics getGraphics() {
		return g;
	}

	public int getHeight() {
		return image.getHeight();
	}

	public int filledWidth() {
		return maxwidth;
	}

	public int getParts() {
		return parts;
	}
}
