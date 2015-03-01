package org.csstudio.askap.sb;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.mihalis.opal.multiChoice.MultiChoice;

public class MultiCombo {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
        final Display display = new Display();
        final Shell shell = new Shell(display);
        shell.setLayout(new GridLayout(1, false));
        shell.setText("MultiChoice Example");		
		
		final String[] euroZone = new String[] { "Austria", "Belgium", "Cyprus", "Estonia", "Finland", "France", 
				"Germany", "Greece", "Ireland", "Italy", "Luxembourg", "Malta", "Netherlands", "Portugal", 
				"Slovakia", "Slovenia", "Spain" };
				final MultiChoice<String> mcSimple = new MultiChoice<String>(shell, SWT.READ_ONLY);
				mcSimple.addAll(euroZone);
				
				final GridData gridData = new GridData(GridData.FILL, GridData.BEGINNING, true, true);
				mcSimple.setLayoutData(gridData);
				
                // display the shell...
                shell.open();
                shell.pack();
                while (!shell.isDisposed()) {
                        if (!display.readAndDispatch()) {
                                display.sleep();
                        }
                }
                display.dispose();

				
	}

}
