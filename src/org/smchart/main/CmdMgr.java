 package org.smchart.main;
 
 import java.awt.Dimension;
 import java.awt.EventQueue;
 import java.awt.Toolkit;
 import java.io.File;
 import java.io.Serializable;
 import java.util.Iterator;
 import javax.swing.JFileChooser;
 import javax.swing.JFrame;
 import javax.swing.filechooser.FileFilter;
 import org.smchart.charts.ChartGenerator;
 import org.smchart.gui.LeftPane;
 import org.smchart.gui.MainGUI;
 import org.smchart.gui.PlotWeights;
 import org.smchart.gui.RSSUI;
 import org.smchart.gui.ToolOptions;
 import org.smchart.indicators.IndicatorFactory;
 import org.smchart.ui.UserAnnotations;
 import org.smchart.util.ConfigMgmt;
 import org.smchart.xml.ChartIndicator;
 import org.smchart.xml.SMChartSettings;
 import org.smchart.xml.XMLMgr;
 
 public class CmdMgr
   implements Serializable
 {
   private ChartGenerator smCdlChart = null;
   private String m_dataDir;
   private MainGUI mainGUI;
   private LeftPane m_leftPane;
   private UserAnnotations ui = null;
   private JFrame frame;
   private IndicatorFactory ifact;
   private String m_ticker;
 
   public CmdMgr(String dataDir)throws Exception
   {
     this.m_dataDir = dataDir;
   }
 
   public void resetChartGen() {
/*  40 */     this.smCdlChart = null;
/*  41 */     this.ui = null;
/*  42 */     if (this.ifact != null) {
/*  43 */       this.ifact.ClearIndicatorHash();
     }
/*  45 */     this.ifact = null;
   }
 
   public String getFileNameWithoutExtension(String fileName) {
/*  49 */     if (fileName == null) {
/*  50 */       return null;
     }
/*  52 */     int whereDot = fileName.lastIndexOf('.');
/*  53 */     if ((0 < whereDot) && (whereDot <= fileName.length() - 2)) {
/*  54 */       return fileName.substring(0, whereDot);
     }
/*  56 */     return null;
   }
 
   public void GenerateChart(String ticker) throws Exception {
/*  60 */     if (ticker == null) {
/*  61 */       ticker = this.m_leftPane.getSelectedTicker();
/*  62 */       if (ticker == null) {
/*  63 */         return;
       }
     }
/*  66 */     this.m_ticker = ticker;
/*  67 */     String tmpTicker = ticker;
     try
     {
/*  73 */       if (this.smCdlChart == null) {
/*  74 */         boolean proxy = Boolean.parseBoolean(ConfigMgmt.getProperty("webproxy", this.m_dataDir));
 
/*  76 */         if (proxy) {
/*  77 */           this.smCdlChart = new ChartGenerator(tmpTicker, ConfigMgmt.getProperty("webproxyhost", this.m_dataDir), ConfigMgmt.getProperty("webproxyport", this.m_dataDir));
         }
         else {
/*  80 */           this.smCdlChart = new ChartGenerator(tmpTicker);
         }
 
/*  83 */         if (this.ui == null) {
/*  84 */           this.ui = new UserAnnotations(this.smCdlChart.getSMChartPanel());
/*  85 */           this.smCdlChart.getSMChartPanel().AttachObserver(this.ui);
         }
       }
/*  88 */       this.mainGUI.addInternalFrame(tmpTicker, this.smCdlChart.getSMChartPanel());
     }
     catch (Exception ex)
     {
/*  92 */       ex.printStackTrace();
     }
   }
 
   public void ChangeChartType(ChartGenerator.ChartType chrtType)
     throws Exception
   {
/* 108 */     if (this.smCdlChart != null)
/* 109 */       this.smCdlChart.SetChartType(chrtType);
   }
 
   public void ChangeChartScale(ChartGenerator.ChartScale chartScale) throws Exception
   {
/* 114 */     if (this.smCdlChart != null)
/* 115 */       this.smCdlChart.SetChartScale(chartScale);
   }
 
   public void adjustChartScroll(int evtVal, int scrollbarbefore)
   {
/* 120 */     if (this.smCdlChart != null)
/* 121 */       this.smCdlChart.AdjustChartScroll(evtVal, scrollbarbefore);
   }
 
   public void ChartZoomChange(boolean isZoom)
   {
/* 126 */     if (this.smCdlChart != null)
/* 127 */       if (isZoom) {
/* 128 */         this.smCdlChart.getSMChartPanel().setMouseZoomable(true, true);
/* 129 */         this.smCdlChart.getSMChartPanel().setFillZoomRectangle(false);
/* 130 */         if (this.ui != null) {
/* 131 */           this.ui.disableUserDrawings();
         }
/* 133 */         if (this.mainGUI != null)
/* 134 */           this.mainGUI.resetGUI();
       }
       else {
/* 137 */         this.smCdlChart.getSMChartPanel().setMouseZoomable(false, false);
       }
   }
 
   public void activateChartUserDrawings(UserAnnotations.UserDrawingShape shape)
   {
/* 144 */     if (this.ui != null) {
/* 145 */       this.ui.setShape(shape);
/* 146 */       this.ui.enableTrendLines();
/* 147 */       this.smCdlChart.getSMChartPanel().requestFocus();
     }
   }
 
   public void delFile(String csvFile) throws Exception {
/* 152 */     String ext = csvFile.lastIndexOf(".") == -1 ? "" : csvFile.substring(csvFile.lastIndexOf(".") + 1, csvFile.length());
 
/* 154 */     if (ext.compareToIgnoreCase("csv") == 0) {
/* 155 */       File f = new File(csvFile);
/* 156 */       if ((!f.exists()) || (!f.isFile()) || 
/* 157 */         (!f.delete()));
     }
   }
 
   public void ClearUserChartDrawings()
   {
/* 165 */     this.ui.clearAllAnnotations();
   }
 
   public void setDragFlag(boolean flag) {
/* 169 */     if (this.ui != null) {
/* 170 */       if (flag) {
/* 171 */         ChartZoomChange(false);
       }
/* 173 */       this.ui.setDragFlag(flag);
     }
   }
 
   public void deleteselectedMouseDrawing() {
/* 178 */     this.ui.deleteHighlightedDrawing();
/* 179 */     if (this.mainGUI != null)
/* 180 */       this.mainGUI.resetGUI();
   }
 
   public void setMainGUI(MainGUI inMainGUI, LeftPane dd, JFrame frame)
   {
/* 185 */     this.mainGUI = inMainGUI;
/* 186 */     this.m_leftPane = dd;
/* 187 */     this.frame = frame;
/* 188 */     setFrameDimension();
   }
 
   private void setFrameDimension() {
/* 192 */     this.frame.setState(0);
/* 193 */     Toolkit toolkit = Toolkit.getDefaultToolkit();
/* 194 */     Dimension dimension = toolkit.getScreenSize();
/* 195 */     this.frame.setSize(dimension);
   }
 
   public void disPlayRSSHeadLines(final String ticker) {
/* 199 */     EventQueue.invokeLater(new Runnable()
     {
       public void run() {
/* 202 */         boolean proxy = Boolean.parseBoolean(ConfigMgmt.getProperty("webproxy", CmdMgr.this.m_dataDir));
/* 203 */         if (proxy) {
/* 204 */           new RSSUI(ticker, ConfigMgmt.getProperty("webproxyhost", CmdMgr.this.m_dataDir), ConfigMgmt.getProperty("webproxyport", CmdMgr.this.m_dataDir));
         }
         else
/* 207 */           new RSSUI(ticker);
       }
     });
   }
 
   public boolean addIndicator(IndicatorFactory.IndicatorType type)
   {
     try
     {
/* 216 */       if (this.ifact == null) {
/* 217 */         this.ifact = new IndicatorFactory(this.smCdlChart.getCombinedDomainPlot(), this.smCdlChart.getOHLCDataArr(), this.m_ticker);
       }
 
/* 220 */       if (this.ifact.addIndicator(type))
/* 221 */         this.m_leftPane.addIndicatorNode(type, this.m_ticker);
     }
     catch (Exception ex)
     {
/* 225 */       ex.printStackTrace();
/* 226 */       return false;
     }
/* 228 */     return true;
   }
 
   public void clearUpperIndicator(IndicatorFactory.IndicatorType type) {
     try {
/* 233 */       if (this.ifact != null) {
/* 234 */         this.ifact = new IndicatorFactory(this.smCdlChart.getCombinedDomainPlot(), this.smCdlChart.getOHLCDataArr(), this.m_ticker);
 
/* 236 */         this.ifact.removeUpperInidicator(type);
       }
     }
     catch (Exception ex) {
/* 240 */       ex.printStackTrace();
     }
   }
 
   public void DisplayToolOptions() {
/* 245 */     EventQueue.invokeLater(new Runnable()
     {
       public void run() {
/* 248 */         ToolOptions to = new ToolOptions(CmdMgr.this.m_dataDir);
/* 249 */         to.setVisible(true);
       }
     });
   }
 
   public void setPlotWeights() {
/* 255 */     if ((this.smCdlChart != null) && 
/* 256 */       (this.smCdlChart.getCombinedDomainPlot().getSubplots().size() > 1)) {
/* 257 */       PlotWeights pw = new PlotWeights();
/* 258 */       pw.displayOptions(this.smCdlChart.getCombinedDomainPlot().getSubplots().size());
/* 259 */       if (pw.isChangePlotWeights())
/* 260 */         this.smCdlChart.getSMChartPanel().setPlotWeights(pw.getPlotWeights());
     }
   }
 
   public void saveChartSettings()
     throws Exception
   {
/* 267 */     setFrameDimension();
/* 268 */     if (this.ifact != null) {
/* 269 */       XMLMgr.ObjectSerialize(XMLMgr.getChartSettingsToSave(this.smCdlChart.getSMChartPanel(), this.ui.getChartUserAnnotations(), this.smCdlChart.getM_ticker(), this.ifact.getChartIndicatorList()), this.m_dataDir + this.smCdlChart.getM_ticker() + ".ser");
     }
     else
     {
/* 273 */       XMLMgr.ObjectSerialize(XMLMgr.getChartSettingsToSave(this.smCdlChart.getSMChartPanel(), this.ui.getChartUserAnnotations(), this.smCdlChart.getM_ticker(), null), this.m_dataDir + this.smCdlChart.getM_ticker() + ".ser");
     }
   }
 
   public void OpenChart()
  {
    try
    {
      JFileChooser fc = new JFileChooser(this.m_dataDir);
      FileFilter myFilter = new ExtensionFileFilter(null, new String[] { "ser" });

      fc.setFileFilter(myFilter);

      int returnVal = fc.showOpenDialog(this.mainGUI);
      if (returnVal == 0) {
        File file = fc.getSelectedFile();
        if (file.getName().length() > 0) {
          SMChartSettings sm = XMLMgr.ObjectDeserialize(this.m_dataDir + file.getName());
          resetChartGen();
          GenerateChart(sm.getM_ticker());
          this.smCdlChart.restoreChartSettings(sm);
          this.m_ticker = sm.getM_ticker();
          this.ui.RestoreAnnotations(sm.getM_annotationsList());
          if (this.ifact == null) {
            this.ifact = new IndicatorFactory(this.smCdlChart.getCombinedDomainPlot(), this.smCdlChart.getOHLCDataArr(), sm.getM_ticker());
          }

          java.util.ArrayList indList = sm.getM_indList();
          if(indList != null)
          {
          ifact.restoreIndicators(indList);
          Iterator iter = indList.iterator();
          while (iter.hasNext()) {
            IndicatorFactory.IndicatorType type = IndicatorFactory.IndicatorType.valueOf(((ChartIndicator)iter.next()).getM_indName());

            this.m_leftPane.addIndicatorNode(type, this.m_ticker);
          }
          }
        }
      }
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }
 
   private class ExtensionFileFilter extends FileFilter
   {
     String description;
     String[] extensions;
 
     public ExtensionFileFilter(String description, String extension) {
/* 321 */       this(description, new String[] { extension });
     }
 
     public ExtensionFileFilter(String description, String[] extensions) {
/* 325 */       if (description == null)
/* 326 */         this.description = (extensions[0] + "{ " + extensions.length + "} ");
       else {
/* 328 */         this.description = description;
       }
/* 330 */       this.extensions = ((String[])extensions.clone());
/* 331 */       toLower(this.extensions);
     }
 
     private void toLower(String[] array) {
/* 335 */       int i = 0; for (int n = array.length; i < n; i++)
/* 336 */         array[i] = array[i].toLowerCase();
     }
 
     public String getDescription()
     {
/* 341 */       return this.description;
     }
 
     public boolean accept(File file) {
/* 345 */       if (file.isDirectory()) {
/* 346 */         return true;
       }
/* 348 */       String path = file.getAbsolutePath().toLowerCase();
/* 349 */       int i = 0; for (int n = this.extensions.length; i < n; i++) {
/* 350 */         String extension = this.extensions[i];
/* 351 */         if ((path.endsWith(extension)) && (path.charAt(path.length() - extension.length() - 1) == '.')) {
/* 352 */           return true;
         }
       }
 
/* 356 */       return false;
     }
   }
 }