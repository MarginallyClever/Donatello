package com.marginallyclever.donatello.actions;

import com.marginallyclever.donatello.Donatello;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * Clears the editor's current {@link com.marginallyclever.nodegraphcore.Graph}.
 * @author Dan Royer
 * @since 2022-02-21
 */
public class GraphNewAction extends AbstractAction {
    /**
     * The editor being affected.
     */
    private final Donatello editor;

    /**
     * Constructor for subclasses to call.
     * @param name the name of this action visible on buttons and menu items.
     * @param editor the editor affected by this Action.
     */
    public GraphNewAction(String name, Donatello editor) {
        super(name);
        this.editor = editor;
        this.putValue(SHORT_DESCRIPTION, "Clear the current graph.");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        editor.clear();
    }
}
