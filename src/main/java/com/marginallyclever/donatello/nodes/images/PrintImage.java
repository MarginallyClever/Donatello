package com.marginallyclever.donatello.nodes.images;

import com.marginallyclever.nodegraphcore.*;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * This {@link Node} can print a {@link BufferedImage} using the {@link Graphics} context.
 * @author Dan Royer
 * @since 2022-02-23
 */
public class PrintImage extends Node implements PrintWithGraphics {
    private final DockReceiving<BufferedImage> image = new DockReceiving<>("image", BufferedImage.class,new BufferedImage(1,1,BufferedImage.TYPE_INT_ARGB));
    private final DockReceiving<Number> px = new DockReceiving<>("X",Number.class,0);
    private final DockReceiving<Number> py = new DockReceiving<>("Y",Number.class,0);

    /**
     * Constructor for subclasses to call.
     */
    public PrintImage() {
        super("PrintImage");
        addVariable(image);
        addVariable(px);
        addVariable(py);
    }

    @Override
    public void update() {
        if(image.hasPacketWaiting()) image.receive();
        if(px.hasPacketWaiting()) px.receive();
        if(py.hasPacketWaiting()) py.receive();
    }

    @Override
    public void print(Graphics g) {
        g.drawImage((BufferedImage)image.getValue(),px.getValue().intValue(),py.getValue().intValue(),null);
    }
}
