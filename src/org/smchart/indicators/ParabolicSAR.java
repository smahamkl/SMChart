/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.smchart.indicators;

import org.jfree.data.xy.OHLCDataItem;
import org.jfree.data.time.Day;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import java.awt.Color;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.plot.CombinedDomainXYPlot;
import java.awt.BasicStroke;

/**
 *
 * @author venkatasiva
 */
public class ParabolicSAR extends AbstractIndicator {

    private XYPlot mainPlot;
    private double initial = 0.02;
    private double step = 0.02;
    private double maximum = 0.2;
    private int parbolSARPlotSeriesIndex = 8;

    private enum Direction {

        LONG, SHORT
    }
    private Direction dir;
    double[] ep;
    double[] af;
    double sar = 0.0;

    public ParabolicSAR(final OHLCDataItem[] priceDataset, String ticker) {
        super(priceDataset, ticker);
    }

    private TimeSeries getSARTimeSeries() {

        TimeSeries s1 = new TimeSeries(m_ticker);
        ep = new double[m_priceDataSet.length];
        af = new double[m_priceDataSet.length];

        for (int idx = 0; idx < m_priceDataSet.length; idx++) {
            sar = computeSpot(idx);
            s1.add(new Day(m_priceDataSet[idx].getDate()), sar);

        }// end for


        return s1;
    }

    private double computeSpot(int i) {
        double curSAR = 0.0;
        if (i == 0) {
            dir = Direction.LONG;
            curSAR = m_priceDataSet[i].getLow().doubleValue();
            af[i] = initial;
            ep[i] = m_priceDataSet[i].getHigh().doubleValue();

        } else {

            if (dir == Direction.LONG) {
                /** in long-term */
                double currHigh = m_priceDataSet[i].getHigh().doubleValue();
                double prevHigh = m_priceDataSet[i - 1].getHigh().doubleValue();

                if (currHigh > ep[i - 1]) {
                    /** new high, acceleration adds 'step' each day, till 'maximum' */
                    af[i] = Math.min(af[i - 1] + step, maximum);
                    ep[i] = currHigh;
                } else {
                    /** keep same acceleration */
                    af[i] = af[i - 1];
                    ep[i] = ep[i - 1];
                }
                curSAR = sar + af[i] * (prevHigh - sar);

                if (curSAR >= currHigh) {
                    /** turn to short-term */
                    dir = Direction.SHORT;

                    curSAR = currHigh;

                    af[i] = initial;
                    ep[i] = m_priceDataSet[i].getLow().doubleValue();

                } else {
                    /** still in long-term */
                    dir = Direction.LONG;
                }

            } else {
                /** in short-term */
                double currLow = m_priceDataSet[i].getLow().doubleValue();
                double prevLow = m_priceDataSet[i - 1].getLow().doubleValue();

                if (currLow < ep[i - 1]) {
                    af[i] = Math.min(af[i - 1] + step, maximum);
                    ep[i] = currLow;
                } else {
                    af[i] = af[i - 1];
                    ep[i] = ep[i - 1];
                }
                curSAR = sar + af[i] * (prevLow - sar);

                if (curSAR <= currLow) {
                    /** turn to long-term */
                    dir = Direction.LONG;

                    curSAR = currLow;

                    af[i] = initial;
                    ep[i] = m_priceDataSet[i].getHigh().doubleValue();

                } else {
                    /** still in short-term */
                    dir = Direction.SHORT;
                }
            }

        }
        return curSAR;
    }

    public boolean addChartPlot(CombinedDomainXYPlot myplot, AbstractIndicatorInput inpt) {

        if (inpt instanceof ParabolicSARInput == false) {
            return false;
        }
        XYPlot plot = (XYPlot) myplot.getSubplots().get(0);
        super.m_inptObj = inpt;
        initial = ((ParabolicSARInput) inpt).getInitialValue();
        step = ((ParabolicSARInput) inpt).getStepValue();
        maximum = ((ParabolicSARInput) inpt).getMaximumValue();

        if (initial == 0 || step == 0 || maximum == 0) {
            if (parbolSARPlotSeriesIndex > 0) {
                plot.setRenderer(parbolSARPlotSeriesIndex, null);
                plot.setDataset(parbolSARPlotSeriesIndex, null);
            }
        } else {

            XYLineAndShapeRenderer rend = null;
            rend = new XYLineAndShapeRenderer(false, true);
            rend.setSeriesPaint(0, Color.RED);
            BasicStroke s = new BasicStroke(1f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 1f, new float[]{1f}, 0f);
            rend.setSeriesStroke(0, s);
            rend.setSeriesShapesFilled(0, false);
            TimeSeries sar = getSARTimeSeries();
            plot.setRenderer(parbolSARPlotSeriesIndex, rend);
            plot.setDataset(parbolSARPlotSeriesIndex, new TimeSeriesCollection(sar));
        }

        return true;
    }

    public XYPlot getSubPlot() {
        return null;
    }
}
