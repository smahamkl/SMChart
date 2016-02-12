/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.smchart.indicators;

import org.jfree.data.xy.OHLCDataItem;
import java.util.LinkedList;
import org.jfree.data.time.Day;
import org.jfree.chart.plot.CombinedDomainXYPlot;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.renderer.xy.XYBarRenderer;
import java.awt.geom.Rectangle2D;
import org.jfree.chart.annotations.XYShapeAnnotation;
import org.jfree.chart.annotations.AbstractXYAnnotation;
import org.jfree.chart.annotations.XYTextAnnotation;
import java.awt.BasicStroke;
import java.awt.Font;
import org.jfree.date.SerialDate;
import java.awt.Color;

/**
 *
 * @author venkatasiva
 */
public class Stochastic extends AbstractIndicator {

    private int setSize = 14; // Size of set
    private double percent_k = 0.0;
    private LinkedList m_inList;
    private double average = 0.0;
    private StochasticType m_stocType = StochasticType.Fast;

    public Stochastic(final OHLCDataItem[] priceDataset, String ticker, StochasticType inStocType) throws Exception {
        super(priceDataset, ticker);
        m_stocType = inStocType;
    }

    public enum StochasticType {

        Slow, Fast
    }

    private TimeSeries getStochasticSeries(int inSize) {
        setSize = inSize;
        percent_k = 0;
        TimeSeries s1 = new TimeSeries(m_ticker);


        if (m_inList != null) {
            m_inList.clear();
            m_inList = null;
        }
        m_inList = new LinkedList();
        for (int i = 0; i < m_priceDataSet.length; i++) {
            percent_k = getPercentK(m_priceDataSet[i].getClose().doubleValue());
            if (percent_k != -1) {
                s1.add(new Day(m_priceDataSet[i].getDate()), percent_k);
            }
        }
        return s1;
    }

    private double getPercentK(double close) {
        m_inList.add(new Double(close));
        if (m_inList.size() >= setSize) {
            int firstBar = m_inList.size() - 1;
            int lastBar = m_inList.size() - setSize;
            double highest_high = m_priceDataSet[firstBar].getHigh().doubleValue();
            double lowest_low = m_priceDataSet[firstBar].getLow().doubleValue();

            for (int i = firstBar; i >= lastBar; i--) {
                if (m_priceDataSet[i].getHigh().doubleValue() > highest_high) {
                    highest_high = m_priceDataSet[i].getHigh().doubleValue();
                }
                if (m_priceDataSet[i].getLow().doubleValue() < lowest_low) {
                    lowest_low = m_priceDataSet[i].getLow().doubleValue();
                }
            }
            percent_k = 100.0 * ((close - lowest_low) / (highest_high - lowest_low));
        //System.out.println("firstBar:"+ firstBar + "; lastBar:" +  lastBar + ";  lin size:" + m_inList.size() + "; Close:" + close + ";  High:" + highest_high + ";Low:" + lowest_low + "; %K:" + percent_k);
        } else {
            return -1;
        }
        return percent_k;
    }

    private TimeSeries getSMATimeSeries(TimeSeries kSeries, int ma) {
        setSize = ma;
        TimeSeries s1 = new TimeSeries(m_ticker);
        if (m_inList != null) {
            m_inList.clear();
            m_inList = null;
        }
        m_inList = new LinkedList();
        for (int i = 0; i < kSeries.getItemCount(); i++) {
            double mavg = getMA(kSeries.getDataItem(i).getValue().doubleValue());
            s1.add(kSeries.getTimePeriod(i), mavg);
        }
        return s1;
    }

    private double getMA(double data) {
        m_inList.add(new Double(data));
        if (m_inList.size() <= setSize) {
            average = (average * (m_inList.size() - 1) + data) / m_inList.size();
        } else {
            double firstData = ((Double) m_inList.remove(0)).doubleValue();
            average += (data - firstData) / setSize;
        }
        return average;
    }

    public boolean addChartPlot(CombinedDomainXYPlot plot, AbstractIndicatorInput indParamObj) {
        int period, ma;
        if (indParamObj instanceof StochasticInput) {
            period = ((StochasticInput) indParamObj).getTimePeriod();
            ma = ((StochasticInput) indParamObj).getMovingAvg();
        } else {
            return false;
        }

        super.m_inptObj = indParamObj;
        DateAxis macdDomainAxis = new DateAxis("Date");
        XYLineAndShapeRenderer kRend = new XYLineAndShapeRenderer(true, false);
        NumberAxis rangeAxis = new NumberAxis("Percent");
        rangeAxis.setAutoRangeIncludesZero(false);

        TimeSeries k_seriesData = getStochasticSeries(period);
        TimeSeriesCollection dataSet = new TimeSeriesCollection(k_seriesData);
        TimeSeries dSeries = getSMATimeSeries(k_seriesData, ma);
        dataSet.addSeries(dSeries);
        TimeSeries slowDSeries = getSMATimeSeries(dSeries, ma);
        dataSet.addSeries(slowDSeries);

        kRend.setSeriesPaint(0, Color.BLUE);
        kRend.setSeriesPaint(1, Color.RED);
        kRend.setSeriesPaint(2, Color.GREEN);

        this.setDataset(dataSet);
        this.setDomainAxis(macdDomainAxis);
        this.setRangeAxis(rangeAxis);
        this.setRenderer(kRend);

        setPlotUI();
        if (plot != null) {
            plot.add(this);
            addOverBoughtSoldPositions();
        } else {
            return false;
        }
        return true;
    }

    private void addOverBoughtSoldPositions() {
        //long x1 = m_priceDataSet[0].getDate().getTime();
        final double x1 = new Day(1, SerialDate.MARCH, 2000).getMiddleMillisecond();
        final double x2 = new Day(1, SerialDate.MARCH, 2070).getMiddleMillisecond();
        int midDateIdx = m_priceDataSet.length / 2;
        double x3 = m_priceDataSet[midDateIdx].getDate().getTime();
        BasicStroke s = new BasicStroke(1.0f);
        double width = Math.abs(x2 - x1);
        double height = 20.0;
        Rectangle2D l = new Rectangle2D.Double(x1, 0, width, height);
        localAddAnnotation(new XYShapeAnnotation(l, s, Color.RED));
        XYTextAnnotation txtAnn = new XYTextAnnotation("OVERSOLD", x3, 15.0);
        txtAnn.setFont(new Font("SansSerif", Font.PLAIN, 14));
        txtAnn.setPaint(Color.RED);
        localAddAnnotation(txtAnn);
        l = new Rectangle2D.Double(x1, 80, width, height);
        localAddAnnotation(new XYShapeAnnotation(l, s, Color.RED));
        txtAnn = new XYTextAnnotation("OVERBOUGHT", x3, 85.0);
        txtAnn.setFont(new Font("SansSerif", Font.PLAIN, 14));
        txtAnn.setPaint(Color.RED);
        localAddAnnotation(txtAnn);

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
public void updateChartPlot(AbstractIndicatorInput indParamObj)
  {
    int ma;
    int period = 0;
    if ((indParamObj instanceof StochasticInput)) {
       period = ((StochasticInput)indParamObj).getTimePeriod();
      ma = ((StochasticInput)indParamObj).getMovingAvg();
    }
    else
    {
      return;
    }
  
    this.m_inptObj = indParamObj;
    XYLineAndShapeRenderer kRend = (XYLineAndShapeRenderer)getRenderer();

    TimeSeries k_seriesData = getStochasticSeries(period);
    TimeSeriesCollection dataSet = new TimeSeriesCollection(k_seriesData);
    TimeSeries dSeries = getSMATimeSeries(k_seriesData, ma);
    dataSet.addSeries(dSeries);
    TimeSeries slowDSeries = getSMATimeSeries(dSeries, ma);
    dataSet.addSeries(slowDSeries);

    setDataset(dataSet);
    setRenderer(kRend);
  }

    private void setPlotUI() {
        this.setDomainCrosshairVisible(true);
        this.setDomainCrosshairLockedOnData(false);
        this.setRangeCrosshairVisible(true);
        this.setRangeCrosshairLockedOnData(false);
        this.setDomainGridlinesVisible(false);
        this.setRangeGridlinesVisible(true);
    }

}
