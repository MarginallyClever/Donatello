package com.marginallyclever.version2;

import java.awt.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;

public class GraphHelper {

    /**
     * Returns a list of connections that connect to the selected {@link Node}s and unselected {@link Node}s.
     * @param selectedNodes the set of {@link Node}s.
     * @return a list of connections that connect to the selected {@link Node}s and unselected {@link Node}s.
     */
    public static List<Connection> getExteriorConnections(Graph graph, List<Node> selectedNodes) {
        return getConnectionsCounted(graph,selectedNodes,1);
    }

    /**
     * Returns a list of connections that connect between the selected {@link Node}s.
     * @param selectedNodes the set of {@link Node}s.
     * @return a list of connections that connect between the selected {@link Node}s.
     */
    public static List<Connection> getInteriorConnections(Graph graph, List<Node> selectedNodes) {
        return getConnectionsCounted(graph,selectedNodes,2);
    }

    public static Connection getMatchingConnection(Graph graph,ShippingDock from, ReceivingDock to) {
        for(Connection connection : graph.getConnections()) {
            if(connection.getFrom() == from && connection.getTo() == to) {
                return connection;
            }
        }
        return null;
    }

    /**
     * Returns a list of connections that have exactly <code>count</code> connections to the selected {@link Node}s.
     * @param selectedNodes the set of {@link Node}s.
     * @param count the number of connections to match.
     * @return a list of connections that have exactly <code>count</code> connections to the selected {@link Node}s.
     */
    private static List<Connection> getConnectionsCounted(Graph graph, List<Node> selectedNodes, int count) {
        ArrayList<Connection> found = new ArrayList<>();

        for(Connection c : graph.getConnections()) {
            int hits=0;
            for(Node n : selectedNodes) {
                if(c.isConnectedTo(n)) {
                    hits++;
                    if(hits==2) break;
                }
            }
            if(hits==count) {
                found.add(c);
            }
        }
        return found;
    }

    /**
     * Return the first connection point found within radius of a point
     * @param graph the graph to search
     * @param point center of search area
     * @param radiusLimit radius limit
     * @return a {@link Dock} describing the point found or null.
     */
    public static Dock getNearestConnectionPoint(Graph graph, Point point, double radiusLimit) {
        double rr = radiusLimit*radiusLimit;
        Dock info = null;

        for(Node n : graph.getNodes()) {
            // TODO is this the only time we need getInputs and getOutputs?  then lose them!
            for( ReceivingDock v : n.getInputs() ) {
                double r2 = v.getConnectionPoint().distanceSq(point);
                if (r2 < rr) {
                    rr = r2;
                    info = v;
                }
            }
            for( ShippingDock v : n.getOutputs() ) {
                double r2 = v.getConnectionPoint().distanceSq(point);
                if (r2 < rr) {
                    rr = r2;
                    info = v;
                }
            }
        }

        return info;
    }

    /**
     * Find and remove any {@link Connection} that connects to the given {@link ReceivingDock}.
     * @param to the {@link ReceivingDock} to be isolated.
     */
    public void removeAllConnectionsInto(Graph graph, ReceivingDock to) {
        graph.getConnections().removeAll(getAllConnectionsInto(graph,to));
    }

    public static List<Connection> getAllConnectionsInto(Graph graph, ReceivingDock to) {
        ArrayList<Connection> list = new ArrayList<>();

        for(Connection c : graph.getConnections()) {
            if(c.getTo() == to) list.add(c);
        }

        return list;
    }

    public static Graph deepCopy(Graph first) {
        try(ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            GraphWriter writer = new GraphWriter();
            writer.write(first, outputStream);

            byte[] serial = outputStream.toByteArray();
            try(ByteArrayInputStream inputStream = new ByteArrayInputStream(serial)) {
                GraphReader reader = new GraphReader();
                Graph second = reader.parse(inputStream);
                return second;
            }
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Returns a {@link List} of all {@link Node}s that intersect the given rectangle
     * @param searchArea the search area
     * @return a {@link List} of all {@link Node}s that intersect the given rectangle
     */
    public static List<Node> getNodesInRectangle(Graph graph, Rectangle searchArea) {
        if(searchArea==null) throw new InvalidParameterException("selectionArea cannot be null.");
        ArrayList<Node> found = new ArrayList<>();
        for(Node n : graph.getNodes()) {
            if(searchArea.intersects(n.getRectangle())) found.add(n);
        }
        return found;
    }
}
