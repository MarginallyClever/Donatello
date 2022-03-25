package com.marginallyclever.donatello.actions;

import com.marginallyclever.donatello.Donatello;
import com.marginallyclever.donatello.NodeHelper;
import com.marginallyclever.nodegraphcore.Node;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.*;

/**
 * Select all the {@link com.marginallyclever.nodegraphcore.Node}s between two selected nodes.
 * @author Dan Royer
 * @since 2022-03-24
 */
public class SelectShortestPathAction extends AbstractAction implements EditorAction {
    /**
     * The editor being affected.
     */
    private final Donatello editor;

    /**
     * Constructor for subclasses to call.
     * @param name the name of this action visible on buttons and menu items.
     * @param editor the editor affected by this Action.
     */
    public SelectShortestPathAction(String name, Donatello editor) {
        super(name);
        this.editor = editor;
    }

    class DistanceNode {
        public Node node;
        public double distance;
        public DistanceNode prev;

        public DistanceNode(Node n) {
            this(n,Double.POSITIVE_INFINITY);
        }

        public DistanceNode(Node n, double d) {
            node = n;
            distance = d;
            prev = null;
        }
    }
    private final Set<DistanceNode> unvisitedNodes = new HashSet<>();
    private final Set<DistanceNode> visitedNodes = new HashSet<>();

    /**
     * Finds the shortest path between two nodes and selects every node on the path.
     * Uses <a href='http://en.wikipedia.org/wiki/Dijkstra%27s_algorithm'>Dijkstra's algorithm</a>.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        List<Node> selectedNodes = editor.getSelectedNodes();
        Node head = selectedNodes.get(0);
        Node tail = selectedNodes.get(1);

        initializeAlgorithm();
        DistanceNode current = getDistanceNode(head);
        DistanceNode goal = getDistanceNode(tail);
        current.distance = 0;
        while(!unvisitedNodes.isEmpty() && !visitedNodes.contains(goal)) {
            current = getSmallestDistanceNode();
            if(current == null) break;
            visitAllAdjacentNodes(current);
            visitedNodes.add(current);
            unvisitedNodes.remove(current);

        }

        if(visitedNodes.contains(goal)) {
            // path found
            List<Node> path = new ArrayList<>();
            DistanceNode backwards = goal;
            while (backwards != null) {
                path.add(backwards.node);
                backwards = backwards.prev;
            }
            Collections.reverse(path);
            editor.setSelectedNodes(path);
            editor.repaint();
        }
        // clean up
        visitedNodes.clear();
        unvisitedNodes.clear();
    }

    private DistanceNode getSmallestDistanceNode() {
        double smallest = Double.POSITIVE_INFINITY;
        DistanceNode smallestNode = null;
        for(DistanceNode n : unvisitedNodes) {
            if(n.distance < smallest) {
                smallest = n.distance;
                smallestNode = n;
            }
        }
        return smallestNode;
    }

    // For the current node, consider all of its unvisited neighbors and calculate their tentative distances through
    // the current node. Compare the newly calculated tentative distance to the current assigned value and assign the
    // smaller one. For example, if the current node A is marked with a distance of 6, and the edge connecting it with
    // a neighbor B has length 2, then the distance to B through A will be 6 + 2 = 8. If B was previously marked with
    // a distance greater than 8 then change it to 8. Otherwise, the current value will be kept.
    private void visitAllAdjacentNodes(DistanceNode current) {
        for(Node n : getAdjacentNodes(current)) {
            DistanceNode neighbor = getDistanceNode(n);
            if(unvisitedNodes.contains(neighbor)) {
                double newDistance = current.distance + 1;
                if(newDistance < neighbor.distance) {
                    neighbor.distance = newDistance;
                    neighbor.prev = current;
                }
            }
        }
    }

    // get all nodes adjacent to the given node from the unvisited list.
    private List<Node> getAdjacentNodes(DistanceNode current) {
        List<Node> from = new ArrayList<>();
        from.add(current.node);
        List<Node> candidates = NodeHelper.getNeighbors(editor.getGraph(),from);
        List<Node> adjacentNodes = new ArrayList<>();

        for(Node n : candidates) {
            if(getDistanceNode(n)!=null) {
                adjacentNodes.add(n);
            }
        }
        return adjacentNodes;
    }

    private DistanceNode getDistanceNode(Node toFind) {
        for(DistanceNode n : unvisitedNodes) {
            if(n.node == toFind) return n;
        }
        return null;
    }

    // make the initial set of unvisited nodes.  assign each node a distance of infinity.
    private void initializeAlgorithm() {
        for(Node n : editor.getGraph().getNodes()) {
            unvisitedNodes.add(new DistanceNode(n));
        }
    }

    @Override
    public void updateEnableStatus() {
        setEnabled(editor.getSelectedNodes().size() == 2);
    }
}
