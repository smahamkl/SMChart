/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.smchart.gui;

import java.awt.Desktop;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import javax.swing.event.HyperlinkEvent;
import org.smchart.util.RSSReader;
import javax.swing.JFrame;

/**
 *
 * @author venkatasiva
 */
public class RSSUI {

    private RSSReader rdr = null;
    private JFrame frame;
    private String ticker;

    public RSSUI(String ticker) {
        //super(parent, modal);
        this.ticker = ticker;
        rdr = new RSSReader(ticker);
        initComponents();
    }

    public RSSUI(String ticker, String host, String port) {
        //super(parent, modal);
        this.ticker = ticker;
        rdr = new RSSReader(ticker, host, port);
        initComponents();
    }

    private void initComponents() {
        frame = new JFrame();

        jLabel1 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jEditorPane1 = new javax.swing.JEditorPane();

        frame.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        frame.setTitle("Yahoo RSS Feed - " + ticker);
        frame.setBackground(java.awt.Color.white);
        frame.setResizable(false);

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 12));
        jLabel1.setForeground(new java.awt.Color(0, 0, 255));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel1.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 5, 0, 5));
        frame.getContentPane().add(jLabel1, java.awt.BorderLayout.NORTH);

        jButton1.setText("OK");
        jButton1.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton1);

        frame.getContentPane().add(jPanel1, java.awt.BorderLayout.SOUTH);

        jEditorPane1.setContentType("text/html");
        jEditorPane1.setEditable(false);

        //Get the NEWS here
        String newsHTML = rdr.getNews();
        String cssStyle = "<style type='text/css'> " +
                "table {font-size: 95%;font-family: verdana;}" +
                "</style>";
        String htmlContent = "<html>\n<head>" + cssStyle + "\n</head>\n    <body>\n " + newsHTML +
                " </body>\n</html>";

        jEditorPane1.setText(htmlContent);
        jEditorPane1.addHyperlinkListener(new javax.swing.event.HyperlinkListener() {

            public void hyperlinkUpdate(javax.swing.event.HyperlinkEvent evt) {
                jEditorPane1HyperlinkUpdate(evt);
            }
        });
        jScrollPane1.setViewportView(jEditorPane1);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
                jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel2Layout.createSequentialGroup().addContainerGap().addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 489, Short.MAX_VALUE).addContainerGap()));
        jPanel2Layout.setVerticalGroup(
                jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel2Layout.createSequentialGroup().addContainerGap().addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 180, Short.MAX_VALUE).addContainerGap()));

        frame.getContentPane().add(jPanel2, java.awt.BorderLayout.CENTER);

        frame.setSize(800, 600);
        frame.setVisible(true);
    }

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {
        jEditorPane1.removeAll();
        jScrollPane1.removeAll();
        frame.dispose();
    }

    private void jEditorPane1HyperlinkUpdate(javax.swing.event.HyperlinkEvent evt) {
        // TODO add your handling code here:
        if (HyperlinkEvent.EventType.ACTIVATED.equals(evt.getEventType())) {
            URL url = evt.getURL();

            if (Desktop.isDesktopSupported()) {
                Desktop desktop = Desktop.getDesktop();

                if (desktop.isSupported(Desktop.Action.BROWSE)) {
                    if (url == null) {
                        String string = "http://" + evt.getDescription();
                        try {
                            url = new URL(string);
                        } catch (MalformedURLException ex) {
                            return;
                        }
                    }

                    try {
                        desktop.browse(url.toURI());
                    } catch (URISyntaxException ex) {
                        ex.printStackTrace();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        }
    }    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JEditorPane jEditorPane1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration//GEN-END:variables
}
