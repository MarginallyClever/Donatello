package com.marginallyclever.donatello.edits;

import com.marginallyclever.nodegraphcore.Graph;
import com.marginallyclever.donatello.Donatello;

import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import java.awt.*;

public class GraphPasteEdit extends SignificantUndoableEdit {
    private final String name;
    private final Donatello editor;
    private final Graph copiedGraph;
    private final Point m;

    public GraphPasteEdit(String name, Donatello editor, Graph graph) {
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
        editor.lockClock();
        try {
            editor.getGraph().add(copiedGraph);
            editor.setSelectedNodes(copiedGraph.getNodes());
            copiedGraph.updateBounds();
            Rectangle r = copiedGraph.getBounds();
            editor.moveSelectedNodes(m.x - r.x, m.y - r.y);
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
            editor.getGraph().remove(copiedGraph);
            editor.setSelectedNodes(null);
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
