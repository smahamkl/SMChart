 package org.smchart.ui;
 
 import java.awt.BasicStroke;
 import java.awt.Color;
 import java.awt.geom.Line2D;
 //import java.awt.geom.Line2D.Double;
 import java.awt.geom.Point2D;
 import java.awt.geom.Rectangle2D;
 import java.text.DecimalFormat;
 import org.jfree.chart.annotations.AbstractXYAnnotation;
 import org.jfree.chart.annotations.XYTextAnnotation;
 import org.jfree.chart.plot.Plot;
 import org.jfree.chart.plot.PlotOrientation;
 import org.jfree.chart.plot.XYPlot;
 import org.jfree.ui.RectangleEdge;
 
 public class FibonacciArcsAnnotations
 {
/*  26 */   static final double[] RET_PROPORTIONS = { 0.382D, 0.5D, 0.618D, 0.763D };
   static final double SQRT_TWO = 1.414213562373095D;
 
   public static void drawArcs(Line2D.Double mainLine, Rectangle2D plotArea, XYPlot inPlot)
   {
/*  30 */     XYTextAnnotation txtAnn = null;
/*  31 */     Point2D startP = mainLine.getP1();
/*  32 */     Point2D endP = mainLine.getP2();
 
/*  36 */     double lineHeight = getArcRadius(mainLine, plotArea, inPlot);
 
/*  38 */     BasicStroke s = new BasicStroke(1.0F, 1, 1, 1.0F, new float[] { 2.0F }, 0.0F);
 
/*  40 */     DecimalFormat myFormatter = new DecimalFormat("###.##");
 
/*  42 */     if (startP.getY() > endP.getY())
     {
/*  44 */       for (int i = 0; i < RET_PROPORTIONS.length; i++) {
/*  45 */         XYArcAnnotation xCAnn = new XYArcAnnotation(startP.getX(), startP.getY(), lineHeight * RET_PROPORTIONS[i], s, Color.blue);
/*  46 */         addAnnotation(xCAnn, inPlot);
 
/*  48 */         double dist = startP.getY() - RET_PROPORTIONS[i] * getArcChartRadius(mainLine, plotArea, inPlot);
/*  49 */         txtAnn = new XYTextAnnotation("" + myFormatter.format(RET_PROPORTIONS[i] * 100.0D) + "%", startP.getX(), dist);
/*  50 */         addAnnotation(txtAnn, inPlot);
       }
     }
/*  53 */     else for (int i = 0; i < RET_PROPORTIONS.length; i++)
       {
/*  55 */         XYArcAnnotation xCAnn = new XYArcAnnotation(endP.getX(), endP.getY(), lineHeight * RET_PROPORTIONS[i], s, Color.blue);
/*  56 */         addAnnotation(xCAnn, inPlot);
 
/*  58 */         double dist = endP.getY() - RET_PROPORTIONS[i] * getArcChartRadius(mainLine, plotArea, inPlot);
/*  59 */         txtAnn = new XYTextAnnotation("" + myFormatter.format(RET_PROPORTIONS[i] * 100.0D) + "%", endP.getX(), dist);
/*  60 */         addAnnotation(txtAnn, inPlot);
       }
   }
 
   public static double getArcRadius(Line2D.Double mainLine, Rectangle2D plotArea, XYPlot inPlot)
   {
/*  67 */     double perpendHt = Math.abs(getJava2DPriceValue(inPlot, plotArea, mainLine.getY1()) - getJava2DPriceValue(inPlot, plotArea, mainLine.getY2()));
 
/*  69 */     double horizHt = Math.abs(getJava2DDateValue(inPlot, plotArea, mainLine.getX1()) - getJava2DDateValue(inPlot, plotArea, mainLine.getX2()));
 
/*  71 */     double result = Math.sqrt(perpendHt * perpendHt + horizHt * horizHt);
/*  72 */     return result;
   }
 
public static double getArcChartRadius(Line2D mainLine, Rectangle2D plotArea, XYPlot inPlot)
  {
    double perpendHt = Math.abs(mainLine.getY1() - mainLine.getY2());
    double perpendHtJava2D = Math.abs(getJava2DPriceValue(inPlot, plotArea, mainLine.getY1()) - getJava2DPriceValue(inPlot, plotArea, mainLine.getY2()));

    double horizHt = Math.abs(getJava2DDateValue(inPlot, plotArea, mainLine.getX1()) - getJava2DDateValue(inPlot, plotArea, mainLine.getX2()));

    double actHorizHt = perpendHt / perpendHtJava2D * horizHt;
    double result = Math.sqrt(perpendHt * perpendHt + actHorizHt * actHorizHt);
    return result;
  }
 
   public static void RemoveArcs(Line2D.Double mainLine, double radius, double chartRadius, XYPlot inPlot)
   {
/*  90 */     DecimalFormat myFormatter = new DecimalFormat("###.##");
/*  91 */     XYTextAnnotation txtAnn = null;
/*  92 */     Point2D startP = mainLine.getP1();
/*  93 */     Point2D endP = mainLine.getP2();
/*  94 */     BasicStroke s = new BasicStroke(1.0F, 1, 1, 1.0F, new float[] { 2.0F }, 0.0F);
 
/*  96 */     if (startP.getY() > endP.getY())
     {
/*  98 */       for (int i = 0; i < RET_PROPORTIONS.length; i++) {
/*  99 */         XYArcAnnotation xCAnn = new XYArcAnnotation(startP.getX(), startP.getY(), radius * RET_PROPORTIONS[i], s, Color.blue);
/* 100 */         removeAnnotation(xCAnn, inPlot);
 
/* 102 */         double dist = startP.getY() - RET_PROPORTIONS[i] * chartRadius;
/* 103 */         txtAnn = new XYTextAnnotation("" + myFormatter.format(RET_PROPORTIONS[i] * 100.0D) + "%", startP.getX(), dist);
/* 104 */         removeAnnotation(txtAnn, inPlot);
       }
     }
/* 107 */     else for (int i = 0; i < RET_PROPORTIONS.length; i++) {
/* 108 */         XYArcAnnotation xCAnn = new XYArcAnnotation(endP.getX(), endP.getY(), radius * RET_PROPORTIONS[i], s, Color.blue);
/* 109 */         removeAnnotation(xCAnn, inPlot);
 
/* 111 */         double dist = endP.getY() - RET_PROPORTIONS[i] * chartRadius;
/* 112 */         txtAnn = new XYTextAnnotation("" + myFormatter.format(RET_PROPORTIONS[i] * 100.0D) + "%", endP.getX(), dist);
/* 113 */         removeAnnotation(txtAnn, inPlot);
       }
   }
 
   public static void clearArcAnnotations(Line2D.Double mainLine, Rectangle2D plotArea, XYPlot inPlot)
   {
/* 119 */     double lineHeight = getArcRadius(mainLine, plotArea, inPlot);
/* 120 */     double chartRadius = getArcChartRadius(mainLine, plotArea, inPlot);
/* 121 */     RemoveArcs(mainLine, lineHeight, chartRadius, inPlot);
   }
 
   private static void removeAnnotation(AbstractXYAnnotation ann, XYPlot tlSubplot) {
/* 125 */     if (tlSubplot != null)
/* 126 */       for (int i = 0; i < tlSubplot.getAnnotations().size(); i++)
/* 127 */         if (tlSubplot.getAnnotations().get(i).equals(ann))
/* 128 */           tlSubplot.removeAnnotation(ann);
   }
 
   private static void addAnnotation(AbstractXYAnnotation ann, XYPlot tlSubplot)
   {
/* 135 */     boolean annFound = false;
/* 136 */     for (int i = 0; i < tlSubplot.getAnnotations().size(); i++) {
/* 137 */       if (tlSubplot.getAnnotations().get(i).equals(ann)) {
/* 138 */         annFound = true;
       }
     }
/* 141 */     if (!annFound)
/* 142 */       tlSubplot.addAnnotation(ann);
   }
 
   protected static double getJava2DPriceValue(XYPlot plot, Rectangle2D plotArea, double chartYVal)
   {
/* 147 */     double val = 0.0D;
/* 148 */     if (plot != null) {
/* 149 */       PlotOrientation orientation = plot.getOrientation();
/* 150 */       RectangleEdge rangeEdge = Plot.resolveRangeAxisLocation(plot.getRangeAxisLocation(), orientation);
/* 151 */       RectangleEdge domainEdge = Plot.resolveRangeAxisLocation(plot.getDomainAxisLocation(), orientation);
/* 152 */       val = plot.getRangeAxis().valueToJava2D(chartYVal, plotArea, rangeEdge);
/* 153 */       if (orientation == PlotOrientation.HORIZONTAL) {
/* 154 */         val = plot.getDomainAxis().valueToJava2D(chartYVal, plotArea, domainEdge);
       }
     }
/* 157 */     return val;
   }
   protected static double getJava2DDateValue(XYPlot plot, Rectangle2D plotArea, double chartXVal) {
/* 160 */     double val = 0.0D;
/* 161 */     if (plot != null) {
/* 162 */       PlotOrientation orientation = plot.getOrientation();
/* 163 */       RectangleEdge rangeEdge = Plot.resolveRangeAxisLocation(plot.getRangeAxisLocation(), orientation);
/* 164 */       RectangleEdge domainEdge = Plot.resolveRangeAxisLocation(plot.getDomainAxisLocation(), orientation);
/* 165 */       val = plot.getDomainAxis().valueToJava2D(chartXVal, plotArea, rangeEdge);
/* 166 */       if (orientation == PlotOrientation.HORIZONTAL) {
/* 167 */         val = plot.getDomainAxis().valueToJava2D(chartXVal, plotArea, domainEdge);
       }
     }
/* 170 */     return val;
   }
 }