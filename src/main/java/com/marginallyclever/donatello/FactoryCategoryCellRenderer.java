package com.marginallyclever.donatello;

import com.marginallyclever.nodegraphcore.Node;
import com.marginallyclever.nodegraphcore.NodeCategory;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import java.awt.*;

/**
 * Custom cell renderer for displaying node categories in a tree.
 * It sets the text and color based on the category's supplier.
 */
class FactoryCategoryCellRenderer extends DefaultTreeCellRenderer {
    @Override
    public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {
        JLabel label = (JLabel) super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);

        DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
        Object userObject = node.getUserObject();
        if (userObject instanceof NodeCategory category) {
            label.setText(category.getName());
            if (category.getSupplier() != null) {
                try {
                    Node n = category.getSupplier().get();
                    if (n != null) {
                        var icon = n.getIcon();
                        if(icon != null) {
                            label.setIcon(icon);
                        } else {
                            label.setIcon(getDefaultLeafIcon());
                        }
                    } else {
                        label.setIcon(getDefaultLeafIcon());
                    }
                } catch (Exception e) {
                    label.setIcon(getDefaultLeafIcon());
                }
            } else {
                label.setIcon(getDefaultOpenIcon());
            }
        }
        return label;
    }
}
