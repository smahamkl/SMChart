package org.smchart.ui;

import java.awt.Cursor;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Line2D;
import java.awt.geom.Line2D.Double;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.swing.SwingUtilities;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.annotations.XYShapeAnnotation;
import org.jfree.chart.plot.CombinedDomainXYPlot;
import org.jfree.chart.plot.XYPlot;
import org.smchart.charts.SMChartPanel;
import org.smchart.xml.ArcAnnotationsStruct;
import org.smchart.xml.LineStruct;

public class UserAnnotations extends AbstractChartAnnotations
  implements MouseListener, MouseMotionListener
{
  private Point2D tlStartPoint;
  private Point2D tlEndPoint;
  private XYShapeAnnotation trendLine = null;
  private ArrayList<LineAnnotationsStruct> lineAnnStructList = null;
  private Cursor curCursor;
  private boolean freezeRect = false;
  private int selectedIndex = -1;
  private Point2D.Double offset = null;
  private boolean dragging = false;
  private LineAnnotationsStruct curLineAnnStruct = null;
  private UserDrawingShape m_shapeType = UserDrawingShape.TRENDLINE;
  protected boolean m_isdragShape = false;
  private boolean isDrawingsEnabled = false;

  public UserAnnotations(ChartPanel inPanel)
  {
    super(inPanel);

    if (this.offset == null) {
      this.offset = new Point2D.Double();
    }
    if (this.lineAnnStructList == null)
      this.lineAnnStructList = new ArrayList();
  }

  public ArrayList<LineAnnotationsStruct> getChartUserAnnotations()
  {
    return this.lineAnnStructList;
  }

  public void deleteHighlightedDrawing() {
    try {
      if ((this.freezeRect == true) && (this.lineAnnStructList.size() > 0) && (this.curLineAnnStruct != null)) {
        Line2D.Double l = this.curLineAnnStruct.getLine();
        UserDrawingShape tmpShape = this.curLineAnnStruct.getShape();
        if (tmpShape == UserDrawingShape.FIBONACCIRETRACEMENT)
          FiboRetracementAnnotations.clearFibonacciRetLines(this.curLineAnnStruct.getLine(), getTlSubplot());
        else if (tmpShape == UserDrawingShape.FINBONACCIARCS) {
          FibonacciArcsAnnotations.clearArcAnnotations(this.curLineAnnStruct.getLine(), getM_chartPanel().getScreenDataArea(), getTlSubplot());
        }
        removeMyAnnotation(new XYShapeAnnotation(l));
        if (this.lineAnnStructList.contains(this.curLineAnnStruct)) {
          this.lineAnnStructList.remove(this.curLineAnnStruct);
        }
        clearRects();
      }
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  public void enableTrendLines() {
    MouseListener[] ml = getM_chartPanel().getMouseListeners();
    boolean lisFound = false;
    for (int i = 0; i < ml.length; i++) {
      if (ml[i].equals(this)) {
        lisFound = true;
      }
    }
    if (!lisFound) {
      getM_chartPanel().addMouseListener(this);
      getM_chartPanel().addMouseMotionListener(this);
    }
    this.isDrawingsEnabled = true;
  }

  public void disableUserDrawings() {
    clearRects();
    getM_chartPanel().removeMouseListener(this);
    getM_chartPanel().removeMouseMotionListener(this);
    this.isDrawingsEnabled = false;
  }

  public void setShape(UserDrawingShape inShape) {
    this.m_shapeType = inShape;
    System.gc();
  }

  private void addPoints(Point p) {
    getCurPointList().add(mousePointToChartPoint(p));
    if (getTotalPoints() > 0) {
      this.tlStartPoint = ((Point2D)getCurPointList().get(0));
      this.tlEndPoint = ((Point2D)getCurPointList().get(getCurPointList().size() - 1));
    }
  }

  private void setLineEnd(Point p) {
    if (this.trendLine != null) {
      removeMyAnnotation(this.trendLine);
    }
    this.trendLine = null;
    this.tlEndPoint = mousePointToChartPoint(p);
    getCurPointList().clear();

    Line2D.Double tline = new Line2D.Double(this.tlStartPoint.getX(), this.tlStartPoint.getY(), this.tlEndPoint.getX(), this.tlEndPoint.getY());

    LineAnnotationsStruct la = new LineAnnotationsStruct(tline, this.m_shapeType);

    if (this.m_shapeType == UserDrawingShape.FIBONACCIRETRACEMENT) {
      FiboRetracementAnnotations.drawFRetracementLines(tline, super.getTlSubplot());
    } else if (this.m_shapeType == UserDrawingShape.FINBONACCIARCS) {
      addMyAnnotation(new XYShapeAnnotation(tline));
      FibonacciArcsAnnotations.drawArcs(tline, getM_chartPanel().getScreenDataArea(), getTlSubplot());
      la.getM_arcAnnStructObj().setM_arcRadius(FibonacciArcsAnnotations.getArcRadius(tline, getM_chartPanel().getScreenDataArea(), getTlSubplot()));
      la.getM_arcAnnStructObj().setM_ArcRadiusOnChart(FibonacciArcsAnnotations.getArcChartRadius(tline, getM_chartPanel().getScreenDataArea(), getTlSubplot()));
    } else {
      addMyAnnotation(new XYShapeAnnotation(tline));
    }

    if (!this.lineAnnStructList.contains(la))
      this.lineAnnStructList.add(la);
  }

  private void drawLineAnnotation()
  {
    try {
      if (this.tlEndPoint.equals(this.tlStartPoint)) {
        return;
      }
      if (this.trendLine != null) {
        removeMyAnnotation(this.trendLine);
      }
      Line2D l = new Line2D.Double(this.tlStartPoint.getX(), this.tlStartPoint.getY(), this.tlEndPoint.getX(), this.tlEndPoint.getY());

      this.trendLine = new XYShapeAnnotation(l);
      addMyAnnotation(this.trendLine);
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  private void mousePressedEventDelegate(Point p)
  {
    if (getTotalPoints() == 0) {
      setTlSubplotIndex(getSubplotIndex(p));

      setTlSubplot(getSubplot(p));
      if (getTlSubplotIndex() > 0) {
        return;
      }
      if (this.m_shapeType == UserDrawingShape.NODRAWING) {
        return;
      }
    }

    if (isValidChartPoint(p)) {
      clearRects();
      if (this.m_isdragShape == true) {
        Point2D p1 = mousePointToChartPoint(p);
        for (int j = 0; j < getRects().length; j++) {
          if (getRects()[j].contains(p1)) {
            this.selectedIndex = j;
            this.offset.x = (p1.getX() - getRects()[j].x);
            this.offset.y = (p1.getY() - getRects()[j].y);
            this.dragging = true;
            if (this.m_shapeType == UserDrawingShape.FIBONACCIRETRACEMENT) {
              FiboRetracementAnnotations.clearFibonacciRetLines(this.curLineAnnStruct.getLine(), getTlSubplot());
            } else if (this.m_shapeType == UserDrawingShape.FINBONACCIARCS) {
              Rectangle2D panelArea = getM_chartPanel().getScreenDataArea();
              FibonacciArcsAnnotations.clearArcAnnotations(this.curLineAnnStruct.getLine(), panelArea, getTlSubplot());
            }
          }
        }
      }
      else if (getTotalPoints() == 0) {
        addPoints(p);
        drawLineAnnotation();
      } else if ((getTotalPoints() > 0) && 
        (isInsideSubPlot(p))) {
        setLineEnd(p);
      }
    }
  }

  private void mouseMovedEventDelegate(Point p)
  {
    this.curCursor = Cursor.getDefaultCursor();
    getM_chartPanel().setCursor(this.curCursor);
    if ((getTotalPoints() > 0) && (!this.m_isdragShape) && 
      (isValidChartPoint(p)) && (isInsideSubPlot(p))) {
      addPoints(p);
      drawLineAnnotation();
    }

    if ((this.m_isdragShape) && (isValidChartPoint(p)) && (isInsideSubPlot(p)))
      highLightLineAnnotation(p);
  }

  private void mouseDraggedEventDelegate(Point p)
  {
    if ((this.m_isdragShape) && (isValidChartPoint(p)) && (isInsideSubPlot(p))) {
      Point2D p1 = mousePointToChartPoint(p);
      if (this.dragging) {
        double x = p1.getX() - this.offset.x;
        double y = p1.getY() - this.offset.y;
        setRect(this.selectedIndex, x, y);
      }
    }
  }

  public void mouseReleasedEventDelegate(Point p)
  {
    if ((this.m_isdragShape) && (isValidChartPoint(p))) {
      this.selectedIndex = -1;
      this.dragging = false;

      if (this.m_shapeType == UserDrawingShape.FIBONACCIRETRACEMENT) {
        FiboRetracementAnnotations.drawFRetracementLines(this.curLineAnnStruct.getLine(), super.getTlSubplot());
      } else if (this.m_shapeType == UserDrawingShape.FINBONACCIARCS) {
        FibonacciArcsAnnotations.drawArcs(this.curLineAnnStruct.getLine(), getM_chartPanel().getScreenDataArea(), getTlSubplot());
        double r = FibonacciArcsAnnotations.getArcRadius(this.curLineAnnStruct.getLine(), getM_chartPanel().getScreenDataArea(), getTlSubplot());
        this.curLineAnnStruct.getM_arcAnnStructObj().setM_arcRadius(r);
        r = FibonacciArcsAnnotations.getArcChartRadius(this.curLineAnnStruct.getLine(), getM_chartPanel().getScreenDataArea(), getTlSubplot());
        this.curLineAnnStruct.getM_arcAnnStructObj().setM_ArcRadiusOnChart(r);
      }
      if ((this.curLineAnnStruct != null) && 
        (!this.lineAnnStructList.contains(this.curLineAnnStruct)))
        this.lineAnnStructList.add(this.curLineAnnStruct);
    }
  }

  public void mouseEntered(MouseEvent e)
  {
  }

  public void mouseExited(MouseEvent e)
  {
  }

  public void mouseClicked(MouseEvent event) {
  }

  public void mouseDragged(MouseEvent event) {
    if (event.isPopupTrigger()) {
      return;
    }
    final Point p = event.getPoint();
    Runnable mouseDraggedDelegate = new Runnable()
    {
      public void run() {
        UserAnnotations.this.mouseDraggedEventDelegate(p);
      }
    };
    SwingUtilities.invokeLater(mouseDraggedDelegate);
  }

  public void mousePressed(MouseEvent event) {
    if (event.isPopupTrigger()) {
      return;
    }
    final Point p = event.getPoint();
    Runnable mousePressedDelegate = new Runnable()
    {
      public void run()
      {
        UserAnnotations.this.mousePressedEventDelegate(p);
      }
    };
    SwingUtilities.invokeLater(mousePressedDelegate);
  }

  public void mouseMoved(MouseEvent event) {
    if (event.isPopupTrigger()) {
      return;
    }
    final Point p = event.getPoint();

    Runnable mouseMovedDelegate = new Runnable()
    {
      public void run() {
        UserAnnotations.this.mouseMovedEventDelegate(p);
      }
    };
    SwingUtilities.invokeLater(mouseMovedDelegate);
  }

  public void mouseReleased(MouseEvent event) {
    if (event.isPopupTrigger()) {
      return;
    }
    final Point p = event.getPoint();
    Runnable mouseReleasedDelegate = new Runnable()
    {
      public void run()
      {
        UserAnnotations.this.mouseReleasedEventDelegate(p);
      }
    };
    SwingUtilities.invokeLater(mouseReleasedDelegate);
  }

  private void highLightLineAnnotation(Point p)
  {
    if (this.lineAnnStructList.size() > 0)
    {
      for (int i = 0; i < this.lineAnnStructList.size(); i++)
        if (!this.freezeRect) {
          this.curLineAnnStruct = ((LineAnnotationsStruct)this.lineAnnStructList.get(i));
          this.m_shapeType = this.curLineAnnStruct.getShape();
          setRectWidthHeight(p);
          if (this.curLineAnnStruct.IsPointIntersectLine(mousePointToChartPoint(p), getWidth(), getHeight()))
          {
            drawRectangles(this.curLineAnnStruct.getLine().getP1(), this.curLineAnnStruct.getLine().getP2());
          }
        }
    }
  }

  private void drawRectangles(Point2D startPoint, Point2D endPoint)
  {
    if (!this.freezeRect) {
      getRects()[0].setFrame(startPoint.getX() - getWidth() / 2.0D, startPoint.getY() - getHeight() / 2.0D, getWidth(), getHeight());
      getRects()[1].setFrame(endPoint.getX() - getWidth() / 2.0D, endPoint.getY() - getHeight() / 2.0D, getWidth(), getHeight());

      addMyAnnotation(new XYShapeAnnotation(getRects()[0]));
      addMyAnnotation(new XYShapeAnnotation(getRects()[1]));
      setCenterAnnotation(startPoint, endPoint);
      this.freezeRect = true;
    }
  }

  private void clearRects() {
    if (this.tlSubplot != null) {
      removeMyAnnotation(new XYShapeAnnotation(this.rects[0]));
      removeMyAnnotation(new XYShapeAnnotation(this.rects[1]));
      removeMyAnnotation(new XYShapeAnnotation(this.rects[2]));
      this.freezeRect = false;
    }
  }

  private void setCenterAnnotation(Point2D p1, Point2D p2) {
    removeMyAnnotation(new XYShapeAnnotation(getRects()[2]));
    double cx = p1.getX() + (p2.getX() - p1.getX()) / 2.0D;
    double cy = p1.getY() + (p2.getY() - p1.getY()) / 2.0D;
    getRects()[2].setFrameFromCenter(cx, cy, cx + getWidth() / 2.0D, cy + getHeight() / 2.0D);
    addMyAnnotation(new XYShapeAnnotation(getRects()[2]));
  }

  private void setRect(int index, double x, double y) {
    if (index == 2) {
      double dy = y - getRects()[2].y;
      double dx = x - getRects()[2].x;
      removeMyAnnotation(new XYShapeAnnotation(getRects()[0]));
      getRects()[0].setFrame(getRects()[0].x + dx, getRects()[0].y + dy, getWidth(), getHeight());
      addMyAnnotation(new XYShapeAnnotation(getRects()[0]));
      removeMyAnnotation(new XYShapeAnnotation(getRects()[1]));
      getRects()[1].setFrame(getRects()[1].x + dx, getRects()[1].y + dy, getWidth(), getHeight());
      addMyAnnotation(new XYShapeAnnotation(getRects()[1]));
      this.curCursor = Cursor.getPredefinedCursor(13);
      getM_chartPanel().setCursor(this.curCursor);
    } else {
      removeMyAnnotation(new XYShapeAnnotation(getRects()[index]));
      getRects()[index].setFrame(x, y, getWidth(), getHeight());
      addMyAnnotation(new XYShapeAnnotation(getRects()[index]));
      this.curCursor = Cursor.getPredefinedCursor(7);
      getM_chartPanel().setCursor(this.curCursor);
    }
    setLine();
  }

  private void setLine() {
    XYShapeAnnotation sa = null;
    try {
      if (this.curLineAnnStruct != null) {
        Line2D.Double l = this.curLineAnnStruct.getLine();
        removeMyAnnotation(new XYShapeAnnotation(l));
        if (this.lineAnnStructList.contains(this.curLineAnnStruct)) {
          this.lineAnnStructList.remove(this.curLineAnnStruct);
        }
        l.setLine(getCenter(getRects()[0]), getCenter(getRects()[1]));
        sa = new XYShapeAnnotation(l);
        addMyAnnotation(sa);
        setCenterAnnotation(getCenter(getRects()[0]), getCenter(getRects()[1]));
        this.curLineAnnStruct = new LineAnnotationsStruct(l, this.curLineAnnStruct.getShape());
      }
    } catch (Exception ex) {
      ex.printStackTrace();
    } finally {
      sa = null;
    }
  }

  private Point2D.Double getCenter(Rectangle2D.Double r) {
    return new Point2D.Double(r.getCenterX(), r.getCenterY());
  }

  public void setDragFlag(boolean flag)
  {
    enableTrendLines();
    this.m_isdragShape = flag;
  }

  public void clearAllAnnotations()
  {
    try {
      ((XYPlot)getM_chartPanel().getCombinedDomainPlot().getSubplots().get(0)).clearAnnotations();
      this.lineAnnStructList.clear();
      this.curLineAnnStruct = null;
    }
    catch (Exception ex) {
    }
  }

  public void EventUpdate() {
    if (this.isDrawingsEnabled) {
      return;
    }
    if (this.lineAnnStructList != null) {
      Iterator i = this.lineAnnStructList.iterator();
      while (i.hasNext()) {
        LineAnnotationsStruct l = (LineAnnotationsStruct)i.next();
        if (l.getShape() == UserDrawingShape.FINBONACCIARCS) {
          FibonacciArcsAnnotations.RemoveArcs(l.getLine(), l.getM_arcAnnStructObj().getM_arcRadius(), l.getM_arcAnnStructObj().getM_ArcRadiusOnChart(), getTlSubplot());

          FibonacciArcsAnnotations.drawArcs(l.getLine(), getM_chartPanel().getScreenDataArea(), getTlSubplot());
          l.getM_arcAnnStructObj().setM_arcRadius(FibonacciArcsAnnotations.getArcRadius(l.getLine(), getM_chartPanel().getScreenDataArea(), getTlSubplot()));
          l.getM_arcAnnStructObj().setM_ArcRadiusOnChart(FibonacciArcsAnnotations.getArcChartRadius(l.getLine(), getM_chartPanel().getScreenDataArea(), getTlSubplot()));
        }
      }
    }
  }

  public void RestoreAnnotations(ArrayList<LineStruct> lineStructList) {
    try {
      Iterator iter = lineStructList.iterator();
      while (iter.hasNext()) {
        LineStruct ls = (LineStruct)iter.next();
        if (this.lineAnnStructList == null) {
          this.lineAnnStructList = new ArrayList();
        }
        Line2D.Double l = new Line2D.Double(ls.getM_x1(), ls.getM_y1(), ls.getM_x2(), ls.getM_y2());
        UserDrawingShape tmpShape = UserDrawingShape.valueOf(ls.getM_shape());

        LineAnnotationsStruct las = new LineAnnotationsStruct(l, tmpShape);
        if (tmpShape == UserDrawingShape.FINBONACCIARCS) {
          las.getM_arcAnnStructObj().setM_ArcRadiusOnChart(ls.getM_arcAnnStruct().getM_ArcRadiusOnChart());
          las.getM_arcAnnStructObj().setM_arcRadius(ls.getM_arcAnnStruct().getM_arcRadius());
        }

        this.lineAnnStructList.add(las);
      }
      this.tlSubplot = ((XYPlot)((CombinedDomainXYPlot)this.m_chartPanel.getChart().getPlot()).getSubplots().get(0));
      DrawAnnotationsOnChart();
    } catch (Exception ex) {
      this.lineAnnStructList.clear();
      ex.printStackTrace();
    }
  }

  private void DrawAnnotationsOnChart() throws Exception
  {
    Iterator iter = this.lineAnnStructList.iterator();
    while (iter.hasNext()) {
      LineAnnotationsStruct ls = (LineAnnotationsStruct)iter.next();
      switch (ls.getShape().ordinal()) {
      case 0:
        addMyAnnotation(new XYShapeAnnotation(ls.getLine()));
        break;
      case 1:
        FiboRetracementAnnotations.drawFRetracementLines(ls.getLine(), this.tlSubplot);
        break;
      case 2:
        addMyAnnotation(new XYShapeAnnotation(ls.getLine()));
        FibonacciArcsAnnotations.drawArcs(ls.getLine(), this.m_chartPanel.getScreenDataArea(), this.tlSubplot);
      }
    }
  }

  public static enum UserDrawingShape
  {
    TRENDLINE, FIBONACCIRETRACEMENT, FINBONACCIARCS, NODRAWING;
  }
}