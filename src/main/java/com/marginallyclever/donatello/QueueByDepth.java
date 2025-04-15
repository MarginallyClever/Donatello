package com.marginallyclever.donatello;

import com.marginallyclever.nodegraphcore.Connection;
import com.marginallyclever.nodegraphcore.Graph;
import com.marginallyclever.nodegraphcore.Node;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * <p>Queue the {@link Node}s of a {@link Graph} in the {@link com.marginallyclever.nodegraphcore.ThreadPoolScheduler}
 * based on their depth in the graph.  The depth of each {@link Node} in the {@link Graph} is based on the greatest
 * depth of upstream nodes.</p>
 */
public class QueueByDepth {
    private static final Logger logger = LoggerFactory.getLogger(QueueByDepth.class);

    private final Map<Node, ArrayList<Node>> outgoing = new HashMap<>();
    private final Map<Node, Integer> inDegree = new HashMap<>();
    private final Map<Node, Integer> depth = new HashMap<>();
    private final Queue<Node> queue = new LinkedList<>();

    /**
     * Queue the {@link Node}s of a {@link Graph} in the {@link com.marginallyclever.nodegraphcore.ThreadPoolScheduler}
     * @param editor the {@link Donatello} editor
     * @param graph the {@link Graph} to queue
     * @param maxDepth the maximum depth to queue
     */
    public QueueByDepth(Donatello editor, Graph graph,int maxDepth) {
        logger.trace("QueueByDepth start");

        buildOutgoingAndInDegreeMaps(graph);
        buildDepthMap();

        // Step 3: Queue the nodes in the ThreadPoolScheduler based on their depth
        // the nodes with the lowest depth are queued first
        ArrayList<Node> nodes = new ArrayList<>(graph.getNodes());
        // Sort nodes based on their depth.  unattached nodes are at depth 0
        nodes.sort(Comparator.comparingInt(n -> depth.getOrDefault(n, 0)));

        for(Node n : nodes) {
            if(depth.getOrDefault(n,0)<=maxDepth && n.isDirty()) {
                editor.submit(n);
            }
        }
        logger.trace("QueueByDepth end");
    }
    /**
     * Build the map of outgoing connections and in-degree for each node.
     * @param graph the graph to build the map from
     */
    private void buildOutgoingAndInDegreeMaps(Graph graph) {
        for (Connection c : graph.getConnections()) {
            var from = c.getFrom();
            var to = c.getTo();
            outgoing.computeIfAbsent(from, k -> new ArrayList<>()).add(to);
            inDegree.put(to, inDegree.getOrDefault(to, 0) + 1);
            inDegree.putIfAbsent(from, 0);
            depth.putIfAbsent(from, 0);
            depth.putIfAbsent(to, 0);
        }
    }

    /**
     * Kahn's algorithm for topological sort + depth calc
     */
    private void buildDepthMap() {
        for (Node n : inDegree.keySet()) {
            if (0 == inDegree.get(n)) queue.add(n);
        }

        while (!queue.isEmpty()) {
            Node node = queue.poll();
            int currentDepth = depth.get(node);

            for (Node child : outgoing.getOrDefault(node, new ArrayList<>())) {
                // Update depth
                depth.put(child, Math.max(depth.get(child), currentDepth + 1));

                // Decrease in-degree
                inDegree.put(child, inDegree.get(child) - 1);
                if (inDegree.get(child) == 0) {
                    queue.add(child);
                }
            }
        }
    }

}
