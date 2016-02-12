package org.smchart.ui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.geom.Line2D;
//import java.awt.geom.Line2D.Double;
import java.awt.geom.Point2D;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.List;
import org.jfree.chart.annotations.AbstractXYAnnotation;
import org.jfree.chart.annotations.XYLineAnnotation;
import org.jfree.chart.annotations.XYShapeAnnotation;
import org.jfree.chart.annotations.XYTextAnnotation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.time.Day;

public abstract class FiboRetracementAnnotations
{
  static final double[] RET_PROPORTIONS = { 0.0D, 1.0D, 0.236D, 0.5D, 0.382D, 0.618D, 1.236D, 1.618D, 2.618D };

  public static void drawFRetracementLines(Line2D.Double mainLine, XYPlot inPlot) {
    XYTextAnnotation txtAnn = null;
    Point2D startP = mainLine.getP1();
    Point2D endP = mainLine.getP2();

    removeAnnotation(new XYShapeAnnotation(mainLine), inPlot);

    Calendar cal = Calendar.getInstance();
    int year = cal.get(1);
    int month = cal.get(2);

    double lineHeight = Math.abs(startP.getY() - endP.getY());
    BasicStroke s = new BasicStroke(1.0F, 1, 1, 1.0F, new float[] { 2.0F }, 0.0F);
    double maxXX;
    double minXX;
   
    if (startP.getX() > endP.getX()) {
       minXX = endP.getX();
      maxXX = new Day(29, month + 1, year).getMiddleMillisecond();
    } else {
      minXX = startP.getX();
      maxXX = new Day(29, month + 1, year).getMiddleMillisecond();
    }

    double annXX = minXX;

    double Ret38YY = startP.getY();
    DecimalFormat myFormatter = new DecimalFormat("###.##");
    if (startP.getY() > endP.getY())
    {
      for (int i = 0; i < RET_PROPORTIONS.length; i++) {
        addAnnotation(new XYLineAnnotation(minXX, Ret38YY - lineHeight * RET_PROPORTIONS[i], maxXX, Ret38YY - lineHeight * RET_PROPORTIONS[i], s, Color.BLUE), inPlot);

        txtAnn = new XYTextAnnotation("" + myFormatter.format(RET_PROPORTIONS[i] * 100.0D) + "%", annXX, Ret38YY - lineHeight * RET_PROPORTIONS[i]);

        addAnnotation(txtAnn, inPlot);
      }
    }
    else for (int i = 0; i < RET_PROPORTIONS.length; i++) {
        addAnnotation(new XYLineAnnotation(minXX, Ret38YY + lineHeight * RET_PROPORTIONS[i], maxXX, Ret38YY + lineHeight * RET_PROPORTIONS[i], s, Color.BLUE), inPlot);

        txtAnn = new XYTextAnnotation("" + myFormatter.format(RET_PROPORTIONS[i] * 100.0D) + "%", annXX, Ret38YY + lineHeight * RET_PROPORTIONS[i]);

        addAnnotation(txtAnn, inPlot);
      }
  }

  public static void clearFibonacciRetLines(Line2D.Double mainLine, XYPlot inPlot)
  {
    XYTextAnnotation txtAnn = null;
    Calendar cal = Calendar.getInstance();
    int year = cal.get(1);
    int month = cal.get(2);

    Point2D startP = mainLine.getP1();
    Point2D endP = mainLine.getP2();
    double maxXX;
    double minXX;
    
    if (startP.getX() > endP.getX()) {
      minXX = endP.getX();
      maxXX = new Day(29, month + 1, year).getMiddleMillisecond();
    } else {
      minXX = startP.getX();
      maxXX = new Day(29, month + 1, year).getMiddleMillisecond();
    }
    double annXX = minXX;

    double lineHeight = Math.abs(startP.getY() - endP.getY());
    BasicStroke s = new BasicStroke(1.0F, 1, 1, 1.0F, new float[] { 2.0F }, 0.0F);

    double Ret38YY = startP.getY();
    DecimalFormat myFormatter = new DecimalFormat("###.##");
    if (startP.getY() > endP.getY())
    {
      for (int i = 0; i < RET_PROPORTIONS.length; i++) {
        removeAnnotation(new XYLineAnnotation(minXX, Ret38YY - lineHeight * RET_PROPORTIONS[i], maxXX, Ret38YY - lineHeight * RET_PROPORTIONS[i], s, Color.BLUE), inPlot);

        txtAnn = new XYTextAnnotation("" + myFormatter.format(RET_PROPORTIONS[i] * 100.0D) + "%", annXX, Ret38YY - lineHeight * RET_PROPORTIONS[i]);

        removeAnnotation(txtAnn, inPlot);
      }
    }
    else for (int i = 0; i < RET_PROPORTIONS.length; i++) {
        removeAnnotation(new XYLineAnnotation(minXX, Ret38YY + lineHeight * RET_PROPORTIONS[i], maxXX, Ret38YY + lineHeight * RET_PROPORTIONS[i], s, Color.BLUE), inPlot);

        txtAnn = new XYTextAnnotation("" + myFormatter.format(RET_PROPORTIONS[i] * 100.0D) + "%", annXX, Ret38YY + lineHeight * RET_PROPORTIONS[i]);

        removeAnnotation(txtAnn, inPlot);
      }
  }

  private static void removeAnnotation(AbstractXYAnnotation ann, XYPlot tlSubplot)
  {
    if (tlSubplot != null)
      for (int i = 0; i < tlSubplot.getAnnotations().size(); i++)
        if (tlSubplot.getAnnotations().get(i).equals(ann))
          tlSubplot.removeAnnotation(ann);
  }

  private static void addAnnotation(AbstractXYAnnotation ann, XYPlot tlSubplot)
  {
    boolean annFound = false;
    for (int i = 0; i < tlSubplot.getAnnotations().size(); i++) {
      if (tlSubplot.getAnnotations().get(i).equals(ann)) {
        annFound = true;
      }
    }
    if (!annFound)
      tlSubplot.addAnnotation(ann);
  }
}