package com.packtpub.e4.clock.ui.internal;


import java.util.Date;
import java.util.TimeZone;

import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource;
import org.eclipse.ui.views.properties.PropertyDescriptor;




public class TimeZonePropertySource implements IPropertySource {

	private static final Object ID = new Object();
	private static final Object DAYLIGHT = new Object();
	private static final Object NAME = new Object();
	
	private TimeZone timeZone;
	
	public TimeZonePropertySource(TimeZone timeZone) {
		this.timeZone = timeZone;
	}
	
	@Override
	public Object getEditableValue() {
		return null;
	}

	@Override
	public IPropertyDescriptor[] getPropertyDescriptors() {
		return new IPropertyDescriptor[] {
			new PropertyDescriptor(ID, "Time Zone"),
			new PropertyDescriptor(DAYLIGHT, "Daylight Savings"),
			new PropertyDescriptor(NAME, "Name"),
		};
	}

	@Override
	public Object getPropertyValue(Object id) {
		if (id.equals(ID))
			return timeZone.getID();
		
		if (id.equals(DAYLIGHT))
			return timeZone.inDaylightTime(new Date());
		
		if (id.equals(NAME))
			return timeZone.getDisplayName();
		
		return null;
	}

	@Override
	public boolean isPropertySet(Object id) {
		return false;
	}

	@Override
	public void resetPropertyValue(Object id) {

	}

	@Override
	public void setPropertyValue(Object id, Object value) {

	}

}
