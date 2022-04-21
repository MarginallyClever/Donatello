package com.marginallyclever.donatello.actions;

import com.marginallyclever.donatello.Donatello;
import com.marginallyclever.version2.Node;
import com.marginallyclever.version2.Connection;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * Find and de-select the outer-most {@link Node}s from the list of selected nodes.
 */
public class ShrinkSelectionAction extends AbstractAction implements EditorAction {
    /**
     * The editor being affected.
     */
    private final Donatello editor;

    /**
     * Constructor for subclasses to call.
     * @param name the name of this action visible on buttons and menu items.
     * @param editor the editor affected by this Action.
     */
    public ShrinkSelectionAction(String name, Donatello editor) {
        super(name);
        this.editor = editor;
    }

    /**
     * Find all selected nodes that do not have an input to another selected node OR do not have an output to another
     * selected node.  Then de-select all of the found nodes.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        List<Connection> connections = editor.getGraph().getConnections();
        List<Node> selectedNodes = new ArrayList<>(editor.getSelectedNodes());
        List<Node> edgeNodes = new ArrayList<>();

        for( Node n : selectedNodes ) {
            int inCount=0;
            int outCount=0;

            for( Connection c : connections ) {
                if (c.isConnectedTo(n)) {
                    if(c.getFromNode()==n) inCount++;
                    if(c.getToNode()==n) outCount++;

                    // node has a connection to another selected node?
                    if( !selectedNodes.contains(c.getOtherNode(n)) ) {
                        edgeNodes.add(n);
                        break;
                    }
                }
            }
            if(inCount==0 || outCount==0) {
                edgeNodes.add(n);
            }
        }

        System.out.println("edge nodes: "+edgeNodes.size());

        selectedNodes.removeAll(edgeNodes);
        editor.setSelectedNodes(selectedNodes);
        editor.repaint();
    }

    @Override
    public void updateEnableStatus() {
        setEnabled(editor.getSelectedNodes().size()>0);
    }
}
