/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.smchart.indicators;

/**
 *
 * @author siva
 */
public class EMAInput extends AbstractIndicatorInput {

    private int[] m_movingAvgArr;

    public EMAInput(int[] maArr) {
        m_movingAvgArr = maArr;
    }

    public int[] getMovingAvgArr() {
        return m_movingAvgArr;
    }
}
