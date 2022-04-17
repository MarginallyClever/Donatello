package com.marginallyclever.donatello.contextsensitivetools;

import com.marginallyclever.donatello.Donatello;
import com.marginallyclever.donatello.GraphViewPanel;
import com.marginallyclever.donatello.UnicodeIcon;
import com.marginallyclever.donatello.edits.MoveNodesEdit;
import com.marginallyclever.nodegraphcore.Node;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

public class NodeMoveTool extends ContextSensitiveTool {
    private final Donatello editor;

    /**
     * true while dragging one or more annotatednodes around.
     */
    private boolean dragOn=false;

    /**
     * for tracking relative motion, useful for relative moves like dragging.
     */
    private final Point mousePreviousPosition = new Point();

    /**
     * for tracking total motion, useful for undoable edit.
     */
    private final Point mouseStartPosition = new Point();

    public NodeMoveTool(Donatello editor) {
        super();
        this.editor = editor;
    }

    @Override
    public void paint(Graphics g) {
        // TODO draw rectangle around selected annotatednodes?
        // TODO draw start of drag point to current point?
    }

    @Override
    public String getName() {
        return "Move";
    }

    @Override
    public Icon getSmallIcon() {
        return new UnicodeIcon("⬌\r⬍");
    }

    @Override
    public boolean isCorrectContext(Point p) {
        for( Node node : editor.getSelectedNodes() ) {
            if(node.getRectangle().contains(p)) {
                return true;
            }
        }
        return false;
    }

    public KeyStroke getAcceleratorKey() {
        return KeyStroke.getKeyStroke(KeyEvent.VK_M,0);
    }

    @Override
    public void attachMouseAdapter() {
        super.attachMouseAdapter();
        GraphViewPanel paintArea = editor.getPaintArea();
        paintArea.addMouseMotionListener(this);
        paintArea.addMouseListener(this);
    }

    @Override
    public void detachMouseAdapter() {
        super.detachMouseAdapter();
        GraphViewPanel paintArea = editor.getPaintArea();
        paintArea.removeMouseMotionListener(this);
        paintArea.removeMouseListener(this);
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if(dragOn) {
            Point p = editor.getPaintArea().transformMousePoint(e.getPoint());
            int dx = p.x - mousePreviousPosition.x;
            int dy = p.y - mousePreviousPosition.y;
            editor.moveSelectedNodes(dx, dy);
            editor.repaint();
        }
        mousePreviousPosition.setLocation(editor.getPaintArea().transformMousePoint(e.getPoint()));
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        mousePreviousPosition.setLocation(editor.getPaintArea().transformMousePoint(e.getPoint()));
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if(!dragOn) {
            dragOn=true;
            setActive(true);
            mousePreviousPosition.setLocation(editor.getPaintArea().transformMousePoint(e.getPoint()));
            mouseStartPosition.setLocation(mousePreviousPosition);
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if(dragOn) {
            dragOn=false;
            setActive(false);
            Point p = editor.getPaintArea().transformMousePoint(e.getPoint());
            int dx = p.x - mouseStartPosition.x;
            int dy = p.y - mouseStartPosition.y;
            editor.addEdit(new MoveNodesEdit(getName(),editor,dx,dy));
        }
    }
}
