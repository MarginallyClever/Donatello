package com.marginallyclever.donatello.actions.undoable;

import com.marginallyclever.version2.Node;
import com.marginallyclever.donatello.Donatello;
import com.marginallyclever.donatello.actions.EditorAction;
import com.marginallyclever.donatello.edits.DeleteGraphEdit;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * Deletes the editor's selected {@link Node}s and sundry.
 * @author Dan Royer
 * @since 2022-02-21
 */
public class DeleteGraphAction extends AbstractAction implements EditorAction {
    /**
     * The editor being affected.
     */
    private final Donatello editor;

    /**
     * Constructor for subclasses to call.
     * @param name the name of this action visible on buttons and menu items.
     * @param editor the editor affected by this Action.
     */
    public DeleteGraphAction(String name, Donatello editor) {
        super(name);
        this.editor = editor;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        editor.addEdit(new DeleteGraphEdit((String)this.getValue(Action.NAME),editor,editor.getSelectedNodes()));
    }

    @Override
    public void updateEnableStatus() {
        setEnabled(!editor.getSelectedNodes().isEmpty());
    }
}
