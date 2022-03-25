package com.marginallyclever.donatello.actions;

import com.marginallyclever.nodegraphcore.Node;
import com.marginallyclever.donatello.Donatello;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * Forces all of the editor's selected {@link Node}s to {@link Node#update()}, regarless of {@link Node#isDirty()}
 * status.
 * @author Dan Royer
 * @since 2022-02-21
 */
public class ForciblyUpdateNodesAction extends AbstractAction implements EditorAction {
    /**
     * The editor being affected.
     */
    private final Donatello editor;

    /**
     * Constructor for subclasses to call.
     * @param name the name of this action visible on buttons and menu items.
     * @param editor the editor affected by this Action.
     */
    public ForciblyUpdateNodesAction(String name, Donatello editor) {
        super(name);
        this.editor = editor;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        for(Node n : editor.getSelectedNodes()) {
            try {
                n.update();
            } catch(Exception e1) {
                // TODO report?
            }
        }
    }

    @Override
    public void updateEnableStatus() {
        setEnabled(!editor.getSelectedNodes().isEmpty());
    }
}
