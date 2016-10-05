package com.packtpub.e4.junit.example;


import static org.junit.Assert.*;

import org.junit.Test;




public class MathUtilTest {

	@Test
	public void testOdd() {
		assertTrue(MathUtil.isOdd(3));
		assertFalse(MathUtil.isOdd(4));
	}
	
}
