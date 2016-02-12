/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.smchart.indicators;

import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.plot.CombinedDomainXYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.general.DatasetChangeEvent;
import org.jfree.data.general.DatasetChangeListener;
import org.jfree.data.xy.*;
import java.awt.*;

public class BollingerBands extends AbstractIndicator {    
    

    public BollingerBands(final OHLCDataItem[] priceDataset, String ticker) {
        super(priceDataset, ticker);
    }

    public boolean addChartPlot(CombinedDomainXYPlot plot, AbstractIndicatorInput inpt) {
        
        if(inpt instanceof BollingerInput == false)
        {
            return false;
        }

        XYPlot mainPlot = (XYPlot)plot.getSubplots().get(0);
        super.m_inptObj = inpt;
        
        double stdDev = ((BollingerInput)inpt).getStdDev();
        int ma = ((BollingerInput)inpt).getMovingAverage();
        XYDataset priceDataset = new DefaultOHLCDataset(m_ticker, m_priceDataSet);

        XYLineAndShapeRenderer bbRenderer = new XYLineAndShapeRenderer(true, false);
        XYDataset bbDataset = new BollingerBandsDataset((OHLCDataset) priceDataset, ma, stdDev);

        //This is the code that adds the line data to the candlestick chart

        if (ma == 0 || stdDev == 0) {
            mainPlot.setRenderer(7, null);
            mainPlot.setDataset(7, null);
        } else {
            mainPlot.setRenderer(7, bbRenderer);
            mainPlot.setDataset(7, bbDataset);

            bbRenderer.setSeriesPaint(0, Color.GREEN);
            bbRenderer.setSeriesPaint(1, Color.BLUE);
            bbRenderer.setSeriesPaint(2, Color.RED);
        }
    // bbRenderer.setSeriesVisible(1, false); //Uncomment this line to remove the moving average (center line)
       return true;
    }

    private static class BollingerBandsDataset extends AbstractXYDataset implements DatasetChangeListener {

        protected OHLCDataset ohlcDataset;
        protected int maxLength;
        protected double upperBandwidth;
        protected double lowerBandwidth;
        Double[] upperValues;
        Double[] lowerValues;
        Double[] averageValues;

        public BollingerBandsDataset(OHLCDataset ohlcDataset, int ma, double stdDev) {
            this(ohlcDataset, ma, stdDev, stdDev);
        }

        public BollingerBandsDataset(OHLCDataset ohlcDataset, int maxLength, double upperBandwidth, double lowerbandwidth) {
            this.maxLength = maxLength;
            this.upperBandwidth = upperBandwidth;
            this.lowerBandwidth = lowerbandwidth;
            this.setOhlcDataset(ohlcDataset);
        }

        public OHLCDataset getOhlcDataset() {
            return ohlcDataset;
        }

        private void setOhlcDataset(OHLCDataset ohlcDataset) {
            if (this.ohlcDataset != null) {
                this.ohlcDataset.removeChangeListener(this);
            }
            this.ohlcDataset = ohlcDataset;
            this.ohlcDataset.addChangeListener(this);
            calculateBollingerBands();
            fireDatasetChanged();
        }

        protected void calculateBollingerBands() {
            int size = ohlcDataset.getItemCount(0);
            upperValues = new Double[size];
            averageValues = new Double[size];
            lowerValues = new Double[size];

            for (int i = maxLength, n = ohlcDataset.getItemCount(0); i < n; i++) {
                double sma = this.calculateSMA(i);
                double stdDev = this.calculateStdDev(i, sma);

                averageValues[i] = sma;
                upperValues[i] = sma + (stdDev * upperBandwidth);
                lowerValues[i] = sma - (stdDev * lowerBandwidth);
            }
        }

        protected double calculateSMA(int end) {
            double total = 0.0;
            for (int i = end - maxLength; i < end; i++) {
                total += getSourceValue(i);
            }
            return total / maxLength;
        }

        protected double calculateStdDev(int end, double sma) {
            double stdDev = 0.0;
            double total = 0.0;
            for (int i = end - maxLength; i < end; i++) {
                double dev = getSourceValue(i) - sma;
                total += (dev * dev);
            }
            total = (total / maxLength);
            stdDev = Math.sqrt(total);
            return stdDev;
        }

        protected double getSourceValue(int item) {
            return ohlcDataset.getCloseValue(0, item);
        }

        public int getSeriesCount() {
            return 3;
        }

        public Comparable getSeriesKey(int series) {
            switch (series) {
                case 0:
                    return "Bollinger Bands Lower";
                case 1:
                    return "Bollinger Bands Average";
                case 2:
                    return "Bollinger Bands Upper";
                default:
                    return null;
            }
        }

        public int getItemCount(int series) {
            return ohlcDataset.getItemCount(0);
        }

        public Number getX(int series, int item) {
            return ohlcDataset.getX(0, item);
        }

        public Number getY(int series, int item) {
            switch (series) {
                case 0:
                    return lowerValues[item];
                case 1:
                    return averageValues[item];
                case 2:
                    return upperValues[item];
                default:
                    return null;
            }
        }

        public void datasetChanged(DatasetChangeEvent event) {
            calculateBollingerBands();
            fireDatasetChanged();
        }
    }
}

