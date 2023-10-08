package com.marginallyclever.donatello.actions.undoable;

import com.marginallyclever.nodegraphcore.Graph;
import com.marginallyclever.donatello.Donatello;
import com.marginallyclever.donatello.actions.EditorAction;
import com.marginallyclever.donatello.edits.GraphPasteEdit;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * Duplicates the editor's copy buffer, inserts the contents into the editor's current {@link Graph}, and sets the
 * new content as the editor's selected items.
 * @author Dan Royer
 * @since 2022-02-21
 */
public class PasteGraphAction extends AbstractAction implements EditorAction {
    /**
     * The editor being affected.
     */
    private final Donatello editor;

    /**
     * Constructor for subclasses to call.
     * @param name the name of this action visible on buttons and menu items.
     * @param editor the editor affected by this Action.
     */
    public PasteGraphAction(String name, Donatello editor) {
        super(name);
        this.editor = editor;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        editor.addEdit(new GraphPasteEdit((String)this.getValue(Action.NAME),editor,editor.getCopiedGraph()));
    }

    @Override
    public void updateEnableStatus() {
        setEnabled(!editor.getCopiedGraph().isEmpty());
    }
}
