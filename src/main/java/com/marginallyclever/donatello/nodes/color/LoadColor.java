package com.marginallyclever.donatello.nodes.color;

import com.marginallyclever.nodegraphcore.Node;
import com.marginallyclever.nodegraphcore.NodeVariable;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Loads a {@link Color} into the graph.
 * @author Dan Royer
 * @since 2022-03-19
 */
public class LoadColor extends Node {
    private final NodeVariable<Number> r = NodeVariable.newInstance("r", Number.class, 0,true,false);
    private final NodeVariable<Number> g = NodeVariable.newInstance("g", Number.class, 0,true,false);
    private final NodeVariable<Number> b = NodeVariable.newInstance("b", Number.class, 0,true,false);
    private final NodeVariable<Number> a = NodeVariable.newInstance("a", Number.class, 0,true,false);
    private final NodeVariable<Color> output = NodeVariable.newInstance("output", Color.class, new Color(0,0,0,0),false,true);

    /**
     * Constructor for subclasses to call.
     */
    public LoadColor() {
        super("LoadColor");
        addVariable(r);
        addVariable(g);
        addVariable(b);
        addVariable(a);
        addVariable(output);
    }

    /**
     * Constructor that sets starting values.
     * @param img the starting value.
     * @param sampleSize how many pixels to sample.  A value of zero is a single pixel.  One is the single pixel plus
     *                   one on each side.  Two would be two on each side in a square, and so on.
     */
    public LoadColor(int r,int g,int b, int a) {
        this();
        this.r.setValue(r);
        this.g.setValue(g);
        this.b.setValue(b);
        this.a.setValue(a);
    }

    @Override
    public Node create() {
        return new LoadColor();
    }

    @Override
    public void update() {
        int rr = r.getValue().intValue();
        int gg = g.getValue().intValue();
        int bb = b.getValue().intValue();
        int aa = a.getValue().intValue();
        output.setValue(new Color(rr,gg,bb,aa));
        cleanAllInputs();
    }
}
