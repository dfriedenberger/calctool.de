package de.calctool.func;

import static org.junit.Assert.*;

import org.junit.Test;

public class TestFindFactor {

	@Test
	public void test() {
	
		FindFactor ff = new FindFactor(Math.PI,2);
	
		double epsilon = 0.000000000009;
		assertTrue(ff.getFactor(0.0 * Math.PI + epsilon).is(0));
		assertTrue(ff.getFactor(0.5 * Math.PI + epsilon).is(1));
		assertTrue(ff.getFactor(1.0 * Math.PI + epsilon).is(2));
		assertTrue(ff.getFactor(1.5 * Math.PI + epsilon).is(3));
		assertTrue(ff.getFactor(2.0 * Math.PI + epsilon).is(4));

		assertTrue(ff.getFactor(0.0 * Math.PI - epsilon).is(0));
		assertTrue(ff.getFactor(0.5 * Math.PI - epsilon).is(1));
		assertTrue(ff.getFactor(1.0 * Math.PI - epsilon).is(2));
		assertTrue(ff.getFactor(1.5 * Math.PI - epsilon).is(3));
		assertTrue(ff.getFactor(2.0 * Math.PI - epsilon).is(4));
		
		assertTrue(ff.getFactor(0.0 * Math.PI).mod(4).is(0));
		assertTrue(ff.getFactor(0.5 * Math.PI).mod(4).is(1));
		assertTrue(ff.getFactor(1.0 * Math.PI).mod(4).is(2));
		assertTrue(ff.getFactor(1.5 * Math.PI).mod(4).is(3));
		assertTrue(ff.getFactor(2.0 * Math.PI).mod(4).is(0));
		
		assertTrue(ff.getFactor(0.0 * Math.PI).mod(2).is(0));
		assertTrue(ff.getFactor(0.5 * Math.PI).mod(2).is(1));
		assertTrue(ff.getFactor(1.0 * Math.PI).mod(2).is(0));
		assertTrue(ff.getFactor(1.5 * Math.PI).mod(2).is(1));
		assertTrue(ff.getFactor(2.0 * Math.PI).mod(2).is(0));
		
		
		assertTrue(ff.getFactor(-0.5 * Math.PI).is(-1));
		assertTrue(ff.getFactor(-1.0 * Math.PI).is(-2));
		assertTrue(ff.getFactor(-1.5 * Math.PI).is(-3));
		assertTrue(ff.getFactor(-2.0 * Math.PI).is(-4));
		
		
		assertTrue(ff.getFactor(-0.0 * Math.PI).mod(4).is(0));
		assertTrue(ff.getFactor(-0.5 * Math.PI).mod(4).is(-1));
		assertTrue(ff.getFactor(-1.0 * Math.PI).mod(4).is(-2));
		assertTrue(ff.getFactor(-1.5 * Math.PI).mod(4).is(-3));
		assertTrue(ff.getFactor(-2.0 * Math.PI).mod(4).is(0));
		
		
		
		assertTrue(ff.getFactor(1.5707963267948966).mod(2).is(1));
		
		
		assertFalse(ff.getFactor(-0.1 * Math.PI).mod(4).is(0));
		
	}

}
