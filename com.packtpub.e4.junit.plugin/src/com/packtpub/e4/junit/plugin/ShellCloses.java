package com.packtpub.e4.junit.plugin;


import org.eclipse.swt.widgets.Shell;
import org.eclipse.swtbot.swt.finder.waits.DefaultCondition;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotShell;




/**
 * 
 */
public class ShellCloses extends DefaultCondition {

	/**  */
	private final SWTBotShell shell;

	/**
	 * 
	 * 
	 * @param shellToWait
	 */
	public ShellCloses(SWTBotShell shellToWait) {
		this.shell = shellToWait;
	}

	/**
	 * 
	 */
	@Override
	public boolean test() throws Exception {
		return shell.widget.isDisposed() || !((Shell) shell.widget).isVisible();
	}

	/**
	 * 
	 */
	@Override
	public String getFailureMessage() {
		return "The shell \"" + shell.getText() + "\" did not close.";
	}

}
