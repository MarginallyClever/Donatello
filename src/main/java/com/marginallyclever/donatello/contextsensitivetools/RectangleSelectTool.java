package com.marginallyclever.donatello.contextsensitivetools;

import com.marginallyclever.donatello.*;
import com.marginallyclever.donatello.graphview.GraphViewPanel;
import com.marginallyclever.nodegraphcore.Node;

import javax.swing.*;
import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static javax.swing.JComponent.WHEN_IN_FOCUSED_WINDOW;

public class RectangleSelectTool extends ContextSensitiveTool {
    private final Donatello editor;

    private static final int STROKE_WIDTH=2;

    /**
     * true while drawing a box to select nodes.
     */
    private boolean selectionOn=false;

    /**
     * first corner of the bounding area when a user clicks and drags to form a box.
     */
    private final Point selectionAreaStart = new Point();

    /**
     * for tracking relative motion, useful for relative moves like dragging.
     */
    private final Point mousePreviousPosition = new Point();

    /**
     * Remembers the state of the keys for selection actions
     */
    private final KeyStateMemory keyStateMemory = new KeyStateMemory();

    public RectangleSelectTool(Donatello editor) {
        super();
        this.editor=editor;
    }

    @Override
    public String getName() {
        return "Select";
    }

    @Override
    public Icon getSmallIcon() {
        return new ImageIcon(Objects.requireNonNull(getClass().getResource("/com/marginallyclever/donatello/icons8-rectangle-16.png")));
    }

    @Override
    public boolean isCorrectContext(Point p) {
        return true;
    }

    public KeyStroke getAcceleratorKey() {
        return KeyStroke.getKeyStroke(KeyEvent.VK_S,0);
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
    public void detachKeyboardAdapter() {
        super.detachKeyboardAdapter();
        GraphViewPanel paintArea = editor.getPaintArea();
        paintArea.getActionMap().put("press",null);
        paintArea.getActionMap().put("release",null);
    }

    @Override
    public void attachKeyboardAdapter() {
        super.attachKeyboardAdapter();
        GraphViewPanel paintArea = editor.getPaintArea();

        paintArea.getInputMap(WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_SHIFT, InputEvent.SHIFT_DOWN_MASK,false),"press");
        paintArea.getInputMap(WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_SHIFT, 0,true),"release");

        paintArea.getActionMap().put("press",new RememberKeyStateAction(keyStateMemory, KeyEvent.VK_SHIFT,false));
        paintArea.getActionMap().put("release",new RememberKeyStateAction(keyStateMemory, KeyEvent.VK_SHIFT,true));
    }

    @Override
    public void paint(Graphics g) {
        if(selectionOn) paintSelectionArea(g);
    }

    /**
     * Paints the rectangle selection area.
     * @param g the {@link Graphics} context
     */
    private void paintSelectionArea(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();
        Stroke dashed = new BasicStroke(STROKE_WIDTH, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER,
                2, new float[]{3}, 0);
        g2d.setStroke(dashed);

        g2d.setColor(keyStateMemory.isShiftKeyDown() ? Color.YELLOW : Color.GREEN);
        Rectangle r = getSelectionArea(mousePreviousPosition);
        g2d.drawRect((int)r.getMinX(),(int)r.getMinY(),(int)r.getWidth()-STROKE_WIDTH,(int)r.getHeight()-STROKE_WIDTH);
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        //Rectangle r = getRepaintArea(e.getPoint());
        mousePreviousPosition.setLocation(editor.getPaintArea().transformScreenToWorldPoint(e.getPoint()));
        if(selectionOn) editor.repaint();
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        //Rectangle r = getRepaintArea(e.getPoint());
        mousePreviousPosition.setLocation(editor.getPaintArea().transformScreenToWorldPoint(e.getPoint()));
        if(selectionOn) editor.repaint();
    }

    private Rectangle getRepaintArea(Point p) {
        Rectangle r = getSelectionArea(p);
        r.add(new Rectangle(mousePreviousPosition,new Dimension(1,1)));
        return r;
    }

    public void mousePressed(MouseEvent e) {
        if(SwingUtilities.isLeftMouseButton(e)) {
            // nothing under point, start new selection.
            selectionOn=true;
            setActive(true);
            beginSelectionArea(editor.getPaintArea().transformScreenToWorldPoint(e.getPoint()));
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if(selectionOn) {
            selectionOn=false;
            setActive(false);
            endSelectionArea(editor.getPaintArea().transformScreenToWorldPoint(e.getPoint()));
        }
    }

    /**
     * What to do when the rectangle selection begins.
     * @param point the first corner of the rectangle selection area.
     */
    private void beginSelectionArea(Point point) {
        selectionAreaStart.x=point.x;
        selectionAreaStart.y=point.y;
    }

    /**
     * What to do when the rectangle selection ends.
     * @param point the second corner of the rectangle selection area.
     */
    private void endSelectionArea(Point point) {
        Rectangle selectionArea = getSelectionArea(point);
        java.util.List<Node> nodesInSelectionArea = editor.getGraph().getNodesInRectangle(selectionArea);
        if(!keyStateMemory.isShiftKeyDown()) {
            editor.setSelectedNodes(nodesInSelectionArea);
        } else {
            List<Node> already = new ArrayList<>(editor.getSelectedNodes());
            List<Node> intersection = new ArrayList<>(nodesInSelectionArea);
            // get the intersection of previous and newly selected nodes.
            intersection.retainAll(already);
            // remove the intersecting bit
            nodesInSelectionArea.removeAll(intersection);
            already.removeAll(intersection);
            // keep whatever is left
            already.addAll(nodesInSelectionArea);

            editor.setSelectedNodes(already);
        }
        editor.repaint();
    }

    /**
     * Returns the rectangle formed by the first selection point and this new point.
     * @param point the second point of the selection area.
     * @return the rectangle formed by the first selection point and this new point.
     */
    private Rectangle getSelectionArea(Point point) {
        Rectangle r = new Rectangle(point);
        r.add(new Rectangle(selectionAreaStart, new Dimension(1, 1)));
        return r;
    }
}
