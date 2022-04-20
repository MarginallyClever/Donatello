package com.marginallyclever.version2;

import java.awt.*;
import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Graph extends AbstractNamedEntity implements Serializable {
    @Serial
    private static final long serialVersionUID = -569456899240989898L;

    /**
     * The nodes in this graph.
     */
    private final List<Node> nodes = new ArrayList<>();

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

    private final Rectangle bounds = new Rectangle();

    public Graph() {
        super();
    }

    public void add(Node node) {
        nodes.add(node);
    }

    public void remove(Node node) {
        nodes.remove(node);
    }

    /**
     * Add all {@link Node}s and {@link Connection}s from one model to this model.
     * @param graph the model to add.
     */
    public void add(Graph graph) {
        if(graph==null) throw new IllegalArgumentException("nodeGraph cannot be null.");

        nodes.addAll(graph.nodes);
        connections.addAll(graph.connections);
    }

    public void remove(Graph graph) {
        if(graph==null) throw new IllegalArgumentException("graph cannot be null.");
        nodes.removeAll(graph.nodes);
        connections.removeAll(graph.connections);
    }

    public List<Node> getNodes() {
        return nodes;
    }

    public void add(Connection connection) {
        connections.add(connection);
    }

    public void remove(Connection connection) {
        connections.remove(connection);
    }

    public List<Connection> getConnections() {
        return connections;
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
                "entryPoints=[" + getEntryPointsAsStrings() + ']' +
                ", exitPoints=[" + getExitPointsAsStrings() + ']' +
                ", nodes=" + nodes +
                ", connections=" + connections +
                '}';
    }

    private String getEntryPointsAsStrings() {
        return getDocksAsString(getEntryPoints());
    }

    private String getExitPointsAsStrings() {
        return getDocksAsString(getExitPoints());
    }

    private String getDocksAsString(List<? extends Dock> docks) {
        String result = "";
        String add="";
        for( Dock dock: docks ) {
            result += add + dock.toString();
            add=",";
        }
        return result;
    }

    public void addEntryPoint(ShippingDock dock) {
        entryPoints.add(dock);
    }

    public List<ShippingDock> getEntryPoints() {
        return entryPoints;
    }

    /**
     * @param name the dock to find
     * @return the dock with the given name, or null if not found.
     */
    public ShippingDock getEntryPoint(String name) {
        for(ShippingDock d : getEntryPoints()) {
            if(d.getName().equals(name)) return d;
        }
        return null;
    }

    public void addExitPoint(ReceivingDock dock) {
        exitPoints.add(dock);
    }

    public List<ReceivingDock> getExitPoints() {
        return exitPoints;
    }

    /**
     * @param name the dock to find
     * @return the dock with the given name, or null if not found.
     */
    public ReceivingDock getExitPoint(String name) {
        for(ReceivingDock d : getExitPoints()) {
            if(d.getName().equals(name)) return d;
        }
        return null;
    }

    public Rectangle getBounds() {
        return bounds;
    }

    /**
     * update bounds of all nodes in this graph.
     */
    public void updateBounds() {
        for(Node n : nodes) {
            n.updateBounds();
        }
    }

    public boolean isEmpty() {
        return getNodes().isEmpty() && getConnections().isEmpty();
    }
}
