package com.marginallyclever.donatello;

import com.marginallyclever.donatello.search.SearchBar;
import com.marginallyclever.nodegraphcore.Node;
import com.marginallyclever.nodegraphcore.NodeCategory;
import com.marginallyclever.nodegraphcore.NodeFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.event.EventListenerList;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Supplier;

/**
 * Swing UI allowing a user to create a new {@link Node} as registered with a {@link NodeFactory}.
 * @author Dan Royer
 * @since 2022-02-11
 */
public class NodeFactoryPanel extends JPanel {
    private static final Logger logger = LoggerFactory.getLogger(NodeFactoryPanel.class);
    private final JTree tree = new JTree();
    private final SearchBar searchBar = new SearchBar();
    private final EventListenerList listeners = new EventListenerList();
    private final JButton addButton = new JButton("Add");

    public NodeFactoryPanel() {
        super(new BorderLayout());

        setupTree();
        setupSearch();
        setupAddButton();

        add(searchBar, BorderLayout.NORTH);
        add(new JScrollPane(tree), BorderLayout.CENTER);
        add(addButton,BorderLayout.SOUTH);

        populateTree();
    }

    private void setupSearch() {
        searchBar.addPropertyChangeListener("match", e-> populateTree() );
    }

    private void populateTree() {
        var root = NodeFactory.getRoot();

        var matches = findAllTypesMatching(root);
        //logger.debug("Found {} matches", matches.size());
        addAllParents(matches);
        //logger.debug("Grown to {} matches", matches.size());

        DefaultMutableTreeNode rootTreeNode = new DefaultMutableTreeNode(root);
        addBranches(root, rootTreeNode, matches);
        tree.setModel(new DefaultTreeModel(rootTreeNode));

        // Select the first match if there are any matches
        if (!matches.isEmpty()) {
            selectNodeInTree(matches.get(0));
        }
    }

    private void selectNodeInTree(NodeCategory node) {
        var rootNode = (DefaultMutableTreeNode)tree.getModel().getRoot();
        TreePath path = findNodeInTree(rootNode, node);
        if (path != null) {
            tree.setSelectionPath(path);
            tree.scrollPathToVisible(path);
        }
    }

    private TreePath findNodeInTree(DefaultMutableTreeNode branch, NodeCategory node) {
        if (getCategory(branch) == node) {
            return new TreePath(branch.getPath());
        }
        for (int i = 0; i < branch.getChildCount(); i++) {
            TreePath path = findNodeInTree((DefaultMutableTreeNode) branch.getChildAt(i), node);
            if (path != null) {
                return path;
            }
        }
        return null;
    }

    private void addBranches(NodeCategory node, DefaultMutableTreeNode branch, List<NodeCategory> matches) {
        // Sort the children alphabetically by their names
        List<NodeCategory> sortedChildren = new ArrayList<>(node.getChildren());
        sortedChildren.sort(Comparator.comparing(NodeCategory::getName));

        for(NodeCategory child : sortedChildren) {
            if(!matches.contains(child)) continue;

            DefaultMutableTreeNode childTreeNode = new DefaultMutableTreeNode(child);
            branch.add(childTreeNode);
            addBranches(child, childTreeNode,matches);
        }
    }

    private void addAllParents(List<NodeCategory> matches) {
        List<NodeCategory> toAdd = new ArrayList<>();
        for (NodeCategory node : matches) {
            NodeCategory parent = node.getParent();
            while (parent != null) {
                if(!matches.contains(parent) && !toAdd.contains(parent)) {
                    toAdd.add(parent);
                }
                parent = parent.getParent();
            }
        }
        matches.addAll(toAdd);
    }

    /**
     * Find all categories that match the search criteria.
     * @param root the root of the tree to search
     * @return a list of all categories that match the search criteria
     */
    private List<NodeCategory> findAllTypesMatching(NodeCategory root) {
        List<NodeCategory> matches = new ArrayList<>();
        List<NodeCategory> toSearch = new ArrayList<>();
        toSearch.add(root);
        while(!toSearch.isEmpty()) {
            NodeCategory category = toSearch.remove(0);
            String name = category.getName();
            if(searchBar.matches(name)) {
                matches.add(category);
            }
            toSearch.addAll(category.getChildren());
        }
        return matches;
    }

    private void setupTree() {
        tree.setCellRenderer(new FactoryCategoryCellRenderer());
        tree.addTreeSelectionListener(e -> {
            TreePath path = tree.getSelectionPath();
            if (path != null) {
                NodeCategory category = getCategory((DefaultMutableTreeNode) path.getLastPathComponent());
            }
        });
        tree.setToolTipText("");

        // Add a MouseListener for double-clicks
        tree.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (e.getClickCount() == 2) { // Double-click detected
                    TreePath path = tree.getPathForLocation(e.getX(), e.getY());
                    if (path != null) {
                        addNow(path);
                    }
                }
            }
        });
    }

    private void setupAddButton() {
        addButton.addActionListener(e -> {
            TreePath path = tree.getSelectionPath();
            if(path != null) addNow(path);
        });
    }

    private void addNow(TreePath path) {
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) path.getLastPathComponent();
        NodeCategory category = getCategory(node);
        if (category.getSupplier() != null) {
            // Trigger the action
            Supplier<Node> factory = category.getSupplier();
            if (factory != null) {
                fireAddNode(factory.get());
            }
        }
    }

    public String getSelectedNode() {
        TreePath path = tree.getSelectionPath();
        if (path != null) {
            NodeCategory category = getCategory((DefaultMutableTreeNode) path.getLastPathComponent());
            return category.getName();
        }
        return null;
    }

    NodeCategory getCategory(DefaultMutableTreeNode branch) {
        Object obj = branch.getUserObject();
        return (NodeCategory)obj;
    }

    public void addListener(AddNodeListener listener) {
        listeners.add(AddNodeListener.class, listener);
    }

    public void removeListener(AddNodeListener listener) {
        listeners.remove(AddNodeListener.class, listener);
    }

    public void fireAddNode(Node node) {
        for(AddNodeListener listener : listeners.getListeners(AddNodeListener.class)) {
            listener.addNode(node);
        }
    }
}
