package com.marginallyclever.donatello.edits;

import com.marginallyclever.version2.GraphHelper;
import com.marginallyclever.version2.Node;
import com.marginallyclever.version2.Connection;
import com.marginallyclever.version2.Graph;
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
        connections.addAll(GraphHelper.getExteriorConnections(editor.getGraph(),selectedNodes));
        doIt();
    }

    @Override
    public String getPresentationName() {
        return name;
    }

    private void doIt() {
        Graph graph = editor.getGraph();
        graph.getConnections().removeAll(connections);
        editor.repaint();
    }

    @Override
    public void undo() throws CannotUndoException {
        Graph g = editor.getGraph();
        for(Connection c : connections) g.add(c);
        editor.repaint();
        super.undo();
    }

    @Override
    public void redo() throws CannotRedoException {
        doIt();
        super.redo();
    }
}
