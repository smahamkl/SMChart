 package org.smchart.gui;
 
 import java.awt.Toolkit;
 import java.io.PrintStream;
 import javax.swing.Icon;
 import javax.swing.ImageIcon;
 import javax.swing.JScrollPane;
 import javax.swing.JTree;
 import javax.swing.event.TreeModelEvent;
 import javax.swing.event.TreeModelListener;
 import javax.swing.tree.DefaultMutableTreeNode;
 import javax.swing.tree.DefaultTreeCellRenderer;
 import javax.swing.tree.DefaultTreeModel;
 import javax.swing.tree.MutableTreeNode;
 import javax.swing.tree.TreePath;
 import javax.swing.tree.TreeSelectionModel;
 
 public class DynamicTree
 {
   protected DefaultMutableTreeNode rootNode;
   protected DefaultTreeModel treeModel;
   protected JTree tree;
/*  17 */   private Toolkit toolkit = Toolkit.getDefaultToolkit();
/*  18 */   private Icon customIcon = new ImageIcon(getClass().getResource("/org/smchart/images/yfinance.png"));
 
   public DynamicTree(JScrollPane leftPane) {
/*  21 */     this.rootNode = new DefaultMutableTreeNode("Stocks");
/*  22 */     this.treeModel = new DefaultTreeModel(this.rootNode);
/*  23 */     this.treeModel.addTreeModelListener(new MyTreeModelListener());
 
/*  25 */     this.tree = new JTree(this.treeModel);
/*  26 */     this.tree.setEditable(false);
/*  27 */     DefaultTreeCellRenderer renderer = new DefaultTreeCellRenderer();
/*  28 */     renderer.setLeafIcon(this.customIcon);
/*  29 */     renderer.setClosedIcon(null);
/*  30 */     this.tree.setCellRenderer(renderer);
/*  31 */     this.tree.getSelectionModel().setSelectionMode(1);
/*  32 */     this.tree.setShowsRootHandles(true);
/*  33 */     this.tree.setSize(300, 2);
   }
 
   public JTree getTree() {
/*  37 */     return this.tree;
   }
 
   public void clear()
   {
/*  42 */     this.rootNode.removeAllChildren();
/*  43 */     this.treeModel.reload();
   }
 
   public void removeCurrentNode()
   {
/*  48 */     TreePath currentSelection = this.tree.getSelectionPath();
/*  49 */     if (currentSelection != null) {
/*  50 */       DefaultMutableTreeNode currentNode = (DefaultMutableTreeNode)currentSelection.getLastPathComponent();
/*  51 */       MutableTreeNode parent = (MutableTreeNode)currentNode.getParent();
/*  52 */       if (parent != null) {
/*  53 */         this.treeModel.removeNodeFromParent(currentNode);
/*  54 */         return;
       }
 
     }
 
/*  59 */     this.toolkit.beep();
   }
 
   public DefaultMutableTreeNode addObject(Object child)
   {
/*  64 */     DefaultMutableTreeNode parentNode = null;
/*  65 */     TreePath parentPath = this.tree.getSelectionPath();
 
/*  67 */     if (parentPath == null)
/*  68 */       parentNode = this.rootNode;
     else {
/*  70 */       parentNode = (DefaultMutableTreeNode)parentPath.getLastPathComponent();
     }
 
/*  73 */     return addObject(parentNode, child, true);
   }
 
   public DefaultMutableTreeNode addObject(DefaultMutableTreeNode parent, Object child)
   {
/*  78 */     return addObject(parent, child, false);
   }
 
   public DefaultMutableTreeNode addObject(DefaultMutableTreeNode parent, Object child, boolean shouldBeVisible)
   {
/*  84 */     DefaultMutableTreeNode childNode = new DefaultMutableTreeNode(child);
 
/*  87 */     if (parent == null) {
/*  88 */       parent = this.rootNode;
     }
/*  90 */     boolean childFound = false;
/*  91 */     for (int i = 0; i < parent.getChildCount(); i++) {
/*  92 */       if (parent.getChildAt(i).toString().compareToIgnoreCase(child.toString()) == 0) {
/*  93 */         childFound = true;
       }
     }
/*  96 */     if (!childFound)
     {
/*  98 */       this.treeModel.insertNodeInto(childNode, parent, parent.getChildCount());
     }
 
/* 103 */     if (shouldBeVisible) {
/* 104 */       this.tree.scrollPathToVisible(new TreePath(childNode.getPath()));
     }
/* 106 */     return childNode;
   }
   class MyTreeModelListener implements TreeModelListener {
     MyTreeModelListener() {
     }
 
     public void treeNodesChanged(TreeModelEvent e) {
/* 113 */       DefaultMutableTreeNode node = (DefaultMutableTreeNode)e.getTreePath().getLastPathComponent();
       try
       {
/* 122 */         int index = e.getChildIndices()[0];
/* 123 */         node = (DefaultMutableTreeNode)node.getChildAt(index);
       }
       catch (NullPointerException exc) {
       }
/* 127 */       System.out.println("The user has finished editing the node.");
/* 128 */       System.out.println("New value: " + node.getUserObject());
     }
 
     public void treeNodesInserted(TreeModelEvent e)
     {
     }
 
     public void treeNodesRemoved(TreeModelEvent e)
     {
     }
 
     public void treeStructureChanged(TreeModelEvent e)
     {
     }
   }
 }