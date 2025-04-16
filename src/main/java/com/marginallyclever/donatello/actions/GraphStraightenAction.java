package com.marginallyclever.donatello.actions;

import com.marginallyclever.donatello.Donatello;
import com.marginallyclever.donatello.graphview.GraphViewSettings;
import com.marginallyclever.nodegraphcore.Node;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
    private static final Logger logger = LoggerFactory.getLogger(GraphStraightenAction.class);

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
        GraphViewSettings settings = editor.getPaintArea().getSettings();
        var gs = settings.getGridSize();
        for(Node n : g.getNodes()) {
            Rectangle r = n.getRectangle();
            var x = round(r.x,gs);
            var y = round(r.y,gs);
            if(r.x!=x) logger.debug("x round({},{})={}", r.x, gs, x);
            if(r.y!=y) logger.debug("y round({},{})={}", r.y, gs, y);
            r.x = x;
            r.y = y;
            n.updateBounds();
        }
        editor.repaint();
    }

    private int round(int v, int step) {
        return Math.round((float) v / step) * step;
    }

    @Override
    public void updateEnableStatus() {
        setEnabled(!editor.getSelectedNodes().isEmpty());
    }
}
