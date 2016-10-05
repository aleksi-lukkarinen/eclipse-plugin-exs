package com.packtpub.e4.clock.ui.internal;


import java.util.TimeZone;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;




public class TimeZoneSelectionListener implements ISelectionListener {

	private Viewer viewer;
	private IWorkbenchPart part;
	
	public TimeZoneSelectionListener(Viewer viewer, IWorkbenchPart part) {
		this.viewer = viewer;
		this.part = part;
	}
	
	@Override
	public void selectionChanged(IWorkbenchPart part, ISelection selection) {
		ISelection viewerSelection = viewer.getSelection();
		if (part == this.part || 
				!(selection instanceof IStructuredSelection) ||
				!(viewerSelection instanceof StructuredSelection)) {
			return;
		}

		Object selected = ((IStructuredSelection) selection).getFirstElement();
		Object current = ((IStructuredSelection) viewerSelection).getFirstElement();

		if (selected != current && selected instanceof TimeZone) {
			viewer.setSelection(selection);

			if (viewer instanceof StructuredViewer)
				((StructuredViewer) viewer).reveal(selected);
		}

	}

}
