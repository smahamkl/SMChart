 package org.smchart.gui;
 
 import javax.swing.JDialog;
 import javax.swing.JOptionPane;
 import javax.swing.JTextField;
 import org.smchart.indicators.IndicatorFactory.IndicatorType;
 import org.smchart.indicators.IndicatorFactory;
 
 public class IndicatorOptions
 {
   private JTextField fAvg;
   private JTextField sAvg;
   private JTextField tAvg;
/*  19 */   private String fAvgStr = "0"; private String sAvgStr = "0"; private String tAvgStr = "0";
/*  20 */   private boolean isAddIndicator = false;
 
   public IndicatorOptions() {
/*  23 */     this.fAvg = new JTextField();
/*  24 */     this.sAvg = new JTextField();
/*  25 */     this.tAvg = new JTextField();
   }
 
   public void displayOptions(IndicatorFactory.IndicatorType indType) {
/*  29 */     JDialog dialog = null;
/*  30 */     JOptionPane op = null;
/*  31 */     this.fAvg.setText(this.fAvgStr);
/*  32 */     this.sAvg.setText(this.sAvgStr);
/*  33 */     this.tAvg.setText(this.tAvgStr);
 
/*  35 */     if ((indType == IndicatorFactory.IndicatorType.SMA) || (indType == IndicatorFactory.IndicatorType.EMA)) {
/*  36 */       Object[] msg = { "Moving Avg 1:", this.fAvg, "Moving Avg 2:", this.sAvg, "Moving Avg 3:", this.tAvg };
/*  37 */       op = new JOptionPane(msg, -1, 2, null, null);
 
/*  43 */       dialog = op.createDialog(null, "Enter " + indType.name() + " Averages");
/*  44 */       dialog.setVisible(true);
/*  45 */       this.fAvg.requestFocusInWindow();
/*  46 */     } else if (indType == IndicatorFactory.IndicatorType.BOLLINGER) {
/*  47 */       Object[] bollinger_msg = { "Moving Avg:", this.fAvg, "Std Dev:", this.sAvg };
/*  48 */       op = new JOptionPane(bollinger_msg, -1, 2, null, null);
 
/*  54 */       dialog = op.createDialog(null, "Enter Average & StdDev");
/*  55 */       dialog.setVisible(true);
/*  56 */     } else if (indType == IndicatorFactory.IndicatorType.STOCHASTIC)
     {
/*  60 */       Object[] bollinger_msg = { "TimePeriod:", this.fAvg, "Smoothing Curve period(%D):", this.sAvg };
/*  61 */       op = new JOptionPane(bollinger_msg, -1, 2, null, null);
 
/*  67 */       dialog = op.createDialog(null, "Enter time period & average");
/*  68 */       dialog.setVisible(true);
/*  69 */       this.fAvg.requestFocusInWindow();
/*  70 */     } else if (indType == IndicatorFactory.IndicatorType.RSI) {
/*  71 */       Object[] bollinger_msg = { "TimePeriod:", this.fAvg };
/*  72 */       op = new JOptionPane(bollinger_msg, -1, 2, null, null);
 
/*  78 */       dialog = op.createDialog(null, "Enter time period");
/*  79 */       dialog.setVisible(true);
/*  80 */       this.fAvg.requestFocusInWindow();
/*  81 */     } else if (indType == IndicatorFactory.IndicatorType.MACD) {
/*  82 */       Object[] msg = { "Slow:", this.fAvg, "Fast:", this.sAvg, "EMA:", this.tAvg };
/*  83 */       op = new JOptionPane(msg, -1, 2, null, null);
 
/*  89 */       dialog = op.createDialog(null, "Enter MACD Averages");
/*  90 */       dialog.setVisible(true);
/*  91 */       this.fAvg.requestFocusInWindow();
     }
/*  93 */     else if (indType == IndicatorFactory.IndicatorType.PARABOLICSAR) {
/*  94 */       Object[] msg = { "Initial:", this.fAvg, "Increment:", this.sAvg, "Maximum:", this.tAvg };
/*  95 */       op = new JOptionPane(msg, -1, 2, null, null);
 
/* 101 */       dialog = op.createDialog(null, "Enter Parabolic SAR inputs");
/* 102 */       dialog.setVisible(true);
/* 103 */       this.fAvg.requestFocusInWindow();
     }
     try
     {
/* 107 */       int result = ((Integer)op.getValue()).intValue();
/* 108 */       if (result == 0) {
/* 109 */         this.isAddIndicator = true;
/* 110 */         if ((this.fAvg.getText() == null) || (this.fAvg.getText().length() == 0))
/* 111 */           this.fAvgStr = "0";
         else {
/* 113 */           this.fAvgStr = this.fAvg.getText();
         }
/* 115 */         if ((this.sAvg.getText() == null) || (this.sAvg.getText().length() == 0))
/* 116 */           this.sAvgStr = "0";
         else {
/* 118 */           this.sAvgStr = this.sAvg.getText();
         }
/* 120 */         if ((this.tAvg.getText() == null) || (this.tAvg.getText().length() == 0))
/* 121 */           this.tAvgStr = "0";
         else {
/* 123 */           this.tAvgStr = this.tAvg.getText();
         }
/* 125 */         dialog.dispose();
       } else {
/* 127 */         this.isAddIndicator = false;
/* 128 */         dialog.dispose();
       }
     }
     catch (Exception ex) {
     }
   }
 
   public boolean isOkToAddIndicator() {
/* 136 */     return this.isAddIndicator;
   }
 
   public String getFAvg() {
/* 140 */     return this.fAvgStr;
   }
 
   public String getSAvg() {
/* 144 */     return this.sAvgStr;
   }
 
   public String getTAvg() {
/* 148 */     return this.tAvgStr;
   }
 
   public void setFAvg(String inStr) {
/* 152 */     this.fAvgStr = inStr;
   }
 
   public void setSAvg(String inStr) {
/* 156 */     this.sAvgStr = inStr;
   }
 
   public void setTAvg(String inStr) {
/* 160 */     this.tAvgStr = inStr;
   }
 }