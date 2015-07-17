package org.csstudio.askap.utility;

/**
 * Created this class to handle URLs as file name.
 * 
 */

import java.io.InputStream;
import java.net.URI;
import java.net.URL;

import org.eclipse.jface.preference.FileFieldEditor;
import org.eclipse.swt.widgets.Composite;

public class URIFileFieldEditor extends FileFieldEditor {

	public URIFileFieldEditor(String name, String labelText, Composite parent) {
		super(name, labelText, parent);
	}

	@Override
	protected boolean checkState() {
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
    	return super.checkState();
	}

	

}
