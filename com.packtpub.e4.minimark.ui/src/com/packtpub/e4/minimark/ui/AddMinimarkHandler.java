package com.packtpub.e4.minimark.ui;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.handlers.HandlerUtil;




public class AddMinimarkHandler extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		ISelection sel = HandlerUtil.getCurrentSelection(event);

		if (!(sel instanceof IStructuredSelection))
			return null;

		for (Object o : ((IStructuredSelection) sel).toArray()) {
			if (o instanceof IProject) {
				addMinimarkNatureTo((IProject) o);
			}
		}

		return null;
	}


	private void addMinimarkNatureTo(IProject project) throws ExecutionException {
		try {
			addNatureTo(MinimarkNature.ID, project);
		}
		catch (CoreException e) {
			throw new ExecutionException("Failed to set nature on " + project, e);
		}
	}


	private void addNatureTo(String natureId, IProject project) throws CoreException {
		IProjectDescription description = project.getDescription();

		List<String> natures = new ArrayList<>(Arrays.asList(description.getNatureIds()));
		if (natures.contains(natures))
			return;

		natures.add(natureId);
		description.setNatureIds(natures.toArray(new String[0]));
		project.setDescription(description, null);
	}
	
}
