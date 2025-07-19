package com.marginallyclever.donatello;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class IconHelper {
    public static final int ICON_SIZE = 24;

    public static Icon scaleIcon(Icon icon, int width, int height) {
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
