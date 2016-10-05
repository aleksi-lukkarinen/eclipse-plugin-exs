package com.packtpub.e4.clock.ui.internal;


import java.util.TimeZone;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerComparator;




public class TimeZoneViewerComparator extends ViewerComparator {

	@Override
	public int compare(Viewer viewer, Object e1, Object e2) {
		int result;

		if (e1 instanceof TimeZone && e2 instanceof TimeZone) {
			long curMillis = System.currentTimeMillis();

			int offset1 = ((TimeZone) e1).getOffset(curMillis);
			int offset2 = ((TimeZone) e2).getOffset(curMillis);

			result = offset2 - offset1; 

			boolean reverse = Boolean.parseBoolean(String.valueOf(viewer.getData("REVERSE")));
			if (reverse)
				result = -result;
		}

		result = e1.toString().compareTo(e2.toString());

		return result;
	}
	
}
