package com.marginallyclever.donatello.actions;

import com.marginallyclever.donatello.graphview.GraphViewPanel;
import com.marginallyclever.donatello.graphview.GraphViewSettings;
import com.marginallyclever.nodegraphcore.Node;
import com.marginallyclever.nodegraphcore.Graph;
import com.marginallyclever.donatello.Donatello;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * Straightens the editor's {@link Node}s by rounding their top-left corner x and y values to the nearest
 * {@link GraphViewSettings#getGridSize()}.
 * @author Dan Royer
 * @since 2022-02-21
 */
public class GraphStraightenAction extends AbstractAction implements EditorAction {
    /**
     * The editor being affected.
     */
    private final Donatello editor;

    /**
     * Constructor for subclasses to call.
     * @param name the name of this action visible on buttons and menu items.
     * @param editor the editor affected by this Action.
     */
    public GraphStraightenAction(String name, Donatello editor) {
        super(name);
        this.editor = editor;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        var g = editor.getGraph();
        GraphViewSettings settings = editor.getGraphView().getSettings();
        var gs = settings.getGridSize();
        for(Node n : g.getNodes()) {
            Rectangle r = n.getRectangle();
            r.x -= r.x % gs;
            r.y -= r.y % gs;
        }
        editor.repaint();
    }

    @Override
    public void updateEnableStatus() {
        setEnabled(!editor.getSelectedNodes().isEmpty());
    }
}
