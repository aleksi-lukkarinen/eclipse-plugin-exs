package com.packtpub.e4.clock.ui.internal;


import java.util.Date;
import java.util.TimeZone;




public class TimeZoneOffsetColumn extends TimeZoneColumn {

	@Override
	public String getText(Object element) {
		if (element instanceof TimeZone)
			return String.valueOf(((TimeZone) element).getOffset(((TimeZone) element).getOffset(new Date().getTime())));
		
		return "";
	}

	@Override
	public String getTitle() {
		return "Offset";
	}

}
