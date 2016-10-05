package com.packtpub.e4.clock.ui.views;


import java.util.TimeZone;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.*;

import com.packtpub.e4.clock.ui.ClockWidget;




/**
 * This sample class demonstrates how to plug-in a new
 * workbench view. The view shows data obtained from the
 * model. The sample creates a dummy model on the fly,
 * but a real implementation would connect to the model
 * available either in this or another plug-in (e.g. the workspace).
 * The view is connected to the model using a content provider.
 * <p>
 * The view uses a label provider to define how model
 * objects should be presented in the view. Each
 * view can present the same model objects using
 * different labels and icons, if needed. Alternatively,
 * a single label provider can be shared between views
 * in order to ensure that objects of the same type are
 * presented in the same way everywhere.
 * <p>
 */
public class ClockView extends ViewPart {

	private Combo timezones;

	
	/**
	 * The constructor.
	 */
	public ClockView() {

	}

	/**
	 * This is a callback that will allow us
	 * to create the viewer and initialize it.
	 */
	public void createPartControl(Composite parent) {
		/*
		Object[] oo = parent.getDisplay().getDeviceData().objects;
		int colorCount = 0;
		for (int i=0; i < oo.length; i++) {
			if (oo[i] instanceof Color)
				colorCount++;
		}
		System.err.print("there are " + colorCount + " Color instanses.");
		*/

		RowLayout layout = new RowLayout(SWT.HORIZONTAL);
		parent.setLayout(layout);

		final ClockWidget clock1 = new ClockWidget(parent, SWT.NONE, new RGB(255, 0, 0));
		clock1.setLayoutData(new RowData(20, 20));
		
		// final Canvas clock2 = 
		new ClockWidget(parent, SWT.NONE, new RGB(0, 255, 0));

		final ClockWidget clock3 = new ClockWidget(parent, SWT.NONE, new RGB(0, 0, 255));
		clock3.setLayoutData(new RowData(100, 100));
		
		String[] ids = TimeZone.getAvailableIDs();
		timezones = new Combo(parent, SWT.SIMPLE);
		for (int i=0; i<ids.length; i++)
			timezones.add(ids[i]);
		timezones.setVisibleItemCount(5);
		timezones.addSelectionListener(new SelectionListener() {
			public void widgetSelected(SelectionEvent e) {
				String z = timezones.getText();
				TimeZone tz = z == null ? null : TimeZone.getTimeZone(z);
				TimeZone dt = TimeZone.getDefault();
				long curTime = System.currentTimeMillis();
				int offset = tz == null ? 0 : (tz.getOffset(curTime) - dt.getOffset(curTime)) / 3600000;
				clock3.setOffset(offset);
				clock3.redraw();
			}

			public void widgetDefaultSelected(SelectionEvent e) {
				clock3.setOffset(0);
				clock3.redraw();
			}
		});
	}

	/**
	 * Passing the focus request to the viewer's control.
	 */
	public void setFocus() {
		timezones.setFocus();
	}

}
