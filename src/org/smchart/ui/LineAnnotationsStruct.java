/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.smchart.ui;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import org.smchart.ui.UserAnnotations;

/**
 *
 * @author siva
 */
public class LineAnnotationsStruct {

    private Line2D.Double line = null;
    private UserAnnotations.UserDrawingShape m_shape;
    private ArcAnnotationsStrust m_arcAnnStructObj = null;

    public LineAnnotationsStruct(Line2D.Double l, UserAnnotations.UserDrawingShape inShape) {
        line = l;
        m_shape = inShape;
        if(m_shape == UserAnnotations.UserDrawingShape.FINBONACCIARCS)
        {
          m_arcAnnStructObj = new  ArcAnnotationsStrust();
        }
    }

    public Line2D.Double getLine() {
        return line;
    }

    public boolean IsPointIntersectLine(Point2D p, double width, double height) {
        if (line.intersects(p.getX(), p.getY(), width, height)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * @return the m_shape
     */
    public UserAnnotations.UserDrawingShape getShape() {
        return m_shape;
    }

    /**
     * @param m_shape the m_shape to set
     */
    public void setShape(UserAnnotations.UserDrawingShape m_shape) {
        this.m_shape = m_shape;
    }

    /**
     * @return the m_arcAnnStructObj
     */
    public ArcAnnotationsStrust getM_arcAnnStructObj() {
        return m_arcAnnStructObj;
    }


    public class ArcAnnotationsStrust {

        private double m_arcRadius;
        private double m_ArcRadiusOnChart;

        /**
         * @return the m_arcRadius
         */
        public double getM_arcRadius() {
            return m_arcRadius;
        }

        /**
         * @param m_arcRadius the m_arcRadius to set
         */
        public void setM_arcRadius(double m_arcRadius) {
            this.m_arcRadius = m_arcRadius;
        }

        /**
         * @return the m_ArcRadiusOnChart
         */
        public double getM_ArcRadiusOnChart() {
            return m_ArcRadiusOnChart;
        }

        /**
         * @param m_ArcRadiusOnChart the m_ArcRadiusOnChart to set
         */
        public void setM_ArcRadiusOnChart(double m_ArcRadiusOnChart) {
            this.m_ArcRadiusOnChart = m_ArcRadiusOnChart;
        }
    }
}
