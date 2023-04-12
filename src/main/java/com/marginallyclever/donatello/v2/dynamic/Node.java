package com.marginallyclever.donatello.v2.dynamic;

/**
 * A node is a component that can be connected to other nodes to form a graph.
 */
public interface Node {
    void setInput(Object input);
    Object getOutput();
    void process();
}