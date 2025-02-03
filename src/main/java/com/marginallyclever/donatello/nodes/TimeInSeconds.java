package com.marginallyclever.donatello.nodes;

import com.marginallyclever.donatello.ports.OutputInt;
import com.marginallyclever.nodegraphcore.Node;

/**
 * Publishes the time in seconds continuously.
 */
public class TimeInSeconds extends Node {
    private final OutputInt seconds = new OutputInt("seconds", 0);
    private final double startTime = System.currentTimeMillis();

    public TimeInSeconds() {
        super("TimeInSeconds");
        addVariable(seconds);
    }

    @Override
    public void update() {
        double t = (System.currentTimeMillis()-startTime)/1000.0;
        seconds.send((int)t);
    }
}
