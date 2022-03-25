package com.marginallyclever.donatello.actions;

import javax.swing.*;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.UndoManager;
import java.awt.event.ActionEvent;

public class RedoAction extends AbstractAction {
    private final UndoManager undoManager;
    private UndoAction actionUndo;

    public RedoAction(UndoManager undoManager) {
        super("Redo");
        setEnabled(false);
        this.undoManager = undoManager;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            undoManager.redo();
        }
        catch (CannotRedoException ex) {
            // TODO deal with this
            ex.printStackTrace();
        }
        update();
        actionUndo.update();
    }

    public void update() {
        if (undoManager.canRedo()) {
            setEnabled(true);
            putValue(Action.NAME, undoManager.getRedoPresentationName());
        } else {
            setEnabled(false);
            putValue(Action.NAME, "Redo");
        }
    }

    public void setActionUndo(UndoAction actionUndo) {
        this.actionUndo = actionUndo;
    }
}
