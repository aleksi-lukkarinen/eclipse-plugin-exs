package com.packtpub.e4.junit.plugin;


import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.widgets.Widget;
import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;
import org.eclipse.swtbot.eclipse.finder.matchers.WidgetMatcherFactory;
import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotView;
import org.eclipse.swtbot.swt.finder.exceptions.WidgetNotFoundException;
import org.eclipse.swtbot.swt.finder.finders.UIThreadRunnable;
import org.eclipse.swtbot.swt.finder.junit.SWTBotJunit4ClassRunner;
import org.eclipse.swtbot.swt.finder.results.StringResult;
import org.eclipse.swtbot.swt.finder.waits.DefaultCondition;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotShell;
import org.hamcrest.Matcher;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;




@RunWith(SWTBotJunit4ClassRunner.class)
public class UITest {

	private static SWTWorkbenchBot bot;
	
	@Before
	public void initTest() {
		bot = new SWTWorkbenchBot();
		
		try {
			bot.viewByTitle("Welcome").close();
		}
		catch (WidgetNotFoundException e) { /* NOTHING */ }
	}

	@After
	public void finalizeTest() {
		bot.closeAllShells();
	}

	@Test
	public void shellTitles() {
		SWTBotShell[] shells = bot.shells();
		for (SWTBotShell shell : shells) {
			if (shell.isVisible()) {
				assertTrue(shell.getText().endsWith("Java - Eclipse SDK"));
			}
		}
	}

	@Test
	public void createProject() {
		bot.menu("File").menu("New").menu("Project...").click();

		SWTBotShell shell = bot.shell("New Project");
		shell.activate();

		bot.tree().expandNode("General").select("Project");
		bot.button("Next >").click();
		bot.textWithLabel("Project name:").setText("SWTBot Test Project");
		bot.button("Finish").click();
		bot.waitUntil(new ShellCloses(shell), 30000);
	}

	@Test
	public void TimeZoneView() {
		bot.menu("Window").menu("Show View").menu("Other...").click();
		SWTBotShell shell = bot.shell("Show View");
		shell.activate();

		bot.tree().expandNode("Timekeeping").select("TimeZoneView");
		bot.button("OK").click();

		SWTBotView timeZoneView = bot.viewByTitle("TimeZoneView");
		assertNotNull(timeZoneView);
		
		Widget widget = timeZoneView.getWidget();
		Matcher<CTabItem> matcher = WidgetMatcherFactory.widgetOfType(CTabItem.class);
		final List<? extends CTabItem> ctabs = bot.widgets(matcher, widget);
		assertEquals(17, ctabs.size());
		
		String tabText = UIThreadRunnable.syncExec(new StringResult() {
			@Override
			public String run() {
				return ctabs.get(0).getText();
			}
		});
		assertEquals("Africa", tabText);
	}

	@Test
	public void createJavaProject() throws Exception {
		String projectName = "SWTBot Java Project";
		
		bot.menu("File").menu("New").menu("Project...").click();

		SWTBotShell shell = bot.shell("New Project");
		shell.activate();
		bot.tree().expandNode("Java").select("Java Project");
		bot.button("Next >").click();
		bot.textWithLabel("Project name:").setText(projectName);
		bot.button("Finish").click();
		bot.waitUntil(new ShellCloses(shell), 30000);

		final IProject project = getProject();
		assertTrue(project.exists());

		final IFolder src = project.getFolder("src");
		if (!src.exists()) {
			src.create(true, true, null);
		}

		final IFolder bin = project.getFolder("bin");

		IFile test = src.getFile("Test.java");
		test.create(new ByteArrayInputStream("class Test{}".getBytes()), true, null);

		bot.waitUntil(new DefaultCondition() {
			@Override
			public boolean test() throws Exception {
				return project.getFolder("bin").getFile("Test.class").exists();
			}

			@Override
			public String getFailureMessage() { return "File bin/Test.class was not created"; }
		});

		assertTrue(bin.getFile("Test.class").exists());
	}

	private IProject getProject() {
		return ResourcesPlugin.getWorkspace().getRoot().getProject();
	}
	
}
