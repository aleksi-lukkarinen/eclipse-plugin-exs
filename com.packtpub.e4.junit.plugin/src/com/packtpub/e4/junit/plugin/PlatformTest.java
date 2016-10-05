package com.packtpub.e4.junit.plugin;


import static org.junit.Assert.*;

import org.eclipse.core.runtime.Platform;
import org.junit.Test;




public class PlatformTest {

	@Test
	public void test() {
		assertTrue(Platform.isRunning());
	}
	
}
