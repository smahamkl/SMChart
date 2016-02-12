 package org.smchart.xml;
 
 import java.awt.geom.Rectangle2D;
 import java.io.FileReader;
 import java.io.FileWriter;
 import java.util.ArrayList;
 import java.util.Iterator;
 import org.exolab.castor.mapping.Mapping;
 import org.exolab.castor.xml.Marshaller;
 import org.exolab.castor.xml.Unmarshaller;
 import org.smchart.charts.SMChartPanel;
 import org.smchart.ui.LineAnnotationsStruct;
 import org.smchart.ui.UserAnnotations;
 
 public class XMLMgr
 {
   public static void ObjectSerialize(SMChartSettings sm, String filePath)
     throws Exception
   {
/*  19 */     FileWriter writer = null;
     try {
/*  21 */       Mapping mapping = new Mapping();
mapping.loadMapping(XMLMgr.class.getResource("/org/smchart/xml/mapping.xml"));
 
/*  24 */       writer = new FileWriter(filePath);
/*  25 */       Marshaller marshaller = new Marshaller(writer);
/*  26 */       marshaller.setMapping(mapping);
 
/*  28 */       marshaller.marshal(sm);
     } catch (Exception e) {
/*  30 */       System.err.println(e.getMessage());
/*  31 */       e.printStackTrace(System.err);
     } finally {
/*  33 */       if (writer != null) {
/*  34 */         writer.close();
/*  35 */         writer = null;
       }
     }
   }
 
   public static SMChartSettings ObjectDeserialize(String filePath) throws Exception {
/*  41 */     SMChartSettings ls = null;
/*  42 */     FileReader reader = null;
     try {
/*  44 */       Mapping mapping = new Mapping();
mapping.loadMapping(XMLMgr.class.getResource("/org/smchart/xml/mapping.xml"));
/*  46 */       reader = new FileReader(filePath);
/*  47 */       Unmarshaller um = new Unmarshaller(mapping);
/*  48 */       um.setClass(SMChartSettings.class);
/*  49 */       ls = (SMChartSettings)um.unmarshal(reader);
     } catch (Exception e) {
/*  51 */       System.err.println(e.getMessage());
/*  52 */       e.printStackTrace(System.err);
     } finally {
/*  54 */       if (reader != null) {
/*  55 */         reader.close();
/*  56 */         reader = null;
       }
     }
/*  59 */     return ls;
   }
 
   public static SMChartSettings getChartSettingsToSave(SMChartPanel mainPanel, ArrayList<LineAnnotationsStruct> lineAnnList, String ticker, ArrayList<ChartIndicator> indList)
   {
/*  64 */     SMChartSettings sm = null;
     try
     {
/*  67 */       sm = new SMChartSettings();
/*  68 */       sm.setM_vlower(mainPanel.getvLower());
/*  69 */       sm.setM_hlower(mainPanel.gethLower());
/*  70 */       sm.setM_hupper(mainPanel.gethUpper());
/*  71 */       sm.setM_vupper(mainPanel.getvUpper());
/*  72 */       sm.setM_ticker(ticker);
/*  73 */       sm.setM_selectOriginX(mainPanel.getSelectOrigin().getX());
/*  74 */       sm.setM_selectOriginY(mainPanel.getSelectOrigin().getY());
/*  75 */       Rectangle2D r = mainPanel.getChartRenderingInfo().getPlotInfo().getDataArea();
/*  76 */       sm.setM_screenDataAreaX(r.getX());
/*  77 */       sm.setM_screenDataAreaY(r.getY());
/*  78 */       sm.setM_screenDataAreaWidth(r.getWidth());
/*  79 */       sm.setM_screenDataAreaHeight(r.getHeight());
/*  80 */       sm.setScreenX(mainPanel.getScaleX());
/*  81 */       sm.setScreenY(mainPanel.getScaleY());
/*  82 */       if (indList != null) {
/*  83 */         sm.setM_indList(indList);
       }
 
/*  86 */       if (lineAnnList != null) {
/*  87 */         Iterator iter = lineAnnList.iterator();
/*  88 */         while (iter.hasNext()) {
/*  89 */           LineAnnotationsStruct las = (LineAnnotationsStruct)iter.next();
/*  90 */           LineStruct ls = new LineStruct();
/*  91 */           ls.setM_shape(las.getShape().name());
                    ls.setM_x1(las.getLine().x1);
                    ls.setM_x2(las.getLine().x2);
                    ls.setM_y1(las.getLine().y1);
                    ls.setM_y2(las.getLine().y2);
/*  97 */           if (las.getShape() == UserAnnotations.UserDrawingShape.FINBONACCIARCS) {
/*  98 */             ls.getM_arcAnnStruct().setM_ArcRadiusOnChart(las.getM_arcAnnStructObj().getM_ArcRadiusOnChart());
/*  99 */             ls.getM_arcAnnStruct().setM_arcRadius(las.getM_arcAnnStructObj().getM_arcRadius());
           }
/* 101 */           sm.addLineStruct(ls);
         }
       }
     }
     catch (Exception ex) {
/* 106 */       ex.printStackTrace();
     }
/* 108 */     return sm;
   }
 }