package com.marginallyclever.donatello;

import com.marginallyclever.donatello.actions.RedoAction;
import com.marginallyclever.donatello.actions.UndoAction;

import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.undo.UndoManager;

public class UndoHandler implements UndoableEditListener {
    private final UndoManager undoManager;
    private final UndoAction actionUndo;
    private final RedoAction actionRedo;

    public UndoHandler(UndoManager undoManager, UndoAction actionUndo, RedoAction actionRedo) {
        super();
        this.undoManager = undoManager;
        this.actionUndo = actionUndo;
        this.actionRedo = actionRedo;
    }

    @Override
    public void undoableEditHappened(UndoableEditEvent e) {
        System.out.println("Undoable edit "+e.getEdit().getPresentationName());
        undoManager.addEdit(e.getEdit());
        actionUndo.update();
        actionRedo.update();
    }
}
