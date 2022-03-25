package com.marginallyclever.donatello.edits;

import com.marginallyclever.nodegraphcore.NodeGraph;
import com.marginallyclever.donatello.Donatello;

import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import java.awt.*;

public class PasteGraphEdit extends SignificantUndoableEdit {
    private final String name;
    private final Donatello editor;
    private final NodeGraph copiedGraph;
    private final Point m;

    public PasteGraphEdit(String name, Donatello editor, NodeGraph graph) {
        super();
        this.name = name;
        this.editor = editor;
        this.copiedGraph = graph.deepCopy();
        this.m = editor.getPaintArea().transformMousePoint(editor.getMousePosition());
        doIt();
    }

    @Override
    public String getPresentationName() {
        return name;
    }

    private void doIt() {
        editor.getGraph().add(copiedGraph);
        editor.setSelectedNodes(copiedGraph.getNodes());
        copiedGraph.updateBounds();
        Rectangle r = copiedGraph.getBounds();
        editor.moveSelectedNodes(m.x-r.x,m.y-r.y);
        editor.repaint();
    }

    @Override
    public void undo() throws CannotUndoException {
        editor.getGraph().remove(copiedGraph);
        editor.setSelectedNodes(null);
        editor.repaint();
        super.undo();
    }

    @Override
    public void redo() throws CannotRedoException {
        doIt();
        super.redo();
    }
}
