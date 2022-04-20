package com.marginallyclever.version2;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
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

    public static Connection getMatchingConnection(Graph graph, Connection toMatch) {
        for(Connection c : graph.getConnections()) {
            if(c.getFrom().getUniqueAddress().equals(toMatch.getFrom().getUniqueAddress()) &&
                    c.getTo().getUniqueAddress().equals(toMatch.getTo().getUniqueAddress())) {
                return c;
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
}
