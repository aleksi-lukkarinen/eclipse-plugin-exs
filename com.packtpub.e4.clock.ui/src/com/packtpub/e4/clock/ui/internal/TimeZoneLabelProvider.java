package com.packtpub.e4.clock.ui.internal;


import java.util.Map;
import java.util.Set;
import java.util.TimeZone;

import org.eclipse.jface.resource.FontRegistry;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.StyledString;
import org.eclipse.jface.viewers.DelegatingStyledCellLabelProvider.IStyledLabelProvider;
import org.eclipse.jface.viewers.IFontProvider;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;




public class TimeZoneLabelProvider extends LabelProvider 
		implements IStyledLabelProvider, IFontProvider {

	private FontRegistry fr;
	private ImageRegistry ir;
	private Font italic;
	
	private Image folder = PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_OBJ_FOLDER);
	
	
	public TimeZoneLabelProvider(ImageRegistry ir, FontRegistry fr) {
		this.fr = fr;
		this.ir = ir;
		this.italic = fr.getItalic(JFaceResources.DEFAULT_FONT);
	}

	@SuppressWarnings("unchecked")
	public String getText(Object element) {
		if (element instanceof Map)
			return "Time Zones";

		if (element instanceof Map.Entry)
			return ((Map.Entry<String, Set<TimeZone>>) element).getKey().toString();

		if (element instanceof TimeZone)
			return ((TimeZone) element).getID().split("/")[1];

		return "Unknown type: " + element.getClass();
	}

	@Override
	public Image getImage(Object element) {
		if (element instanceof Map.Entry)
			return folder;

		if (element instanceof TimeZone)
			return ir.get("sample");

		return super.getImage(element);
	}

	@Override
	public StyledString getStyledText(Object element) {
		String text = getText(element);
		StyledString ss = new StyledString(text);
		
		if (element instanceof TimeZone) {
			int offset = -((TimeZone) element).getOffset(0);
			ss.append(" (" + offset / 3600000 + " h)", StyledString.DECORATIONS_STYLER);
		}
		
		return ss;
	}

	@Override
	public Font getFont(Object element) {
		if (element instanceof TimeZone)
			return italic;
		
		return null;
	}

}
