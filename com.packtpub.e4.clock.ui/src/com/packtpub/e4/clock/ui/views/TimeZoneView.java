package com.packtpub.e4.clock.ui.views;


import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TimeZone;

import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.ui.IMemento;
import org.eclipse.ui.IViewSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.ViewPart;

import com.packtpub.e4.clock.ui.Activator;
import com.packtpub.e4.clock.ui.ClockWidget;
import com.packtpub.e4.clock.ui.internal.TimeZoneComparator;




public class TimeZoneView extends ViewPart {

	private transient String lastTabSelected;
	
	
	public TimeZoneView() {

	}

	@Override
	public void createPartControl(Composite parent) {
		Display display = Display.getDefault();
		long curTime = System.currentTimeMillis();
		Map<String, Set<TimeZone>> timeZones = TimeZoneComparator.getTimeZones();
		CTabFolder tabs = new CTabFolder(parent, SWT.BOTTOM);
		Iterator<Entry<String, Set<TimeZone>>> regionIterator = timeZones.entrySet().iterator();
		while (regionIterator.hasNext()) {
			Entry<String, Set<TimeZone>> region = regionIterator.next();
			CTabItem item = new CTabItem(tabs, SWT.NONE);
			item.setText(region.getKey());
			
			ScrolledComposite scrolled = new ScrolledComposite(tabs, SWT.H_SCROLL | SWT.V_SCROLL);  
			item.setControl(scrolled);

			Composite clocks = new Composite(scrolled, SWT.NONE);
			clocks.setLayout(new RowLayout());
			clocks.setBackground(display.getSystemColor(SWT.COLOR_LIST_BACKGROUND));
			scrolled.setContent(clocks);

			RGB rgb = new RGB(128, 128, 128);
			TimeZone td = TimeZone.getDefault();
			int td_offset = td.getOffset(curTime);
			Iterator<TimeZone> timezoneIterator = region.getValue().iterator();
			while (timezoneIterator.hasNext()) {
				TimeZone tz = timezoneIterator.next();

				Group group = new Group(clocks, SWT.SHADOW_ETCHED_IN);
				group.setText(tz.getID().split("/")[1]);
				group.setLayout(new FillLayout());
				
				ClockWidget clock = new ClockWidget(group, SWT.NONE, rgb);
				
				int offset = (tz.getOffset(curTime) - td_offset) / 3600000;
				clock.setOffset(offset);
			}
			
			Point size = clocks.computeSize(SWT.DEFAULT, SWT.DEFAULT);
			scrolled.setMinSize(size);
			scrolled.setExpandHorizontal(true);
			scrolled.setExpandVertical(true);
		}

		IDialogSettings settings = Activator.getDefault().getDialogSettings();
		lastTabSelected = settings.get("lastTabSelected");
		
		if (lastTabSelected == null) {
			tabs.setSelection(0);
		}
		else {
			CTabItem[] items = tabs.getItems();
			for (CTabItem item : items) {
				if (lastTabSelected.equals(item.getText())) {
					tabs.setSelection(item);
					break;
				}
			}
		}
		
		tabs.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (e.item instanceof CTabItem) {
					lastTabSelected = ((CTabItem) e.item).getText();
					settings.put("lastTabSelected", lastTabSelected);
				}
			}
		});
	}

	@Override
	public void setFocus() {

	}

}
