package com.packtpub.e4.clock.ui.views;


import java.net.URL;
import java.util.TimeZone;

import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.resource.FontRegistry;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.resource.LocalResourceManager;
import org.eclipse.jface.resource.ResourceManager;
import org.eclipse.jface.viewers.DelegatingStyledCellLabelProvider;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.part.ViewPart;

import com.packtpub.e4.clock.ui.internal.TimeZoneComparator;
import com.packtpub.e4.clock.ui.internal.TimeZoneContentProvider;
import com.packtpub.e4.clock.ui.internal.TimeZoneDialog;
import com.packtpub.e4.clock.ui.internal.TimeZoneLabelProvider;
import com.packtpub.e4.clock.ui.internal.TimeZoneViewerComparator;
import com.packtpub.e4.clock.ui.internal.TimeZoneViewerFilter;




public class TimeZoneTreeView extends ViewPart {

	private TreeViewer treeViewer = null;

	public TimeZoneTreeView() {

	}

	@Override
	public void createPartControl(Composite parent) {
		FontRegistry fr = JFaceResources.getFontRegistry();
		ImageRegistry ir = createImageRegistry(parent);

		treeViewer = createTreeViewer(parent, fr, ir);
	}

	private ImageRegistry createImageRegistry(Composite parent) {
		ResourceManager rm = JFaceResources.getResources();
		LocalResourceManager lrm = new LocalResourceManager(rm, parent);

		ImageRegistry ireg = new ImageRegistry(lrm);
		
		URL sample = getClass().getResource("/icons/sample.gif");
		ireg.put("sample", ImageDescriptor.createFromURL(sample));

		return ireg;
	}

	private TreeViewer createTreeViewer(Composite parent, FontRegistry fr, ImageRegistry ir) {
		final int viewerStyle = SWT.MULTI | SWT.V_SCROLL | SWT.H_SCROLL; 
		TreeViewer tv = new TreeViewer(parent, viewerStyle);

		tv.setLabelProvider(new DelegatingStyledCellLabelProvider(new TimeZoneLabelProvider(ir, fr)));
		tv.setContentProvider(new TimeZoneContentProvider());
		tv.setFilters(new ViewerFilter[] {new TimeZoneViewerFilter("GMT")});

		tv.setData("REVERSE", Boolean.TRUE);
		tv.setComparator(new TimeZoneViewerComparator());

		tv.setInput(new Object[] {TimeZoneComparator.getTimeZones()});
		
		tv.addDoubleClickListener(new IDoubleClickListener() {
			@Override
			public void doubleClick(DoubleClickEvent event) {
				Viewer viewer = event.getViewer();
				Shell shell = viewer.getControl().getShell();
				
				ISelection sel = viewer.getSelection();
				Object selectedValue;
				
				if (!(sel instanceof IStructuredSelection) || (sel.isEmpty())) {
					selectedValue = null;
				}
				else {
					selectedValue = ((IStructuredSelection) sel).getFirstElement();
				}
				
				if (selectedValue instanceof TimeZone) {
					TimeZone timeZone = (TimeZone) selectedValue;
					new TimeZoneDialog(shell, timeZone).open();
				}
			}
		});

		getSite().setSelectionProvider(tv);

		// System.out.println("Adapter is " + Platform.getAdapterManager().getAdapter(TimeZone.getDefault(), IPropertySource.class));

		hookContextMenu(tv);

		return tv;
	}

	@Override
	public void setFocus() {
		treeViewer.getControl().setFocus();
	}
	
	private void hookContextMenu(Viewer viewer) { 
		MenuManager manager = new MenuManager("#PopupMenu"); 
		Menu menu = manager.createContextMenu(viewer.getControl()); 
		viewer.getControl().setMenu(menu); 
		getSite().registerContextMenu(manager, viewer);
	}

}
