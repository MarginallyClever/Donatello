package com.marginallyclever.donatello;

import com.marginallyclever.version2.*;

import java.util.ArrayList;
import java.util.List;

public class NodeHelper {
    public static List<NamedEntity> getAllOutgoingConnections(Graph graph, Node source) {
        List<NamedEntity> adjacent = new ArrayList<>();

        for( Connection c : graph.getConnections() ) {
            if (c.isConnectedTo(source) && c.getFrom().getOwner()==source) {
                adjacent.add(c.getTo().getOwner());
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
