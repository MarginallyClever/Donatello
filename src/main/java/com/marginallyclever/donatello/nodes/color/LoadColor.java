package com.marginallyclever.donatello.nodes.color;

import com.marginallyclever.version2.Node;
import com.marginallyclever.version2.Dock;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Loads a {@link Color} into the graph.
 * @author Dan Royer
 * @since 2022-03-19
 */
public class LoadColor extends Node {
    private final Dock<Number> r = Dock.newInstance("r", Number.class, 0,true,false);
    private final Dock<Number> g = Dock.newInstance("g", Number.class, 0,true,false);
    private final Dock<Number> b = Dock.newInstance("b", Number.class, 0,true,false);
    private final Dock<Number> a = Dock.newInstance("a", Number.class, 0,true,false);
    private final Dock<Color> output = Dock.newInstance("output", Color.class, new Color(0,0,0,0),false,true);

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
