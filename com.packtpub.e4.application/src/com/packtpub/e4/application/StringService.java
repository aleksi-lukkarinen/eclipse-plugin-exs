package com.packtpub.e4.application;


import org.eclipse.e4.core.di.annotations.Creatable;




@Creatable
public class StringService {
	
	public String process(String stringToProcess) {
		return stringToProcess.toUpperCase();
	}
	
}
