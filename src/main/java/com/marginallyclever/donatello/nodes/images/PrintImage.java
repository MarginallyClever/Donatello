package com.marginallyclever.donatello.nodes.images;

import com.marginallyclever.nodegraphcore.*;
import com.marginallyclever.nodegraphcore.port.Input;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * This {@link Node} can print a {@link BufferedImage} using the {@link Graphics} context.
 * @author Dan Royer
 * @since 2022-02-23
 */
public class PrintImage extends Node implements PrintWithGraphics {
    private final Input<BufferedImage> image = new Input<>("image", BufferedImage.class,new BufferedImage(1,1,BufferedImage.TYPE_INT_ARGB));
    private final Input<Number> px = new Input<>("X",Number.class,0);
    private final Input<Number> py = new Input<>("Y",Number.class,0);

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
    }

    @Override
    public void print(Graphics g) {
        g.drawImage(image.getValue(),px.getValue().intValue(),py.getValue().intValue(),null);
    }
}
