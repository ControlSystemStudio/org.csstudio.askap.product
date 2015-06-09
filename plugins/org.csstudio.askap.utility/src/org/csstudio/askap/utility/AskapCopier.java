package org.csstudio.askap.utility;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.runtime.IProgressMonitor;

public interface AskapCopier {
	public void copy(String sourceName, IContainer container,
			IProgressMonitor monitor);

}
