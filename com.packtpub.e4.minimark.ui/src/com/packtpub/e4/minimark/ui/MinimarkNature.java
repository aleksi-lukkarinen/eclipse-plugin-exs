package com.packtpub.e4.minimark.ui;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.resources.ICommand;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IProjectNature;
import org.eclipse.core.runtime.CoreException;




public class MinimarkNature implements IProjectNature {

	public static final String ID = 
		"com.packtpub.e4.minimark.ui.MinimarkNature";

	private IProject project;


	@Override
	public void configure() throws CoreException {
		List<ICommand> commands = retrieveBuildCommandList();

		if (commands.contains(MinimarkBuilder.ID))
			return;

		addMinimarkBuilderTo(commands);
		applyBuildCommandList(commands);
	}


	@Override
	public void deconfigure() throws CoreException {
		List<ICommand> commands = retrieveBuildCommandList();
		
		Iterator<ICommand> i = commands.iterator();
		while (i.hasNext()) {
			ICommand c = i.next();
			if (c.getBuilderName().equals(MinimarkBuilder.ID))
				i.remove();
		}

		applyBuildCommandList(commands);
	}


	private List<ICommand> retrieveBuildCommandList() throws CoreException {
		ICommand[] buildSpec = project.getDescription().getBuildSpec();
		List<ICommand> commands = new ArrayList<>(Arrays.asList(buildSpec));
		
		return commands;
	}


	private void addMinimarkBuilderTo(List<ICommand> commands) throws CoreException {
		IProjectDescription desc = project.getDescription();
		ICommand newCommand = desc.newCommand();
		newCommand.setBuilderName(MinimarkBuilder.ID);
		commands.add(newCommand);
	}


	private void applyBuildCommandList(List<ICommand> commands) throws CoreException {
		IProjectDescription desc = project.getDescription();

		ICommand[] commandArray = commands.toArray(new ICommand[0]);
		desc.setBuildSpec(commandArray);

		project.setDescription(desc, null);
	}


	@Override
	public IProject getProject() {
		return project;
	}


	@Override
	public void setProject(IProject project) {
		this.project = project;
	}

}
