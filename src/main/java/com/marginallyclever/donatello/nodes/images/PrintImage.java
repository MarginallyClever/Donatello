package com.marginallyclever.donatello.nodes.images;

import com.marginallyclever.donatello.ports.InputImage;
import com.marginallyclever.donatello.ports.InputInt;
import com.marginallyclever.nodegraphcore.Node;
import com.marginallyclever.nodegraphcore.PrintWithGraphics;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * This {@link Node} can print a {@link BufferedImage} using the {@link Graphics} context.
 * @author Dan Royer
 * @since 2022-02-23
 */
public class PrintImage extends Node implements PrintWithGraphics {
    private final InputImage image = new InputImage("image");
    private final InputInt layer = new InputInt("layer",2);

    /**
     * Constructor for subclasses to call.
     */
    public PrintImage() {
        super("PrintImage");
        addPort(image);
        addPort(layer);
    }

    @Override
    public void update() {}

    @Override
    public void print(Graphics g) {
        var img = image.getValue();
        g.drawImage(img,-img.getWidth()/2,-img.getHeight()/2,null);
    }

    @Override
    public int getLayer() {
        return layer.getValue();
    }
}
