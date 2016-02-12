 package org.smchart.gui;
 
 import java.awt.EventQueue;
 import java.awt.event.ActionEvent;
 import java.awt.event.ActionListener;
 import javax.swing.JFrame;
 import javax.swing.JMenu;
 import javax.swing.JMenuBar;
 import javax.swing.JMenuItem;
 import org.smchart.main.CmdMgr;
 import org.smchart.util.ScreenCapture;
 
 public class MenuBar
 {
/*  22 */   private static MenuBar instance = null;
   private JFrame frame;
   private CmdMgr cmdMgrObj;
 
   public static MenuBar getInstance(JFrame frame, CmdMgr cmdMgr)
   {
/*  27 */     if (instance == null) {
/*  28 */       instance = new MenuBar(frame, cmdMgr);
     }
/*  30 */     return instance;
   }
 
   private MenuBar(JFrame frame, CmdMgr cmdMgr) {
/*  34 */     this.frame = frame;
/*  35 */     this.cmdMgrObj = cmdMgr;
/*  36 */     JMenuBar jMenuBar2 = new JMenuBar();
 
/*  38 */     JMenu jMenu3 = new JMenu();
/*  39 */     JMenuItem jMenuItem1 = new JMenuItem();
/*  40 */     JMenuItem jMenuItemOpen = new JMenuItem();
/*  41 */     JMenuItem jMenuItemSaveChart = new JMenuItem();
/*  42 */     JMenuItem jMenuItemExit = new JMenuItem();
/*  43 */     JMenu jMenu4 = new JMenu();
/*  44 */     JMenu menuChart = new JMenu();
 
/*  46 */     jMenu3.setText("File");
 
/*  48 */     jMenuItem1.setText("Save Image");
/*  49 */     jMenuItem1.addActionListener(new ActionListener()
     {
       public void actionPerformed(ActionEvent e) {
         try {
/*  53 */           MenuBar.this.screenCapture();
         } catch (Exception ex) {
/*  55 */           ex.printStackTrace();
         }
       }
     });
/*  59 */     jMenu3.add(jMenuItem1);
 
/*  61 */     jMenuItemOpen.setText("Open Chart");
/*  62 */     jMenuItemOpen.addActionListener(new ActionListener()
     {
       public void actionPerformed(ActionEvent e) {
         try {
/*  66 */           MenuBar.this.openChart();
         } catch (Exception ex) {
/*  68 */           ex.printStackTrace();
         }
       }
     });
/*  72 */     jMenu3.add(jMenuItemOpen);
 
/*  74 */     jMenuItemSaveChart.setText("Save Chart");
/*  75 */     jMenuItemSaveChart.addActionListener(new ActionListener()
     {
       public void actionPerformed(ActionEvent e) {
         try {
/*  79 */           MenuBar.this.saveChart();
         } catch (Exception ex) {
/*  81 */           ex.printStackTrace();
         }
       }
     });
/*  85 */     jMenu3.add(jMenuItemSaveChart);
 
/*  87 */     jMenuItemExit.setText("Exit");
/*  88 */     jMenuItemExit.addActionListener(new ActionListener()
     {
       public void actionPerformed(ActionEvent e) {
         try {
/*  92 */           System.exit(0);
         } catch (Exception ex) {
/*  94 */           ex.printStackTrace();
         }
       }
     });
/*  98 */     jMenu3.add(jMenuItemExit);
 
/* 100 */     jMenuBar2.add(jMenu3);
 
/* 105 */     menuChart.setText("Chart");
 
/* 107 */     JMenuItem menuItemAddIndicator = new JMenuItem("Indicators");
 
/* 109 */     menuItemAddIndicator.addActionListener(new ActionListener()
     {
       public void actionPerformed(ActionEvent e) {
/* 112 */         EventQueue.invokeLater(new Runnable()
         {
           public void run() {
/* 115 */             IndicatorList il = new IndicatorList();
/* 116 */             il.setCommandManager(MenuBar.this.cmdMgrObj);
/* 117 */             il.setVisible(true);
           }
         });
       }
     });
/* 122 */     menuChart.add(menuItemAddIndicator);
 
/* 124 */     JMenuItem menuItemOptions = new JMenuItem("Options");
 
/* 126 */     menuItemOptions.addActionListener(new ActionListener()
     {
       public void actionPerformed(ActionEvent e) {
/* 129 */         MenuBar.this.menuItemOptionsActionPerformed(e);
       }
     });
/* 132 */     menuChart.add(menuItemOptions);
 
/* 135 */     jMenuBar2.add(menuChart);
 
/* 138 */     JMenu jHelpMenu = new JMenu();
/* 139 */     jHelpMenu.setText("Help");
 
/* 141 */     JMenuItem jHelpMenuItem = new JMenuItem();
 
/* 143 */     jHelpMenuItem.setText("HelpWith");
 
/* 150 */     jHelpMenu.add(jHelpMenuItem);
 
/* 152 */     JMenuItem jAboutMenuItem = new JMenuItem();
 
/* 154 */     jAboutMenuItem.setText("About...");
/* 155 */     jAboutMenuItem.addActionListener(new ActionListener()
     {
       public void actionPerformed(ActionEvent evt) {
/* 158 */         MenuBar.this.jAboutMenuItemActionPerformed(evt);
       }
     });
/* 161 */     jHelpMenu.add(jAboutMenuItem);
 
/* 163 */     jMenuBar2.add(jHelpMenu);
 
/* 165 */     frame.setJMenuBar(jMenuBar2);
   }
 
   private void screenCapture() throws Exception {
/* 169 */     ScreenCapture sc = new ScreenCapture(this.frame);
/* 170 */     sc.TakeScreenShot();
   }
 
   private void saveChart() throws Exception {
/* 174 */     this.cmdMgrObj.saveChartSettings();
   }
 
   private void openChart() throws Exception {
/* 178 */     this.cmdMgrObj.OpenChart();
   }
 
   private void menuItemOptionsActionPerformed(ActionEvent evt) {
/* 182 */     this.cmdMgrObj.DisplayToolOptions();
   }
 
   private void jAboutMenuItemActionPerformed(ActionEvent evt)
   {
/* 187 */     new AboutJDialog(this.frame, true).setVisible(true);
   }
 }