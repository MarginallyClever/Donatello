package com.marginallyclever.donatello.edits;

import com.marginallyclever.nodegraphcore.Node;
import com.marginallyclever.nodegraphcore.NodeConnection;
import com.marginallyclever.nodegraphcore.NodeGraph;
import com.marginallyclever.donatello.Donatello;

import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import java.util.ArrayList;
import java.util.List;

public class IsolateGraphEdit extends SignificantUndoableEdit {
    private final String name;
    private final Donatello editor;
    private final List<NodeConnection> connections = new ArrayList<>();

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
        NodeGraph graph = editor.getGraph();
        graph.getConnections().removeAll(connections);
        editor.repaint();
    }

    @Override
    public void undo() throws CannotUndoException {
        NodeGraph g = editor.getGraph();
        for(NodeConnection c : connections) g.add(c);
        editor.repaint();
        super.undo();
    }

    @Override
    public void redo() throws CannotRedoException {
        doIt();
        super.redo();
    }
}
