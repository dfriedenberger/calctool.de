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

import java.util.*;

public class MathRuntime {
	private Hashtable<String, MathTerm> definition = new Hashtable<String, MathTerm>();
	private Vector<Hashtable<String, MathTerm>> varstack = new Vector<Hashtable<String, MathTerm>>();
	

	public MathRuntime(String file) {
		init();
	}

	public final static String METHOD = "method";
	public final static String SYMBOLIC = "symbolic";
	public final static String NUMERIC = "numeric";

	private Hashtable<String, String> config = new Hashtable<String, String>();

	public void set(String k, String v) {
		config.put(k, v);
	}

	public String get(String k) {
		return config.get(k);
	}

	private void init() {
		set(METHOD, SYMBOLIC);
		//set(METHOD, NUMERIC);

	}

	/*
	public void setWidth(int width) {
		WIDTH = width;
	}

	public void setPartHeight(int height) {
		HP = height;
	}

	public void setBGColor(int c[]) {
		bgcolor = new int[] { c[0], c[1], c[2] };
	}
*/
	public void setDefinition(MathTerm def) {
		// System.out.println("define ("+def.getFuncname()+") = " + def);
		definition.put(def.getFuncname(), def);
	}

	private String lock = null;

	public void lock(String def) {
		lock = def;
	}

	public void unlock(String def) {
		lock = null;
	}

	public MathTerm getDefinition(String name) {
		if (name.equals(lock))
			return null;
		// System.out.println("definition ("+name+") = " +
		// definition.get(name));
		return definition.get(name);
	}

	public void cleanup() {
		varstack = new Vector<Hashtable<String, MathTerm>>();
		lock = null;
	}

	public void createVarStack() {
		if (varstack.size() > 250)
			throw new RuntimeException("Stackoverflow deep: 250");
		varstack.add(new Hashtable<String, MathTerm>());
	}

	public void removeVarStack() {
		varstack.removeElementAt(varstack.size() - 1);
	}

	public void setVar(String varname, MathTerm term) {
		varstack.elementAt(varstack.size() - 1).put(varname, term);
	}

	public MathTerm getVar(String varname) {
		for (int i = varstack.size() - 1; i >= 0; i--) {
			Hashtable<String, MathTerm> ht = varstack.elementAt(i);
			MathTerm term = ht.get(varname);
			if (term != null)
				return term;
		}
		return null;
	}

	public final static int MaxSumLoops = 5000;

	public void log(Object o, Object str) {
		// System.out.println(""+str);
	}
}
