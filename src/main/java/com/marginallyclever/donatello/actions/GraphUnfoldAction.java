package com.marginallyclever.donatello.actions;

import com.marginallyclever.donatello.Donatello;
import com.marginallyclever.nodegraphcore.Graph;
import com.marginallyclever.nodegraphcore.Node;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * Uncollapses all the editor's selected items that are {@link Graph}s.  The new list of selected items will contain
 * all the new graph elements plus any old elements that were not {@link Graph}s.
 * Each newly exposed graph is positioned relative to the original {@link Graph}.
 * @author Dan Royer
 * @since 2022-02-21
 */
public class GraphUnfoldAction extends AbstractAction implements EditorAction {
    /**
     * The editor being affected.
     */
    private final Donatello editor;

    /**
     * Constructor for subclasses to call.
     * @param name the name of this action visible on buttons and menu items.
     * @param editor the editor affected by this Action.
     */
    public GraphUnfoldAction(String name, Donatello editor) {
        super(name);
        this.editor = editor;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        List<Node> wasSelected = editor.getSelectedNodes();
        ArrayList<Node> toBeDeleted = new ArrayList<>();
        List<Node> newSelection = editor.getSelectedNodes();

        for(Node n : wasSelected) {
            if (n instanceof Graph) {
                toBeDeleted.add(n);
            }
        }

        for(Node n : toBeDeleted) {
            Graph inner = (Graph)n;
            // add the subgraph to this graph.
            editor.getGraph().add(inner);
            // make sure it is selected
            newSelection.addAll(inner.getNodes());
            // position it relative to the Subgraph it is replacing
            positionNodesRelativeTo(inner,n.getRectangle().x,n.getRectangle().y);
            // make sure to delete the Subgraph and clean up.
            editor.getGraph().remove(n);
            inner.clear();
        }

        // the list of selected nodes is all old nodes - subgraphs + newly expanded nodes.
        wasSelected.removeAll(toBeDeleted);
        newSelection.addAll(wasSelected);
        editor.setSelectedNodes(newSelection);
    }

    private void positionNodesRelativeTo(Graph nodeGraph,int dx, int dy) {
        Rectangle r = nodeGraph.getBounds();
        dx-=r.x;
        dy-=r.y;
        for(Node n : nodeGraph.getNodes()) {
            n.moveRelative(dx,dy);
        }
    }

    @Override
    public void updateEnableStatus() {
        List<Node> list = editor.getSelectedNodes();
        for(Node n : list) {
            if(n instanceof Graph) {
                setEnabled(true);
                return;
            }
        }
        setEnabled(false);
    }
}
