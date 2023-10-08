package com.marginallyclever.donatello.actions;

import com.marginallyclever.nodegraphcore.Node;
import com.marginallyclever.nodegraphcore.Connection;
import com.marginallyclever.nodegraphcore.Graph;
import com.marginallyclever.donatello.Donatello;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.List;

/**
 * Copies the editor's selected {@link Node}s and their shared {@link Connection}s to the copy buffer.  The copy
 * buffer ensures the items so that they can be pasted even after they are deleted.<br/>
 * <br/>
 * It cannot be undone.  It does not affect the {@link Graph}.  Furthermore, this lets the user undo several
 * commands, copy something, redo back to where they were, and then paste the copy.
 * @author Dan Royer
 * @since 2022-02-21
 */
public class GraphCopyAction extends AbstractAction implements EditorAction {
    /**
     * The editor being affected.
     */
    private final Donatello editor;

    /**
     * Constructor for subclasses to call.
     * @param name the name of this action visible on buttons and menu items.
     * @param editor the editor affected by this Action.
     */
    public GraphCopyAction(String name, Donatello editor) {
        super(name);
        this.editor = editor;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Graph graph = editor.getGraph();

        Graph modelB = new Graph();
        List<Node> selectedNodes = editor.getSelectedNodes();
        for(Node n : selectedNodes) modelB.add(n);
        List<Connection> selectedConnections = graph.getInteriorConnections(selectedNodes);
        for(Connection c : selectedConnections) modelB.add(c);
        editor.setCopiedGraph(modelB.deepCopy());
    }

    @Override
    public void updateEnableStatus() {
        setEnabled(!editor.getSelectedNodes().isEmpty());
    }
}
