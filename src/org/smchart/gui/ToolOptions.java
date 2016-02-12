 package org.smchart.gui;
 
 import java.awt.Container;
 import java.awt.Font;
 import java.awt.event.MouseAdapter;
 import java.awt.event.MouseEvent;
 import javax.swing.ButtonGroup;
 import javax.swing.GroupLayout;
 import javax.swing.GroupLayout.Alignment;
 import javax.swing.GroupLayout.ParallelGroup;
 import javax.swing.GroupLayout.SequentialGroup;
 import javax.swing.JButton;
 import javax.swing.JFrame;
 import javax.swing.JLabel;
 import javax.swing.JPanel;
 import javax.swing.JRadioButton;
 import javax.swing.JTabbedPane;
 import javax.swing.JTextField;
 import javax.swing.LayoutStyle.ComponentPlacement;
 import org.smchart.util.ConfigMgmt;
 
 public class ToolOptions extends JFrame
 {
   String m_dataDir;
   private ButtonGroup buttonGroup1;
   private JButton jBtnClose;
   private JButton jBtnSave;
   private JLabel jLabel1;
   private JLabel jLabel2;
   private JPanel jPanel1;
   private JRadioButton jRadioNoProxy;
   private JRadioButton jRadioWebProxy;
   private JTabbedPane jTabbedPane1;
   private JTextField jTxtHost;
   private JTextField jTxtPort;
 
   public ToolOptions(String dataDir)
   {
/*  24 */     this.m_dataDir = dataDir;
/*  25 */     initComponents();
 
/*  27 */     if (ConfigMgmt.getProperty("webproxy", this.m_dataDir) != null) {
/*  28 */       boolean proxy = Boolean.parseBoolean(ConfigMgmt.getProperty("webproxy", this.m_dataDir));
/*  29 */       if (proxy) {
/*  30 */         this.jRadioWebProxy.setSelected(true);
/*  31 */         this.jRadioNoProxy.setSelected(false);
       } else {
/*  33 */         this.jRadioWebProxy.setSelected(false);
/*  34 */         this.jRadioNoProxy.setSelected(true);
       }
     }
/*  37 */     if (ConfigMgmt.getProperty("webproxyhost", this.m_dataDir) != null) {
/*  38 */       this.jTxtHost.setText(ConfigMgmt.getProperty("webproxyhost", this.m_dataDir));
     }
/*  40 */     if (ConfigMgmt.getProperty("webproxyport", this.m_dataDir) != null)
/*  41 */       this.jTxtPort.setText(ConfigMgmt.getProperty("webproxyport", this.m_dataDir));
   }
 
   private void initComponents()
   {
/*  55 */     this.buttonGroup1 = new ButtonGroup();
/*  56 */     this.jTabbedPane1 = new JTabbedPane();
/*  57 */     this.jPanel1 = new JPanel();
/*  58 */     this.jRadioNoProxy = new JRadioButton();
/*  59 */     this.jRadioWebProxy = new JRadioButton();
/*  60 */     this.jTxtHost = new JTextField();
/*  61 */     this.jTxtPort = new JTextField();
/*  62 */     this.jLabel1 = new JLabel();
/*  63 */     this.jLabel2 = new JLabel();
/*  64 */     this.jBtnSave = new JButton();
/*  65 */     this.jBtnClose = new JButton();
 
/*  67 */     this.buttonGroup1.add(this.jRadioNoProxy);
/*  68 */     this.buttonGroup1.add(this.jRadioWebProxy);
 
/*  70 */     setDefaultCloseOperation(2);
/*  71 */     setTitle("Options");
/*  72 */     setResizable(false);
 
/*  74 */     this.jTabbedPane1.setName("jTabbedPane1");
 
/*  76 */     this.jPanel1.setToolTipText("Network Options");
/*  77 */     this.jPanel1.setName("jPanel1");
 
/*  79 */     this.jRadioNoProxy.setSelected(true);
/*  80 */     this.jRadioNoProxy.setText("No Proxy");
/*  81 */     this.jRadioNoProxy.setName("jRadioNoProxy");
 
/*  83 */     this.jRadioWebProxy.setText("Web Proxy");
/*  84 */     this.jRadioWebProxy.setName("jRadioWebProxy");
 
/*  86 */     this.jTxtHost.setName("jTxtHost");
 
/*  88 */     this.jTxtPort.setName("jTxtPort");
 
/*  90 */     this.jLabel1.setFont(new Font("Tahoma", 0, 12));
/*  91 */     this.jLabel1.setText("Host");
/*  92 */     this.jLabel1.setName("jLabel1");
 
/*  94 */     this.jLabel2.setFont(new Font("Tahoma", 0, 12));
/*  95 */     this.jLabel2.setText("Port");
/*  96 */     this.jLabel2.setName("jLabel2");
 
/*  98 */     this.jBtnSave.setText("Save");
/*  99 */     this.jBtnSave.setName("jBtnSave");
/* 100 */     this.jBtnSave.addMouseListener(new MouseAdapter() {
       public void mouseClicked(MouseEvent evt) {
/* 102 */         ToolOptions.this.jBtnSaveMouseClicked(evt);
       }
     });
/* 106 */     this.jBtnClose.setText("Close");
/* 107 */     this.jBtnClose.setName("jBtnClose");
/* 108 */     this.jBtnClose.addMouseListener(new MouseAdapter() {
       public void mouseClicked(MouseEvent evt) {
/* 110 */         ToolOptions.this.jBtnCloseMouseClicked(evt);
       }
     });
/* 114 */     GroupLayout jPanel1Layout = new GroupLayout(this.jPanel1);
/* 115 */     this.jPanel1.setLayout(jPanel1Layout);
/* 116 */     jPanel1Layout.setHorizontalGroup(jPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(jPanel1Layout.createSequentialGroup().addGap(62, 62, 62).addGroup(jPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(this.jLabel1).addComponent(this.jLabel2)).addGap(33, 33, 33).addGroup(jPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING, false).addComponent(this.jRadioWebProxy).addComponent(this.jRadioNoProxy).addComponent(this.jTxtHost, -1, 205, 32767).addComponent(this.jTxtPort)).addContainerGap(155, 32767)).addGroup(GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup().addContainerGap(300, 32767).addComponent(this.jBtnClose).addGap(18, 18, 18).addComponent(this.jBtnSave).addGap(88, 88, 88)));
 
/* 137 */     jPanel1Layout.setVerticalGroup(jPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(jPanel1Layout.createSequentialGroup().addGap(21, 21, 21).addGroup(jPanel1Layout.createParallelGroup(GroupLayout.Alignment.TRAILING).addComponent(this.jLabel1).addGroup(jPanel1Layout.createSequentialGroup().addComponent(this.jRadioNoProxy).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addComponent(this.jRadioWebProxy).addGap(48, 48, 48).addComponent(this.jTxtHost, -2, -1, -2))).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(jPanel1Layout.createParallelGroup(GroupLayout.Alignment.TRAILING).addComponent(this.jTxtPort, -2, -1, -2).addComponent(this.jLabel2)).addGap(78, 78, 78).addGroup(jPanel1Layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(this.jBtnSave).addComponent(this.jBtnClose)).addContainerGap(38, 32767)));
 
/* 160 */     this.jTabbedPane1.addTab("Network", this.jPanel1);
 
/* 162 */     GroupLayout layout = new GroupLayout(getContentPane());
/* 163 */     getContentPane().setLayout(layout);
/* 164 */     layout.setHorizontalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(this.jTabbedPane1, -1, 484, 32767));
 
/* 168 */     layout.setVerticalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(this.jTabbedPane1, -1, 349, 32767));
 
/* 173 */     pack();
   }
 
   private void jBtnSaveMouseClicked(MouseEvent evt)
   {
/* 178 */     if (this.jRadioNoProxy.isSelected()) {
/* 179 */       ConfigMgmt.setProperty("webproxy", "False", this.m_dataDir);
/* 180 */       ConfigMgmt.setProperty("webproxyhost", this.jTxtHost.getText(), this.m_dataDir);
/* 181 */       ConfigMgmt.setProperty("webproxyport", this.jTxtPort.getText(), this.m_dataDir);
     }
/* 183 */     if ((this.jRadioWebProxy.isSelected()) && (this.jTxtHost.getText() != null) && (this.jTxtPort.getText() != null)) {
/* 184 */       ConfigMgmt.setProperty("webproxy", "True", this.m_dataDir);
/* 185 */       ConfigMgmt.setProperty("webproxyhost", this.jTxtHost.getText(), this.m_dataDir);
/* 186 */       ConfigMgmt.setProperty("webproxyport", this.jTxtPort.getText(), this.m_dataDir);
     }
   }
 
   private void jBtnCloseMouseClicked(MouseEvent evt)
   {
/* 192 */     jBtnSaveMouseClicked(null);
/* 193 */     dispose();
   }
 }