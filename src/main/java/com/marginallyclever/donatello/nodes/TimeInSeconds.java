package com.marginallyclever.donatello.nodes;

import com.marginallyclever.nodegraphcore.Node;
import com.marginallyclever.nodegraphcore.port.Output;

/**
 * Publishes the time in seconds continuously.
 */
public class TimeInSeconds extends Node {
    private final Output<Number> seconds = new Output<>("seconds", Number.class, 0);
    private final double startTime = System.currentTimeMillis();

    public TimeInSeconds() {
        super("TimeInSeconds");
        addVariable(seconds);
    }

    @Override
    public void update() {
        double t = (System.currentTimeMillis()-startTime)/1000.0;
        seconds.send(t);
    }
}
