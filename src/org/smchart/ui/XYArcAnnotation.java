package org.smchart.ui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Stroke;
import java.awt.geom.Arc2D;
import java.awt.geom.Arc2D.Double;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import org.jfree.chart.annotations.AbstractXYAnnotation;
import org.jfree.chart.annotations.XYAnnotation;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.Plot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.PlotRenderingInfo;
import org.jfree.chart.plot.XYPlot;
import org.jfree.io.SerialUtilities;
import org.jfree.ui.RectangleEdge;
import org.jfree.util.ObjectUtilities;
import org.jfree.util.PaintUtilities;
import org.jfree.util.PublicCloneable;

public class XYArcAnnotation extends AbstractXYAnnotation
  implements XYAnnotation, Cloneable, PublicCloneable, Serializable
{
  private double x;
  private double y;
  private double radius;
  private transient Stroke stroke;
  private transient Paint paint;

  public XYArcAnnotation(double inx, double iny, double rad)
  {
    this.x = inx;
    this.y = iny;
    this.radius = rad;
    this.stroke = new BasicStroke(1.0F);
    this.paint = Color.black;
  }

  public XYArcAnnotation(double inx, double iny, double rad, Stroke stroke, Paint paint) {
    this.x = inx;
    this.y = iny;
    this.radius = rad;
    this.stroke = stroke;
    this.paint = paint;
  }

  public void draw(Graphics2D g2, XYPlot plot, Rectangle2D dataArea, ValueAxis domainAxis, ValueAxis rangeAxis, int rendererIndex, PlotRenderingInfo info)
  {
    PlotOrientation orientation = plot.getOrientation();
    RectangleEdge domainEdge = Plot.resolveDomainAxisLocation(plot.getDomainAxisLocation(), orientation);
    RectangleEdge rangeEdge = Plot.resolveRangeAxisLocation(plot.getRangeAxisLocation(), orientation);

    double xx = domainAxis.valueToJava2D(this.x, dataArea, domainEdge);
    double yy = rangeAxis.valueToJava2D(this.y, dataArea, rangeEdge);

    if (orientation == PlotOrientation.HORIZONTAL) {
      double temp = xx;
      xx = yy;
      yy = temp;
    }

    Arc2D.Double a1 = new Arc2D.Double();
    a1.setArcByCenter(xx, yy, this.radius, 145.0D, 250.0D, 0);
    g2.setPaint(this.paint);
    g2.setStroke(this.stroke);
    g2.draw(a1);
  }

  public boolean equals(Object obj)
  {
    if (obj == this) {
      return true;
    }
    if (!super.equals(obj)) {
      return false;
    }
    if (!(obj instanceof XYArcAnnotation)) {
      return false;
    }
    XYArcAnnotation that = (XYArcAnnotation)obj;
    if (this.x != that.x) {
      return false;
    }
    if (this.y != that.y) {
      return false;
    }
    if (this.radius != that.radius) {
      return false;
    }
    if (!PaintUtilities.equal(this.paint, that.paint)) {
      return false;
    }
    if (!ObjectUtilities.equal(this.stroke, that.stroke)) {
      return false;
    }

    return true;
  }

  public int hashCode()
  {
    long temp = Long.parseLong("" + this.x);
    int result = (int)(temp ^ temp >>> 32);
    temp = Long.parseLong("" + this.y);
    result = 29 * result + (int)(temp ^ temp >>> 32);
    temp = Long.parseLong("" + this.radius);
    result = 29 * result + (int)(temp ^ temp >>> 32);
    return result;
  }

  public Object clone() throws CloneNotSupportedException
  {
    return super.clone();
  }

  private void writeObject(ObjectOutputStream stream) throws IOException
  {
    stream.defaultWriteObject();
    SerialUtilities.writePaint(this.paint, stream);
    SerialUtilities.writeStroke(this.stroke, stream);
  }

  private void readObject(ObjectInputStream stream)
    throws IOException, ClassNotFoundException
  {
    stream.defaultReadObject();
    this.paint = SerialUtilities.readPaint(stream);
    this.stroke = SerialUtilities.readStroke(stream);
  }
}