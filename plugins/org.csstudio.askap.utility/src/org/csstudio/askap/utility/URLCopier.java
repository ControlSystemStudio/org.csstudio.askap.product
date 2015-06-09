package org.csstudio.askap.utility;

import java.io.ByteArrayInputStream;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.osgi.util.NLS;
import org.jsoup.Connection;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class URLCopier implements AskapCopier {

	private static final Logger logger = Logger.getLogger(URLCopier.class.getName());

	public URLCopier() {
	}

	@Override
	public void copy(String sourceName, IContainer container,
			IProgressMonitor monitor) {
		try{
			Connection connection = Jsoup.connect(sourceName);
			Document doc = connection.get();
			copy(doc, container, monitor);
		} catch (Exception e) {
			logger.log(Level.WARNING, "Could not retrieve from " + sourceName, e);
			MessageDialog.openError(null, "Error", NLS.bind("Could not retrieve from " + sourceName + ": \n{0}.", e));
		}
	}
	
	public void copy(Document doc, IContainer container,
			IProgressMonitor monitor) {
		Elements links = doc.select("td a[href]");
		
		for (Iterator<Element> iter=links.iterator(); iter.hasNext();) {
			Element link = iter.next();
			// skip 'Parent Directory'
			if (!link.ownText().equals("Parent Directory"))
				copy(link, container, monitor);
		}
	}
	
	
	private void copy(Element element, IContainer container,
			IProgressMonitor monitor) {
		try {
			String name = element.ownText();
			String base = element.baseUri();
			String url = base + name;
			Connection connection = Jsoup.connect(url).ignoreContentType(true);
			Response response = connection.execute();
			String contentType = response.contentType();

			monitor.subTask("Copying " + name);
			
			if (contentType!=null && contentType.toLowerCase().startsWith("text/html")) {
				// process as directory
				IFolder folder = container.getFolder(new Path(name));
				if (folder.exists()) {
					return;
				}

				folder.create(true, true, null);
				
				Document doc = connection.get();
				copy(doc, folder, monitor);
			} else {
				// process as file
				IFile pFile = container.getFile(new Path(name));
				if (!pFile.exists()) {					
					ByteArrayInputStream stream = new ByteArrayInputStream(response.bodyAsBytes());
					pFile.create (stream, true, monitor);
				}
				monitor.internalWorked(1);
			}
		} catch (Exception e) {
			MessageDialog.openError(null, "Error",
					NLS.bind("Error happened during copy: \n{0}.", e));
		}
	}
}
