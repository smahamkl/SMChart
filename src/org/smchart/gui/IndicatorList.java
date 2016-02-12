 package org.smchart.gui;
 
 import java.awt.Container;
 import java.awt.EventQueue;
 import java.awt.Font;
 import java.awt.event.MouseAdapter;
 import java.awt.event.MouseEvent;
 import javax.swing.AbstractListModel;
 import javax.swing.GroupLayout;
 import javax.swing.GroupLayout.Alignment;
 import javax.swing.GroupLayout.ParallelGroup;
 import javax.swing.GroupLayout.SequentialGroup;
 import javax.swing.JButton;
 import javax.swing.JFrame;
 import javax.swing.JLabel;
 import javax.swing.JList;
 import javax.swing.JScrollPane;
 import org.smchart.indicators.IndicatorFactory.IndicatorType;
 import org.smchart.main.CmdMgr;
 import org.smchart.indicators.IndicatorFactory;
 
 public class IndicatorList extends JFrame
 {
/*  22 */   private CmdMgr m_cmdMgr = null;
   private JButton jBtnAdd;
   private JButton jBtnCancel;
   private JLabel jLabel1;
   private JList jListIndicator;
   private JScrollPane jScrollPane1;
 
   public IndicatorList()
   {
/*  26 */     initComponents();
   }
 
   public void setCommandManager(CmdMgr cmdObj) {
/*  30 */     this.m_cmdMgr = cmdObj;
   }
 
   private void initComponents()
   {
/*  42 */     this.jScrollPane1 = new JScrollPane();
/*  43 */     this.jListIndicator = new JList();
/*  44 */     this.jLabel1 = new JLabel();
/*  45 */     this.jBtnAdd = new JButton();
/*  46 */     this.jBtnCancel = new JButton();
 
/*  48 */     setDefaultCloseOperation(2);
 
/*  50 */     this.jScrollPane1.setName("jScrollPane1");
 
/*  52 */     this.jListIndicator.setModel(new AbstractListModel() {
/*  53 */       String[] strings = { "SMA", "EMA", "BOLLINGER", "MACD", "STOCHASTIC", "RSI", "VOLUME", "PARABOLICSAR" };
 
/*  54 */       public int getSize() { return this.strings.length; } 
/*  55 */       public Object getElementAt(int i) { return this.strings[i]; }
 
     });
/*  57 */     this.jListIndicator.setSelectionMode(0);
/*  58 */     this.jListIndicator.setName("jListIndicator");
/*  59 */     this.jScrollPane1.setViewportView(this.jListIndicator);
 
/*  61 */     this.jLabel1.setFont(new Font("Tahoma", 1, 12));
/*  62 */     this.jLabel1.setText("Choose Indicator");
/*  63 */     this.jLabel1.setName("jLabel1");
 
/*  65 */     this.jBtnAdd.setText("Add Indicator");
/*  66 */     this.jBtnAdd.setName("jBtnAdd");
/*  67 */     this.jBtnAdd.addMouseListener(new MouseAdapter() {
       public void mouseClicked(MouseEvent evt) {
/*  69 */         IndicatorList.this.jBtnAddMouseClicked(evt);
       }
     });
/*  73 */     this.jBtnCancel.setText("Cancel");
/*  74 */     this.jBtnCancel.setName("jBtnCancel");
/*  75 */     this.jBtnCancel.addMouseListener(new MouseAdapter() {
       public void mouseClicked(MouseEvent evt) {
/*  77 */         IndicatorList.this.jBtnCancelMouseClicked(evt);
       }
     });
/*  81 */     GroupLayout layout = new GroupLayout(getContentPane());
/*  82 */     getContentPane().setLayout(layout);
/*  83 */     layout.setHorizontalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addGap(41, 41, 41).addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(this.jScrollPane1, -2, 311, -2).addComponent(this.jLabel1, -2, 131, -2))).addGroup(layout.createSequentialGroup().addGap(74, 74, 74).addComponent(this.jBtnAdd).addGap(56, 56, 56).addComponent(this.jBtnCancel))).addContainerGap(48, 32767)));
 
/*  99 */     layout.setVerticalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addGap(27, 27, 27).addComponent(this.jLabel1, -2, 15, -2).addGap(18, 18, 18).addComponent(this.jScrollPane1, -2, 180, -2).addGap(18, 18, 18).addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(this.jBtnAdd).addComponent(this.jBtnCancel)).addContainerGap(28, 32767)));
 
/* 113 */     pack();
   }
 
  private void jBtnAddMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jBtnAddMouseClicked
        // TODO add your handling code here:        
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {

                    if (jListIndicator.isSelectionEmpty() == false && m_cmdMgr != null) {
                        //System.out.println(jListIndicator.getSelectedValue().toString());
                        m_cmdMgr.addIndicator(((IndicatorFactory.IndicatorType) IndicatorFactory.IndicatorType.valueOf(jListIndicator.getSelectedValue().toString())));
                    }

                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
        this.dispose();
    }
 
   private void jBtnCancelMouseClicked(MouseEvent evt)
   {
/* 137 */     dispose();
   }
 
   public static void main(String[] args)
   {
/* 144 */     EventQueue.invokeLater(new Runnable()
     {
       public void run() {
/* 147 */         new IndicatorList().setVisible(true);
       }
     });
   }
 }
