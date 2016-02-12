/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.smchart.indicators;

/**
 *
 * @author venkatasiva
 */
public class ParabolicSARInput extends AbstractIndicatorInput {

    private double m_initialValue;
    private double m_step;
    private double m_maximum;

    public ParabolicSARInput(double iv, double step, double max) {
        m_initialValue = iv;
        m_step = step;
        m_maximum = max;
    }

    public double getInitialValue() {
        return m_initialValue;
    }

    public double getStepValue() {
        return m_step;
    }

    public double getMaximumValue() {
        return m_maximum;
    }
}
