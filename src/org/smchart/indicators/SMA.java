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
public class SMA extends AbstractIndicator {

    private int setSize = 1; // Size of set
    private double average = 0.0;
    private LinkedList m_inList;

    public SMA(final OHLCDataItem[] priceDataset, String ticker) {
        super(priceDataset, ticker);
    }

    private TimeSeries getSMATimeSeries(int inSize) {
        setSize = inSize;
        TimeSeries s1 = new TimeSeries(m_ticker);
        if (m_inList != null) {
            m_inList.clear();
            m_inList = null;
        }
        m_inList = new LinkedList();
        for (int i = 0; i < m_priceDataSet.length; i++) {
            double mavg = add(m_priceDataSet[i].getClose().doubleValue());
            s1.add(new Day(m_priceDataSet[i].getDate()), mavg);
        }
        return s1;
    }

    private double add(double data) {
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
        int dataSetIdx = 1;
        if (inpt instanceof SMAInput == false) {
            return false;
        }
        XYPlot myPlot = (XYPlot) plot.getSubplots().get(0);
        super.m_inptObj = inpt;
        XYLineAndShapeRenderer rend = null;
        Color[] colorArr = new Color[]{Color.RED, Color.BLUE, Color.BLACK};

        for (int i = dataSetIdx; i < dataSetIdx + 3; i++) {
            myPlot.setRenderer(i, null);
            myPlot.setDataset(i, null);
        }


        for (int i = 0; i < ((SMAInput) inpt).getMovingAvgArray().length; i++) {
            if (((SMAInput) inpt).getMovingAvgArray()[i] != 0) {
                rend = new XYLineAndShapeRenderer(true, false);
                rend.setSeriesPaint(0, colorArr[i]);
                TimeSeries mav = getSMATimeSeries(((SMAInput) inpt).getMovingAvgArray()[i]);
                myPlot.setRenderer(dataSetIdx, rend);
                myPlot.setDataset(dataSetIdx, new TimeSeriesCollection(mav));
                dataSetIdx += 1;
            }
        }

        return true;
    }

    public XYPlot getSubPlot() {
        return null;
    }

 }
