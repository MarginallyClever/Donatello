package com.marginallyclever.donatello.edits;

import com.marginallyclever.donatello.Donatello;
import com.marginallyclever.donatello.QueueByDepth;
import com.marginallyclever.nodegraphcore.Graph;
import com.marginallyclever.nodegraphcore.Node;

import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import java.awt.*;

public class GraphPasteEdit extends SignificantUndoableEdit {
    private final String name;
    private final Donatello editor;
    private final Graph copiedGraph;

    public GraphPasteEdit(String name, Donatello editor, Graph graph, Point position) {
        super();
        this.name = name;
        this.editor = editor;
        this.copiedGraph = graph.deepCopy();
        copiedGraph.updateBounds();
        Rectangle r = copiedGraph.getBounds();
        int dx = position.x - r.x - r.width/2;
        int dy = position.y - r.y - r.height/2;

        for(Node n : copiedGraph.getNodes()) {
            n.moveRelative(dx, dy);
        }
        System.out.println("test "+position.x+" "+position.y);

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
            editor.repaint();
            new QueueByDepth(editor, copiedGraph,0);
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
