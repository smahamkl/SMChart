/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.smchart.indicators;

import org.jfree.data.xy.OHLCDataItem;
import java.util.LinkedList;
import org.jfree.data.time.Day;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.plot.CombinedDomainXYPlot;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import java.awt.Color;
import java.util.ArrayList;

/**
 *
 * @author venkatasiva
 */
public class EMA extends AbstractIndicator {

    private int setSize = 22; // Size of set
    private double average = 0.0;
    private LinkedList m_inList;
    private double m_kValue = 0.0;    

    public EMA(final OHLCDataItem[] priceDataset, String ticker) {
        super(priceDataset, ticker);
    }

    private void calculateKValue(int numberOfDays) {
        m_kValue = (double) (2.0 / ((double) numberOfDays + 1.0));
    }

    public TimeSeries getEMATimeSeries(int inSize) {
        setSize = inSize;
        calculateKValue(setSize);
        TimeSeries s1 = new TimeSeries(m_ticker);
        if (m_inList != null) {
            m_inList.clear();
            m_inList = null;
        }
        m_inList = new LinkedList();
        for (int i = 0; i < m_priceDataSet.length; i++) {
            double ema = getEMA(m_priceDataSet[i].getClose().doubleValue());
            if (ema != -1) {
                s1.add(new Day(m_priceDataSet[i].getDate()), ema);
            // System.out.println(ema);
            }
        }
        return s1;
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

    public boolean addChartPlot(CombinedDomainXYPlot combPlot, AbstractIndicatorInput inpt) {
        int dataSetNum = 4;
        if (inpt instanceof EMAInput == false) {
            return false;
        }
        XYPlot myPlot = (XYPlot)combPlot.getSubplots().get(0);
        super.m_inptObj = inpt;
        XYLineAndShapeRenderer rend = null;
        Color[] colorArr = new Color[]{Color.ORANGE, Color.PINK, Color.DARK_GRAY};
        
            for (int i = dataSetNum; i < dataSetNum + 3; i++) {
                myPlot.setRenderer(i, null);
                myPlot.setDataset(i, null);
            }

        for (int i = 0; i < ((EMAInput)inpt).getMovingAvgArr().length; i++) {
            if (((EMAInput)inpt).getMovingAvgArr()[i] != 0) {
                rend = new XYLineAndShapeRenderer(true, false);
                rend.setSeriesPaint(0, colorArr[i]);
                TimeSeries emaSeries = getEMATimeSeries(((EMAInput)inpt).getMovingAvgArr()[i]);
                myPlot.setRenderer(dataSetNum, rend);
                myPlot.setDataset(dataSetNum, new TimeSeriesCollection(emaSeries));
                dataSetNum += 1;                
            }
        }

        return true;

    }
}
