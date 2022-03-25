package com.marginallyclever.donatello.edits;

import com.marginallyclever.nodegraphcore.NodeConnection;
import com.marginallyclever.nodegraphcore.NodeGraph;
import com.marginallyclever.donatello.Donatello;

import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import java.util.List;

/**
 * Adds a {@link NodeConnection} to a {@link com.marginallyclever.nodegraphcore.NodeVariable}.
 * Since the inbound node can only have one connection at a time, this edit also preserves any connection that has
 * to be removed.
 */
public class AddConnectionEdit extends SignificantUndoableEdit {
    private final String name;
    private final Donatello editor;
    private final NodeConnection connection;
    private final List<NodeConnection> connectionsInto;

    public AddConnectionEdit(String name, Donatello editor, NodeConnection connection) {
        super();
        this.name = name;
        this.editor = editor;
        this.connection = connection;
        this.connectionsInto = editor.getGraph().getAllConnectionsInto(connection.getOutVariable());
        doIt();
    }

    @Override
    public String getPresentationName() {
        return name;
    }

    private void doIt() {
        NodeGraph graph = editor.getGraph();
        for(NodeConnection c : connectionsInto) {
            graph.remove(c);
        }
        graph.add(connection);
    }

    @Override
    public void undo() throws CannotUndoException {
        NodeGraph graph = editor.getGraph();
        graph.remove(connection);
        for(NodeConnection c : connectionsInto) {
            graph.add(c);
        }
        super.undo();
    }

    @Override
    public void redo() throws CannotRedoException {
        doIt();
        super.redo();
    }
}
