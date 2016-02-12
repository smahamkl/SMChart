/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.smchart.indicators;

/**
 *
 * @author siva
 */
public class BollingerInput extends AbstractIndicatorInput {

    private int m_stdDev;
    private int m_movingAvg;

    public BollingerInput(int stdDev, int ma) {
        m_stdDev = stdDev;
        m_movingAvg = ma;
    }

    public int getMovingAverage() {
        return m_movingAvg;
    }

    public int getStdDev() {
        return m_stdDev;
    }
}
