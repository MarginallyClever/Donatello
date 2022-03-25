package com.marginallyclever.donatello.actions;

import javax.swing.*;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoManager;
import java.awt.event.ActionEvent;

public class UndoAction extends AbstractAction {
    private final UndoManager undoManager;
    private RedoAction actionRedo;

    public UndoAction(UndoManager undoManager) {
        super("Undo");
        setEnabled(false);
        this.undoManager = undoManager;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            undoManager.undo();
        }
        catch (CannotUndoException ex) {
            // TODO deal with this
            //ex.printStackTrace();
        }
        update();
        actionRedo.update();
    }

    public void update() {
        if (undoManager.canUndo()) {
            setEnabled(true);
            putValue(Action.NAME, undoManager.getUndoPresentationName());
        } else {
            setEnabled(false);
            putValue(Action.NAME, "Undo");
        }
    }

    public void setActionRedo(RedoAction actionRedo) {
        this.actionRedo = actionRedo;
    }
}
