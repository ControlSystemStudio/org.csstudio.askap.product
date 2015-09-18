package org.csstudio.askap.widgets.swt;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.PlatformUI;
import org.swtchart.Chart;
import org.swtchart.IBarSeries;
import org.swtchart.ISeries.SeriesType;
import org.swtchart.Range;

public class BarGraph extends Composite {
	
	private static final String SERIES_ID = "bar_series";
	private Chart chart = null;
	
	private double barValues[] = null;
	
	public BarGraph(Composite parent) {
		super(parent, SWT.NONE);		
        setLayout(new FillLayout());
        
		this.chart = new Chart(this, SWT.NONE);
		chart.getLegend().setVisible(false);
		
        // set titles
        chart.getTitle().setText("Bar Chart");

        // create bar series
        IBarSeries barSeries = (IBarSeries) chart.getSeriesSet().createSeries(
                SeriesType.BAR, SERIES_ID);
        double s[] = new double[5];
        s[0] = 30;
        s[1] = 70;
        s[2] = 50;
        s[3] = 20;
        s[4] = 80;
		barSeries.setYSeries(s);
		
		chart.getAxisSet().adjustRange();
		this.pack();
        
	}
	
	public void setValue(int barIndex, double value) {
		if (barValues!=null && barValues.length>barIndex) {
			barValues[barIndex] = value;
			
			IBarSeries barSeries = (IBarSeries) chart.getSeriesSet().getSeries(SERIES_ID);
			barSeries.setYSeries(barValues);
			
	        // adjust the axis range
	        chart.getAxisSet().getXAxis(0).adjustRange();	    
	        chart.redraw();
		}
	}
	
	public void setNumberOfBars(int n) {
		barValues = new double[n];
	}
	
	/**
	 * 
	 * @return the bar index the mouse it currently hoovering 
	 */
	public int getMouseOverBarIndex() {
		Control plotArea = chart.getPlotArea();
		Point cursorLocation = PlatformUI.getWorkbench().getDisplay().getCursorLocation();
		Point mousePoint = plotArea.toControl(cursorLocation);
		
		IBarSeries barSeries = (IBarSeries) chart.getSeriesSet().getSeries(SERIES_ID);
		
		if (barSeries==null)
			return -1;
		
		Rectangle[] barRects = barSeries.getBounds();
		
		if (barRects==null)
			return -1;
				
		// find rect the mouse it currently in
		for (int i=0; i<barRects.length; i++) {
			if (barRects[i]!=null && barRects[i].contains(mousePoint)) {
				return i;
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
	}
	
	public void setYAxisRange(double min, double max) {
		chart.getAxisSet().getYAxis(0).setRange(new Range(min, max));
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
