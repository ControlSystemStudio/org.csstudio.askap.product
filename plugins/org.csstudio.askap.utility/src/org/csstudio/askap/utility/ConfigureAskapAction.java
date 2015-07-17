package org.csstudio.askap.utility;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.csstudio.ui.util.dialogs.ExceptionDetailsErrorDialog;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import org.eclipse.ui.PlatformUI;

public class ConfigureAskapAction extends Action implements
		IWorkbenchWindowActionDelegate {
	
	private static final Logger logger = Logger.getLogger(ConfigureAskapAction.class.getName());

	public ConfigureAskapAction() {
		
	}
	
	@Override
	public void run(IAction action) {
		try {
			File file = getFile();
			
			if (file==null)
				return;
			
			Properties properties = new Properties();
	        properties.load(new FileInputStream(file));
	        
	        // the parameter name is in the format of <node name>/<parameter name>
	        for (String name: properties.stringPropertyNames()) {
	        	String value = properties.getProperty(name);
	        	StringTokenizer tokenizer = new StringTokenizer(name, "/");
	        	if (tokenizer.countTokens()!=2) {
	        		// can't process this one, skip
	        		continue;
	        	}
	        	
	        	String nodeName = tokenizer.nextToken();
	        	String paramName = tokenizer.nextToken();
		        IEclipsePreferences preferences = InstanceScope.INSTANCE.getNode(nodeName);
		        preferences.put(paramName, value);
		        
		        preferences.flush();     
	        }
	        
			MessageDialog.openInformation(null, "Finished", "Configuration has completed successfully.");	        
		} catch (Exception e) {
			logger.log(Level.WARNING, "Could not configure ASKAP: " + e.getMessage());
			
	        Shell shell = PlatformUI.getWorkbench().getModalDialogShellProvider().getShell();		        
            ExceptionDetailsErrorDialog.openError(shell,
                    "ERROR",
                    "Could not configure ASKAP",
                    e);
		}

	}

	
    private File getFile() {
    	Shell shell = PlatformUI.getWorkbench().getModalDialogShellProvider().getShell();
        FileDialog dialog = new FileDialog (shell, SWT.OPEN | SWT.SHEET);
        
        String file = dialog.open();
        if (file != null) {
            file = file.trim();
            if (file.length() > 0) {
				return new File(file);
			}
        }

        return null;
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
