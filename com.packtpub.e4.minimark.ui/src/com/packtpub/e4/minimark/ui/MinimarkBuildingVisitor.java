package com.packtpub.e4.minimark.ui;


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.AbstractMap;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.eclipse.core.filesystem.EFS;
import org.eclipse.core.filesystem.IFileInfo;
import org.eclipse.core.filesystem.IFileStore;
import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.resources.IResourceProxy;
import org.eclipse.core.resources.IResourceProxyVisitor;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;




public class MinimarkBuildingVisitor implements IResourceProxyVisitor, IResourceDeltaVisitor {

	private Map<String, ? extends Object> markerAttributesMinimarkFileIsEmpty = 
		Collections.unmodifiableMap(Stream.of(
			new AbstractMap.SimpleEntry<>(IMarker.SEVERITY, IMarker.SEVERITY_ERROR),
			new AbstractMap.SimpleEntry<>(IMarker.MESSAGE, "Minimark file is empty"),
			new AbstractMap.SimpleEntry<>(IMarker.LINE_NUMBER, 0),
			new AbstractMap.SimpleEntry<>(IMarker.CHAR_START, 0),
			new AbstractMap.SimpleEntry<>(IMarker.CHAR_END, 0)
		).collect(Collectors.toMap((e) -> e.getKey(), (e) -> e.getValue())));


	@Override
	public boolean visit(IResourceDelta delta) throws CoreException {
		IResource resource = delta.getResource();
		String name = resource.getName();

		if (name.endsWith(".minimark")) {
			boolean deleted = (IResourceDelta.REMOVED & delta.getKind()) != 0;
			if (deleted) {
				String htmlName = name.replace(".minimark", ".html");
				IFile htmlFile = resource.getParent().getFile(new Path(htmlName));
				if (htmlFile.exists()) {
					htmlFile.delete(true, null);
				}
			}
			else {
				generateHtmlFileFor(resource);
			}
		}
		else if (name.endsWith(".html")) {
			String minimarkName = name.replace(".html", ".minimark");
			IFile minimarkFile = resource.getParent().getFile(new Path(minimarkName));
			if (minimarkFile.exists()) {
				generateHtmlFileFor(minimarkFile);
			}
		}

		return true;
	}


	@Override
	public boolean visit(IResourceProxy proxy) throws CoreException {
		String name = proxy.getName();
		
		if (name != null && name.endsWith(".minimark")) {
			generateHtmlFileFor(proxy.requestResource());
		}
		
		return true;
	}


	private void generateHtmlFileFor(IResource existingResource) throws CoreException {
		if (!(existingResource instanceof IFile) || !existingResource.exists())
			return;

		existingResource.deleteMarkers(
				"com.packtpub.e4.minimark.ui.MinimarkMarker", 
				true, 
				IResource.DEPTH_INFINITE);

		IFile minimarkFile = (IFile) existingResource;

		IFileStore minimarkStore = EFS.getStore(minimarkFile.getLocationURI());
		IFileInfo minimarkFileInfo = minimarkStore.fetchInfo();
		if (minimarkFileInfo.getLength() < 1) {
			existingResource.createMarker("com.packtpub.e4.minimark.ui.MinimarkMarker")
						.setAttributes(markerAttributesMinimarkFileIsEmpty);
		}
		
		String htmlName = minimarkFile.getName().replace(".minimark", ".html");
		IContainer container = minimarkFile.getParent();
		IFile htmlFile = container.getFile(new Path(htmlName));

		InputStream in = minimarkFile.getContents();
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		
		try {
			MinimarkTranslator.convert(
					new InputStreamReader(in),
					new OutputStreamWriter(baos));
		}
		catch (IOException e) {
			throw new CoreException(
				new Status(
					Status.ERROR, 
					Activator.PLUGIN_ID, 
					"Failed to generate HTML version of Minimark file", 
					e));
		}

		ByteArrayInputStream contents = new ByteArrayInputStream(baos.toByteArray());
		if (htmlFile.exists()) {
			htmlFile.setContents(contents, true, false, null);
		}
		else {
			htmlFile.create(contents, true, null);
		}

		htmlFile.setDerived(true, null);
	}
	
}
