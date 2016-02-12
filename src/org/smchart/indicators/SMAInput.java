/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.smchart.indicators;

/**
 *
 * @author siva
 */
public class SMAInput extends AbstractIndicatorInput {

    private int m_movingAvg1;
    private int m_movingAvg2;
    private int m_movingAvg3;

    public SMAInput(int ma1, int ma2, int ma3) {
        m_movingAvg1 = ma1;
        m_movingAvg2 = ma2;
        m_movingAvg3 = ma3;
    }
     public SMAInput(int[] maArr) {
        m_movingAvg1 = maArr[0];
        m_movingAvg2 = maArr[1];
        m_movingAvg3 = maArr[2];
    }

    public int getMovingAvg1() {
        return m_movingAvg1;
    }

    public int getMovingAvg2() {
        return m_movingAvg2;
    }

    public int getMovingAvg3() {
        return m_movingAvg3;
    }
    public int[] getMovingAvgArray()
    {
        return new int[]{m_movingAvg1, m_movingAvg2, m_movingAvg3};
    }
}
