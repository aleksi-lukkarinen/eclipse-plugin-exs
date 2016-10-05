package com.packtpub.e4.clock.ui.handlers;


import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.Command;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.core.commands.IHandlerListener;
import org.eclipse.core.commands.ParameterizedCommand;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.SubMonitor;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.commands.ICommandService;
import org.eclipse.ui.progress.IProgressConstants2;
import org.eclipse.ui.statushandlers.StatusManager;

import com.packtpub.e4.clock.ui.Activator;




public class HelloHandler extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		Job job = new Job("About to say Hello...") {
			@Override
			protected IStatus run(IProgressMonitor monitor) {
				try {
					SubMonitor subMonitor = SubMonitor.convert(monitor, "Preparing", 5000);
					
					for (int i = 0; i < 50 && !subMonitor.isCanceled(); i++) {
						if (i == 10)
							subMonitor.subTask("Starting to process");
						else if (i == 12)
							checkDozen(subMonitor.newChild(100));
						else if (i == 25)
							subMonitor.subTask("Processing");
						else if (i == 40)
							subMonitor.subTask("Processing (nearly complete)");

						try {
							Thread.sleep(100);
						} catch (InterruptedException e) {
							/* NOTHING */ }

						subMonitor.worked(100);
					} 

					if (!subMonitor.isCanceled()) {
						Display.getDefault().asyncExec(new Runnable() {
							@Override
							public void run() {
								MessageDialog.openInformation(null, "Hello", "World!");
							}
						});
					}
				} catch (NullPointerException e) {
					StatusManager statusManager = StatusManager.getManager();
					Status status = new Status(IStatus.ERROR, Activator.PLUGIN_ID, "Programming bug?", e);
					statusManager.handle(status, StatusManager.LOG);
				}

				return Status.OK_STATUS;
			}
			
			private void checkDozen(IProgressMonitor monitor) {
				SubMonitor subMonitor = SubMonitor.convert(monitor);
				subMonitor.beginTask("Checking a dozen", 12);
				
				for (int i=0; i<12 && !subMonitor.isCanceled(); i++) {
					subMonitor.subTask(String.valueOf(i));
					
					try {
						Thread.sleep(100);
					}
					catch (InterruptedException e) { /* NOTHING */ }
					
					subMonitor.worked(1);
				}
			}
			
		};
		
		ICommandService service = (ICommandService) PlatformUI.getWorkbench().getService(ICommandService.class);
		Command command = service == null ? null : service.getCommand("com.packtpub.e4.clock.ui.command.hello");
		if (command != null)
			job.setProperty(IProgressConstants2.COMMAND_PROPERTY, 
					ParameterizedCommand.generateCommand(command, null));
		
		job.setProperty(
				IProgressConstants2.ICON_PROPERTY,
				ImageDescriptor.createFromURL(HelloHandler.class.getResource("/icons/sample.gif")));
		
		job.schedule();

		return null;
	}

}
