 package org.smchart.xml;
 
 import java.io.Serializable;
 import java.util.ArrayList;
 
 public class SMChartSettings
   implements Serializable
 {
   private ArrayList<LineStruct> m_annotationsList;
   private ArrayList<ChartIndicator> m_indList;
   private double m_vlower;
   private double m_vupper;
   private double m_hlower;
   private double m_hupper;
   private String m_ticker;
   private double m_selectOriginX;
   private double m_selectOriginY;
   private double m_screenDataAreaX;
   private double m_screenDataAreaY;
   private double m_screenDataAreaWidth;
   private double m_screenDataAreaHeight;
   private double screenX;
   private double screenY;
 
   public SMChartSettings()
   {
/*  34 */     this.m_annotationsList = new ArrayList();
   }
 
   public ArrayList<LineStruct> getM_annotationsList()
   {
/*  41 */     return this.m_annotationsList;
   }
 
   public void setM_annotationsList(ArrayList<LineStruct> m_annotationsList)
   {
/*  48 */     this.m_annotationsList = m_annotationsList;
   }
 
   public void addLineStruct(LineStruct l) {
/*  52 */     if (this.m_annotationsList == null) {
/*  53 */       this.m_annotationsList = new ArrayList();
     }
/*  55 */     this.m_annotationsList.add(l);
   }
 
   public double getM_vlower()
   {
/*  62 */     return this.m_vlower;
   }
 
   public void setM_vlower(double m_vlower)
   {
/*  69 */     this.m_vlower = m_vlower;
   }
 
   public double getM_vupper()
   {
/*  76 */     return this.m_vupper;
   }
 
   public void setM_vupper(double m_vupper)
   {
/*  83 */     this.m_vupper = m_vupper;
   }
 
   public double getM_hlower()
   {
/*  90 */     return this.m_hlower;
   }
 
   public void setM_hlower(double m_hlower)
   {
/*  97 */     this.m_hlower = m_hlower;
   }
 
   public double getM_hupper()
   {
/* 104 */     return this.m_hupper;
   }
 
   public void setM_hupper(double m_hupper)
   {
/* 111 */     this.m_hupper = m_hupper;
   }
 
   public String getM_ticker()
   {
/* 118 */     return this.m_ticker;
   }
 
   public void setM_ticker(String m_ticker)
   {
/* 125 */     this.m_ticker = m_ticker;
   }
 
   public double getM_selectOriginX()
   {
/* 132 */     return this.m_selectOriginX;
   }
 
   public void setM_selectOriginX(double m_selectOriginX)
   {
/* 139 */     this.m_selectOriginX = m_selectOriginX;
   }
 
   public double getM_selectOriginY()
   {
/* 146 */     return this.m_selectOriginY;
   }
 
   public void setM_selectOriginY(double m_selectOriginY)
   {
/* 153 */     this.m_selectOriginY = m_selectOriginY;
   }
 
   public double getM_screenDataAreaX()
   {
/* 160 */     return this.m_screenDataAreaX;
   }
 
   public void setM_screenDataAreaX(double m_screenDataAreaX)
   {
/* 167 */     this.m_screenDataAreaX = m_screenDataAreaX;
   }
 
   public double getM_screenDataAreaY()
   {
/* 174 */     return this.m_screenDataAreaY;
   }
 
   public void setM_screenDataAreaY(double m_screenDataAreaY)
   {
/* 181 */     this.m_screenDataAreaY = m_screenDataAreaY;
   }
 
   public double getM_screenDataAreaWidth()
   {
/* 188 */     return this.m_screenDataAreaWidth;
   }
 
   public void setM_screenDataAreaWidth(double m_screenDataAreaWidth)
   {
/* 195 */     this.m_screenDataAreaWidth = m_screenDataAreaWidth;
   }
 
   public double getM_screenDataAreaHeight()
   {
/* 202 */     return this.m_screenDataAreaHeight;
   }
 
   public void setM_screenDataAreaHeight(double m_screenDataAreaHeight)
   {
/* 209 */     this.m_screenDataAreaHeight = m_screenDataAreaHeight;
   }
 
   public double getScreenX()
   {
/* 216 */     return this.screenX;
   }
 
   public void setScreenX(double screenX)
   {
/* 223 */     this.screenX = screenX;
   }
 
   public double getScreenY()
   {
/* 230 */     return this.screenY;
   }
 
   public void setScreenY(double screenY)
   {
/* 237 */     this.screenY = screenY;
   }
 
   public ArrayList<ChartIndicator> getM_indList()
   {
/* 244 */     return this.m_indList;
   }
 
   public void setM_indList(ArrayList<ChartIndicator> m_indList)
   {
/* 251 */     this.m_indList = m_indList;
   }
 }