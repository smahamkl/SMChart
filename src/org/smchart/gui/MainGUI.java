 package org.smchart.gui;
 
 import java.awt.Container;
 import java.awt.Dimension;
 import java.awt.EventQueue;
 import java.awt.Font;
 import java.awt.event.ActionEvent;
 import java.awt.event.ActionListener;
 import java.awt.event.AdjustmentEvent;
 import java.awt.event.AdjustmentListener;
 import java.awt.event.MouseAdapter;
 import java.awt.event.MouseEvent;
 import java.io.PrintStream;
 import java.net.URL;
 import javax.swing.DefaultComboBoxModel;
 import javax.swing.DesktopManager;
 import javax.swing.GroupLayout;
 import javax.swing.GroupLayout.Alignment;
 import javax.swing.GroupLayout.ParallelGroup;
 import javax.swing.GroupLayout.SequentialGroup;
 import javax.swing.ImageIcon;
 import javax.swing.InputMap;
 import javax.swing.JButton;
 import javax.swing.JComboBox;
 import javax.swing.JDesktopPane;
 import javax.swing.JInternalFrame;
 import javax.swing.JMenuItem;
 import javax.swing.JPanel;
 import javax.swing.JPopupMenu;
 import javax.swing.JScrollBar;
 import javax.swing.JScrollPane;
 import javax.swing.JSplitPane;
 import javax.swing.JToggleButton;
 import javax.swing.JToolBar;
 import javax.swing.JToolBar.Separator;
 import javax.swing.KeyStroke;
 import javax.swing.LayoutStyle.ComponentPlacement;
 import javax.swing.event.InternalFrameEvent;
 import javax.swing.event.InternalFrameListener;
 import org.jfree.chart.ChartPanel;
 import org.smchart.charts.ChartGenerator.ChartScale;
 import org.smchart.charts.ChartGenerator.ChartType;
 import org.smchart.main.CmdMgr;
 import org.smchart.ui.UserAnnotations.UserDrawingShape;
 import org.smchart.ui.UserAnnotations;
 import org.smchart.charts.ChartGenerator;
 
 public class MainGUI extends JPanel
 {
   private int scrollbarbefore;
   private CmdMgr cmdMgrObj;
   private static MainGUI instance;
   private ImageIcon[] images;
/*  29 */   private String[] drawings = { "LINE", "FIBORETRACEMENT" };
   private JButton btnChartResize;
   private JButton btnDelete;
   private JButton btnErase;
   private JButton btnMoveDrawing;
   private JButton jBtnAddIndicator;
   private JButton jBtnChartSize;
   private JButton jBtnPopDrawings;
   private JComboBox jComboBox1;
   private JDesktopPane jDesktopPane1;
   private JScrollBar jScrollBar1;
   private JToolBar.Separator jSeparator1;
   private JToolBar.Separator jSeparator2;
   private JToolBar.Separator jSeparator3;
   private JToolBar.Separator jSeparator4;
   private JToolBar.Separator jSeparator5;
   private JToolBar.Separator jSeparator6;
   private JToolBar.Separator jSeparator7;
   private JToolBar.Separator jSeparator9;
   private JSplitPane jSplitPane1;
   private JToggleButton jTogChartScale;
   private JToolBar jToolBar1;
   private JScrollPane leftScrollPane;
 
   public static MainGUI getInstance(CmdMgr cmdMgr)
     throws Exception
   {
/*  32 */     if (instance == null) {
/*  33 */       instance = new MainGUI(cmdMgr);
     }
/*  35 */     return instance;
   }
 
   private MainGUI(CmdMgr cmdMgr) throws Exception {
/*  39 */     this.cmdMgrObj = cmdMgr;
/*  40 */     initComponents();
/*  41 */     this.scrollbarbefore = this.jScrollBar1.getValue();
 
/*  43 */     registerKeyEvents();
   }
 
   public void addInternalFrame(String title, ChartPanel panel)
   {
/*  48 */     JInternalFrame jif = new JInternalFrame(title, true, true, true, true);
/*  49 */     jif.addInternalFrameListener(new InternalFrameListener()
     {
       public void internalFrameClosing(InternalFrameEvent e)
       {
       }
 
       public void internalFrameDeactivated(InternalFrameEvent e)
       {
       }
 
       public void internalFrameActivated(InternalFrameEvent e)
       {
       }
 
       public void internalFrameDeiconified(InternalFrameEvent e)
       {
       }
 
       public void internalFrameIconified(InternalFrameEvent e)
       {
       }
 
       public void internalFrameClosed(InternalFrameEvent e)
       {
       }
 
       public void internalFrameOpened(InternalFrameEvent e)
       {
       }
     });
       jif.getContentPane().add(panel);       
       jDesktopPane1.add(jif);
       jScrollBar1.setValue(jScrollBar1.getMaximum() / 2);
       jScrollBar1.setBlockIncrement(1);
       jSplitPane1.setDividerLocation(120);
       jDesktopPane1.getDesktopManager().openFrame(jif);
       jif.pack();
     try
     {
/*  74 */       jif.setMaximum(true);
     }
     catch (Exception ex) {
     }
/*  78 */     jif.setVisible(true);
/*  79 */     resetGUI();
   }
   public void resetGUI() {
/*  82 */     this.jComboBox1.setSelectedIndex(0);
   }
 
protected static ImageIcon createImageIcon(String file) {
        java.net.URL imgURL = MainGUI.class.getResource("/org/smchart/images/" + file);
        if (imgURL != null) {
            return new ImageIcon(imgURL);
        } else {
            System.err.println("Couldn't find file: " + file);
            return null;
        }
    }
 
   public JScrollPane getLeftPane()
   {
/*  97 */     return this.leftScrollPane;
   }
 
   public JSplitPane getSplitPane() {
/* 101 */     return this.jSplitPane1;
   }
 
   public JScrollBar getScrollBar()
   {
/* 106 */     return this.jScrollBar1;
   }
 
   private void jButtonClearLinesActionPerformed(ActionEvent evt)
   {
/* 111 */     this.cmdMgrObj.ClearUserChartDrawings();
   }
 
   private void initComponents()
   {
/* 122 */     this.jSplitPane1 = new JSplitPane();
/* 123 */     this.leftScrollPane = new JScrollPane();
/* 124 */     this.jDesktopPane1 = new JDesktopPane();
/* 125 */     this.jToolBar1 = new JToolBar();
/* 126 */     this.btnChartResize = new JButton();
/* 127 */     this.jSeparator4 = new JToolBar.Separator();
/* 128 */     this.btnMoveDrawing = new JButton();
/* 129 */     this.jSeparator2 = new JToolBar.Separator();
/* 130 */     this.btnErase = new JButton();
/* 131 */     this.jSeparator3 = new JToolBar.Separator();
/* 132 */     this.btnDelete = new JButton();
/* 133 */     this.jSeparator5 = new JToolBar.Separator();
/* 134 */     this.jTogChartScale = new JToggleButton();
/* 135 */     this.jSeparator6 = new JToolBar.Separator();
/* 136 */     this.jBtnAddIndicator = new JButton();
/* 137 */     this.jSeparator7 = new JToolBar.Separator();
/* 138 */     this.jBtnChartSize = new JButton();
/* 139 */     this.jSeparator9 = new JToolBar.Separator();
/* 140 */     this.jBtnPopDrawings = new JButton();
/* 141 */     this.jSeparator1 = new JToolBar.Separator();
/* 142 */     this.jComboBox1 = new JComboBox();
/* 143 */     this.jScrollBar1 = new JScrollBar();
 
/* 145 */     this.jSplitPane1.setDividerLocation(120);
/* 146 */     this.jSplitPane1.setOneTouchExpandable(true);
/* 147 */     this.jSplitPane1.setPreferredSize(new Dimension(400, 200));
 
/* 149 */     this.leftScrollPane.setAutoscrolls(true);
/* 150 */     this.jSplitPane1.setLeftComponent(this.leftScrollPane);
/* 151 */     this.jSplitPane1.setRightComponent(this.jDesktopPane1);
 
/* 153 */     this.jToolBar1.setRollover(true);
/* 154 */     this.jToolBar1.setMaximumSize(new Dimension(75, 53));
/* 155 */     this.jToolBar1.setMinimumSize(new Dimension(75, 53));
 
btnChartResize.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/smchart/images/resize.gif")));
/* 158 */     this.btnChartResize.setToolTipText("Chart zoom enable");
/* 159 */     this.btnChartResize.setFocusable(false);
/* 160 */     this.btnChartResize.setHorizontalTextPosition(0);
/* 161 */     this.btnChartResize.setVerticalTextPosition(3);
/* 162 */     this.btnChartResize.addMouseListener(new MouseAdapter() {
       public void mouseClicked(MouseEvent evt) {
/* 164 */         MainGUI.this.btnChartResizeMouseClicked(evt);
       }
     });
/* 167 */     this.jToolBar1.add(this.btnChartResize);
/* 168 */     this.jToolBar1.add(this.jSeparator4);
 
btnMoveDrawing.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/smchart/images/translateBtn.gif")));
/* 171 */     this.btnMoveDrawing.setToolTipText("Move Drawing");
/* 172 */     this.btnMoveDrawing.setFocusable(false);
/* 173 */     this.btnMoveDrawing.setHorizontalTextPosition(0);
/* 174 */     this.btnMoveDrawing.setVerticalTextPosition(3);
/* 175 */     this.btnMoveDrawing.addMouseListener(new MouseAdapter() {
       public void mouseClicked(MouseEvent evt) {
/* 177 */         MainGUI.this.btnMoveDrawingMouseClicked(evt);
       }
     });
/* 180 */     this.jToolBar1.add(this.btnMoveDrawing);
/* 181 */     this.jToolBar1.add(this.jSeparator2);
 
btnErase.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/smchart/images/erase.gif")));
/* 184 */     this.btnErase.setToolTipText("Clear drawings");
/* 185 */     this.btnErase.setFocusable(false);
/* 186 */     this.btnErase.setHorizontalTextPosition(0);
/* 187 */     this.btnErase.setVerticalTextPosition(3);
/* 188 */     this.btnErase.addMouseListener(new MouseAdapter() {
       public void mouseClicked(MouseEvent evt) {
/* 190 */         MainGUI.this.btnEraseMouseClicked(evt);
       }
     });
/* 193 */     this.jToolBar1.add(this.btnErase);
/* 194 */     this.jToolBar1.add(this.jSeparator3);
 
btnDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/smchart/images/delete.jpg")));
/* 197 */     this.btnDelete.setFocusable(false);
/* 198 */     this.btnDelete.setHorizontalTextPosition(0);
/* 199 */     this.btnDelete.setVerticalTextPosition(3);
/* 200 */     this.btnDelete.addMouseListener(new MouseAdapter() {
       public void mouseClicked(MouseEvent evt) {
/* 202 */         MainGUI.this.btnDeleteMouseClicked(evt);
       }
     });
/* 205 */     this.jToolBar1.add(this.btnDelete);
/* 206 */     this.jToolBar1.add(this.jSeparator5);
 
jTogChartScale.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/smchart/images/log.gif")));
/* 210 */     this.jTogChartScale.setFocusable(false);
/* 211 */     this.jTogChartScale.setHorizontalTextPosition(0);
/* 212 */     this.jTogChartScale.setVerticalTextPosition(3);
/* 213 */     this.jTogChartScale.addMouseListener(new MouseAdapter() {
       public void mouseClicked(MouseEvent evt) {
/* 215 */         MainGUI.this.jTogChartScaleMouseClicked(evt);
       }
     });
/* 218 */     this.jToolBar1.add(this.jTogChartScale);
/* 219 */     this.jToolBar1.add(this.jSeparator6);
 
jBtnAddIndicator.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/smchart/images/indicator.gif"))); 
/* 222 */     this.jBtnAddIndicator.setToolTipText("Add Indicator");
/* 223 */     this.jBtnAddIndicator.setFocusable(false);
/* 224 */     this.jBtnAddIndicator.setHorizontalTextPosition(0);
/* 225 */     this.jBtnAddIndicator.setVerticalTextPosition(3);
/* 226 */     this.jBtnAddIndicator.addMouseListener(new MouseAdapter() {
       public void mouseClicked(MouseEvent evt) {
/* 228 */         MainGUI.this.jBtnAddIndicatorMouseClicked(evt);
       }
     });
/* 231 */     this.jToolBar1.add(this.jBtnAddIndicator);
/* 232 */     this.jToolBar1.add(this.jSeparator7);
 
jBtnChartSize.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/smchart/images/chartplots.gif")));
/* 235 */     this.jBtnChartSize.setToolTipText("Set Chart Plot Weights");
/* 236 */     this.jBtnChartSize.setFocusable(false);
/* 237 */     this.jBtnChartSize.setHorizontalTextPosition(0);
/* 238 */     this.jBtnChartSize.setVerticalTextPosition(3);
/* 239 */     this.jBtnChartSize.addMouseListener(new MouseAdapter() {
       public void mouseClicked(MouseEvent evt) {
/* 241 */         MainGUI.this.jBtnChartSizeMouseClicked(evt);
       }
     });
/* 244 */     this.jToolBar1.add(this.jBtnChartSize);
/* 245 */     this.jToolBar1.add(this.jSeparator9);
 
jBtnPopDrawings.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/smchart/images/LINE.gif")));
/* 249 */     this.jBtnPopDrawings.setFocusable(false);
/* 250 */     this.jBtnPopDrawings.setHorizontalTextPosition(0);
/* 251 */     this.jBtnPopDrawings.setVerticalTextPosition(3);
/* 252 */     this.jBtnPopDrawings.addMouseListener(new MouseAdapter() {
       public void mouseClicked(MouseEvent evt) {
/* 254 */         MainGUI.this.jBtnPopDrawingsMouseClicked(evt);
       }
     });
/* 257 */     this.jToolBar1.add(this.jBtnPopDrawings);
/* 258 */     this.jToolBar1.add(this.jSeparator1);
 
/* 260 */     this.jComboBox1.setFont(new Font("Tahoma", 0, 10));
/* 261 */     this.jComboBox1.setModel(new DefaultComboBoxModel(new String[] { "CANDLESTICK", "CLOSEPRICE", "HIGHLOW" }));
/* 262 */     this.jComboBox1.setAlignmentX(1.0F);
/* 263 */     this.jComboBox1.setMaximumSize(new Dimension(100, 30));
/* 264 */     this.jComboBox1.addActionListener(new ActionListener() {
       public void actionPerformed(ActionEvent evt) {
/* 266 */         MainGUI.this.jComboBox1ActionPerformed(evt);
       }
     });
/* 269 */     this.jToolBar1.add(this.jComboBox1);
 
/* 271 */     this.jScrollBar1.setMaximum(500);
/* 272 */     this.jScrollBar1.setOrientation(0);
/* 273 */     this.jScrollBar1.addAdjustmentListener(new AdjustmentListener() {
       public void adjustmentValueChanged(AdjustmentEvent evt) {
/* 275 */         MainGUI.this.jScrollBar1AdjustmentValueChanged(evt);
       }
     });
/* 279 */     GroupLayout layout = new GroupLayout(this);
/* 280 */     setLayout(layout);
/* 281 */     layout.setHorizontalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(this.jSplitPane1, -1, 710, 32767).addComponent(this.jScrollBar1, -1, 710, 32767).addGroup(layout.createSequentialGroup().addComponent(this.jToolBar1, -1, 700, 32767).addContainerGap()));
 
/* 289 */     layout.setVerticalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addComponent(this.jToolBar1, -2, 32, -2).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(this.jSplitPane1, -1, 424, 32767).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(this.jScrollBar1, -2, -1, -2)));
   }
 
   private void jScrollBar1AdjustmentValueChanged(AdjustmentEvent evt)
   {
/* 302 */     this.cmdMgrObj.adjustChartScroll(evt.getValue(), this.scrollbarbefore);
/* 303 */     this.scrollbarbefore = this.jScrollBar1.getValue();
   }
 
   private void jComboBox1ActionPerformed(ActionEvent evt)
   {
     try
     {
/* 310 */       JComboBox cb = (JComboBox)evt.getSource();
/* 311 */        cmdMgrObj.ChangeChartType((ChartGenerator.ChartType) ChartGenerator.ChartType.valueOf(cb.getSelectedItem().toString()));
     } catch (Exception ex) {
/* 313 */       ex.printStackTrace();
     }
   }
 
   private void btnMoveDrawingMouseClicked(MouseEvent evt)
   {
/* 319 */     this.cmdMgrObj.setDragFlag(true);
   }
 
   private void btnEraseMouseClicked(MouseEvent evt)
   {
/* 324 */     this.cmdMgrObj.setDragFlag(false);
/* 325 */     this.cmdMgrObj.ClearUserChartDrawings();
   }
 
   private void btnChartResizeMouseClicked(MouseEvent evt)
   {
/* 330 */     this.cmdMgrObj.setDragFlag(false);
/* 331 */     this.cmdMgrObj.ChartZoomChange(true);
   }
 
   private void btnDeleteMouseClicked(MouseEvent evt)
   {
/* 336 */     this.cmdMgrObj.deleteselectedMouseDrawing();
   }
 
   private void jTogChartScaleMouseClicked(MouseEvent evt)
   {
     try {
/* 342 */       if (this.jTogChartScale.isSelected())
/* 343 */         this.cmdMgrObj.ChangeChartScale(ChartGenerator.ChartScale.valueOf("LOG"));
       else
/* 345 */         this.cmdMgrObj.ChangeChartScale(ChartGenerator.ChartScale.valueOf("LINEAR"));
     }
     catch (Exception ex) {
/* 348 */       ex.printStackTrace();
     }
   }
 
   private void jBtnAddIndicatorMouseClicked(MouseEvent evt)
   {
/* 354 */     EventQueue.invokeLater(new Runnable()
     {
       public void run() {
/* 357 */         IndicatorList il = new IndicatorList();
/* 358 */         il.setCommandManager(MainGUI.this.cmdMgrObj);
/* 359 */         il.setVisible(true);
       }
     });
   }
 
   private void jBtnChartSizeMouseClicked(MouseEvent evt)
   {
/* 366 */     this.cmdMgrObj.setPlotWeights();
   }
 
   private void jBtnPopDrawingsMouseClicked(MouseEvent evt)
   {
/* 371 */     JPopupMenu popupMenu = new JPopupMenu();
 
/* 373 */     this.images = new ImageIcon[this.drawings.length];
/* 374 */     for (int i = 0; i < this.drawings.length; i++) {
/* 375 */       this.images[i] = createImageIcon(this.drawings[i] + ".gif");
/* 376 */       JMenuItem menuItem = new JMenuItem(this.images[i]);
/* 377 */       menuItem.setName(this.drawings[i]);
/* 378 */       menuItem.addActionListener(new ActionListener()
       {
         public void actionPerformed(ActionEvent evt) {
/* 381 */           JMenuItem item = (JMenuItem)evt.getSource();
/* 382 */           MainGUI.this.UserDrawingsPopupListener(item.getName());
         }
       });
/* 387 */       popupMenu.add(menuItem);
     }
 
/* 390 */     popupMenu.setBounds(evt.getXOnScreen(), evt.getY(), popupMenu.getWidth(), popupMenu.getHeight());
/* 391 */     popupMenu.show(this, evt.getXOnScreen(), evt.getY());
   }
 
   private void UserDrawingsPopupListener(String cmd)
   {
/* 396 */     this.cmdMgrObj.setDragFlag(false);
/* 397 */     this.cmdMgrObj.ChartZoomChange(false);
 
/* 399 */     if (cmd.compareToIgnoreCase("LINE") == 0)
/* 400 */       this.cmdMgrObj.activateChartUserDrawings(UserAnnotations.UserDrawingShape.TRENDLINE);
/* 401 */     else if (cmd.compareToIgnoreCase("FIBORETRACEMENT") == 0)
/* 402 */       this.cmdMgrObj.activateChartUserDrawings(UserAnnotations.UserDrawingShape.FIBONACCIRETRACEMENT);
/* 403 */     else if (cmd.compareToIgnoreCase("FIBOARCS") == 0)
/* 404 */       this.cmdMgrObj.activateChartUserDrawings(UserAnnotations.UserDrawingShape.FINBONACCIARCS);
   }
 
   public void registerKeyEvents()
   {
/* 410 */     InputMap im = new InputMap()
     {
       public Object get(KeyStroke keyStroke) {
/* 413 */         if ((keyStroke.getKeyEventType() == 401) && 
/* 414 */           (keyStroke.getKeyCode() == 127) && (MainGUI.this.cmdMgrObj != null))
         {
/* 416 */           MainGUI.this.cmdMgrObj.deleteselectedMouseDrawing();
         }
 
/* 419 */         return super.get(keyStroke);
       }
     };
/* 422 */     JSplitPane rp = getSplitPane();
/* 423 */     im.setParent(rp.getInputMap(1));
 
/* 425 */     rp.setInputMap(1, im);
   }
 }
