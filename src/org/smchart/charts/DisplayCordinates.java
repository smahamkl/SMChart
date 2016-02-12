 package org.smchart.charts;
 
 import java.awt.Color;
 import java.awt.Font;
 import java.awt.Point;
 import java.awt.geom.Point2D;
 //import java.awt.geom.Point2D.Double;
 import java.awt.geom.Rectangle2D;
 import java.io.Serializable;
 import java.text.DecimalFormat;
 import java.text.SimpleDateFormat;
 import java.util.ArrayList;
 import java.util.Date;
 import java.util.GregorianCalendar;
 import java.util.Hashtable;
 import java.util.List;
 import org.jfree.chart.ChartPanel;
 import org.jfree.chart.ChartRenderingInfo;
 import org.jfree.chart.JFreeChart;
 import org.jfree.chart.annotations.AbstractXYAnnotation;
 import org.jfree.chart.annotations.XYTextAnnotation;
 import org.jfree.chart.axis.ValueAxis;
 import org.jfree.chart.plot.CombinedDomainXYPlot;
 import org.jfree.chart.plot.PlotRenderingInfo;
 import org.jfree.chart.plot.XYPlot;
 import org.jfree.data.xy.OHLCDataItem;
 import org.jfree.ui.RectangleEdge;
 
 public class DisplayCordinates
   implements Serializable
 {
   private int m_iX;
   private int m_iY;
   private double m_dX;
   private double m_dY;
   private double m_dXX;
   private double m_dYY;
   private ChartPanel m_chartPanel;
   private XYTextAnnotation m_txtAnnotation;
   private int subPlotIndex;
   private String m_ticker;
   private OHLCDataItem[] m_OHLCData = null;
   private Hashtable m_dataHash = null;
 
   public DisplayCordinates(ChartPanel panel, String inTicker, OHLCDataItem[] inDataItemArr)
   {
/*  47 */     this.m_chartPanel = panel;
/*  48 */     this.m_OHLCData = inDataItemArr;
/*  49 */     this.m_ticker = inTicker;
/*  50 */     this.m_dataHash = new Hashtable();
 
/*  52 */     SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
     try {
/*  54 */       for (int i = 0; i < this.m_OHLCData.length; i++) {
/*  55 */         String s = "Open:" + this.m_OHLCData[i].getOpen() + ";";
/*  56 */         s = s + "High:" + this.m_OHLCData[i].getHigh() + ";";
/*  57 */         s = s + "Low:" + this.m_OHLCData[i].getLow() + ";";
/*  58 */         s = s + "Close:" + this.m_OHLCData[i].getClose() + ";";
/*  59 */         this.m_dataHash.put(df.format(this.m_OHLCData[i].getDate()).toString(), s);
       }
     }
     catch (Exception ex) {
     }
/*  64 */     if (this.m_txtAnnotation == null) {
/*  65 */       this.m_txtAnnotation = new XYTextAnnotation("", -1.0D, -1.0D);
/*  66 */       this.m_txtAnnotation.setFont(new Font("SansSerif", 0, 10));
/*  67 */       this.m_txtAnnotation.setPaint(Color.BLUE);
     }
   }
 
   private void drawRTInfo() {
     try {
/*  73 */       Rectangle2D screenDataArea = this.m_chartPanel.getScreenDataArea(this.m_iX, this.m_iY);
/*  74 */       if (screenDataArea == null) {
/*  75 */         return;
       }
 
/*  78 */       Point2D java2DPoint = this.m_chartPanel.translateScreenToJava2D(new Point(this.m_iX, this.m_iY));
/*  79 */       ChartRenderingInfo chartInfo = this.m_chartPanel.getChartRenderingInfo();
/*  80 */       PlotRenderingInfo plotInfo = chartInfo.getPlotInfo();
 
/*  82 */       this.subPlotIndex = plotInfo.getSubplotIndex(java2DPoint);
/*  83 */       if (this.subPlotIndex != 0) {
/*  84 */         return;
       }
 
/*  87 */       int iDAMinX = (int)screenDataArea.getMinX();
/*  88 */       int iDAMinY = (int)screenDataArea.getMinY();
/*  89 */       Point2D p2dXY = this.m_chartPanel.translateScreenToJava2D(new Point(this.m_iX, this.m_iY));
 
/*  91 */       XYPlot plot = (CombinedDomainXYPlot)this.m_chartPanel.getChart().getPlot();
 
/*  93 */       ValueAxis domainAxis = plot.getDomainAxis();
/*  94 */       ValueAxis rangeAxis = plot.getRangeAxis();
/*  95 */       RectangleEdge domainAxisEdge = plot.getDomainAxisEdge();
/*  96 */       RectangleEdge rangeAxisEdge = plot.getRangeAxisEdge();
 
/*  98 */       if ((plot instanceof CombinedDomainXYPlot)) {
/*  99 */         CombinedDomainXYPlot combineddomainxyplot = (CombinedDomainXYPlot)plot;
/* 100 */         plot = combineddomainxyplot.findSubplot(this.m_chartPanel.getChartRenderingInfo().getPlotInfo(), p2dXY);
/* 101 */         if (plot != null) {
/* 102 */           domainAxis = plot.getDomainAxis();
/* 103 */           rangeAxis = plot.getRangeAxis();
/* 104 */           domainAxisEdge = plot.getDomainAxisEdge();
/* 105 */           rangeAxisEdge = plot.getRangeAxisEdge();
         } else {
/* 107 */           return;
         }
       }
 
/* 111 */       double dXX = domainAxis.java2DToValue(this.m_dX, screenDataArea, domainAxisEdge);
/* 112 */       double dYY = rangeAxis.java2DToValue(this.m_dY, screenDataArea, rangeAxisEdge);
/* 113 */       this.m_dXX = dXX;
/* 114 */       this.m_dYY = dYY;
 
/* 116 */       ArrayList alInfo = getInfo(rangeAxis.getLabel());
 
/* 118 */       if (alInfo.size() == 0) {
/* 119 */         return;
       }
/* 121 */       int iLenInfo = alInfo.size();
 
/* 123 */       StringBuilder sb = new StringBuilder();
/* 124 */       sb.append(this.m_ticker + ";");
/* 125 */       for (int i = iLenInfo - 1; i >= 0; i--) {
/* 126 */         sb.append((String)alInfo.get(i) + " ");
       }
/* 128 */       removeAnnotation(plot, getM_txtAnnotation());
/* 129 */       Point2D p = mousePointToChartPoint(plot, new Point(iDAMinX * 5, iDAMinY + 14));
/* 130 */       getM_txtAnnotation().setText(sb.toString());
/* 131 */       getM_txtAnnotation().setX(p.getX());
/* 132 */       getM_txtAnnotation().setY(p.getY());
/* 133 */       addAnnotation(plot, getM_txtAnnotation());
/* 134 */       sb = null;
     } catch (Exception ex) {
/* 136 */       ex.printStackTrace();
     }
   }
 
   private ArrayList<String> getInfo(String rangeAxisLabel) {
        ArrayList<String> alV = new ArrayList<String>();
        try {
            DecimalFormat dfT = new DecimalFormat("00");
            GregorianCalendar gc = new GregorianCalendar();

            String[] asT = new String[]{"Date", rangeAxisLabel};

            long lDte = (long) this.m_dXX;
            Date dtXX = new Date(lDte);
            gc.setTime(dtXX);
            String sMM = dfT.format(Double.valueOf(String.valueOf(gc.get(GregorianCalendar.MONTH))) + 1);
            String sDD = dfT.format(Double.valueOf(String.valueOf(gc.get(GregorianCalendar.DATE))) + 1);
            String sYY = dfT.format(Double.valueOf(String.valueOf(gc.get(GregorianCalendar.YEAR))));
            String sV = sMM + "/" + sDD + "/" + sYY;

            if (m_dataHash.containsKey(sYY + "-" + sMM + "-" + sDD)) {
                alV.add(asT[0] + ":" + sV);
                sV = m_dataHash.get(sYY + "-" + sMM + "-" + sDD).toString();
                alV.add(sV);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return alV;
    }
 
   public void mouseMoved_e(Point p) {
/* 169 */     int iX = p.x; int iY = p.y;
/* 170 */     this.m_iX = iX;
/* 171 */     this.m_iY = iY;
/* 172 */     this.m_dX = iX;
/* 173 */     this.m_dY = iY;
/* 174 */     drawRTInfo();
   }
 
   private void addAnnotation(XYPlot plot, AbstractXYAnnotation ann) {
/* 178 */     boolean annFound = false;
 
/* 184 */     if (!annFound)
/* 185 */       plot.addAnnotation(ann);
   }
 
   private void removeAnnotation(XYPlot plot, AbstractXYAnnotation ann)
   {
/* 190 */     for (int i = 0; i < plot.getAnnotations().size(); i++)
/* 191 */       if (plot.getAnnotations().get(i).equals(ann))
/* 192 */         plot.removeAnnotation(ann);
   }
 
   private Point2D mousePointToChartPoint(XYPlot plot, Point p)
   {
/* 198 */     double xx = 0.0D; double yy = 0.0D;
     try {
/* 200 */       ChartRenderingInfo chartInfo = this.m_chartPanel.getChartRenderingInfo();
/* 201 */       Point2D java2DPoint = this.m_chartPanel.translateScreenToJava2D(p);
 
/* 203 */       xx = ((CombinedDomainXYPlot)this.m_chartPanel.getChart().getPlot()).getDomainAxis().java2DToValue(java2DPoint.getX(), chartInfo.getPlotInfo().getDataArea(), ((CombinedDomainXYPlot)this.m_chartPanel.getChart().getPlot()).getDomainAxisEdge());
 
/* 207 */       Rectangle2D panelArea = this.m_chartPanel.getScreenDataArea();
 
/* 209 */       yy = plot.getRangeAxis().java2DToValue(p.y, panelArea, plot.getRangeAxisEdge());
     }
     catch (Exception ex)
     {
/* 213 */       return null;
     }
/* 215 */     return new Point2D.Double(xx, yy);
   }
 
   public Hashtable getM_dataHash()
   {
      return this.m_dataHash;
   }
 
   public void setM_dataHash(Hashtable m_dataHash)
   {
      this.m_dataHash = m_dataHash;
   }
 
   public XYTextAnnotation getM_txtAnnotation()
   {
       return this.m_txtAnnotation;
   }
 
   public void setM_txtAnnotation(XYTextAnnotation m_txtAnnotation)
   {
      this.m_txtAnnotation = m_txtAnnotation;
   }
 }