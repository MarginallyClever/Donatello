package com.marginallyclever.donatello.edits;

import com.marginallyclever.nodegraphcore.Node;
import com.marginallyclever.nodegraphcore.NodeConnection;
import com.marginallyclever.nodegraphcore.NodeGraph;
import com.marginallyclever.donatello.Donatello;

import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import java.util.ArrayList;
import java.util.List;

public class DeleteGraphEdit extends SignificantUndoableEdit {
    private final String name;
    protected final Donatello editor;
    private final List<Node> nodes = new ArrayList<>();
    private final List<NodeConnection> interiorConnections = new ArrayList<>();
    private final List<NodeConnection> exteriorConnections = new ArrayList<>();

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
        NodeGraph g = editor.getGraph();
        for(Node n : nodes) g.remove(n);
        for(NodeConnection c : exteriorConnections) g.remove(c);
        for(NodeConnection c : interiorConnections) g.remove(c);
        editor.setSelectedNodes(null);
        editor.repaint();
    }

    @Override
    public void undo() throws CannotUndoException {
        NodeGraph g = editor.getGraph();
        for(Node n : nodes) g.add(n);
        for(NodeConnection c : exteriorConnections) g.add(c);
        for(NodeConnection c : interiorConnections) g.add(c);
        editor.setSelectedNodes(nodes);
        editor.repaint();
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

    public List<NodeConnection> getInteriorConnections() {
        return interiorConnections;
    }

    public List<NodeConnection> getExteriorConnections() {
        return exteriorConnections;
    }

}
