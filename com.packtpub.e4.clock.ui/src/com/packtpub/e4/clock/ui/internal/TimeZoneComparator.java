package com.packtpub.e4.clock.ui.internal;


import java.util.Comparator;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;
import java.util.TreeMap;
import java.util.TreeSet;




public class TimeZoneComparator implements Comparator<TimeZone> {

	public int compare(TimeZone o1, TimeZone o2) {
		return o1.getID().compareTo(o2.getID());
	}

	public static Map<String, Set<TimeZone>> getTimeZones() {
		Map<String, Set<TimeZone>> timeZones = new TreeMap<>();
		
		String[] ids = TimeZone.getAvailableIDs();
		for (int i = 0; i < ids.length; i++) {
			String[] parts = ids[i].split("/");
			if (parts.length == 2) {
				String region = parts[0];
				Set<TimeZone> zones = timeZones.get(region);
				if (zones == null) {
					zones = new TreeSet<TimeZone>(new TimeZoneComparator());
					timeZones.put(region, zones);
				}
				TimeZone timeZone = TimeZone.getTimeZone(ids[i]);
				zones.add(timeZone);
			}
		}
		return timeZones;
	}
	
}
