package com.marginallyclever.donatello.edits;

import com.marginallyclever.version2.Graph;
import com.marginallyclever.donatello.Donatello;
import com.marginallyclever.version2.GraphHelper;

import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import java.awt.*;

public class PasteGraphEdit extends SignificantUndoableEdit {
    private final String name;
    private final Donatello editor;
    private final Graph copiedGraph;
    private final Point m;

    public PasteGraphEdit(String name, Donatello editor, Graph graph) {
        super();
        this.name = name;
        this.editor = editor;
        this.copiedGraph = GraphHelper.deepCopy(graph);
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
