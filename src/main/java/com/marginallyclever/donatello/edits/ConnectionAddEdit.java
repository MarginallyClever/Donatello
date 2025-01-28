package com.marginallyclever.donatello.edits;

import com.marginallyclever.nodegraphcore.Connection;
import com.marginallyclever.nodegraphcore.Graph;
import com.marginallyclever.donatello.Donatello;

import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import java.util.List;

/**
 * Adds a {@link Connection} to a {@link com.marginallyclever.nodegraphcore.NodeVariable}.
 * Since the inbound node can only have one connection at a time, this edit also preserves any connection that has
 * to be removed.
 */
public class ConnectionAddEdit extends SignificantUndoableEdit {
    private final String name;
    private final Donatello editor;
    private final Connection connection;
    private final List<Connection> connectionsInto;

    public ConnectionAddEdit(String name, Donatello editor, Connection connection) {
        super();
        this.name = name;
        this.editor = editor;
        this.connection = connection;
        this.connectionsInto = editor.getGraph().getAllConnectionsInto(connection.getInput());
        doIt();
    }

    @Override
    public String getPresentationName() {
        return name;
    }

    private void doIt() {
        editor.lockClock();
        try {
            Graph graph = editor.getGraph();
            for(Connection c : connectionsInto) {
                graph.remove(c);
            }
            graph.add(connection);
            connection.getInput().setValue(connection.getOutput().getValue());
            editor.submit(connection.getTo());
        }
        finally {
            editor.unlockClock();
        }
    }

    @Override
    public void undo() throws CannotUndoException {
        editor.lockClock();
        try {
            Graph graph = editor.getGraph();
            graph.remove(connection);
            for(Connection c : connectionsInto) {
                graph.add(c);
            }
        }
        finally {
            editor.unlockClock();
        }
        super.undo();
    }

    @Override
    public void redo() throws CannotRedoException {
        doIt();
        super.redo();
    }
}
