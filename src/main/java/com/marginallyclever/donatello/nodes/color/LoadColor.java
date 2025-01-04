package com.marginallyclever.donatello.nodes.color;

import com.marginallyclever.nodegraphcore.*;
import com.marginallyclever.nodegraphcore.dock.Input;
import com.marginallyclever.nodegraphcore.dock.Output;

import java.awt.*;

/**
 * Loads a {@link Color} into the graph.
 * @author Dan Royer
 * @since 2022-03-19
 */
public class LoadColor extends Node {
    private final Input<Number> r = new Input<>("r", Number.class, 0);
    private final Input<Number> g = new Input<>("g", Number.class, 0);
    private final Input<Number> b = new Input<>("b", Number.class, 0);
    private final Input<Number> a = new Input<>("a", Number.class, 0);
    private final Output<Color> output = new Output<>("output", Color.class, new Color(0,0,0,0));

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
        if(r.hasPacketWaiting()) r.receive();
        if(g.hasPacketWaiting()) g.receive();
        if(b.hasPacketWaiting()) b.receive();
        if(a.hasPacketWaiting()) a.receive();

        int rr = r.getValue().intValue();
        int gg = g.getValue().intValue();
        int bb = b.getValue().intValue();
        int aa = a.getValue().intValue();
        output.send(new Packet<>(new Color(rr,gg,bb,aa)));
    }
}
