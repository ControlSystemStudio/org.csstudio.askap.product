package org.csstudio.askap.widgets.swt;

import org.csstudio.opibuilder.util.AlarmRepresentationScheme;
import org.csstudio.opibuilder.util.OPIColor;
import org.csstudio.ui.util.CustomMediaFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.PlatformUI;
import org.swtchart.Chart;
import org.swtchart.IAxis.Position;
import org.swtchart.IBarSeries;
import org.swtchart.ISeries.SeriesType;
import org.swtchart.Range;

public class BarGraph extends Composite {
		
	enum ALARM_SERIES {
		NO_ALARM("NONE", new double[]{30, 0, 0, 20, 0}),
		MAJOR_ALARM("MAJOR", new double[]{0, 70, 0, 0}),
		MINOR_ALARM("MINOR", new double[]{0, 0, 50, 0, 0}),
		INVALID_ALARM("INVALID", new double[]{0, 0, 0, 0, 40});
		
		String alarmName;
		double[] values;
		
		ALARM_SERIES(String alarmName, double values[]) {
			this.alarmName = alarmName;
			this.values = values;
		}		
	}
	
	private Chart chart = null;
	
	public BarGraph(Composite parent) {
		super(parent, SWT.NONE);		
        setLayout(new FillLayout());
        
		this.chart = new Chart(this, SWT.NONE);
		chart.getLegend().setVisible(false);
		
        // set titles
        chart.getTitle().setText("Bar Chart");

        chart.getAxisSet().getXAxis(0).enableCategory(true);
        chart.getAxisSet().getXAxis(0).setCategorySeries(new String[]{"1", "2", "3", "4", "5"});

        chart.getAxisSet().getYAxis(0).setPosition(Position.Secondary);
        
        // create bar series
        for (ALARM_SERIES s : ALARM_SERIES.values()) {       
        	IBarSeries barSeries = (IBarSeries)chart.getSeriesSet().createSeries(SeriesType.BAR, s.alarmName);
    		barSeries.setYSeries(s.values);
    		
    		RGB colorRBG = null;
			switch (s) {
    			case MAJOR_ALARM:
    				colorRBG = AlarmRepresentationScheme.getMajorColor();
    				break;
    			case MINOR_ALARM:
    				colorRBG = AlarmRepresentationScheme.getMinorColor();
    				break;
    			case INVALID_ALARM:
    				colorRBG = AlarmRepresentationScheme.getInValidColor();
    				break;
    		}
    		if (colorRBG != null) {
    			Color color = CustomMediaFactory.getInstance().getColor(colorRBG);			
    			barSeries.setBarColor(color);
    		}
        }

        for (ALARM_SERIES s : ALARM_SERIES.values()) {       
        	IBarSeries barSeries = (IBarSeries)chart.getSeriesSet().getSeries(s.alarmName);
        	barSeries.enableStack(true);
        }
 		
		chart.getAxisSet().adjustRange();
		this.pack();
        
	}
	
	public void setValue(int barIndex, double value, String alarmStatus) {
		
		for (ALARM_SERIES s : ALARM_SERIES.values()) {
			if (s.alarmName.equals(alarmStatus)) {
				if (s.values!=null && s.values.length>barIndex) {
					s.values[barIndex] = value;
				}
			} else {				
				if (s.values!=null && s.values.length>barIndex) {
					s.values[barIndex] = 0;					
				}
			}			
			IBarSeries barSeries = (IBarSeries) chart.getSeriesSet().getSeries(s.alarmName);
			barSeries.setYSeries(s.values);
		}
		
        // adjust the axis range
        chart.getAxisSet().getXAxis(0).adjustRange();	    
        chart.redraw();
	}
	
	public void setNumberOfBars(int n) {
		for (ALARM_SERIES s : ALARM_SERIES.values()) {
			s.values = new double[n];
		}
		
		String categoryNames[] = new String[n];
		for (int i=0; i<n; i++)
			categoryNames[i] = "" +i;
		
        chart.getAxisSet().getXAxis(0).setCategorySeries(categoryNames);

	}
	
	/**
	 * 
	 * @return the bar index the mouse it currently hoovering 
	 */
	public int getMouseOverBarIndex() {
		Control plotArea = chart.getPlotArea();
		Point cursorLocation = PlatformUI.getWorkbench().getDisplay().getCursorLocation();
		Point mousePoint = plotArea.toControl(cursorLocation);
		
		for (ALARM_SERIES s : ALARM_SERIES.values()) {
			IBarSeries barSeries = (IBarSeries) chart.getSeriesSet().getSeries(s.alarmName);
			
			if (barSeries==null)
				continue;
			
			Rectangle[] barRects = barSeries.getBounds();			
			if (barRects==null)
				continue;
					
			// find rect the mouse it currently in
			for (int i=0; i<barRects.length; i++) {
				if (barRects[i]!=null && barRects[i].contains(mousePoint)) {
					return i;
				}
			}
		}
		
		return -1;
	}

	public void setShowXAxis(boolean b) {
		chart.getAxisSet().getXAxis(0).getTitle().setVisible(b);
		chart.getAxisSet().getXAxis(0).getTick().setVisible(b);
	}
	
	public void setShowYAxis(boolean b) {
		chart.getAxisSet().getYAxis(0).getTitle().setVisible(b);
		chart.getAxisSet().getYAxis(0).getTick().setVisible(b);		
	}
	
	public void setYAxisRange(double min, double max) {
		chart.getAxisSet().getYAxis(0).setRange(new Range(min, max));
	}

	public void setBarColor(Color color) {		
		IBarSeries barSeries = (IBarSeries) chart.getSeriesSet().getSeries(ALARM_SERIES.NO_ALARM.alarmName);
		barSeries.setBarColor(color);
	}

	
	public void setTitle(String title) {
		if (title!=null && title.length()>0) {
	        chart.getTitle().setText("Bar Chart");
	        chart.getTitle().setVisible(true);
		} else {
	        chart.getTitle().setVisible(false);			
		}
	}
}
