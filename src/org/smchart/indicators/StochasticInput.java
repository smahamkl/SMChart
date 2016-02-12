/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.smchart.indicators;

/**
 *
 * @author siva
 */
public class StochasticInput extends AbstractIndicatorInput {

    private int m_movingAvg;
    private int m_timePeriod;

    public StochasticInput(int ma, int timePeriod) {
        m_movingAvg = ma;
        m_timePeriod = timePeriod;
    }

    public int getTimePeriod() {
        return m_timePeriod;
    }

    public int getMovingAvg() {
        return m_movingAvg;
    }
}
