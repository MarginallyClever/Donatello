package com.marginallyclever.donatello;

import com.marginallyclever.nodegraphcore.Node;
import com.marginallyclever.nodegraphcore.NodeConnection;
import com.marginallyclever.nodegraphcore.NodeGraph;

import java.util.ArrayList;
import java.util.List;

public class NodeHelper {
    public static List<Node> getNeighbors(NodeGraph graph, List<Node> startingNodes) {
        List<Node> adjacent = new ArrayList<>();
        List<NodeConnection> potential = new ArrayList<>();

        for( NodeConnection c : graph.getConnections() ) {
            for( Node n : startingNodes ) {
                if (c.isConnectedTo(n)) {
                    potential.add(c);
                    break;
                }
            }
        }

        for( Node n : graph.getNodes() ) {
            if(startingNodes.contains(n)) continue;
            for( NodeConnection c : potential ) {
                if (c.isConnectedTo(n)) {
                    adjacent.add(n);
                }
            }
        }

        return adjacent;
    }
}
