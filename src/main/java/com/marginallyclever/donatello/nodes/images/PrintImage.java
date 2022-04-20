package com.marginallyclever.donatello.nodes.images;

import com.marginallyclever.version2.PrintWithGraphics;
import com.marginallyclever.version2.Node;
import com.marginallyclever.version2.Dock;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * This {@link Node} can print a {@link BufferedImage} using the {@link Graphics} context.
 * @author Dan Royer
 * @since 2022-02-23
 */
public class PrintImage extends Node implements PrintWithGraphics {
    private final Dock<BufferedImage> image = Dock.newInstance("image", BufferedImage.class,new BufferedImage(1,1,BufferedImage.TYPE_INT_ARGB),true,false);
    private final Dock<Number> px = Dock.newInstance("X",Number.class,0,true,false);
    private final Dock<Number> py = Dock.newInstance("Y",Number.class,0,true,false);

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
        cleanAllInputs();
    }

    @Override
    public void print(Graphics g) {
        g.drawImage((BufferedImage)image.getValue(),px.getValue().intValue(),py.getValue().intValue(),null);
    }
}
