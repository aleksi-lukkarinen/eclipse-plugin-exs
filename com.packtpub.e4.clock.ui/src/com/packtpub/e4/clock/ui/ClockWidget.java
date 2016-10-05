package com.packtpub.e4.clock.ui;


import java.util.Date;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;




public class ClockWidget extends Canvas {

	private final Color color;

	private int offset;
	
	
	public ClockWidget(Composite parent, int style, RGB rgb) {
		super(parent, style);
		
		this.color = new Color(parent.getDisplay(), rgb);

		addDisposeListener(new DisposeListener() {
			public void widgetDisposed(DisposeEvent e) {
				if (color != null && !color.isDisposed())
					color.dispose();
			}
		});
		
		addPaintListener(new PaintListener() {
			public void paintControl(PaintEvent e) {
				ClockWidget.this.paintControl(e);
			}
		});

		new Thread("TickTock") {
			public void run() {
				while (!ClockWidget.this.isDisposed()) {
					ClockWidget.this.getDisplay().asyncExec(new Runnable() {
						public void run() {
							if (!ClockWidget.this.isDisposed())
								ClockWidget.this.redraw();
						}
					});

					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						return;
					}
				}
			}
		}.start();
	}

	public void paintControl(PaintEvent e) {
		e.gc.drawArc(e.x, e.y, e.width-1, e.height-1, 0, 360);

		int seconds = new Date().getSeconds();
		int minutes = new Date().getMinutes();
		int hours = new Date().getHours() + offset;

		int arcSeconds = (15 - seconds) * 6 % 360;
		int arcMinutes = (15 - minutes) * 6 % 360;
		int arcHours = (3 - hours) * 30 % 360;

		e.gc.setBackground(this.color);
		e.gc.fillArc(e.x, e.y, e.width-1, e.height-1, arcSeconds-1, 2);
		e.gc.fillArc(e.x, e.y, e.width-1, e.height-1, arcMinutes-1, 2);
		e.gc.fillArc(e.x, e.y, e.width-1, e.height-1, arcHours-5, 10);
	}
	
	@Override
	public Point computeSize(int wHint, int hHint, boolean changed) {
		int size = 0;

		if (wHint == SWT.DEFAULT) {
			size = hHint;
		}
		else if (hHint == SWT.DEFAULT) {
			size = wHint;
		}
		else {
			size = Math.min(wHint, hHint);
		}

		if (size == SWT.DEFAULT)
			size = 50;

		return new Point(size, size);
	}

	public int offset() {
		return offset;
	}

	public void setOffset(int newOffset) {
		this.offset = newOffset;
	}
	
}
