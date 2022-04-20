package com.marginallyclever.donatello.edits;

import com.marginallyclever.version2.Connection;
import com.marginallyclever.version2.Graph;
import com.marginallyclever.donatello.Donatello;

import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import java.util.List;

/**
 * Adds a {@link Connection} to a {@link com.marginallyclever.nodegraphcore.Dock}.
 * Since the inbound node can only have one connection at a time, this edit also preserves any connection that has
 * to be removed.
 */
public class AddConnectionEdit extends SignificantUndoableEdit {
    private final String name;
    private final Donatello editor;
    private final Connection connection;
    private final List<Connection> connectionsInto;

    public AddConnectionEdit(String name, Donatello editor, Connection connection) {
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
        Graph graph = editor.getGraph();
        for(Connection c : connectionsInto) {
            graph.remove(c);
        }
        graph.add(connection);
    }

    @Override
    public void undo() throws CannotUndoException {
        Graph graph = editor.getGraph();
        graph.remove(connection);
        for(Connection c : connectionsInto) {
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
