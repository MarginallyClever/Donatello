package com.marginallyclever.donatello.actions;

import com.marginallyclever.donatello.Donatello;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * Select all the {@link com.marginallyclever.nodegraphcore.Node}s in the {@link com.marginallyclever.nodegraphcore.Graph}.
 */
public class SelectAllAction extends AbstractAction {
    /**
     * The editor being affected.
     */
    private final Donatello editor;

    /**
     * Constructor for subclasses to call.
     * @param name the name of this action visible on buttons and menu items.
     * @param editor the editor affected by this Action.
     */
    public SelectAllAction(String name, Donatello editor) {
        super(name);
        this.editor = editor;
    }

    /**
     * Updates the model and repaints the panel.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        editor.setSelectedNodes(editor.getGraph().getNodes());
        editor.repaint();
    }
}
