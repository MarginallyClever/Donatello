package com.marginallyclever.donatello.nodes.images;

import com.marginallyclever.nodegraphcore.PrintWithGraphics;
import com.marginallyclever.nodegraphcore.Node;
import com.marginallyclever.nodegraphcore.NodeVariable;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * This {@link Node} can print a {@link BufferedImage} using the {@link Graphics} context.
 * @author Dan Royer
 * @since 2022-02-23
 */
public class PrintImage extends Node implements PrintWithGraphics {
    private final NodeVariable<BufferedImage> image = NodeVariable.newInstance("image", BufferedImage.class,new BufferedImage(1,1,BufferedImage.TYPE_INT_ARGB),true,false);
    private final NodeVariable<Number> px = NodeVariable.newInstance("X",Number.class,0,true,false);
    private final NodeVariable<Number> py = NodeVariable.newInstance("Y",Number.class,0,true,false);

    /**
     * Constructor for subclasses to call.
     */
    public PrintImage() {
        super("PrintImage");
        addVariable(image);
        addVariable(px);
        addVariable(py);
    }

    /**
     * Constructor that sets starting values.
     * @param img the starting value.
     * @param x the starting value.
     * @param y the starting value.
     */
    public PrintImage(BufferedImage img,double x,double y) {
        this();
        this.image.setValue(img);
        this.px.setValue(x);
        this.py.setValue(y);
    }

    @Override
    public Node create() {
        return new PrintImage();
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
