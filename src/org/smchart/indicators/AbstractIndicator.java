/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.smchart.indicators;

import org.jfree.data.xy.OHLCDataItem;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.plot.CombinedDomainXYPlot;

/**
 *
 * @author siva
 */
public abstract class AbstractIndicator extends XYPlot{
    protected final OHLCDataItem[] m_priceDataSet;
    protected String m_ticker;
    protected AbstractIndicatorInput m_inptObj = null;

    public AbstractIndicator(final OHLCDataItem[] priceDataset, String ticker)
    {
        super();
        m_priceDataSet = priceDataset;
        m_ticker = ticker;
    }
    public abstract boolean addChartPlot(CombinedDomainXYPlot plot, AbstractIndicatorInput indParamObj);

    public XYPlot getSubPlot()
    {
        return this;
    }

    public AbstractIndicatorInput getChartInput()
    {
        return m_inptObj;
    }

}
