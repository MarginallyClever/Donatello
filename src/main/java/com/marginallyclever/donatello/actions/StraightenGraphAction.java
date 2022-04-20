package com.marginallyclever.donatello.actions;

import com.marginallyclever.donatello.GraphViewPanel;
import com.marginallyclever.version2.Node;
import com.marginallyclever.version2.Graph;
import com.marginallyclever.donatello.Donatello;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * Straightens the editor's {@link Node}s by rounding their top-left corner x and y values to the nearest
 * {@link GraphViewPanel#GRID_SIZE}.
 * @author Dan Royer
 * @since 2022-02-21
 */
public class StraightenGraphAction extends AbstractAction implements EditorAction {
    /**
     * The editor being affected.
     */
    private final Donatello editor;

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
        Graph g = editor.getGraph();

        for(Node n : g.getNodes()) {
            Rectangle r = n.getRectangle();
            r.x -= r.x % GraphViewPanel.GRID_SIZE;
            r.y -= r.y % GraphViewPanel.GRID_SIZE;
        }
        editor.repaint();
    }

    @Override
    public void updateEnableStatus() {
        setEnabled(!editor.getSelectedNodes().isEmpty());
    }
}
