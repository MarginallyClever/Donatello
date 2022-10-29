package com.marginallyclever.donatello.edits;

import com.marginallyclever.nodegraphcore.Node;
import com.marginallyclever.donatello.Donatello;

import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import java.util.List;

public class ReorderEdit extends SignificantUndoableEdit {
    private final String name;
    private final Donatello editor;
    private final Node node;
    private final int from, to;

    public ReorderEdit(String name, Donatello editor, Node node, int to) {
        super();
        this.name = name;
        this.editor = editor;
        this.node = node;
        this.to = to;
        this.from = editor.getGraph().getNodes().indexOf(node);
    }

    @Override
    public String getPresentationName() {
        return name;
    }

    public void doIt() {
        editor.lockClock();
        try {
            List<Node> list = editor.getGraph().getNodes();
            list.remove(node);
            list.add(to,node);
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
            List<Node> list = editor.getGraph().getNodes();
            list.remove(node);
            list.add(from,node);
            editor.repaint();
            super.undo();
        }
        finally {
            editor.unlockClock();
        }
    }

    @Override
    public void redo() throws CannotRedoException {
        doIt();
        super.redo();
    }
}
