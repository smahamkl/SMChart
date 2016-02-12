package org.smchart.indicators;

import java.awt.Color;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CombinedDomainXYPlot;
import org.jfree.chart.renderer.xy.XYBarRenderer;
import org.jfree.data.xy.AbstractIntervalXYDataset;
import org.jfree.data.xy.DefaultOHLCDataset;
import org.jfree.data.xy.IntervalXYDataset;
import org.jfree.data.xy.OHLCDataItem;
import org.jfree.data.xy.OHLCDataset;

public class Volume extends AbstractIndicator
{
  static final Long ONE_DAY = Long.valueOf(86400000L);

  public Volume(OHLCDataItem[] priceDataArr, String ticker) {
    super(priceDataArr, ticker);
    DateAxis domainAxis = new DateAxis("Date");
    IntervalXYDataset volumeDataset = getVolumeDataset(getOHLCDataSet(priceDataArr));
    NumberAxis volumeAxis = new NumberAxis("Volume");
    XYBarRenderer volumeRenderer = new XYBarRenderer();
    setDataset(volumeDataset);
    setDomainAxis(domainAxis);
    setRangeAxis(volumeAxis);
    setRenderer(volumeRenderer);
    volumeRenderer.setSeriesPaint(0, Color.BLUE);
  }

  private OHLCDataset getOHLCDataSet(OHLCDataItem[] priceDataset) {
    return new DefaultOHLCDataset(this.m_ticker, priceDataset);
  }

  private static IntervalXYDataset getVolumeDataset(final OHLCDataset priceDataset) {
        return new AbstractIntervalXYDataset() {

            public int getSeriesCount() {
                return priceDataset.getSeriesCount();
            }

            public Comparable getSeriesKey(int series) {
                return priceDataset.getSeriesKey(series) + "-Volume";
            }

            public int getItemCount(int series) {
                return priceDataset.getItemCount(series);
            }

            public Number getX(int series, int item) {
                return priceDataset.getX(series, item);
            }

            public Number getY(int series, int item) {
                return priceDataset.getVolume(series, item);
            }

            public Number getStartX(int series, int item) {
                return priceDataset.getX(series, item).doubleValue() - ONE_DAY / 2;
            }

            public Number getEndX(int series, int item) {
                return priceDataset.getX(series, item).doubleValue() + ONE_DAY / 2;
            }

            public Number getStartY(int series, int item) {
                return new Double(0.0);
            }

            public Number getEndY(int series, int item) {
                return priceDataset.getVolume(series, item);
            }
        };
    }

  public boolean addChartPlot(CombinedDomainXYPlot plot, AbstractIndicatorInput inpt) {
    if (!(plot instanceof CombinedDomainXYPlot)) {
      return false;
    }
    setDomainCrosshairVisible(true);
    setDomainCrosshairLockedOnData(false);
    setRangeCrosshairVisible(true);
    setRangeCrosshairLockedOnData(false);
    setDomainGridlinesVisible(false);
    setRangeGridlinesVisible(true);
    plot.add(this);
    return true;
  }
}