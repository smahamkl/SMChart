/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.smchart.indicators;

/**
 *
 * @author siva
 */
public class MACDInput extends AbstractIndicatorInput {

    private int m_slowMA;
    private int m_fastMA;
    private int m_emaPeriod;

    public MACDInput(int slowMA, int fastMA, int emaPeriod) {
        m_slowMA = slowMA;
        m_fastMA = fastMA;
        m_emaPeriod = emaPeriod;
    }

    public int getSlowMA() {
        return m_slowMA;
    }

    public int getFastMA() {
        return m_fastMA;
    }

    public int getEMATimePeriod() {
        return m_emaPeriod;
    }
}
