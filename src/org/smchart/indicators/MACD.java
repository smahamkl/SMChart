/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.smchart.indicators;

import org.jfree.data.xy.OHLCDataItem;
import java.util.LinkedList;
import org.jfree.data.time.Day;
import org.jfree.chart.plot.CombinedDomainXYPlot;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.renderer.xy.XYBarRenderer;

/**
 *
 * @author venkatasiva
 */
public class MACD extends AbstractIndicator {

    private int setSize = 22; // Size of set
    private double average = 0.0;
    private LinkedList m_inList;
    private double m_kValue = 0.0;    

    public MACD(final OHLCDataItem[] priceDataset, String ticker) throws Exception {
        super(priceDataset, ticker);
    }

    private void calculateKValue(int numberOfDays) {
        m_kValue = (double) (2.0 / ((double) numberOfDays + 1.0));
    }

    public double[] getEMASeries(int inSize) {
        setSize = inSize;
        average = 0;
        double emaData[] = new double[m_priceDataSet.length];
        calculateKValue(setSize);

        if (m_inList != null) {
            m_inList.clear();
            m_inList = null;
        }
        m_inList = new LinkedList();
        for (int i = 0; i < m_priceDataSet.length; i++) {
            double ema = getEMA(m_priceDataSet[i].getClose().doubleValue());
            if (ema != -1) {
                emaData[i] = ema;
            // System.out.println(ema);
            } else {
                emaData[i] = 0.0;
            }
        }
        return emaData;
    }

    private double getEMA(double data) {
        m_inList.add(new Double(data));

        if (m_inList.size() <= setSize) {
            average += data;
            return -1;
        } else if (m_inList.size() == (setSize + 1)) {
            average = (average / setSize);
            //EMA = Price(t) * k + EMA(y) * (1 - k)
            average = (data * m_kValue) + (average * (1 - m_kValue));
        } else if (m_inList.size() > (setSize + 1)) {
            average = (data * m_kValue) + (average * (1 - m_kValue));
        }
        return average;
    }

    private TimeSeries getSMATimeSeries(int inSize, TimeSeries seriesData) {
        setSize = inSize;
        TimeSeries t = new TimeSeries("sma");

        if (m_inList != null) {
            m_inList.clear();
            m_inList = null;
        }
        m_inList = new LinkedList();
        for (int i = 0; i < seriesData.getItemCount(); i++) {
            double sma = sma(seriesData.getDataItem(i).getValue().doubleValue());
            if (sma != -1) {
                t.add(seriesData.getTimePeriod(i), sma);
            //seriesData.getDataItem(i).setValue(sma);
            } else {
                t.add(seriesData.getTimePeriod(i), 0.0);
            }
        }
        return t;
    }

    private double sma(double data) {
        m_inList.add(new Double(data));
        if (m_inList.size() <= setSize) {
            average = (average * (m_inList.size() - 1) + data) / m_inList.size();
        } else {
            double firstData = ((Double) m_inList.remove(0)).doubleValue();
            average += (data - firstData) / setSize;
        }
        return average;
    }

    public boolean addChartPlot(CombinedDomainXYPlot plot, AbstractIndicatorInput inpt) {

        if (inpt instanceof MACDInput == false) {
            return false;
        }
        super.m_inptObj = inpt;
        int slowMA = ((MACDInput) inpt).getSlowMA();
        int fastMA = ((MACDInput) inpt).getFastMA();
        int ema = ((MACDInput) inpt).getEMATimePeriod();
        DateAxis macdDomainAxis = new DateAxis("Date");
        XYBarRenderer barRend = new XYBarRenderer();
        NumberAxis rangeAxis = new NumberAxis("MACD");
        XYLineAndShapeRenderer xyRend = new XYLineAndShapeRenderer(true, false);

        double ema26Data[] = getEMASeries(slowMA);
        double ema12Data[] = getEMASeries(fastMA);


        TimeSeries macdData = new TimeSeries("macd");
        TimeSeries histData = new TimeSeries("hist");

        for (int i = 0; i < m_priceDataSet.length; i++) {
            if ((ema12Data[i] == 0) || (ema26Data[i] == 0)) {
                macdData.add(new Day(m_priceDataSet[i].getDate()), 0);
            } else {
                macdData.add(new Day(m_priceDataSet[i].getDate()), ema12Data[i] - ema26Data[i]);
            }
        }
        //signal line
        TimeSeries signalSeries = getSMATimeSeries(ema, macdData);


        for (int i = 0; i < m_priceDataSet.length; i++) {
            double macd = 0.0, histSize = 0.0, signal = 0.0;
            if ((ema12Data[i] == 0) || (ema26Data[i] == 0)) {
            } else {
                macd = ema12Data[i] - ema26Data[i];
            }
            signal = signalSeries.getDataItem(i).getValue().doubleValue();
            //System.out.println("MACD:" + macd + ";  signal:" + signalSeries.getDataItem(i).getValue().doubleValue());
            if (macd != 0 && signal != 0) {
                histSize = macd - signal;
            }
            histData.add(new Day(m_priceDataSet[i].getDate()), histSize);
        }

        barRend.setShadowVisible(false);
        barRend.setDrawBarOutline(false);
        barRend.setSeriesPaint(0, java.awt.Color.BLUE);
        barRend.setMargin(0.5);

        this.setDataset(new TimeSeriesCollection(histData));
        this.setDomainAxis(macdDomainAxis);
        this.setRangeAxis(rangeAxis);
        this.setRenderer(barRend);

        xyRend.setSeriesPaint(0, java.awt.Color.RED);
        xyRend.setSeriesPaint(1, java.awt.Color.BLACK);

        TimeSeriesCollection c = new TimeSeriesCollection(macdData);
        c.addSeries(signalSeries);

        this.setDataset(1, c);
        this.setRenderer(1, xyRend);


        setPlotUI();
        if (plot != null) {
            plot.add(this);
        }
        return true;
    }
    
    public void updateChartPlot(AbstractIndicatorInput indParamObj)
  {
    if (!(indParamObj instanceof MACDInput)) {
      return;
    }
    this.m_inptObj = indParamObj;
    int slowMA = ((MACDInput)indParamObj).getSlowMA();
    int fastMA = ((MACDInput)indParamObj).getFastMA();
    int ema = ((MACDInput)indParamObj).getEMATimePeriod();

    XYBarRenderer barRend = (XYBarRenderer)getRenderer();
    XYLineAndShapeRenderer xyRend = (XYLineAndShapeRenderer)getRenderer(1);

    double[] ema26Data = getEMASeries(slowMA);
    double[] ema12Data = getEMASeries(fastMA);

    TimeSeries macdData = new TimeSeries("macd");
    TimeSeries histData = new TimeSeries("hist");

    for (int i = 0; i < this.m_priceDataSet.length; i++) {
      if ((ema12Data[i] == 0.0D) || (ema26Data[i] == 0.0D))
        macdData.add(new Day(this.m_priceDataSet[i].getDate()), 0.0D);
      else {
        macdData.add(new Day(this.m_priceDataSet[i].getDate()), ema12Data[i] - ema26Data[i]);
      }
    }

    TimeSeries signalSeries = getSMATimeSeries(ema, macdData);

    for (int i = 0; i < this.m_priceDataSet.length; i++) {
      double macd = 0.0D; double histSize = 0.0D; double signal = 0.0D;
      if ((ema12Data[i] != 0.0D) && (ema26Data[i] != 0.0D))
      {
        macd = ema12Data[i] - ema26Data[i];
      }
      signal = signalSeries.getDataItem(i).getValue().doubleValue();

      if ((macd != 0.0D) && (signal != 0.0D)) {
        histSize = macd - signal;
      }
      histData.add(new Day(this.m_priceDataSet[i].getDate()), histSize);
    }

    setDataset(new TimeSeriesCollection(histData));
    setRenderer(barRend);

    TimeSeriesCollection c = new TimeSeriesCollection(macdData);
    c.addSeries(signalSeries);

    setDataset(1, c);
    setRenderer(1, xyRend);
  }


    private void setPlotUI() {
        //mainPlot.setBackgroundPaint(java.awt.Color.LIGHT_GRAY);
        this.setDomainCrosshairVisible(true);
        this.setDomainCrosshairLockedOnData(false);
        this.setRangeCrosshairVisible(true);
        this.setRangeCrosshairLockedOnData(false);
        this.setDomainGridlinesVisible(false);
        this.setRangeGridlinesVisible(false);
    }
}
