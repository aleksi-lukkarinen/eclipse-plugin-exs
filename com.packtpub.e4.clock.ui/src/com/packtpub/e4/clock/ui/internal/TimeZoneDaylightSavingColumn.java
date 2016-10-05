package com.packtpub.e4.clock.ui.internal;


import java.util.TimeZone;




public class TimeZoneDaylightSavingColumn extends TimeZoneColumn {

	@Override
	public String getText(Object element) {
		if (element instanceof TimeZone)
			return String.valueOf(((TimeZone) element).useDaylightTime());
		
		return "";
	}

	@Override
	public String getTitle() {
		return "Daylight Saving";
	}

}
