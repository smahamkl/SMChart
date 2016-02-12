/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.smchart.indicators;

/**
 *
 * @author siva
 */
public class RSIInput extends AbstractIndicatorInput {

    private int m_timePeriod;

    public RSIInput(int period) {
        m_timePeriod = period;
    }

    public int getTimePeriod() {
        return m_timePeriod;
    }
}
