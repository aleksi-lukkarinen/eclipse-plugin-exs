package com.packtpub.e4.clock.ui;


import java.util.TimeZone;

import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.ColorFieldEditor;
import org.eclipse.jface.preference.ComboFieldEditor;
import org.eclipse.jface.preference.DirectoryFieldEditor;
import org.eclipse.jface.preference.FieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.FileFieldEditor;
import org.eclipse.jface.preference.IntegerFieldEditor;
import org.eclipse.jface.preference.PathEditor;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.jface.preference.RadioGroupFieldEditor;
import org.eclipse.jface.preference.ScaleFieldEditor;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;




public class ClockPreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

	public ClockPreferencePage() {
		super(GRID);
	}
	
	@Override
	public void init(IWorkbench workbench) {
		setPreferenceStore(Activator.getDefault().getPreferenceStore());
	}

	@Override
	protected void createFieldEditors() {
		addField(new IntegerFieldEditor(
				"launchCount", 
				"Number of times it has been launched", 
				getFieldEditorParent()));

		IntegerFieldEditor iField = new IntegerFieldEditor(
				"offset", 
				"Current offset from GMT", 
				getFieldEditorParent());
		iField.setValidRange(-14, 12);
		addField(iField);

		String[][] data;
		String[] ids = TimeZone.getAvailableIDs();
		data = new String[ids.length][];
		for (int i=0; i<ids.length; i++) {
			data[i] = new String[] { ids[i], ids[i] };
		}
		ComboFieldEditor cField = new ComboFieldEditor(
				"favourite", 
				"Favourite time zone", 
				data, 
				getFieldEditorParent());
		addField(cField);
		
		addField(new BooleanFieldEditor(
				"tick", 
				"Boolean value", 
				getFieldEditorParent()));
		
		addField(new ColorFieldEditor(
				"color", 
				"Favorite color", 
				getFieldEditorParent()));
		
		addField(new ScaleFieldEditor(
				"scale", 
				"Scale", 
				getFieldEditorParent(),
				0, 360, 10, 90));
		
		addField(new FileFieldEditor(
				"file", 
				"Pick a file", 
				getFieldEditorParent()));
		
		addField(new DirectoryFieldEditor(
				"dir", 
				"Pick a directory", 
				getFieldEditorParent()));
		
		addField(new PathEditor(
				"path", 
				"Path",
				"Directory",
				getFieldEditorParent()));
		
		addField(new RadioGroupFieldEditor(
				"group", 
				"Radio choices",
				3,
				data,
				getFieldEditorParent(),
				true));
	}

}
