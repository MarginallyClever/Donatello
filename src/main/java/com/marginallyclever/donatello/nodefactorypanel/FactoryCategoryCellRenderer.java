package com.marginallyclever.donatello.nodefactorypanel;

import com.marginallyclever.nodegraphcore.Node;
import com.marginallyclever.nodegraphcore.NodeCategory;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Custom cell renderer for displaying node categories in a tree.
 * It sets the text and color based on the category's supplier.
 */
class FactoryCategoryCellRenderer extends DefaultTreeCellRenderer {
    private static final int ICON_SIZE = 24;

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
                            label.setIcon(scaleIcon(icon,ICON_SIZE,ICON_SIZE));
                        } else {
                            label.setIcon(scaleIcon(getDefaultLeafIcon(),ICON_SIZE,ICON_SIZE));
                        }
                    } else {
                        label.setIcon(scaleIcon(getDefaultLeafIcon(),ICON_SIZE,ICON_SIZE));
                    }
                } catch (Exception e) {
                    label.setIcon(scaleIcon(getDefaultLeafIcon(),ICON_SIZE,ICON_SIZE));
                }
            } else {
                label.setIcon(scaleIcon(getDefaultOpenIcon(),ICON_SIZE,ICON_SIZE));
            }
        }
        return label;
    }

    private Icon scaleIcon(Icon icon, int width, int height) {
        BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = img.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);

        // Paint the icon into a temp image at its original size
        BufferedImage iconImage = new BufferedImage(icon.getIconWidth(), icon.getIconHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D gIcon = iconImage.createGraphics();
        icon.paintIcon(null, gIcon, 0, 0);
        gIcon.dispose();

        // Draw the temp image scaled to the target size
        g2.drawImage(iconImage, 0, 0, width, height, null);
        g2.dispose();
        return new ImageIcon(img);
    }
}
