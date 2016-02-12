/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.smchart.indicators;

import org.jfree.data.xy.OHLCDataItem;
import java.util.LinkedList;
import org.jfree.data.time.Day;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import java.awt.Color;
import org.jfree.chart.plot.CombinedDomainXYPlot;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.NumberAxis;
import java.awt.geom.Rectangle2D;
import org.jfree.chart.annotations.XYShapeAnnotation;
import org.jfree.chart.annotations.AbstractXYAnnotation;
import org.jfree.chart.annotations.XYTextAnnotation;
import java.awt.BasicStroke;
import java.awt.Font;
import org.jfree.date.SerialDate;

/**
 *
 * @author 544901
 */
public class RSI extends AbstractIndicator {

    private int setSize = 14; // Size of set
    private double avgGain = 0.0;
    private double avgLoss = 0.0;
    private double average = 0.0;
    private LinkedList m_inList;

    public RSI(final OHLCDataItem[] priceDataset, String ticker) {
        super(priceDataset, ticker);
    }

    public TimeSeries getRSITimeSeries(int inSize) {
        setSize = inSize;
        TimeSeries s1 = new TimeSeries(m_ticker);
        if (m_inList != null) {
            m_inList.clear();
            m_inList = null;
        }
        m_inList = new LinkedList();
        for (int i = 1; i < m_priceDataSet.length; i++) {
            double rsi = getRSI(m_priceDataSet[i - 1].getClose().doubleValue(), m_priceDataSet[i].getClose().doubleValue());
            if (rsi != -1) {
                s1.add(new Day(m_priceDataSet[i].getDate()), rsi);
            } else {
                s1.add(new Day(m_priceDataSet[i].getDate()), 50);
            }
        }
        return s1;
    }

    private double getRSI(double prevPrice, double curPrice) {
        m_inList.add(new Double(curPrice));
        if (m_inList.size() < setSize) {
            if (curPrice > prevPrice) {
                avgGain += (curPrice - prevPrice);
            } else if (curPrice < prevPrice) {
                avgLoss += (prevPrice - curPrice);
            }
            return -1;
        } else if (m_inList.size() == setSize) {
            if (curPrice > prevPrice) {
                avgGain += (curPrice - prevPrice);
            } else if (curPrice < prevPrice) {
                avgLoss += (prevPrice - curPrice);
            }
            average = 100.0 - (100.0 / (1.0 + ((avgGain / setSize) / (avgLoss / setSize))));
        } else if (m_inList.size() > setSize) {
            if (curPrice > prevPrice) {
                avgGain = ((avgGain * (setSize - 1)) + (curPrice - prevPrice)) / setSize;
                avgLoss = ((avgLoss * (setSize - 1)) + (0.0)) / setSize;
            } else if (curPrice < prevPrice) {
                avgLoss = ((avgLoss * (setSize - 1)) + (prevPrice - curPrice)) / setSize;
                avgGain = ((avgGain * (setSize - 1)) + (0.0)) / setSize;
            }
            average = 100.0 - (100.0 / (1.0 + ((avgGain / setSize) / (avgLoss / setSize))));
        }
        //System.out.println(average);
        return average;
    }

    public boolean addChartPlot(CombinedDomainXYPlot plot, AbstractIndicatorInput inpt){
        if(inpt instanceof RSIInput == false)
        {
            return false;
        }
        super.m_inptObj = inpt;
        int period = ((RSIInput)inpt).getTimePeriod();
        TimeSeries rsiSeries = getRSITimeSeries(period);
        DateAxis macdDomainAxis = new DateAxis("Date");
        NumberAxis rangeAxis = new NumberAxis("RSI");
        rangeAxis.setAutoRangeIncludesZero(false);
        XYLineAndShapeRenderer xyRend = new XYLineAndShapeRenderer(true, false);
        xyRend.setSeriesPaint(0, Color.BLUE);
        TimeSeriesCollection dataSet = new TimeSeriesCollection(rsiSeries);
        this.setDataset(dataSet);
        this.setDomainAxis(macdDomainAxis);
        this.setRangeAxis(rangeAxis);
        this.setRenderer(xyRend);
        addOverBoughtSoldPositions();

        if (plot != null) {
            plot.add(this);
            setPlotUI();            
        }
        return true;
    }

   public void updateChartPlot(AbstractIndicatorInput indParamObj)
  {
    int period;
    if ((indParamObj instanceof RSIInput))
      period = ((RSIInput)indParamObj).getTimePeriod();
    else
      return;
 
    this.m_inptObj = indParamObj;
    TimeSeries rsiSeries = getRSITimeSeries(period);
    XYLineAndShapeRenderer xyRend = (XYLineAndShapeRenderer)getRenderer();
    TimeSeriesCollection dataSet = new TimeSeriesCollection(rsiSeries);
    setDataset(dataSet);
    setRenderer(xyRend);
  }
      
    private void addOverBoughtSoldPositions() {
        //long x1 = m_priceDataSet[0].getDate().getTime();
        final double x1 = new Day(1, SerialDate.MARCH, 1920).getMiddleMillisecond();
        final double x2 = new Day(1, SerialDate.MARCH, 2070).getMiddleMillisecond();
        int midDateIdx = m_priceDataSet.length/2;
        double x3 = m_priceDataSet[midDateIdx].getDate().getTime();
        BasicStroke s = new BasicStroke(1.0f);
        double width = Math.abs(x2 - x1);
        double height = 30.0;
        Rectangle2D l = new Rectangle2D.Double(x1, 0, width, height);
        localAddAnnotation(new XYShapeAnnotation(l, s, Color.RED));
        XYTextAnnotation txtAnn = new XYTextAnnotation("OVERSOLD", x3, 25.0);
        txtAnn.setFont(new Font("SansSerif", Font.PLAIN, 14));
        txtAnn.setPaint(Color.RED);
        localAddAnnotation(txtAnn);
        l = new Rectangle2D.Double(x1, 70, width, height);
        localAddAnnotation(new XYShapeAnnotation(l, s, Color.RED));
        txtAnn = new XYTextAnnotation("OVERBOUGHT", x3, 75.0);
        txtAnn.setFont(new Font("SansSerif", Font.PLAIN, 14));
        txtAnn.setPaint(Color.RED);
        localAddAnnotation(txtAnn);

    }

    private void setPlotUI() {
        this.setDomainCrosshairVisible(true);
        this.setDomainCrosshairLockedOnData(false);
        this.setRangeCrosshairVisible(true);
        this.setRangeCrosshairLockedOnData(false);
        this.setDomainGridlinesVisible(false);
        this.setRangeGridlinesVisible(true);
    }

    private void localAddAnnotation(AbstractXYAnnotation ann) {
        boolean annFound = false;
        for (int i = 0; i < this.getAnnotations().size(); i++) {
            if (this.getAnnotations().get(i).equals(ann)) {
                annFound = true;
            }
        }
        if (annFound == false) {
            this.addAnnotation(ann);
        }
    }
}
