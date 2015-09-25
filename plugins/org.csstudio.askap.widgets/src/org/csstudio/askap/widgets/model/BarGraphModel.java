package org.csstudio.askap.widgets.model;

import org.csstudio.askap.widgets.swt.BarGraph;
import org.csstudio.opibuilder.model.AbstractPVWidgetModel;
import org.csstudio.opibuilder.properties.ColorProperty;
import org.csstudio.opibuilder.properties.IntegerProperty;
import org.csstudio.opibuilder.properties.NameDefinedCategory;
import org.csstudio.opibuilder.properties.PVNameProperty;
import org.csstudio.opibuilder.properties.PVValueProperty;
import org.csstudio.opibuilder.properties.StringProperty;
import org.csstudio.opibuilder.properties.WidgetPropertyCategory;
import org.csstudio.opibuilder.util.OPIColor;
import org.eclipse.swt.graphics.RGB;

public class BarGraphModel extends AbstractPVWidgetModel {
	
    /**
     * The ID of this widget model.
     * 
     */
    public static final String ID = "org.csstudio.askap.widgets.barGraph"; //$NON-NLS-1$

    public static final String PROP_MAX_Y_AXIS_VALUE = "max_y_axis_value";    

    public static final String PROP_BASE_PV_NAME = "base_pvname";    
    
    public static final String PROP_BAR_COLOR = "bar_color";    

    public static final String PROP_BAR_COUNT = "bar_count";    
    public static final int MAX_NUMBER_OF_BARS = 1000;
	
    private static final RGB DEFAULT_AXIS_COLOR = new RGB(80, 240, 180);

	
    public enum BarProperty { 	
        YPV("y_pv", "Y PV"),
        YPV_VALUE("y_pv_value", "Y PV Value");

        public String propIDPre;
        public String description;

        private BarProperty(String propertyIDPrefix, String description) {
            this.propIDPre = propertyIDPrefix;
            this.description = description;
        }

        @Override
        public String toString() {
            return description;
        }
    }

	private BarGraph barGraph;
	

	public BarGraphModel() {
	}	
	
	@Override
	protected void configureProperties() {
		
        addProperty(new IntegerProperty(PROP_BAR_COUNT, "Bar Count",
                WidgetPropertyCategory.Behavior, 1, 1, MAX_NUMBER_OF_BARS));

        addProperty(new IntegerProperty(PROP_MAX_Y_AXIS_VALUE, "Max Y Axis Value",
                WidgetPropertyCategory.Behavior, 100, 0, 100000));

        addProperty(new ColorProperty(PROP_BAR_COLOR, "Bar color", WidgetPropertyCategory.Display, DEFAULT_AXIS_COLOR));

        addProperty(new StringProperty(PROP_BASE_PV_NAME, "Base PV Name", WidgetPropertyCategory.Basic, ""));
        
		for (int i=0; i<MAX_NUMBER_OF_BARS; i++) {
	        String propID = makeBarPropID(BarProperty.YPV.propIDPre, i);
	        WidgetPropertyCategory category = new NameDefinedCategory("Bar " + i);

            addPVProperty(new PVNameProperty(propID, BarProperty.YPV.toString(), category, ""),
                    new PVValueProperty(makeBarPropID(BarProperty.YPV_VALUE.propIDPre, i), null));
		}
		
		setPropertyVisible(PROP_PVNAME, false);
	}
    public int getBarCount() {
        return (Integer) getProperty(PROP_BAR_COUNT).getPropertyValue();
    }
    public int getMaxYValue() {
        return (Integer) getProperty(PROP_MAX_Y_AXIS_VALUE).getPropertyValue();
    }

    public OPIColor getBarColor() {
    	return (OPIColor) getProperty(PROP_BAR_COLOR).getPropertyValue();
    }
    
 	@Override
	public String getTypeID() {
		return ID;
	}
	
	@Override
	public String getTooltip() {
		int i = barGraph.getMouseOverBarIndex();
		if (i>=0) {			
			String pvName = (String) getProperty(makeBarPropID(BarProperty.YPV.propIDPre, i)).getPropertyValue();
			String pvValue = getProperty(makeBarPropID(BarProperty.YPV_VALUE.propIDPre, i)).getPropertyValue().toString();			
			return pvName + "\n" + pvValue;
		} else {
			return "";
		}
	}

    public static String makeBarPropID(String propIDPre, int index){
        return "bar_" +index + "_" + propIDPre; //$NON-NLS-1$ //$NON-NLS-2$
    }

	public void setBarGraph(BarGraph barGraph) {
		this.barGraph = barGraph;
	}

}
