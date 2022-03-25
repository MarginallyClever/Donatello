package com.marginallyclever.donatello.edits;

import com.marginallyclever.nodegraphcore.NodeConnection;
import com.marginallyclever.donatello.Donatello;

import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;

public class RemoveConnectionEdit extends SignificantUndoableEdit {
    private final String name;
    private final Donatello editor;
    private final NodeConnection connection;

    public RemoveConnectionEdit(String name, Donatello editor, NodeConnection connection) {
        super();
        this.name = name;
        this.editor = editor;
        this.connection = connection;
        doIt();
    }

    @Override
    public String getPresentationName() {
        return name;
    }

    @Override
    public void undo() throws CannotUndoException {
        editor.getGraph().add(connection);
        super.undo();
    }

    private void doIt() {
        editor.getGraph().remove(connection);
    }

    @Override
    public void redo() throws CannotRedoException {
        doIt();
        super.redo();
    }
}
