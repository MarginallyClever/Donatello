package com.marginallyclever.donatello.actions;

import com.marginallyclever.version2.Node;
import com.marginallyclever.donatello.Donatello;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * Invert the selection.
 */
public class InvertSelectionAction extends AbstractAction {
    /**
     * The editor being affected.
     */
    private final Donatello editor;

    /**
     * Constructor for subclasses to call.
     * @param name the name of this action visible on buttons and menu items.
     * @param editor the editor affected by this Action.
     */
    public InvertSelectionAction(String name, Donatello editor) {
        super(name);
        this.editor = editor;
    }

    /**
     * Updates the model and repaints the panel.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        List<Node> difference = new ArrayList<>(editor.getGraph().getNodes());
        difference.removeAll(editor.getSelectedNodes());
        editor.setSelectedNodes(difference);
        editor.repaint();
    }
}
