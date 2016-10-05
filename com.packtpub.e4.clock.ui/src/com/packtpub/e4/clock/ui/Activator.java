package com.packtpub.e4.clock.ui;


import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tray;
import org.eclipse.swt.widgets.TrayItem;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;




/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends AbstractUIPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "com.packtpub.e4.clock.ui"; //$NON-NLS-1$

	// The shared instance
	private static Activator plugin;

	private Shell shell;
	private TrayItem trayItem;
	private Image image;

	/**
	 * The constructor
	 */
	public Activator() {
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;

		int launchCount = getPreferenceStore().getInt("launchCount");
		IEclipsePreferences eclipsePreferences = InstanceScope.INSTANCE.getNode(PLUGIN_ID);
		int launchCount2 = eclipsePreferences.getInt("launchCount", -1);
		System.out.println("Launch count: " + launchCount + " / " + launchCount2);
		getPreferenceStore().setValue("launchCount", launchCount + 1);
		
		new Thread(new Runnable() {
			public void run() {
				try { Thread.sleep(1000); } catch (Exception e) { }

				final Display display = Display.getDefault();
				display.asyncExec(new Runnable() {
					public void run() {
						image = new Image(display, Activator.class.getResourceAsStream("/icons/sample.gif"));
						Tray tray = display.getSystemTray();
						if (tray != null && image != null) {
							trayItem = new TrayItem(tray, SWT.NONE);
							trayItem.setToolTipText("Hello World!");
							trayItem.setText("Hello World!");
							trayItem.setImage(new Image(trayItem.getDisplay(),
										Activator.class.getResourceAsStream("/icons/sample.gif")));
							trayItem.addSelectionListener(new SelectionListener() {
								public void widgetSelected(SelectionEvent e) {
									if (shell == null) {
										shell = new Shell(trayItem.getDisplay(), SWT.NO_TRIM | SWT.ON_TOP);
										shell.setAlpha(128);
										shell.setLayout(new FillLayout());
										new ClockWidget(shell, SWT.NONE, new RGB(255, 0, 255));
										shell.pack();
									}
									shell.open();
								}
								public void widgetDefaultSelected(SelectionEvent e) { }
							});
							trayItem.setVisible(true);
						}
					}
				});
			}
		}).start();
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);

		if (trayItem != null && !trayItem.isDisposed()) {
			Display.getDefault().asyncExec(new Runnable() {
				public void run() {
					if (trayItem != null && !trayItem.isDisposed()) {
						trayItem.dispose();
					}
				}
			});
		}

		if (image != null && !image.isDisposed()) {
			Display.getDefault().asyncExec(new Runnable() {
				public void run() {
					if (image != null && !image.isDisposed()) {
						image.dispose();
					}
				}
			});
		}

		if (shell != null && !shell.isDisposed()) {
			Display.getDefault().asyncExec(new Runnable() {
				public void run() {
					if (shell != null && !shell.isDisposed()) {
						shell.dispose();
					}
				}
			});
		}
	}

	/**
	 * Returns the shared instance
	 *
	 * @return the shared instance
	 */
	public static Activator getDefault() {
		return plugin;
	}

	/**
	 * Returns an image descriptor for the image file at the given
	 * plug-in relative path
	 *
	 * @param path the path
	 * @return the image descriptor
	 */
	public static ImageDescriptor getImageDescriptor(String path) {
		return imageDescriptorFromPlugin(PLUGIN_ID, path);
	}

}
