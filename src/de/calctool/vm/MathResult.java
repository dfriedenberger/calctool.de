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

import java.awt.image.BufferedImage;

import de.calctool.image.MathImageCreator;

public class MathResult {
	int type = 0; // 0 - virtuell , 1 = single-line text , 2 - MathTerm
	String text = "";
	public MathTerm term;

	public MathResult(String text) {
		type = 1;
		this.text = text;
	}

	public MathResult(MathTerm term) {
		this.term = term;
		type = 2;
	}

	public boolean isText() {
		return type == 1;
	}

	public String getText() {
		return text;
	}

	public String toString() {
		switch (type) {
		case 1:
		case 3:
			return text;
		case 2:
			if (term.isAscii())
				return term.getString(false);
			if (term.isVirtuell())
				return "";
		}
		throw new RuntimeException("type = " + type + " not supported");
	}

}
