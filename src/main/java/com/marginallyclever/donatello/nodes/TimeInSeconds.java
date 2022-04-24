package com.marginallyclever.donatello.nodes;

import com.marginallyclever.nodegraphcore.Node;
import com.marginallyclever.nodegraphcore.NodeVariable;

/**
 * Publishes the time in seconds continuously.
 */
public class TimeInSeconds extends Node {
    private final NodeVariable<Number> seconds = NodeVariable.newInstance("seconds", Number.class, 0,false,true);

    public TimeInSeconds() {
        super("TimeInSeconds");
        addVariable(seconds);
    }

    @Override
    public void updateIfNotDirty() throws Exception {
        update();
    }

    @Override
    public void update() throws Exception {
        seconds.setValue(System.currentTimeMillis()*0.001);
    }
}
