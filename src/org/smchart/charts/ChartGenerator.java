 package org.smchart.charts;
 
 import java.awt.Color;
 import java.awt.geom.Point2D;
 //import java.awt.geom.Point2D.Double;
 import java.awt.geom.Rectangle2D;
 //import java.awt.geom.Rectangle2D.Double;
 import java.io.BufferedInputStream;
 import java.io.DataInputStream;
 import java.io.File;
 import java.io.FileInputStream;
 import java.text.DateFormat;
 import java.text.SimpleDateFormat;
 import java.util.ArrayList;
 import java.util.Calendar;
 import java.util.Collections;
 import java.util.Date;
 import java.util.List;
 import java.util.StringTokenizer;
 import org.jfree.chart.ChartColor;
 import org.jfree.chart.JFreeChart;
 import org.jfree.chart.axis.DateAxis;
 import org.jfree.chart.axis.NumberAxis;
 import org.jfree.chart.plot.CombinedDomainXYPlot;
 import org.jfree.chart.plot.Plot;
 import org.jfree.chart.plot.PlotOrientation;
 import org.jfree.chart.plot.PlotRenderingInfo;
 import org.jfree.chart.plot.XYPlot;
 import org.jfree.chart.plot.Zoomable;
 import org.jfree.chart.renderer.xy.CandlestickRenderer;
 import org.jfree.chart.renderer.xy.HighLowRenderer;
 import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
 import org.jfree.data.time.Day;
 import org.jfree.data.time.TimeSeries;
 import org.jfree.data.time.TimeSeriesCollection;
 import org.jfree.data.xy.AbstractXYDataset;
 import org.jfree.data.xy.DefaultOHLCDataset;
 import org.jfree.data.xy.OHLCDataItem;
 import org.jfree.data.xy.XYDataset;
 import org.smchart.util.DownloadQuotes;
 import org.smchart.xml.SMChartSettings;
 
 public class ChartGenerator
 {
/*  49 */   private OHLCDataItem[] m_OHLCData = null;
/*  50 */   private DownloadQuotes m_quotesDataObj = null;
   private String m_ticker;
/*  52 */   private String YAHOO_URL = "http://ichart.finance.yahoo.com/table.csv?s=";
/*  53 */   private boolean m_isProxy = false;
/*  54 */   private String m_proxyHost = "";
/*  55 */   private String m_proxyPort = "";
   private String m_dataDir;
/*  57 */   private SMChartPanel mainPanel = null;
/*  58 */   private DisplayCordinates mui = null;
 
   public String getM_ticker()
   {
/*  64 */     return this.m_ticker;
   }
 
   public void setM_ticker(String m_ticker)
   {
/*  71 */     this.m_ticker = m_ticker;
   }
 
   public OHLCDataItem[] getM_OHLCData()
   {
/*  78 */     return this.m_OHLCData;
   }
 
   public void setM_OHLCData(OHLCDataItem[] m_OHLCData)
   {
/*  85 */     this.m_OHLCData = m_OHLCData;
   }
 
   public ChartGenerator()
   {
/*  99 */     String sep = System.getProperty("file.separator");
/* 100 */     this.m_dataDir = (System.getProperty("user.dir") + sep + "data" + sep);
   }
 
   public ChartGenerator(String inTicker) {
     try {
/* 105 */       this.m_ticker = inTicker;
/* 106 */       String sep = System.getProperty("file.separator");
/* 107 */       this.m_dataDir = (System.getProperty("user.dir") + sep + "data" + sep);
/* 108 */       genChartPanel();
     } catch (Exception ex) {
/* 110 */       ex.printStackTrace();
     }
   }
 
   public ChartGenerator(String inTicker, String host, String port) {
     try {
/* 116 */       this.m_ticker = inTicker;
/* 117 */       String sep = System.getProperty("file.separator");
/* 118 */       this.m_dataDir = (System.getProperty("user.dir") + sep + "data" + sep);
/* 119 */       this.m_isProxy = true;
/* 120 */       this.m_proxyHost = host;
/* 121 */       this.m_proxyPort = port;
/* 122 */       genChartPanel();
     } catch (Exception ex) {
/* 124 */       ex.printStackTrace();
     }
   }
 
   private void genChartPanel() throws Exception {
/* 129 */     NumberAxis rangeAxis = new NumberAxis("Price");
     try {
/* 131 */       if (getM_OHLCData() == null) {
/* 132 */         genOHLCData();
       }
/* 134 */       CandlestickRenderer renderer = new CandlestickRenderer(-1.0D, false, null);
/* 135 */       DateAxis cdleDomainAxis = new DateAxis("Date");
 
/* 137 */       renderer.setAutoWidthMethod(0);
 
/* 139 */       renderer.setUpPaint(Color.WHITE);
 
/* 143 */       renderer.setDownPaint(null);
 
/* 145 */       rangeAxis.setAutoRangeIncludesZero(false);
 
/* 147 */       XYDataset dataset = getDataSet();
/* 148 */       XYPlot mainPlot = new XYPlot(dataset, cdleDomainAxis, rangeAxis, renderer);
/* 149 */       renderer.setSeriesPaint(0, ChartColor.BLACK);
/* 150 */       cdleDomainAxis.setAutoRange(true);
 
/* 153 */       makePlotMinimalFP(mainPlot, rangeAxis);
 
/* 155 */       CombinedDomainXYPlot combinedPlot = new CombinedDomainXYPlot(cdleDomainAxis);
/* 156 */       combinedPlot.add(mainPlot);
/* 157 */       combinedPlot.setGap(2.0D);
/* 158 */       JFreeChart chart = new JFreeChart("", null, combinedPlot, false);
/* 159 */       chart.setBackgroundPaint(null);
/* 160 */       this.mainPanel = new SMChartPanel(chart);
/* 161 */       this.mainPanel.setFillZoomRectangle(false);
/* 162 */       if (this.mui == null) {
/* 163 */         this.mui = new DisplayCordinates(this.mainPanel, this.m_ticker, this.m_OHLCData);
/* 164 */         this.mainPanel.setChartInfoDisplayer(this.mui);
       }
     }
     catch (Exception ex) {
/* 168 */       ex.printStackTrace();
     }
   }
 
   private AbstractXYDataset getDataSet() throws Exception
   {
/* 174 */     DefaultOHLCDataset result = null;
     try
     {
/* 179 */       OHLCDataItem[] data = getM_OHLCData();
 
/* 181 */       if (result == null)
/* 182 */         result = new DefaultOHLCDataset(getM_ticker(), data);
     }
     catch (Exception ex) {
/* 185 */       throw new Exception(ex.getMessage(), ex);
     }
 
/* 188 */     return result;
   }
 
   public void genOHLCData() throws Exception {
/* 192 */     List dataItems = new ArrayList();
/* 193 */     DataInputStream in = null;
     try
     {
/* 196 */       Calendar startCal = Calendar.getInstance();
 
/* 199 */       String dateStr = "&d=" + (startCal.get(2) + 1) + "&e=" + startCal.get(5);
/* 200 */       dateStr = dateStr + "&f=" + startCal.get(1) + "&g=d";
 
/* 204 */       String strUrl = this.YAHOO_URL + getM_ticker() + dateStr + "&a=3&b=9&c=2000&ignore=.csv";
/* 205 */       String destFile = this.m_dataDir + getM_ticker() + ".csv";
 
/* 207 */       if (this.m_isProxy)
/* 208 */         this.m_quotesDataObj = new DownloadQuotes(strUrl, destFile, this.m_proxyHost, this.m_proxyPort);
       else {
/* 210 */         this.m_quotesDataObj = new DownloadQuotes(strUrl, destFile);
       }
 
/* 215 */       File file = new File(destFile);
/* 216 */       in = new DataInputStream(new BufferedInputStream(new FileInputStream(file)));
/* 217 */       DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
 
/* 220 */       if (in.available() != 0) {
/* 221 */         in.readLine();
/* 222 */         if (in.available() == 0)
/* 223 */           throw new Exception("No Input data found");
         String inputLine;
/* 225 */         while ((inputLine = in.readLine()) != null) {
/* 226 */           StringTokenizer st = new StringTokenizer(inputLine, ",");
 
/* 228 */           Date date = df.parse(st.nextToken());
/* 229 */           double open = Double.parseDouble(st.nextToken());
/* 230 */           double high = Double.parseDouble(st.nextToken());
/* 231 */           double low = Double.parseDouble(st.nextToken());
/* 232 */           double close = Double.parseDouble(st.nextToken());
/* 233 */           double volume = Double.parseDouble(st.nextToken());
/* 234 */           double adjClose = Double.parseDouble(st.nextToken());
 
/* 236 */           OHLCDataItem item = new OHLCDataItem(date, open, high, low, close, volume);
/* 237 */           dataItems.add(item);
         }
       }
/* 240 */       Collections.reverse(dataItems);
/* 241 */       setM_OHLCData((OHLCDataItem[])dataItems.toArray(new OHLCDataItem[dataItems.size()]));
     }
     catch (Exception e) {
/* 244 */       throw new Exception(e.getMessage(), e);
     } finally {
/* 246 */       if (in != null)
/* 247 */         in.close();
     }
   }
 
   public OHLCDataItem[] getOHLCDataArr()
   {
/* 253 */     return getM_OHLCData();
   }
 
   public void makePlotMinimalFP(XYPlot mainPlot, NumberAxis rangeAxis) {
/* 257 */     mainPlot.setDomainCrosshairVisible(true);
/* 258 */     mainPlot.setDomainCrosshairLockedOnData(false);
/* 259 */     mainPlot.setRangeCrosshairVisible(true);
/* 260 */     mainPlot.setRangeCrosshairLockedOnData(false);
 
/* 263 */     mainPlot.setDomainGridlinesVisible(false);
/* 264 */     mainPlot.setRangeGridlinesVisible(true);
   }
 
   public CombinedDomainXYPlot getCombinedDomainPlot() {
/* 268 */     return (CombinedDomainXYPlot)this.mainPanel.getChart().getPlot();
   }
 
   public SMChartPanel getSMChartPanel() {
/* 272 */     return this.mainPanel;
   }
 
   public void setSMChartPanel(SMChartPanel panel) {
/* 276 */     this.mainPanel = panel;
   }
 
   public void SetChartType(ChartType chrtType) {
     try {
/* 281 */       if (chrtType == ChartType.CANDLESTICK)
/* 282 */         setCandleStickChart((XYPlot)getCombinedDomainPlot().getSubplots().get(0));
/* 283 */       else if (chrtType == ChartType.CLOSEPRICE)
/* 284 */         setClosePriceChart((XYPlot)getCombinedDomainPlot().getSubplots().get(0));
/* 285 */       else if (chrtType == ChartType.HIGHLOW) {
/* 286 */         setHighLowChart((XYPlot)getCombinedDomainPlot().getSubplots().get(0));
       }
/* 288 */       System.gc();
     } catch (Exception ex) {
/* 290 */       ex.printStackTrace();
     }
   }
 
   private void setCandleStickChart(XYPlot mPlot) throws Exception {
/* 295 */     mPlot.setDataset(0, null);
/* 296 */     mPlot.setRenderer(0, null);
/* 297 */     CandlestickRenderer rend = new CandlestickRenderer(-1.0D, false, null);
/* 298 */     rend.setAutoWidthMethod(1);
 
/* 300 */     rend.setUpPaint(Color.WHITE);
/* 301 */     rend.setDownPaint(null);
/* 302 */     rend.setSeriesPaint(0, ChartColor.BLACK);
 
/* 305 */     XYDataset dataset = new DefaultOHLCDataset(getM_ticker(), this.m_OHLCData);
/* 306 */     mPlot.setDataset(0, dataset);
/* 307 */     mPlot.setRenderer(0, rend);
   }
 
   private void setHighLowChart(XYPlot mPlot) throws Exception
   {
/* 312 */     mPlot.setDataset(0, null);
/* 313 */     mPlot.setRenderer(0, null);
/* 314 */     HighLowRenderer rend = new HighLowRenderer();
 
/* 316 */     rend.setSeriesPaint(0, ChartColor.BLACK);
 
/* 318 */     XYDataset dataset = new DefaultOHLCDataset(getM_ticker(), this.m_OHLCData);
/* 319 */     mPlot.setDataset(0, dataset);
/* 320 */     mPlot.setRenderer(0, rend);
   }
 
   private void setClosePriceChart(XYPlot mPlot) throws Exception
   {
/* 325 */     mPlot.setDataset(0, null);
/* 326 */     mPlot.setRenderer(0, null);
/* 327 */     XYLineAndShapeRenderer lineShapeRen = new XYLineAndShapeRenderer(true, false);
/* 328 */     lineShapeRen.setSeriesPaint(0, Color.BLACK);
/* 329 */     XYDataset dataset = getTimeSeriesData();
/* 330 */     mPlot.setDataset(0, dataset);
/* 331 */     mPlot.setRenderer(0, lineShapeRen);
   }
 
   private TimeSeriesCollection getTimeSeriesData() throws Exception {
/* 335 */     TimeSeriesCollection seriesData = null;
     try
     {
/* 338 */       seriesData = new TimeSeriesCollection();
/* 339 */       seriesData.addSeries(getClosePriceTimeSeries());
     } catch (Exception ex) {
/* 341 */       throw new Exception(ex.getMessage(), ex);
     }
/* 343 */     return seriesData;
   }
 
   private TimeSeries getClosePriceTimeSeries() {
/* 347 */     TimeSeries s1 = new TimeSeries(getM_ticker());
/* 348 */     for (int i = 0; i < this.m_OHLCData.length; i++) {
/* 349 */       s1.add(new Day(this.m_OHLCData[i].getDate()), Double.parseDouble(this.m_OHLCData[i].getClose().toString()));
     }
/* 351 */     return s1;
   }
 
   public void SetChartScale(ChartScale chartScale) {
     try {
/* 356 */       if (chartScale == ChartScale.LOG)
/* 357 */         this.mainPanel.setChartScale(chartScale, (XYPlot)getCombinedDomainPlot().getSubplots().get(0));
/* 358 */       else if (chartScale == ChartScale.LINEAR) {
/* 359 */         this.mainPanel.setChartScale(chartScale, (XYPlot)getCombinedDomainPlot().getSubplots().get(0));
       }
/* 361 */       System.gc();
     } catch (Exception ex) {
/* 363 */       ex.printStackTrace();
     }
   }
 
   public void AdjustChartScroll(int evtVal, int scrollbarbefore) {
/* 368 */     double x = evtVal * 60 * 60 * 1000;
/* 369 */     double r1 = getCombinedDomainPlot().getDomainAxis().getLowerBound();
/* 370 */     double r2 = getCombinedDomainPlot().getDomainAxis().getUpperBound();
 
/* 372 */     if (evtVal < scrollbarbefore)
/* 373 */       this.mainPanel.SetCdleDomainAxisRange(r1 - x, r2 - x);
     else
/* 375 */       this.mainPanel.SetCdleDomainAxisRange(r1 + x, r2 + x);
   }
 
   public void restoreChartSettings(SMChartSettings sm)
   {
/* 380 */     this.mainPanel.setChildScaleX(sm.getScreenX());
/* 381 */     this.mainPanel.setChildScaleY(sm.getScreenY());
/* 382 */     Rectangle2D r = new Rectangle2D.Double(sm.getM_screenDataAreaX(), sm.getM_screenDataAreaY(), sm.getM_screenDataAreaWidth(), sm.getM_screenDataAreaHeight());
 
/* 384 */     this.mainPanel.getChartRenderingInfo().getPlotInfo().setDataArea(r);
/* 385 */     this.mainPanel.restoreAutoBounds();
 
/* 388 */     PlotRenderingInfo plotInfo = this.mainPanel.getChartRenderingInfo().getPlotInfo();
/* 389 */     Point2D selectOrigin = new Point2D.Double(sm.getM_selectOriginX(), sm.getM_selectOriginY());
/* 390 */     Plot p = this.mainPanel.getChart().getPlot();
/* 391 */     if ((p instanceof Zoomable)) {
/* 392 */       Zoomable z = (Zoomable)p;
/* 393 */       if (z.getOrientation() == PlotOrientation.HORIZONTAL) {
/* 394 */         z.zoomDomainAxes(sm.getM_vlower(), sm.getM_vupper(), plotInfo, selectOrigin);
/* 395 */         z.zoomRangeAxes(sm.getM_hlower(), sm.getM_hupper(), plotInfo, selectOrigin);
       } else {
/* 397 */         z.zoomDomainAxes(sm.getM_hlower(), sm.getM_hupper(), plotInfo, selectOrigin);
/* 398 */         z.zoomRangeAxes(sm.getM_vlower(), sm.getM_vupper(), plotInfo, selectOrigin);
       }
     }
/* 401 */     this.mainPanel.setSelectOrigin(new Point2D.Double(sm.getM_selectOriginX(), sm.getM_selectOriginY()));
/* 402 */     this.mainPanel.sethLower(sm.getM_hlower());
/* 403 */     this.mainPanel.sethUpper(sm.getM_hupper());
/* 404 */     this.mainPanel.setvLower(sm.getM_vlower());
/* 405 */     this.mainPanel.setvUpper(sm.getM_vupper());
   }
 
   public static enum ChartScale
   {
/*  95 */     LINEAR, LOG;
   }
 
   public static enum ChartType
   {
/*  90 */     CANDLESTICK, CLOSEPRICE, HIGHLOW;
   }
 }
