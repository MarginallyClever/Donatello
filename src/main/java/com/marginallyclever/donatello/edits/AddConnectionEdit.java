package com.marginallyclever.donatello.edits;

import com.marginallyclever.version2.ReceivingDock;
import com.marginallyclever.version2.Connection;
import com.marginallyclever.donatello.Donatello;
import com.marginallyclever.version2.ShippingDock;

import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;

/**
 * Adds a {@link Connection}, a link between an {@link ShippingDock} and an {@link ReceivingDock}.
 * Since the inbound node can only have one connection at a time, this edit also preserves any connection that has
 * to be removed.
 */
public class AddConnectionEdit extends SignificantUndoableEdit {
    private final String name;
    private final Donatello editor;
    private final Connection connection;
    private final Connection previousConnection;

    public AddConnectionEdit(String name, Donatello editor, ShippingDock from, ReceivingDock to) {
        super();
        this.name = name;
        this.editor = editor;
        this.connection = new Connection(from,to);
        this.previousConnection = to.getFrom();
        doIt();
    }

    @Override
    public String getPresentationName() {
        return name;
    }

    private void doIt() {
        editor.getGraph().remove(previousConnection);
        editor.getGraph().add(connection);
    }

    @Override
    public void undo() throws CannotUndoException {
        editor.getGraph().remove(connection);
        editor.getGraph().add(previousConnection);
        super.undo();
    }

    @Override
    public void redo() throws CannotRedoException {
        doIt();
        super.redo();
    }
}
