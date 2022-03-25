package com.marginallyclever.donatello.actions;

import com.marginallyclever.nodegraphcore.Node;
import com.marginallyclever.nodegraphcore.NodeGraph;
import com.marginallyclever.donatello.Donatello;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * Straightens the editor's {@link Node}s by rounding their top-left corner x and y values to the nearest
 * {@link StraightenGraphAction#SNAP_SIZE}.
 * @author Dan Royer
 * @since 2022-02-21
 */
public class StraightenGraphAction extends AbstractAction implements EditorAction {
    /**
     * The editor being affected.
     */
    private final Donatello editor;

    /**
     * The default snap rounding distance
     */
    private final int SNAP_SIZE = 10;

    /**
     * Constructor for subclasses to call.
     * @param name the name of this action visible on buttons and menu items.
     * @param editor the editor affected by this Action.
     */
    public StraightenGraphAction(String name, Donatello editor) {
        super(name);
        this.editor = editor;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        NodeGraph g = editor.getGraph();

        for(Node n : g.getNodes()) {
            Rectangle r = n.getRectangle();
            r.x -= r.x % SNAP_SIZE;
            r.y -= r.y % SNAP_SIZE;
        }
        editor.repaint();
    }

    @Override
    public void updateEnableStatus() {
        setEnabled(!editor.getSelectedNodes().isEmpty());
    }
}
