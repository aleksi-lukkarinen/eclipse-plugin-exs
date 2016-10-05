package com.packtpub.e4.clock.ui.internal;


import java.util.TimeZone;

import org.eclipse.core.runtime.IAdapterFactory;
import org.eclipse.ui.views.properties.IPropertySource;




public class TimeZoneAdapterFactory implements IAdapterFactory {

	@Override
	public Class<?>[] getAdapterList() {
		return new Class[] { IPropertySource.class };
	}

	@SuppressWarnings("unchecked")
	@Override
	public Object getAdapter(Object adaptableObject, Class adapterType) {
		if (adapterType == IPropertySource.class && adaptableObject instanceof TimeZone)
			return new TimeZonePropertySource((TimeZone) adaptableObject);
		
		return null;
	}

}
