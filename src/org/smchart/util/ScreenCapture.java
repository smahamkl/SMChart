package org.smchart.util;

import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;

public class ScreenCapture
{
  Object cp = null;
  Rectangle m_rect;

  public ScreenCapture(JScrollPane cp)
  {
    this.cp = cp;
    this.m_rect = new Rectangle(cp.getBounds().x, cp.getBounds().y, cp.getBounds().width, cp.getBounds().height);
  }
  public ScreenCapture(JFrame cp) {
    this.cp = cp;
    this.m_rect = new Rectangle(cp.getBounds().x, cp.getBounds().y, cp.getBounds().width, cp.getBounds().height);
  }

  public void TakeScreenShot() throws Exception {
    Robot robot = new Robot();

    BufferedImage image = robot.createScreenCapture(new Rectangle(this.m_rect));
    String userHome = System.getProperty("user.home") + System.getProperty("file.separator");

    String fileName = JOptionPane.showInputDialog(null, "Enter file name(From: User Home) : ", "Chart Screen", 1);
    if (!fileName.toLowerCase().endsWith(".png")) {
      JOptionPane.showMessageDialog(null, "Error: file name must end with \".png\".", "SMChart", 1);
    } else {
      ImageIO.write(image, "png", new File(userHome + fileName));
      JOptionPane.showMessageDialog(null, "Screen captured successfully.", "SMChart", 1);
    }
  }
}