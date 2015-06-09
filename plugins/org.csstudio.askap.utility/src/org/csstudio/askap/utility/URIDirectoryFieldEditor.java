package org.csstudio.askap.utility;

import java.io.InputStream;
import java.net.URI;
import java.net.URL;

import org.eclipse.jface.preference.DirectoryFieldEditor;
import org.eclipse.swt.widgets.Composite;

public class URIDirectoryFieldEditor extends DirectoryFieldEditor {

	public URIDirectoryFieldEditor(String name, String labelText,
			Composite parent) {
		super(name, labelText, parent);
	}

	@Override
	protected boolean doCheckState() {
        String path = getTextControl().getText();
        if (path != null) {
			path = path.trim();
			try {
				URI uri = new URI(path);
				URL url = uri.toURL();
				InputStream stream = url.openStream();
				
	            if (stream.available()>0)
	            	return true;
			} catch (Exception e) {
				// got an error, can ignore
				path = path.trim();
			}
        }
		return super.doCheckState();
	}
}
