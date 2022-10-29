package com.marginallyclever.donatello.edits;

import com.marginallyclever.nodegraphcore.Node;
import com.marginallyclever.nodegraphcore.Connection;
import com.marginallyclever.nodegraphcore.Graph;
import com.marginallyclever.donatello.Donatello;

import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import java.util.ArrayList;
import java.util.List;

public class DeleteGraphEdit extends SignificantUndoableEdit {
    private final String name;
    protected final Donatello editor;
    private final List<Node> nodes = new ArrayList<>();
    private final List<Connection> interiorConnections = new ArrayList<>();
    private final List<Connection> exteriorConnections = new ArrayList<>();

    public DeleteGraphEdit(String name, Donatello editor, List<Node> selectedNodes) {
        super();
        this.name = name;
        this.editor = editor;
        this.nodes.addAll(selectedNodes);
        exteriorConnections.addAll(editor.getGraph().getExteriorConnections(selectedNodes));
        interiorConnections.addAll(editor.getGraph().getInteriorConnections(selectedNodes));
        doIt();
    }

    @Override
    public String getPresentationName() {
        return name;
    }

    protected void doIt() {
        editor.lockClock();
        try {
            Graph g = editor.getGraph();
            for (Node n : nodes) g.remove(n);
            for (Connection c : exteriorConnections) g.remove(c);
            for (Connection c : interiorConnections) g.remove(c);
            editor.setSelectedNodes(null);
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
            for(Node n : nodes) g.add(n);
            for(Connection c : exteriorConnections) g.add(c);
            for(Connection c : interiorConnections) g.add(c);
            editor.setSelectedNodes(nodes);
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

    public List<Node> getNodes() {
        return nodes;
    }

    public List<Connection> getInteriorConnections() {
        return interiorConnections;
    }

    public List<Connection> getExteriorConnections() {
        return exteriorConnections;
    }

}
