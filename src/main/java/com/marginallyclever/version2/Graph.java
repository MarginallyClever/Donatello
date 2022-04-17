package com.marginallyclever.version2;

import java.util.ArrayList;
import java.util.IllegalFormatPrecisionException;
import java.util.List;
import java.util.Map;

public class Graph extends AbstractNode {
    /**
     * The nodes in this graph.
     */
    private final List<AbstractNode> nodes = new ArrayList<>();

    /**
     * The connections between node docks.
     */
    private final List<Connection> connections = new ArrayList<>();

    /**
     * The entry points for data from outside this graph, aka the method parameters.
     */
    private final List<ShippingDock> entryPoints = new ArrayList<>();

    /**
     * The exit points for data to the outside of this graph, aka the return values.
     */
    private final List<ReceivingDock> exitPoints = new ArrayList<>();

    public Graph() {
        super();
    }

    public void addNode(AbstractNode node) {
        nodes.add(node);
    }

    public void addConnection(Connection connection) {
        connections.add(connection);
    }

    public List<Connection> getConnections() {
        return connections;
    }

    public void removeNode(AbstractNode node) {
        nodes.remove(node);
    }

    public void removeConnection(Connection connection) {
        connections.remove(connection);
    }

    public void clear() {
        nodes.clear();
        connections.clear();
    }

    public void update() {
        for(Node n : nodes) n.update();
    }


    @Override
    public String toString() {
        return "Graph{" +
                "name='" + getName() + '\'' +
                ", uniqueID='" + getUniqueID() + '\'' +
                ", inputs=[" + getInputsAsString() + ']' +
                ", outputs=[" + getOutputsAsString() + ']' +
                ", annotatednodes=" + nodes +
                ", connections=" + connections +
                '}';
    }

    public String getInputsAsString() {
        return getDocksAsString(getInputs());
    }

    public String getOutputsAsString() {
        return getDocksAsString(getOutputs());
    }

    public String getDocksAsString(List<? extends Dock> docks) {
        String result = "";
        String add="";
        for( Dock dock: docks ) {
            result += add + dock.toString();
            add=",";
        }
        return result;
    }

    public List<ReceivingDock> getExitPoints() {
        return exitPoints;
    }

    public List<ShippingDock> getEntryPoints() {
        return entryPoints;
    }
}
