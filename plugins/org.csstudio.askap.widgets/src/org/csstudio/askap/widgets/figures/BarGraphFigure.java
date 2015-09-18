package org.csstudio.askap.widgets.figures;


import org.csstudio.askap.widgets.swt.BarGraph;
import org.csstudio.opibuilder.editparts.AbstractBaseEditPart;
import org.csstudio.opibuilder.widgets.figures.AbstractSWTWidgetFigure;
import org.eclipse.swt.widgets.Composite;

public class BarGraphFigure extends AbstractSWTWidgetFigure<BarGraph> {
    public BarGraphFigure(AbstractBaseEditPart editpart) {
        super(editpart);
    }

    @Override
    protected BarGraph createSWTWidget(Composite parent, int style) {
         return new BarGraph(parent);
    }	
}
