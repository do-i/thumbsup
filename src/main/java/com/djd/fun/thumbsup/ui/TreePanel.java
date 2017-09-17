package com.djd.fun.thumbsup.ui;

import java.nio.file.Path;

import javax.swing.JTree;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.event.TreeWillExpandListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.ExpandVetoException;
import javax.swing.tree.TreeSelectionModel;

import com.djd.fun.thumbsup.annotations.InitialDir;
import com.djd.fun.thumbsup.events.tree.TreeNodeSelectEvent;
import com.djd.fun.thumbsup.models.Asset;
import com.djd.fun.thumbsup.models.AssetFactory;
import com.djd.fun.thumbsup.models.TreeNode;
import com.djd.fun.thumbsup.util.Enumerations;
import com.djd.fun.thumbsup.util.Fonts;
import com.google.common.eventbus.EventBus;
import com.google.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TreePanel extends JTree {

  private static final Logger log = LoggerFactory.getLogger(TreePanel.class);
  private final EventBus eventBus;
  private final AssetFactory assetFactory;

  @Inject
  public TreePanel(EventBus eventBus, AssetFactory assetFactory, @InitialDir Path initialDir) {
    log.info("init");
    this.eventBus = eventBus;
    this.assetFactory = assetFactory;
    setFont(Fonts.DROID_SANS_13);
    setShowsRootHandles(true);
    setEditable(false);
    getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
    DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode(new TreeNode(initialDir));
    addChildren(rootNode);
    DefaultTreeModel treeModel = new DefaultTreeModel(rootNode);
    setModel(treeModel);
    TreeWillExpandListener expandListener = new MyTreeWillExpandListener();
    TreeSelectionListener treeSelectionListener = new MyTreeSelectionListener();
    addTreeWillExpandListener(expandListener);
    addTreeSelectionListener(treeSelectionListener);
    colapseTree();
  }

  // TODO investigate why this font pretty does not work on tree view
  //  @Override
  //  public void paintComponent(Graphics g) {
  //    super.paintComponent(g);
  //    // TODO investigate why this font pretty does not work on tree view
  //    Fonts.enableSmoothFont(g);
  //  }

  private void colapseTree() {
    int rowCount = getRowCount();
    while (rowCount > 0) {
      collapseRow(--rowCount);
    }
    addSelectionRow(0);
  }

  private void addChildren(DefaultMutableTreeNode currentNode) {
    TreeNode currentTreeNode = (TreeNode)currentNode.getUserObject();
    currentTreeNode.getFolders().stream()
        .map(TreeNode::new)
        .map(DefaultMutableTreeNode::new)
        .forEach(currentNode::add);
  }

  private class MyTreeWillExpandListener implements TreeWillExpandListener {

    @Override public void treeWillExpand(TreeExpansionEvent event) throws ExpandVetoException {
      log.info("treeWillExpand");
      DefaultMutableTreeNode currentNode = (DefaultMutableTreeNode)event.getPath().getLastPathComponent();
      Enumerations.stream(currentNode.children())
          .forEach(n -> TreePanel.this.addChildren((DefaultMutableTreeNode)n));
    }

    @Override public void treeWillCollapse(TreeExpansionEvent event) throws ExpandVetoException {
      log.info("treeWillCollapse");
    }
  }

  private class MyTreeSelectionListener implements TreeSelectionListener {

    @Override
    public void valueChanged(TreeSelectionEvent event) {
      log.info("valueChanged");
      eventBus.post(TreeNodeSelectEvent.with(createAsset(event)));
    }
  }

  private Asset createAsset(TreeSelectionEvent event) {
    DefaultMutableTreeNode node = (DefaultMutableTreeNode)event.getPath().getLastPathComponent();
    TreeNode treeNode = (TreeNode)node.getUserObject();
    return assetFactory.createAsset(treeNode.getDir());
  }

}
