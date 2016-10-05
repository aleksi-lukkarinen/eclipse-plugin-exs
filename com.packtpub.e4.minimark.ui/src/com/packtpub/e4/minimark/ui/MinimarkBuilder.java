package com.packtpub.e4.minimark.ui;


import java.util.Map;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;




public class MinimarkBuilder extends IncrementalProjectBuilder {

	public static final String ID = 
			"com.packtpub.e4.minimark.ui.MinimarkBuilder";
	
	@Override
	protected IProject[] build(
			int kind, 
			Map<String, String> args, 
			IProgressMonitor monitor) throws CoreException {

		if (kind == FULL_BUILD) {
			performFullBuild(getProject(), monitor);
		}
		else {
			performIncrementalBuild(getProject(), monitor, getDelta(getProject()));
		}
		
		return null;
	}

	
	private void performFullBuild(IProject project, IProgressMonitor monitor) throws CoreException {
		project.accept(new MinimarkBuildingVisitor(), IResource.NONE);
	}


	private void performIncrementalBuild(
			IProject project, 
			IProgressMonitor monitor, 
			IResourceDelta delta) throws CoreException {
		
		if (delta == null)
			performFullBuild(project, monitor);
		else
			delta.accept(new MinimarkBuildingVisitor());
	}
	
	
	@Override
	protected void clean(IProgressMonitor monitor) throws CoreException {
		performCleaning(getProject(), monitor);
	}
	
	
	private void performCleaning(IProject project, IProgressMonitor monitor) throws CoreException {
		project.accept(new MinimarkCleaningVisitor(), IResource.NONE);
	}
	

}
