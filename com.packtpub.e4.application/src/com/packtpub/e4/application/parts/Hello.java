package com.packtpub.e4.application.parts;


import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.core.di.extensions.Preference;
import org.eclipse.e4.ui.di.Focus;
import org.eclipse.e4.ui.di.UIEventTopic;
import org.eclipse.e4.ui.di.UISynchronize;
import org.eclipse.e4.ui.model.application.ui.basic.MWindow;
import org.eclipse.e4.ui.services.EMenuService;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.osgi.service.log.LogService;
import org.osgi.service.prefs.BackingStoreException;




public class Hello {

	/**  */
	@Inject @Optional
	private LogService log;

	/**  */
	@Inject
	private UISynchronize ui;
	
	/**  */
	@Inject @Preference(nodePath="com.packtpub.e4.application", value="greeting")
	private String greeting;

	/**  */
	@Inject @Preference(nodePath="com.packtpub.e4.application")
	private IEclipsePreferences prefs;

	/**  */
	@Inject
	private MWindow window;

	/**  */
	@Inject @Named("math.random")
	private Object random;
	
	/**  */
	private Label label;

	/**  */
	private Button button;


	@PostConstruct
	public void create(Composite parent, EMenuService menu) {
		button = new Button(parent, SWT.NONE);
		button.setText("DO NOT PUSH!!");

		button.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				button.setEnabled(false);

				new Job("Button Pusher") {
					protected IStatus run(IProgressMonitor monitor) {
						ui.asyncExec(new Runnable() {
							@Override
							public void run() {
								button.setEnabled(true);
							}
						});

						return Status.OK_STATUS;
					};
				}.schedule(1000);
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				
			}

		});
		
		label = new Label(parent, SWT.NONE);
		label.setText(greeting + " " + window.getLabel() + " " + random);
		
		menu.registerContextMenu(label, "com.packtpub.e4.application.popupmenu.hello");

		if (log != null) {
			log.log(LogService.LOG_ERROR, "Hello");
		}
	}

	@Focus
	public void focus() {
		label.setFocus();
	}

	@Inject @Optional
	public void setSelection(@Named(IServiceConstants.ACTIVE_SELECTION) Object selection) {
		if (selection != null) {
			label.setText(selection.toString());
		}
	}

	@Inject @Optional
	public void receiveEvent(@UIEventTopic("rainbow/color") String data) {
		label.setText(data);

		prefs.put("greeting", "I like " + data);
		try {
			prefs.sync();
		} catch (BackingStoreException e) {
			e.printStackTrace();
		}
	}

	@Inject @Optional
	void setText(
			@Preference(
				nodePath="com.packtpub.e4.application", 
				value="greeting") 
			String text) {
		
		if (text != null && label != null && !label.isDisposed()) {
			label.setText(text);
		}
	}
	
}
