package com.packtpub.e4.clock.ui.internal;


import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;

import org.eclipse.jface.viewers.ITreeContentProvider;




public class TimeZoneContentProvider implements ITreeContentProvider {

	@Override
	public Object[] getElements(Object inputElement) {
		if (inputElement instanceof Object[])
			return (Object[]) inputElement;
		
		return new Object[0];
	}

	@SuppressWarnings("unchecked")
	@Override
	public Object[] getChildren(Object parentElement) {
		if (parentElement instanceof Map)
			return ((Map<String, Set<TimeZone>>) parentElement).entrySet().toArray();

		if (parentElement instanceof Map.Entry)
			return getChildren(((Map.Entry<String, Set<TimeZone>>) parentElement).getValue());
		
		if (parentElement instanceof Collection)
			return ((Collection<?>) parentElement).toArray();
		
		return new Object[0];
	}

	@Override
	public Object getParent(Object element) {
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean hasChildren(Object element) {
		if (element instanceof Map)
			return !((Map<String, Set<TimeZone>>) element).isEmpty();

		if (element instanceof Map.Entry)
			return hasChildren(((Map.Entry<String, Set<TimeZone>>) element).getValue());
		
		if (element instanceof Collection)
			return !((Collection<?>) element).isEmpty();

		return false;
	}

}
