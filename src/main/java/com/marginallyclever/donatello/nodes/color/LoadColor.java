package com.marginallyclever.donatello.nodes.color;

import com.marginallyclever.donatello.ports.InputInt;
import com.marginallyclever.donatello.ports.OutputColor;
import com.marginallyclever.nodegraphcore.Node;

import java.awt.*;

/**
 * Loads a {@link Color} into the graph.
 * @author Dan Royer
 * @since 2022-03-19
 */
public class LoadColor extends Node {
    private final InputInt r = new InputInt("r", 0);
    private final InputInt g = new InputInt("g", 0);
    private final InputInt b = new InputInt("b", 0);
    private final InputInt a = new InputInt("a", 0);
    private final OutputColor output = new OutputColor("output", Color.BLACK);

    /**
     * Constructor for subclasses to call.
     */
    public LoadColor() {
        super("LoadColor");
        addPort(r);
        addPort(g);
        addPort(b);
        addPort(a);
        addPort(output);
    }

    @Override
    public void update() {
        int rr = r.getValue();
        int gg = g.getValue();
        int bb = b.getValue();
        int aa = a.getValue();
        output.setValue(new Color(rr,gg,bb,aa));
    }
}
