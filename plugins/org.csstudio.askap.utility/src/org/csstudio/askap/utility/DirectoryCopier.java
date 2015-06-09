package org.csstudio.askap.utility;

import java.io.File;
import java.io.FileInputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.osgi.util.NLS;

public class DirectoryCopier implements AskapCopier {

	private static final Logger logger = Logger.getLogger(DirectoryCopier.class.getName());

	public DirectoryCopier() {
	}

	@Override
	public void copy(String sourceName, IContainer container,
			IProgressMonitor monitor) {
		File directory = new File(sourceName);
		
		if (directory.isDirectory()) {			
			copy(directory.listFiles(), container, monitor);			
		} else {
			logger.log(Level.WARNING, sourceName + " is not a directory.");
			MessageDialog.openError(null, "Error",
					NLS.bind("Error happened during copy: \n{0}.", sourceName + " is not a directory."));

		}	
	}

	private void copy(File[] files, IContainer container,
			IProgressMonitor monitor) {
		try {
			for (File file : files) {
				monitor.subTask("Copying " + file.getName());
				if (file.isDirectory()) {
					if (!file.getName().equals("CVS")) {//$NON-NLS-1$
						IFolder folder = container.getFolder(new Path(file
								.getName()));
						if (!folder.exists()) {
							folder.create(true, true, null);
							copy(file.listFiles(), folder, monitor);
						}
					}
				} else {
					IFile pFile = container.getFile(new Path(file.getName()));
					if (!pFile.exists()) {
						pFile.create(new FileInputStream(file), true,
								new NullProgressMonitor());
					}
					monitor.internalWorked(1);
				}

			}
		} catch (Exception e) {
			MessageDialog.openError(null, "Error",
					NLS.bind("Error happened during copy: \n{0}.", e));
		}
	}

}
