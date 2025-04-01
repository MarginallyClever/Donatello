package com.marginallyclever.donatello.organize;

import com.marginallyclever.nodegraphcore.Node;

import javax.swing.*;
import java.awt.*;

/**
 * A custom {@link ListCellRenderer} for displaying {@link Node} objects in a {@link JList}.
 */
class NodeListCellRenderer implements ListCellRenderer<Node> {
    protected DefaultListCellRenderer defaultRenderer = new DefaultListCellRenderer();

    @Override
    public Component getListCellRendererComponent(JList<? extends Node> list, Node node, int index, boolean isSelected, boolean cellHasFocus) {
        String label = node.getUniqueID() + " " + node.getLabel();
        return defaultRenderer.getListCellRendererComponent(list, label, index, isSelected, cellHasFocus);
    }
}