/*    */ package org.smchart.gui;
/*    */ 
/*    */ import javax.swing.JDialog;
/*    */ import javax.swing.JOptionPane;
/*    */ import javax.swing.JTextField;
/*    */ 
/*    */ public class PlotWeights
/*    */ {
/* 16 */   private boolean isSetPlotWeights = false;
/*    */   private int[] weights;
/*    */ 
/*    */   public void displayOptions(int totalPlots)
/*    */   {
/* 23 */     JDialog dialog = null;
/* 24 */     JOptionPane op = null;
/*    */ 
/* 26 */     if (totalPlots > 1) {
/* 27 */       this.weights = new int[totalPlots];
/* 28 */       Object[] msg = new Object[totalPlots];
/*    */ 
/* 30 */       for (int i = 0; i < totalPlots; i++) {
/* 31 */         msg[i] = new JTextField();
/*    */       }
/*    */ 
/* 34 */       op = new JOptionPane(msg, -1, 2, null, null);
/*    */ 
/* 40 */       dialog = op.createDialog(null, "Enter plot weights");
/* 41 */       dialog.setVisible(true);
/*    */       try
/*    */       {
/* 44 */         int result = ((Integer)op.getValue()).intValue();
/* 45 */         if (result == 0) {
/* 46 */           for (int i = 0; i < totalPlots; i++)
/*    */           {
/* 48 */             this.weights[i] = Integer.parseInt(((JTextField)msg[i]).getText());
/*    */           }
/*    */ 
/* 51 */           this.isSetPlotWeights = true;
/* 52 */           dialog.dispose();
/*    */         } else {
/* 54 */           this.isSetPlotWeights = false;
/* 55 */           dialog.dispose();
/*    */         }
/*    */       }
/*    */       catch (Exception ex) {
/*    */       }
/*    */     }
/*    */   }
/*    */ 
/*    */   public boolean isChangePlotWeights() {
/* 64 */     return this.isSetPlotWeights;
/*    */   }
/*    */ 
/*    */   public int[] getPlotWeights()
/*    */   {
/* 69 */     return this.weights;
/*    */   }
/*    */ }