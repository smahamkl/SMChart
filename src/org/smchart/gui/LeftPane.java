/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.smchart.gui;

import java.awt.*;
import java.awt.event.*;
import java.util.Enumeration;
import javax.swing.*;
import javax.swing.tree.*;
import java.io.File;
import org.smchart.main.CmdMgr;
import java.net.URISyntaxException;
import java.io.IOException;
import java.net.URL;
import org.smchart.charts.ChartGenerator;
import org.smchart.indicators.IndicatorFactory;

public class LeftPane {

    final DynamicTree treePanel;
    private static final String CSV_EXT = "csv";
    private String m_filePath;
    private static LeftPane instance;
    private CmdMgr m_cmdMgr;
    private JScrollPane leftPane;

    private enum Level_0_Menu {

        Add
    }

    private enum Level_1_Menu {

        Chart, Reimport, YhooHeadLines, YhooProfile, Remove
    }

    private enum Level_2_Menu {

        Remove, Options
    }

    public static LeftPane getLeftPaneInstance(String filePath, CmdMgr cmdMgr, JScrollPane inPane) throws Exception {
        if (instance == null) {
            instance = new LeftPane(filePath, cmdMgr, inPane);
        }
        return instance;
    }

    private LeftPane(String filePath, CmdMgr cmdMgr, JScrollPane inPane) throws Exception {
        m_filePath = filePath;
        m_cmdMgr = cmdMgr;
        leftPane = inPane;
        //create the components
        treePanel = new DynamicTree(inPane);
        AddTickers();
        inPane.getViewport().add(treePanel.getTree());
        // populateTree(treePanel);

        treePanel.getTree().addMouseListener(new MouseAdapter() {

            public void mousePressed(MouseEvent e) {
                if (SwingUtilities.isRightMouseButton(e)) {
                    try {
                        Robot robot = new java.awt.Robot();
                        robot.mousePress(InputEvent.BUTTON1_MASK);
                        robot.mouseRelease(InputEvent.BUTTON1_MASK);
                    } catch (Exception ae) {
                        ae.printStackTrace();
                    }
                }
            }

            public void mouseReleased(MouseEvent e) {
                if (SwingUtilities.isRightMouseButton(e)) {
                    JTree tree = (JTree) e.getSource();
                    ShowPopUpMenu(e.getX(), e.getY(), getSelectedNodeLevel(tree.getSelectionPath()));
                }
            }
        });

    }

    private int getSelectedNodeLevel(TreePath selectionPath) {

        if (selectionPath != null) {
            DefaultMutableTreeNode currentNode = (DefaultMutableTreeNode) (selectionPath.getLastPathComponent());
//            System.out.println("Level above this node are:" + currentNode.getLevel() + "; Child Count:" +
//                    currentNode.getChildCount() + "; " + currentNode.toString() +
//                    "; Is leaf? " + currentNode.isLeaf() + "; leaf count:" + currentNode.getLeafCount());
            return currentNode.getLevel();
        }
        return -1;
    }

    private void ShowPopUpMenu(int x, int y, int level) {
        if (level == -1) {
            return;
        }

        PopMenuActionListener listener = new PopMenuActionListener();
        JPopupMenu popup = new JPopupMenu();

        if (level == 0) {
            for (Level_0_Menu myEnum : Level_0_Menu.values()) {
                //Do stuff here!
                JMenuItem menuItem = new JMenuItem(myEnum.name());
                menuItem.addActionListener(listener);
                popup.add(menuItem);
            }
        } else if (level == 1) {
            for (Level_1_Menu myEnum : Level_1_Menu.values()) {
                //Do stuff here!
                JMenuItem menuItem = new JMenuItem(myEnum.name());
                menuItem.addActionListener(listener);
                popup.add(menuItem);
            }
        } else if (level == 2) {
            for (Level_2_Menu myEnum : Level_2_Menu.values()) {
                //Do stuff here!
                JMenuItem menuItem = new JMenuItem(myEnum.name());
                menuItem.addActionListener(listener);
                popup.add(menuItem);
            }
        }

        popup.setBounds(x, y, popup.getWidth(), popup.getHeight());
        popup.show(leftPane, x, y);
        popup.setVisible(true);
    }

    public String getSelectedTicker() {
        TreePath currentSelection = treePanel.getTree().getSelectionPath();
        DefaultMutableTreeNode currentNode = (DefaultMutableTreeNode) (currentSelection.getLastPathComponent());
        return getFileNameWithoutExtension(currentNode.toString());
    }

    public void addIndicatorNode(IndicatorFactory.IndicatorType type, String ticker) {
        if (treePanel != null && ticker != null) {
            int totalChild = ((DefaultMutableTreeNode) treePanel.getTree().getModel().getRoot()).getChildCount();
            DefaultMutableTreeNode rootNode = (DefaultMutableTreeNode) treePanel.getTree().getModel().getRoot();
            for (int i = 0; i < totalChild; i++) {
                if (rootNode.getChildAt(i).toString().compareToIgnoreCase(ticker + "." + CSV_EXT) == 0) {
                    treePanel.addObject((DefaultMutableTreeNode) rootNode.getChildAt(i), type.name());
                    break;
                }
            }
        }
    }

    private class PopMenuActionListener implements ActionListener {

        public void actionPerformed(ActionEvent event) {
            try {

                TreePath currentSelection = treePanel.getTree().getSelectionPath();
                DefaultMutableTreeNode currentNode = (DefaultMutableTreeNode) (currentSelection.getLastPathComponent());
                int nodeLevel = getSelectedNodeLevel(currentSelection);

                if (event.getActionCommand().toString().compareToIgnoreCase("Add") == 0) {
                    String name = JOptionPane.showInputDialog(leftPane, "Enter Ticker");
                    if (name != null) {
                        treePanel.addObject(name.toUpperCase() + "." + CSV_EXT);
                    }
                } else if (event.getActionCommand().toString().compareToIgnoreCase("YhooProfile") == 0) {
                    //DrawChart();
                    String tick = getFileNameWithoutExtension(currentNode.toString());
                    URL url = new URL("http://finance.yahoo.com/q/pr?s=" + tick);

                    if (Desktop.isDesktopSupported()) {
                        Desktop desktop = Desktop.getDesktop();

                        if (desktop.isSupported(Desktop.Action.BROWSE)) {
                            try {
                                desktop.browse(url.toURI());
                            } catch (URISyntaxException ex) {
                            } catch (IOException ex) {
                            }
                        }
                    }
                } else if (event.getActionCommand().toString().compareToIgnoreCase("Remove") == 0) {
                    //DrawChart();
                    if (nodeLevel == 2) {
                        m_cmdMgr.clearUpperIndicator(((IndicatorFactory.IndicatorType) IndicatorFactory.IndicatorType.valueOf(currentNode.toString())));
                    } else {
                        m_cmdMgr.delFile(m_filePath + currentNode.toString());
                    }
                    treePanel.removeCurrentNode();
                } else if (event.getActionCommand().toString().compareToIgnoreCase("Options") == 0) {
                    //DrawChart();
                    if (nodeLevel == 2) {
                        m_cmdMgr.addIndicator((IndicatorFactory.IndicatorType) IndicatorFactory.IndicatorType.valueOf(currentNode.toString()));
                    }
                } else if (event.getActionCommand().toString().compareToIgnoreCase("Clear") == 0) {
                    //DrawChart();
                    //treePanel.clear();
                } else if (event.getActionCommand().toString().compareToIgnoreCase("Chart") == 0) {
                    m_cmdMgr.resetChartGen();
                    m_cmdMgr.GenerateChart(getFileNameWithoutExtension(currentNode.toString()));
                } else if (event.getActionCommand().toString().compareToIgnoreCase("YhooHeadlines") == 0) {
                    m_cmdMgr.disPlayRSSHeadLines(getFileNameWithoutExtension(currentNode.toString()));
                } else if (event.getActionCommand().toString().compareToIgnoreCase("Reimport") == 0) {
                    m_cmdMgr.delFile(m_filePath + currentNode.toString());
                    m_cmdMgr.resetChartGen();
                    m_cmdMgr.GenerateChart(getFileNameWithoutExtension(currentNode.toString()));
                }

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    private String getFileNameWithoutExtension(String fileName) {
        if (fileName == null) {
            return null;
        }
        int whereDot = fileName.lastIndexOf('.');
        if (0 < whereDot && whereDot <= fileName.length() - 2) {
            return fileName.substring(0, whereDot);
        }
        return null;
    }

    public void AddTickers() throws Exception {
        File folder = new File(m_filePath);
        File[] listOfFiles = folder.listFiles();
        for (int i = 0; i < listOfFiles.length; i++) {
            if (listOfFiles[i].isFile()) {
                String ext = (listOfFiles[i].getName().lastIndexOf(".") == -1) ? "" : listOfFiles[i].getName().substring(listOfFiles[i].getName().lastIndexOf(".") + 1,
                        listOfFiles[i].getName().length());
                if (ext.compareToIgnoreCase(CSV_EXT) == 0) {
                    //m_tickers.addElement(listOfFiles[i].getName());
                    treePanel.addObject(listOfFiles[i].getName());
                }
            }
        }
    }
}

