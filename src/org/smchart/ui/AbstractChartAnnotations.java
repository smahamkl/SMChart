 package org.smchart.ui;
 
 import java.awt.Point;
 import java.awt.geom.Point2D;
 import java.awt.geom.Point2D.Double;
 import java.awt.geom.Rectangle2D;
 import java.io.Serializable;
 import java.util.ArrayList;
 import java.util.List;
 import org.jfree.chart.ChartPanel;
 import org.jfree.chart.ChartRenderingInfo;
 import org.jfree.chart.annotations.AbstractXYAnnotation;
 import org.jfree.chart.axis.ValueAxis;
 import org.jfree.chart.plot.CombinedDomainXYPlot;
 import org.jfree.chart.plot.PlotRenderingInfo;
 import org.jfree.chart.plot.XYPlot;
 import org.smchart.charts.SMChartPanel;
 
 public abstract class AbstractChartAnnotations
   implements Serializable
 {
   protected SMChartPanel m_chartPanel;
   protected CombinedDomainXYPlot m_cdPlot;
   protected XYPlot tlSubplot;
   protected int tlSubplotIndex;
   protected ArrayList<Point2D> curPointList;
/*  31 */   protected double width = 0.0D;
/*  32 */   protected double height = 0.0D;
   protected Rectangle2D.Double[] rects;
 
   public AbstractChartAnnotations()
   {
/*  36 */     this.curPointList = new ArrayList();
/*  37 */     this.m_cdPlot = this.m_chartPanel.getCombinedDomainPlot();
/*  38 */     ActivateRects();
   }
 
   public AbstractChartAnnotations(ChartPanel inPanel) {
/*  42 */     this.m_chartPanel = ((SMChartPanel)inPanel);
/*  43 */     this.curPointList = new ArrayList();
/*  44 */     this.m_cdPlot = this.m_chartPanel.getCombinedDomainPlot();
/*  45 */     ActivateRects();
   }
 
   private void ActivateRects() {
/*  49 */     if (this.rects == null) {
/*  50 */       this.rects = new Rectangle2D.Double[3];
     }
 
/*  53 */     if (this.rects[0] == null) {
/*  54 */       this.rects[0] = new Rectangle2D.Double();
     }
 
/*  57 */     if (this.rects[1] == null) {
/*  58 */       this.rects[1] = new Rectangle2D.Double();
     }
 
/*  61 */     if (this.rects[2] == null)
/*  62 */       this.rects[2] = new Rectangle2D.Double();
   }
 
   protected PlotRenderingInfo getPlotRenderingInfo()
   {
/*  67 */     ChartRenderingInfo chartInfo = getM_chartPanel().getChartRenderingInfo();
/*  68 */     PlotRenderingInfo plotInfo = chartInfo.getPlotInfo();
/*  69 */     return plotInfo;
   }
 
   protected int getSubplotIndex(Point p) {
/*  73 */     Point2D java2DPoint = getM_chartPanel().translateScreenToJava2D(p);
/*  74 */     ChartRenderingInfo chartInfo = getM_chartPanel().getChartRenderingInfo();
/*  75 */     PlotRenderingInfo plotInfo = chartInfo.getPlotInfo();
/*  76 */     return plotInfo.getSubplotIndex(java2DPoint);
   }
 
   protected XYPlot getSubplot(Point p) {
/*  80 */     Point2D java2DPoint = getM_chartPanel().translateScreenToJava2D(p);
/*  81 */     ChartRenderingInfo chartInfo = getM_chartPanel().getChartRenderingInfo();
/*  82 */     PlotRenderingInfo plotInfo = chartInfo.getPlotInfo();
/*  83 */     return getM_cdPlot().findSubplot(plotInfo, java2DPoint);
   }
 
   public Point2D mousePointToChartPoint(Point p) {
/*  87 */     double xx = 0.0D; double yy = 0.0D;
     try {
/*  89 */       ChartRenderingInfo chartInfo = getM_chartPanel().getChartRenderingInfo();
/*  90 */       Point2D java2DPoint = getM_chartPanel().translateScreenToJava2D(p);
 
/*  92 */       xx = getM_cdPlot().getDomainAxis().java2DToValue(java2DPoint.getX(), chartInfo.getPlotInfo().getDataArea(), getM_cdPlot().getDomainAxisEdge());
 
/*  96 */       Rectangle2D panelArea = getM_chartPanel().getScreenDataArea(p.x, p.y);
/*  97 */       if (panelArea == null) {
/*  98 */         panelArea = getM_chartPanel().getScreenDataArea();
       }
 
/* 103 */       yy = getTlSubplot().getRangeAxis().java2DToValue(p.y, panelArea, getTlSubplot().getRangeAxisEdge());
     }
     catch (Exception ex)
     {
/* 107 */       return null;
     }
/* 109 */     return new Point2D.Double(xx, yy);
   }
 
   protected boolean isValidChartPoint(Point p) {
/* 113 */     if (getTlSubplotIndex() < 0)
     {
/* 115 */       return false;
     }
/* 117 */     return true;
   }
 
   public int getTotalPoints() {
/* 121 */     return getCurPointList().size();
   }
 
   protected boolean isInsideSubPlot(Point p) {
/* 125 */     if (getTlSubplotIndex() != getSubplotIndex(p)) {
/* 126 */       return false;
     }
/* 128 */     return true;
   }
 
  protected void setRectWidthHeight(Point p) {
    Rectangle2D screenDataArea = getM_chartPanel().getScreenDataArea(p.x, p.y);
    if (screenDataArea == null) {
      return;
    }
    int iDAMaxX = (int)screenDataArea.getMaxX();
    int iDAMinX = (int)screenDataArea.getMinX();
    int iDAMaxY = (int)screenDataArea.getMaxY();
    int iDAMinY = (int)screenDataArea.getMinY();

    Point2D topLeftPoint2D = mousePointToChartPoint(new Point(iDAMinX, iDAMaxY));
    Point2D bottomRightPoint2D = mousePointToChartPoint(new Point(iDAMaxX, iDAMinY));
    Point2D topRightPoint2D = mousePointToChartPoint(new Point(iDAMaxX, iDAMaxY));

    setWidth(Math.abs(topRightPoint2D.getX() - topLeftPoint2D.getX()) / 50.0D);
    setHeight(Math.abs(topRightPoint2D.getY() - bottomRightPoint2D.getY()) / 50.0D);
  }
 
   protected void removeMyAnnotation(AbstractXYAnnotation ann) {
     try {
/* 153 */       if (this.tlSubplot != null) {
/* 154 */         for (int i = 0; i < getTlSubplot().getAnnotations().size(); i++)
/* 155 */           if (getTlSubplot().getAnnotations().get(i).equals(ann))
/* 156 */             getTlSubplot().removeAnnotation(ann);
       }
     }
     catch (Exception ex)
     {
/* 161 */       ex.printStackTrace();
     } finally {
/* 163 */       ann = null;
     }
   }
 
   protected void addMyAnnotation(AbstractXYAnnotation ann) {
     try {
/* 169 */       boolean annFound = false;
/* 170 */       for (int i = 0; i < this.tlSubplot.getAnnotations().size(); i++) {
/* 171 */         if (this.tlSubplot.getAnnotations().get(i).equals(ann)) {
/* 172 */           annFound = true;
         }
       }
/* 175 */       if (!annFound)
/* 176 */         this.tlSubplot.addAnnotation(ann);
     }
     catch (Exception ex) {
/* 179 */       ex.printStackTrace();
     } finally {
/* 181 */       ann = null;
     }
   }
 
   public abstract void EventUpdate();
 
   public SMChartPanel getM_chartPanel()
   {
/* 191 */     return this.m_chartPanel;
   }
 
   public void setM_chartPanel(SMChartPanel m_chartPanel)
   {
/* 198 */     this.m_chartPanel = m_chartPanel;
   }
 
   public CombinedDomainXYPlot getM_cdPlot()
   {
/* 205 */     return this.m_cdPlot;
   }
 
   public void setM_cdPlot(CombinedDomainXYPlot m_cdPlot)
   {
/* 212 */     this.m_cdPlot = m_cdPlot;
   }
 
   public XYPlot getTlSubplot()
   {
/* 219 */     return this.tlSubplot;
   }
 
   public void setTlSubplot(XYPlot tlSubplot)
   {
/* 226 */     this.tlSubplot = tlSubplot;
   }
 
   public int getTlSubplotIndex()
   {
/* 233 */     return this.tlSubplotIndex;
   }
 
   public void setTlSubplotIndex(int tlSubplotIndex)
   {
/* 240 */     this.tlSubplotIndex = tlSubplotIndex;
   }
 
   public ArrayList<Point2D> getCurPointList()
   {
/* 247 */     return this.curPointList;
   }
 
   public void setCurPointList(ArrayList<Point2D> curPointList)
   {
/* 254 */     this.curPointList = curPointList;
   }
 
   public double getWidth()
   {
/* 261 */     return this.width;
   }
 
   public void setWidth(double width)
   {
/* 268 */     this.width = width;
   }
 
   public double getHeight()
   {
/* 275 */     return this.height;
   }
 
   public void setHeight(double height)
   {
/* 282 */     this.height = height;
   }
 
   public Rectangle2D.Double[] getRects()
   {
/* 289 */     return this.rects;
   }
 
   public void setRects(Rectangle2D.Double[] rects)
   {
/* 296 */     this.rects = rects;
   }
 }