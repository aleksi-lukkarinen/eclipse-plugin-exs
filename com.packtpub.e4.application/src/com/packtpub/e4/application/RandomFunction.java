package com.packtpub.e4.application;


import org.eclipse.e4.core.contexts.ContextFunction;
import org.eclipse.e4.core.contexts.IEclipseContext;




public class RandomFunction extends ContextFunction {

	@Override
	public Object compute(IEclipseContext context, String contextKey) {
		return Math.random();
	}

}
