package com.packtpub.e4.minimark.ui;


import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceProxy;
import org.eclipse.core.resources.IResourceProxyVisitor;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Path;




public class MinimarkCleaningVisitor implements IResourceProxyVisitor {

	@Override
	public boolean visit(IResourceProxy proxy) throws CoreException {
		String name = proxy.getName();
		
		if (name != null && name.endsWith(".minimark")) {
			deleteBuildProductsFor(proxy.requestResource());
		}
		
		return true;
	}


	private void deleteBuildProductsFor(IResource resource) throws CoreException {
		if (resource instanceof IFile && resource.exists()) {
			IFile minimarkFile = (IFile) resource;
			deleteHtmlProductFor(minimarkFile);
		}
	}


	private void deleteHtmlProductFor(IFile minimarkFile) throws CoreException {
		String htmlName = minimarkFile.getName().replace(".minimark", ".html");
		IContainer fileParent = minimarkFile.getParent(); 

		IFile htmlFile = fileParent.getFile(new Path(htmlName));
		if (htmlFile.exists()) {
			htmlFile.delete(true, null);
		}
	}
	
}
