package com.marginallyclever.donatello.nodes.images;

import com.marginallyclever.version2.PrintWithGraphics;
import com.marginallyclever.version2.Node;
import com.marginallyclever.version2.Dock;
import com.marginallyclever.version2.nodes.InPort;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * This {@link Node} can print a {@link BufferedImage} using the {@link Graphics} context.
 * @author Dan Royer
 * @since 2022-02-23
 */
@InPort(name="image",type=BufferedImage.class)
@InPort(name="X",type=Integer.class)
@InPort(name="Y",type=Integer.class)
public class PrintImage extends Node implements PrintWithGraphics {
    @Override
    public void update() {

    }

    @Override
    public void print(Graphics g) {
        g.drawImage((BufferedImage)image.getValue(),px.getValue().intValue(),py.getValue().intValue(),null);
    }
}
