package com.marginallyclever.donatello.actions;

import com.marginallyclever.donatello.Donatello;
import com.marginallyclever.nodegraphcore.Node;
import com.marginallyclever.nodegraphcore.NodeConnection;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * Find and de-select the outer-most {@link Node}s from the list of selected annotatednodes.
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
     * Find all selected annotatednodes that do not have an input to another selected node OR do not have an output to another
     * selected node.  Then de-select all of the found annotatednodes.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        List<NodeConnection> connections = editor.getGraph().getConnections();
        List<Node> selectedNodes = new ArrayList<>(editor.getSelectedNodes());
        List<Node> edgeNodes = new ArrayList<>();

        for( Node n : selectedNodes ) {
            int inCount=0;
            int outCount=0;

            for( NodeConnection c : connections ) {
                if (c.isConnectedTo(n)) {
                    if(c.getInNode()==n) inCount++;
                    if(c.getOutNode()==n) outCount++;

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

        System.out.println("edge annotatednodes: "+edgeNodes.size());

        selectedNodes.removeAll(edgeNodes);
        editor.setSelectedNodes(selectedNodes);
        editor.repaint();
    }

    @Override
    public void updateEnableStatus() {
        setEnabled(editor.getSelectedNodes().size()>0);
    }
}
