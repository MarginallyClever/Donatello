package com.marginallyclever.donatello;

import com.marginallyclever.nodegraphcore.NodeCategory;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import java.awt.*;

class FactoryCategoryCellRenderer extends DefaultTreeCellRenderer {
    @Override
    public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {
        super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
        var branch = (DefaultMutableTreeNode) value;
        if (branch.getUserObject() instanceof NodeCategory category) {
            if (category.getSupplier() == null) {
                setForeground(Color.LIGHT_GRAY);
            } else {
                setForeground(Color.BLACK);
            }
            setText(category.getName());
            /*
            Supplier<Node> supplier = category.getSupplier();
            if (supplier != null) {
                Node node = category.getSupplier().get();
                setIcon(node.getIcon());
            }*/
        }
        return this;
    }
}
