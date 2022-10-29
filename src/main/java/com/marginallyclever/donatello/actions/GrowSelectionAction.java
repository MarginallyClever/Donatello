package com.marginallyclever.donatello.actions;

import com.marginallyclever.donatello.Donatello;
import com.marginallyclever.donatello.NodeHelper;
import com.marginallyclever.nodegraphcore.Node;
import com.marginallyclever.nodegraphcore.Connection;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * Select all the {@link com.marginallyclever.nodegraphcore.Node}s directly connected to the already selected nodes.
 */
public class GrowSelectionAction extends AbstractAction implements EditorAction {
    /**
     * The editor being affected.
     */
    private final Donatello editor;

    /**
     * Constructor for subclasses to call.
     * @param name the name of this action visible on buttons and menu items.
     * @param editor the editor affected by this Action.
     */
    public GrowSelectionAction(String name, Donatello editor) {
        super(name);
        this.editor = editor;
    }

    /**
     * Updates the model and repaints the panel.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        List<Node> list = new ArrayList<>(editor.getSelectedNodes());
        List<Node> adjacent = NodeHelper.getNeighbors(editor.getGraph(),list);
        list.addAll(adjacent);
        editor.setSelectedNodes(list);
        editor.repaint();
    }

    @Override
    public void updateEnableStatus() {
        setEnabled(editor.getSelectedNodes().size() > 0);
    }
}
