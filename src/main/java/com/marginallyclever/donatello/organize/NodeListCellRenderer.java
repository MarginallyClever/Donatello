package com.marginallyclever.donatello.organize;

import com.marginallyclever.version2.Node;

import javax.swing.*;
import java.awt.*;

class NodeListCellRenderer implements ListCellRenderer<Node> {
    protected DefaultListCellRenderer defaultRenderer = new DefaultListCellRenderer();

    @Override
    public Component getListCellRendererComponent(JList<? extends Node> list, Node node, int index, boolean isSelected, boolean cellHasFocus) {
        String label = node.getUniqueName() + " " + node.getLabel();
        JLabel renderer = (JLabel) defaultRenderer.getListCellRendererComponent(list, label, index,
                isSelected, cellHasFocus);
        return renderer;
    }
}