package org.csstudio.askap.utility;

import java.net.URI;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.csstudio.ui.util.dialogs.ExceptionDetailsErrorDialog;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import org.eclipse.ui.PlatformUI;

public class InstallAskapAction extends Action implements
		IWorkbenchWindowActionDelegate {
	
	private static final Logger logger = Logger.getLogger(InstallAskapAction.class.getName());


	public static final String PROJECT_NAME = "askap";

	public InstallAskapAction() {
		// NOP
	}

	@Override
	public void run(IAction action) {
		final IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();

		Job job = new Job("Import askap") {
			@Override
			protected IStatus run(IProgressMonitor monitor) {
				try {
					// copy the sample displays
					IProject project = root.getProject(PROJECT_NAME);
					project.delete(true, true, new NullProgressMonitor());
					project.create(new NullProgressMonitor());
					project.open(new NullProgressMonitor());

					monitor.beginTask("Copying Examples", IProgressMonitor.UNKNOWN);
					
					String src = Preferences.getOPIDirectory();
					URI uri = new URI(src);
					String scheme = uri.getScheme();
					AskapCopier copier = null;
					if (scheme==null || scheme.isEmpty()) {
						copier = new DirectoryCopier();
					} else {
						copier = new URLCopier();
					}
					
					copier.copy(src, project, monitor);
					
				} catch (Exception e) {
					logger.log(Level.WARNING, "Could not create project from " + Preferences.getOPIDirectory() + ": " + e.getMessage());
			        Shell shell = PlatformUI.getWorkbench().getModalDialogShellProvider().getShell();		        
		            ExceptionDetailsErrorDialog.openError(shell,
		                    "ERROR",
		                    "Installation failed.",
		                    e);

				}

				return Status.OK_STATUS;
			}
		};

		job.schedule();
	}

	@Override
	public void selectionChanged(IAction action, ISelection selection) {
		// NOP
	}

	@Override
	public void dispose() {
		// NOP
	}

	@Override
	public void init(IWorkbenchWindow window) {
		// NOP
	}

}
