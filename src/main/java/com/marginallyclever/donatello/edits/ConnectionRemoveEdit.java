package com.marginallyclever.donatello.edits;

import com.marginallyclever.nodegraphcore.Connection;
import com.marginallyclever.donatello.Donatello;

import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;

public class ConnectionRemoveEdit extends SignificantUndoableEdit {
    private final String name;
    private final Donatello editor;
    private final Connection connection;

    public ConnectionRemoveEdit(String name, Donatello editor, Connection connection) {
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
        editor.lockClock();
        try {
            editor.getGraph().add(connection);
            connection.apply();
            editor.submit(connection.getTo());
        }
        finally {
            editor.unlockClock();
        }
        super.undo();
    }

    private void doIt() {
        editor.lockClock();
        try {
            editor.getGraph().remove(connection);
            connection.reset();
            editor.submit(connection.getTo());
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
