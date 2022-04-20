package com.marginallyclever.donatello.actions;

import com.marginallyclever.version2.Node;
import com.marginallyclever.donatello.Donatello;
import com.marginallyclever.donatello.EditNodePanel;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.List;

/**
 * Launches the "edit node" dialog.
 * @author Dan Royer
 * @since 2022-02-21
 */
public class EditNodeAction extends AbstractAction implements EditorAction {
    /**
     * The editor being affected.
     */
    private final Donatello editor;

    /**
     * Constructor for subclasses to call.
     * @param name the name of this action visible on buttons and menu items.
     * @param editor the editor affected by this Action.
     */
    public EditNodeAction(String name, Donatello editor) {
        super(name);
        this.editor = editor;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        List<Node> nodes = editor.getSelectedNodes();
        if(nodes.isEmpty()) return;
        Node firstNode = nodes.get(0);
        EditNodePanel.runAsDialog(firstNode,(JFrame)SwingUtilities.getWindowAncestor(editor));
        editor.repaint(firstNode.getRectangle());
    }

    @Override
    public void updateEnableStatus() {
        setEnabled(!editor.getSelectedNodes().isEmpty());
    }
}
