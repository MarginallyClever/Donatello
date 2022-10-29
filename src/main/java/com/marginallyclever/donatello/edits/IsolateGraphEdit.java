package com.marginallyclever.donatello.edits;

import com.marginallyclever.nodegraphcore.Node;
import com.marginallyclever.nodegraphcore.Connection;
import com.marginallyclever.nodegraphcore.Graph;
import com.marginallyclever.donatello.Donatello;

import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import java.util.ArrayList;
import java.util.List;

public class IsolateGraphEdit extends SignificantUndoableEdit {
    private final String name;
    private final Donatello editor;
    private final List<Connection> connections = new ArrayList<>();

    public IsolateGraphEdit(String name, Donatello editor, List<Node> selectedNodes) {
        super();
        this.name = name;
        this.editor = editor;
        connections.addAll(editor.getGraph().getExteriorConnections(selectedNodes));
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
            graph.getConnections().removeAll(connections);
            editor.repaint();
        }
        finally {
            editor.unlockClock();
        }
    }

    @Override
    public void undo() throws CannotUndoException {
        editor.lockClock();
        try {
            Graph g = editor.getGraph();
            for(Connection c : connections) g.add(c);
            editor.repaint();
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
