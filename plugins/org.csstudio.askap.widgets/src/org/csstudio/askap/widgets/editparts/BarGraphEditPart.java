package org.csstudio.askap.widgets.editparts;


import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.csstudio.askap.widgets.figures.BarGraphFigure;
import org.csstudio.askap.widgets.model.BarGraphModel;
import org.csstudio.askap.widgets.model.BarGraphModel.BarProperty;
import org.csstudio.askap.widgets.swt.BarGraph;
import org.csstudio.opibuilder.editparts.AbstractPVWidgetEditPart;
import org.csstudio.opibuilder.properties.IWidgetPropertyChangeHandler;
import org.csstudio.simplepv.VTypeHelper;
import org.eclipse.draw2d.IFigure;
import org.epics.vtype.VType;



public class BarGraphEditPart extends AbstractPVWidgetEditPart {
	
	BarGraph barGraph = null;
	
    class BarPropertyChangeHandler implements IWidgetPropertyChangeHandler {
        private int barIndex;
        private BarProperty barProperty;
        private String yPVPropID;
        public BarPropertyChangeHandler(int barIndex, BarProperty barProperty, String yPVPropID) {
            this.barIndex = barIndex;
            this.barProperty = barProperty;
            this.yPVPropID = yPVPropID;
        }
        public boolean handleChange(Object oldValue, Object newValue,
                IFigure refreshableFigure) {
            setBarProperty(barProperty, barIndex, newValue, yPVPropID);
            return false;
        }
    }
	
    private void setBarProperty(BarProperty barProperty, int barIndex, Object newValue, String yPVPropID) {
    	switch (barProperty) {
    		case YPV_VALUE:
                if(newValue == null || !(newValue instanceof VType))
                    break;
                
        		double val = VTypeHelper.getDouble((VType) newValue);
                barGraph.setValue(barIndex, val);
                break;
			default:
				break;
    	}
    }    
    
	@Override
	public BarGraphModel getWidgetModel() {
		return (BarGraphModel) super.getWidgetModel();
	}

	@Override
	protected IFigure doCreateFigure() {
		BarGraphFigure barGraphFigure = new BarGraphFigure(this);
		barGraph = barGraphFigure.getSWTWidget();
		barGraph.setNumberOfBars(getWidgetModel().getBarCount());
		barGraph.setShowXAxis(false);
		barGraph.setShowYAxis(false);
		barGraph.setTitle(null);
		barGraph.setYAxisRange(0, getWidgetModel().getMaxYValue());
		
		getWidgetModel().setBarGraph(barGraph);
		
        for(int i=BarGraphModel.MAX_NUMBER_OF_BARS -1; i>= getWidgetModel().getBarCount(); i--){
            String propID = BarGraphModel.makeBarPropID(BarProperty.YPV.propIDPre, i);
            getWidgetModel().setPropertyVisible(propID, false);
        }
        
		return barGraphFigure;
	}

	@Override
	protected void registerPropertyChangeHandlers() {
		registerBarPropertyChangeHandlers();
		registerBarNumberChangeHandler();
		registerYAxisMaxValueChangeHandler();
	}
	
	private void registerBarNumberChangeHandler() {

		final IWidgetPropertyChangeHandler handler = new IWidgetPropertyChangeHandler(){

			@Override
			public boolean handleChange(Object oldValue, Object newValue,
					IFigure figure) {
				
				int newBarCount = (Integer) newValue;
				barGraph.setNumberOfBars(newBarCount);
								
		        for(int i=0; i<BarGraphModel.MAX_NUMBER_OF_BARS; i++){
	                String propID = BarGraphModel.makeBarPropID(BarProperty.YPV.propIDPre, i);
	                
	                if (i<newBarCount)
	                	getWidgetModel().setPropertyVisible(propID, true);
	                else
	                	getWidgetModel().setPropertyVisible(propID, false);
		        }

		        return true;
			}
		};
	       
        getWidgetModel().getProperty(BarGraphModel.PROP_BAR_COUNT).addPropertyChangeListener(
    		new PropertyChangeListener(){
		        public void propertyChange(PropertyChangeEvent evt) {
		            handler.handleChange(evt.getOldValue(), evt.getNewValue(), getFigure());
		        }
        });
	}

	private void registerYAxisMaxValueChangeHandler() {

		final IWidgetPropertyChangeHandler handler = new IWidgetPropertyChangeHandler(){

			@Override
			public boolean handleChange(Object oldValue, Object newValue,
					IFigure figure) {
				
				int newBarCount = (Integer) newValue;
				barGraph.setYAxisRange(0, newBarCount);								
		        return true;
			}
		};
	       
        getWidgetModel().getProperty(BarGraphModel.PROP_MAX_Y_AXIS_VALUE).addPropertyChangeListener(
    		new PropertyChangeListener(){
		        public void propertyChange(PropertyChangeEvent evt) {
		            handler.handleChange(evt.getOldValue(), evt.getNewValue(), getFigure());
		        }
        });
	}

	private void registerBarPropertyChangeHandlers() {
	    for(int i=0; i<BarGraphModel.MAX_NUMBER_OF_BARS; i++){
	        String yPVPropID = BarGraphModel.makeBarPropID(
	        		BarProperty.YPV.propIDPre, i);
            final String propID = BarGraphModel.makeBarPropID(
            		BarProperty.YPV_VALUE.propIDPre, i);
            
            final IWidgetPropertyChangeHandler handler = new BarPropertyChangeHandler(i, BarProperty.YPV_VALUE, yPVPropID);
            setPropertyChangeHandler(propID, handler);
	    }
	}

}