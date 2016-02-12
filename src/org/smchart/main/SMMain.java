/*    */ package org.smchart.main;
/*    */ 
/*    */ import javax.swing.JFrame;
/*    */ import org.smchart.gui.LeftPane;
/*    */ import org.smchart.gui.MainGUI;
/*    */ import org.smchart.gui.MenuBar;
/*    */ 
/*    */ public class SMMain
/*    */ {
/*    */   public static void main(String[] args)
/*    */   {
/*    */     try
/*    */     {
/* 18 */       JFrame frame = new JFrame("SMChart");
/* 19 */       frame.setDefaultCloseOperation(3);
/* 20 */       String sep = System.getProperty("file.separator");
/* 21 */       String dataDir = System.getProperty("user.dir") + sep + "data" + sep;
/*    */ 
/* 23 */       CmdMgr cmdMgrObj = new CmdMgr(dataDir);
/*    */ 
/* 25 */       MenuBar.getInstance(frame, cmdMgrObj);
/* 26 */       MainGUI mainGUI = MainGUI.getInstance(cmdMgrObj);
/*    */ 
/* 28 */       frame.getContentPane().add(mainGUI);
/* 29 */       frame.setVisible(true);
/* 30 */       LeftPane dt = LeftPane.getLeftPaneInstance(dataDir, cmdMgrObj, mainGUI.getLeftPane());
/* 31 */       cmdMgrObj.setMainGUI(mainGUI, dt, frame);
/*    */     }
/*    */     catch (Exception ex) {
/* 34 */       ex.printStackTrace();
/*    */     }
/*    */   }
/*    */ }