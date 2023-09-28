package com.marginallyclever.donatello.actions;

import com.marginallyclever.donatello.Donatello;
import com.marginallyclever.nodegraphcore.Node;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

/**
 * Moves the camera to try and see all selected nodes at once.
 * @author Dan Royer
 * @since 2022-04-14
 */
public class PanAndZoomToSelectedAction extends AbstractAction implements EditorAction {
    /**
     * The editor being affected.
     */
    private final Donatello editor;

    /**
     * Constructor for subclasses to call.
     * @param name the name of this action visible on buttons and menu items.
     * @param editor the editor affected by this Action.
     */
    public PanAndZoomToSelectedAction(String name, Donatello editor) {
        super(name);
        this.editor = editor;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        List<Node> list = editor.getSelectedNodes();
        if(list.isEmpty()) return;

        Rectangle rect = new Rectangle(list.get(0).getRectangle());
        for( Node n : list ) {
            Rectangle r = n.getRectangle();
            rect.add(r);
        }
        editor.getPaintArea().moveAndZoomToFit(rect);
        editor.repaint();
    }

    @Override
    public void updateEnableStatus() {
        setEnabled(!editor.getSelectedNodes().isEmpty());
    }
}
