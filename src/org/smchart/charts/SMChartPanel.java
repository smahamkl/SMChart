package org.smchart.charts;

import java.awt.Insets;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.LogarithmicAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.event.ChartChangeListener;
import org.jfree.chart.plot.CombinedDomainXYPlot;
import org.jfree.chart.plot.Plot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.PlotRenderingInfo;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.plot.Zoomable;
import org.jfree.data.Range;
import org.smchart.indicators.IndicatorFactory;
import org.smchart.ui.AbstractChartAnnotations;

public class SMChartPanel extends ChartPanel
  implements MouseMotionListener, MouseListener, KeyListener, ActionListener, ChartChangeListener, Serializable
{
  private ArrayList observers = new ArrayList();
  static final String CLOSE_POPUP = "CLOSE_PLOT";
  static final String OPTIONS_POPUP = "OPTIONS";
  private final JMenuItem closePlotItem = new JMenuItem("Close Sub Plot");
  private final JMenuItem plotOptions = new JMenuItem("Indicator Options");
  private double hLower = 0.0D; private double hUpper = 0.0D; private double vUpper = 0.0D; private double vLower = 0.0D;
  private Point2D selectOrigin = null;
  private DisplayCordinates mui;
  private double childScaleX = 1.0D; private double childScaleY = 1.0D;

  public SMChartPanel(JFreeChart chart) throws Exception {
    super(chart, false);

    JPopupMenu popmenu = super.getPopupMenu();
    popmenu.add(this.closePlotItem);
    popmenu.add(this.plotOptions);
    super.setPopupMenu(popmenu);

    addKeyListener(this);
    enableEvents(16L);
    enableEvents(32L);

    addMouseListener(this);
    addMouseMotionListener(this);
  }

  public void setChartScale(ChartGenerator.ChartScale chartScale, XYPlot mPlot) throws Exception {
    NumberAxis logRngeAxis = null;
    if (chartScale == ChartGenerator.ChartScale.LINEAR) {
      logRngeAxis = new NumberAxis("Price");
    } else if (chartScale == ChartGenerator.ChartScale.LOG) {
      logRngeAxis = new LogarithmicAxis("Log(Price)");
      logRngeAxis.setAutoRange(true);
      logRngeAxis.setTickLabelsVisible(true);
    }
    logRngeAxis.setAutoRangeIncludesZero(false);
    Range rAxisRange = mPlot.getRangeAxis().getRange();
    mPlot.setRangeAxis(logRngeAxis);
    logRngeAxis.setRange(rAxisRange);
    logRngeAxis.setAutoRangeIncludesZero(false);
  }

  public void SetCdleDomainAxisRange(double r1, double r2) {
    ((CombinedDomainXYPlot)getChart().getPlot()).getDomainAxis().setRange(r1, r2);
  }

  public void SetCdleRangeAxisRange(double r1, double r2) {
    ((CombinedDomainXYPlot)getChart().getPlot()).getRangeAxis().setRange(r1, r2);
  }

  public void setPlotWeights(int[] sizeRatios)
  {
    if ((getCombinedDomainPlot().getSubplots().size() > 1) && (getCombinedDomainPlot().getSubplots().size() == sizeRatios.length))
      for (int i = 0; i < getCombinedDomainPlot().getSubplots().size(); i++)
        ((XYPlot)getCombinedDomainPlot().getSubplots().get(i)).setWeight(sizeRatios[i]);
  }

  public CombinedDomainXYPlot getCombinedDomainPlot()
  {
    return (CombinedDomainXYPlot)getChart().getPlot();
  }

  public void mouseMoved(MouseEvent e)
  {
    super.mouseMoved(e);
    final Point p = e.getPoint();

    Runnable displayCordinates = new Runnable()
    {
      public void run() {
        if (SMChartPanel.this.mui != null)
          SMChartPanel.this.mui.mouseMoved_e(p);
      }
    };
    SwingUtilities.invokeLater(displayCordinates);
  }

  public void mouseDragged(MouseEvent e)
  {
    super.mouseDragged(e);
  }

  public void mouseClicked(MouseEvent e)
  {
    super.mouseClicked(e);
  }

  public void mouseEntered(MouseEvent e)
  {
    super.mouseEntered(e);
  }

  public void mouseExited(MouseEvent e)
  {
    super.mouseExited(e);
  }

  public void mousePressed(MouseEvent e)
  {
    super.mousePressed(e);
  }

  public void mouseReleased(MouseEvent e)
  {
    super.mouseReleased(e);
    ObserverNotify();
  }

  public void keyTyped(KeyEvent e)
  {
  }

  public void keyPressed(KeyEvent e)
  {
  }

  public void keyReleased(KeyEvent e)
  {
  }

  protected void displayPopupMenu(int x, int y)
  {
    if (super.getPopupMenu() == null) {
      return;
    }

    if (getCombinedDomainPlot().getSubplots().size() > 1) {
      this.closePlotItem.setActionCommand(x + "," + y);

      this.closePlotItem.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e) {
          JMenuItem it = (JMenuItem)e.getSource();
          if (it.getActionCommand() == null) {
            return;
          }

          String[] location = it.getActionCommand().split(",");
          if (SMChartPanel.this.getCombinedDomainPlot().getSubplots().size() > 1) { PlotRenderingInfo plotInfo = SMChartPanel.this.getChartRenderingInfo().getPlotInfo();
            Point2D selectOrigin;
            try {
              selectOrigin = SMChartPanel.this.translateScreenToJava2D(new Point(Integer.parseInt(location[0]), Integer.parseInt(location[1])));
            }
            catch (Exception ex) {
              return;
            }

            int subplotIndex = plotInfo.getSubplotIndex(selectOrigin);
            if ((subplotIndex > 0) && 
              ((SMChartPanel.this.getCombinedDomainPlot().getSubplots().get(subplotIndex) instanceof XYPlot))) {
              SMChartPanel.this.getCombinedDomainPlot().remove((XYPlot)SMChartPanel.this.getCombinedDomainPlot().getSubplots().get(subplotIndex));
              SMChartPanel.this.closePlotItem.setActionCommand(null);
            }
          }
        }
      });
      this.plotOptions.setActionCommand(x + "," + y);

      this.plotOptions.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e) {
          JMenuItem it = (JMenuItem)e.getSource();
          if (it.getActionCommand() == null) {
            return;
          }

          String[] location = it.getActionCommand().split(",");
          if (SMChartPanel.this.getCombinedDomainPlot().getSubplots().size() > 1) { PlotRenderingInfo plotInfo = SMChartPanel.this.getChartRenderingInfo().getPlotInfo();
            Point2D selectOrigin;
            try {
              selectOrigin = SMChartPanel.this.translateScreenToJava2D(new Point(Integer.parseInt(location[0]), Integer.parseInt(location[1])));
            }
            catch (Exception ex) {
              return;
            }

            int subplotIndex = plotInfo.getSubplotIndex(selectOrigin);
            if (subplotIndex > 0) {
              IndicatorFactory.updateIndicator((XYPlot)SMChartPanel.this.getCombinedDomainPlot().getSubplots().get(subplotIndex));
              SMChartPanel.this.plotOptions.setActionCommand(null);
            }
          }
        }
      });
    }

    super.displayPopupMenu(x, y);
  }

  public void actionPerformed(ActionEvent event)
  {
    super.actionPerformed(event);
    System.out.print("Action performed");
    String command = event.getActionCommand();
    if ((command.equals("ZOOM_IN_BOTH")) || (command.equals("ZOOM_IN_DOMAIN")) || (command.equals("ZOOM_IN_RANGE")) || (command.equals("ZOOM_OUT_BOTH")) || (command.equals("ZOOM_DOMAIN_BOTH")) || (command.equals("ZOOM_RANGE_BOTH")) || (command.equals("ZOOM_RESET_BOTH")) || (command.equals("ZOOM_RESET_DOMAIN")) || (command.equals("ZOOM_RESET_RANGE")))
    {
      ObserverNotify();
    }
  }

  public void AttachObserver(Object obj) {
    if (!this.observers.contains(obj))
      this.observers.add(obj);
  }

  public void DetachObserver(AbstractChartAnnotations ann)
  {
    if (this.observers.contains(ann))
      this.observers.remove(ann);
  }

  public void ObserverNotify()
  {
    if (this.observers.size() > 0) {
      Iterator i = this.observers.iterator();
      while (i.hasNext()) {
        Object obj = i.next();
        if ((obj instanceof AbstractChartAnnotations)) {
          AbstractChartAnnotations ann = (AbstractChartAnnotations)obj;
          ann.EventUpdate();
        }
      }
    }
  }

  public void zoom(Rectangle2D selection)
  {
    setSelectOrigin(translateScreenToJava2D(new Point((int)Math.ceil(selection.getX()), (int)Math.ceil(selection.getY()))));
    PlotRenderingInfo plotInfo = getChartRenderingInfo().getPlotInfo();
    Rectangle2D scaledDataArea = getScreenDataArea((int)selection.getCenterX(), (int)selection.getCenterY());

    if ((selection.getHeight() > 0.0D) && (selection.getWidth() > 0.0D))
    {
      sethLower((selection.getMinX() - scaledDataArea.getMinX()) / scaledDataArea.getWidth());
      sethUpper((selection.getMaxX() - scaledDataArea.getMinX()) / scaledDataArea.getWidth());
      setvLower((scaledDataArea.getMaxY() - selection.getMaxY()) / scaledDataArea.getHeight());
      setvUpper((scaledDataArea.getMaxY() - selection.getMinY()) / scaledDataArea.getHeight());

      Plot p = getChart().getPlot();
      if ((p instanceof Zoomable)) {
        Zoomable z = (Zoomable)p;
        if (z.getOrientation() == PlotOrientation.HORIZONTAL) {
          z.zoomDomainAxes(getvLower(), getvUpper(), plotInfo, getSelectOrigin());
          z.zoomRangeAxes(gethLower(), gethUpper(), plotInfo, getSelectOrigin());
        } else {
          z.zoomDomainAxes(gethLower(), gethUpper(), plotInfo, getSelectOrigin());
          z.zoomRangeAxes(getvLower(), getvUpper(), plotInfo, getSelectOrigin());
        }
      }
    }
  }

  public Rectangle2D getScreenDataArea()
  {
    Rectangle2D dataArea = getChartRenderingInfo().getPlotInfo().getDataArea();
    Insets insets = getInsets();
    double scaleX = getScaleX();
    double scaleY = getScaleY();
    double h;
    double x;
    double y;
    double w;
    if ((scaleX == 0.0D) && (scaleY == 0.0D)) {
       x = dataArea.getX() * this.childScaleX + insets.left;
       y = dataArea.getY() * this.childScaleY + insets.top;
       w = dataArea.getWidth() * this.childScaleX;
      h = dataArea.getHeight() * this.childScaleY;
    } else {
      x = dataArea.getX() * scaleX + insets.left;
      y = dataArea.getY() * scaleY + insets.top;
      w = dataArea.getWidth() * scaleX;
      h = dataArea.getHeight() * scaleY;
    }

    return new Rectangle2D.Double(x, y, w, h);
  }

  public double gethLower()
  {
    return this.hLower;
  }

  public double gethUpper()
  {
    return this.hUpper;
  }

  public double getvUpper()
  {
    return this.vUpper;
  }

  public double getvLower()
  {
    return this.vLower;
  }

  public Point2D getSelectOrigin()
  {
    return this.selectOrigin;
  }

  public void sethLower(double hLower)
  {
    this.hLower = hLower;
  }

  public void sethUpper(double hUpper)
  {
    this.hUpper = hUpper;
  }

  public void setvUpper(double vUpper)
  {
    this.vUpper = vUpper;
  }

  public void setvLower(double vLower)
  {
    this.vLower = vLower;
  }

  public void setSelectOrigin(Point2D selectOrigin)
  {
    this.selectOrigin = selectOrigin;
  }

  public void setChartInfoDisplayer(DisplayCordinates ui) {
    this.mui = ui;
  }

  public void setChildScaleX(double childScaleX)
  {
    this.childScaleX = childScaleX;
  }

  public void setChildScaleY(double childScaleY)
  {
    this.childScaleY = childScaleY;
  }
}