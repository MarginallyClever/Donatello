package com.marginallyclever.donatello;

import com.marginallyclever.nodegraphcore.Node;
import com.marginallyclever.nodegraphcore.Connection;
import com.marginallyclever.nodegraphcore.Graph;

import java.util.ArrayList;
import java.util.List;

/**
 * Helper class for working with {@link Node} objects in a {@link Graph}.
 * Provides methods to retrieve outgoing connections and neighbors of nodes.
 */
public class NodeHelper {
    public static List<Node> getAllOutgoingConnections(Graph graph, Node source) {
        List<Node> adjacent = new ArrayList<>();

        for( Connection c : graph.getConnections() ) {
            if (c.isConnectedTo(source) && c.getFrom()==source) {
                adjacent.add(c.getTo());
            }
        }

        return adjacent;
    }

    public static List<Node> getNeighbors(Graph graph, List<Node> startingNodes) {
        List<Node> adjacent = new ArrayList<>();
        List<Connection> potential = new ArrayList<>();

        for( Connection c : graph.getConnections() ) {
            for( Node n : startingNodes ) {
                if (c.isConnectedTo(n)) {
                    potential.add(c);
                    break;
                }
            }
        }

        for( Node n : graph.getNodes() ) {
            if(startingNodes.contains(n)) continue;
            for( Connection c : potential ) {
                if (c.isConnectedTo(n)) {
                    adjacent.add(n);
                }
            }
        }

        return adjacent;
    }
}
