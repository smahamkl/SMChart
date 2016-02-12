 package org.smchart.xml;
 
 import org.smchart.ui.UserAnnotations;
 
 public class LineStruct
 {
   private double m_x1;
   private double m_x2;
   private double m_y1;
   private double m_y2;
   private String m_shape;
/*  20 */   private ArcAnnotationsStruct m_arcAnnStruct = null;
 
   public double getM_x1()
   {
/*  29 */     return this.m_x1;
   }
 
   public void setM_x1(double m_x1)
   {
/*  36 */     this.m_x1 = m_x1;
   }
 
   public double getM_x2()
   {
/*  43 */     return this.m_x2;
   }
 
   public void setM_x2(double m_x2)
   {
/*  50 */     this.m_x2 = m_x2;
   }
 
   public double getM_y1()
   {
/*  57 */     return this.m_y1;
   }
 
   public void setM_y1(double m_y1)
   {
/*  64 */     this.m_y1 = m_y1;
   }
 
   public double getM_y2()
   {
/*  71 */     return this.m_y2;
   }
 
   public void setM_y2(double m_y2)
   {
/*  78 */     this.m_y2 = m_y2;
   }
 
   public String getM_shape()
   {
/*  85 */     return this.m_shape;
   }
 
   public void setM_shape(String m_shape)
   {
/*  92 */     this.m_shape = m_shape;
/*  93 */     if (UserAnnotations.UserDrawingShape.valueOf(m_shape) == UserAnnotations.UserDrawingShape.FINBONACCIARCS)
     {
/*  95 */       this.m_arcAnnStruct = new ArcAnnotationsStruct();
     }
   }
 
   public ArcAnnotationsStruct getM_arcAnnStruct()
   {
/* 103 */     return this.m_arcAnnStruct;
   }
 
   public void setM_arcAnnStruct(ArcAnnotationsStruct m_arcAnnStruct)
   {
/* 110 */     this.m_arcAnnStruct = m_arcAnnStruct;
   }
 }