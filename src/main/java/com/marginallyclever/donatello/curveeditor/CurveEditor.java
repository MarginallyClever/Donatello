package com.marginallyclever.donatello.curveeditor;

import javax.swing.*;
import javax.swing.event.EventListenerList;
import javax.vecmath.Point2d;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * A JPanel that allows the user to edit a curve for levels in a greyscale image.
 * The curve is a 1D array of 256 values, each value is a level from 0 to 255.
 */
public class CurveEditor extends JPanel {
    private static class ControlPoint {
        Point2d position;
        Point2d tangentIn;
        Point2d tangentOut;

        public ControlPoint(Point2d position) {
            this.position = position;
            this.tangentIn = new Point2d();
            this.tangentOut = new Point2d();
        }
    }

    private final List<ControlPoint> controlPoints = new ArrayList<>();
    private final Point2d mousePosition = new Point2d();
    private ControlPoint movingControlPoint = null;
    private ControlPoint highlightedControlPoint = null;
    public static final int CLICK_DISTANCE = 5;
    private final EventListenerList listeners = new EventListenerList();

    public CurveEditor() {
        super();
        setMinimumSize(new Dimension(256,256));
        setPreferredSize(new Dimension(256,256));
        setMinimumSize(new Dimension(256,256));
        controlPoints.add(new ControlPoint(new Point2d(0,255)));
        controlPoints.add(new ControlPoint(new Point2d(255,0)));


        clickToAddControlPoint();
        selectControlPointUnderCursor();
        pressDeleteToRemoveHighlightedControlPoint();
        clickAndDragToMoveControlPoint();
    }

    private void clickAndDragToMoveControlPoint() {
        // press and release to start and stop dragging
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);
                ControlPoint p = getNearestControlPoint(e.getX(),e.getY());
                if(p.position.distance(mousePosition) < CLICK_DISTANCE) {
                    movingControlPoint = p;
                    p.position.set(mousePosition);
                    repaint();
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                super.mouseReleased(e);
                if(movingControlPoint!=null) {
                    movingControlPoint = null;
                }
            }
        });

        // drag to move
        addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                super.mouseDragged(e);
                mousePosition.set(e.getX(), e.getY());
                // control point cannot move off the edge
                if(mousePosition.x < 0) mousePosition.x = 0;
                if(mousePosition.x > 255) mousePosition.x = 255;
                if(mousePosition.y < 0) mousePosition.y = 0;
                if(mousePosition.y > 255) mousePosition.y = 255;

                if(movingControlPoint==null) return;
                // control point cannot move left of the previous control point
                // or right of the next control point
                Point2d before = new Point2d(movingControlPoint.position);

                var index = controlPoints.indexOf(movingControlPoint);
                if(index > 0) {
                    ControlPoint prev = controlPoints.get(index-1);
                    if(mousePosition.x < prev.position.x+1) {
                        mousePosition.x = prev.position.x+1;
                    }
                }
                if(index < controlPoints.size()-1) {
                    ControlPoint next = controlPoints.get(index+1);
                    if(mousePosition.x > next.position.x-1) {
                        mousePosition.x = next.position.x-1;
                    }
                }

                if(before.equals(mousePosition)) return;

                movingControlPoint.position.set(mousePosition);
                repaint();
                fireCurveChange();
            }
        });
    }

    private void clickToAddControlPoint() {
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mousePressed(e);
                ControlPoint p = getNearestControlPoint(e.getX(),e.getY());
                if(p.position.distance(mousePosition) >= CLICK_DISTANCE) {
                    var c = new ControlPoint(new Point2d(e.getX(), e.getY()));
                    insertControlPoint(c);
                    repaint();
                }
            }
        });
    }

    private void selectControlPointUnderCursor() {
        addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                super.mouseMoved(e);
                mousePosition.set(e.getX(), e.getY());
                ControlPoint p = getNearestControlPoint(e.getX(), e.getY());
                if (p.position.distance(mousePosition) < CLICK_DISTANCE) {
                    highlightedControlPoint = p;
                    setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                    repaint();
                } else {
                    highlightedControlPoint = null;
                    setCursor(Cursor.getDefaultCursor());
                    repaint();
                }
            }
        });
    }

    private void pressDeleteToRemoveHighlightedControlPoint() {
        setFocusable(true);
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                // on delete key
                if(e.getKeyCode() != KeyEvent.VK_DELETE) return;
                // when there are more than two points
                if(controlPoints.size() <= 2) return;
                // and a point is highlighted
                if(highlightedControlPoint == null) return;
                // remove the highlighted point
                controlPoints.remove(highlightedControlPoint);
                highlightedControlPoint = null;
                repaint();
            }
        });
    }

    private ControlPoint getNearestControlPoint(int x, int y) {
        Point2d p = new Point2d(x,y);
        double minDistance = Double.MAX_VALUE;
        ControlPoint nearest = null;
        for(var c : controlPoints) {
            double d = c.position.distanceSquared(p);
            if(d < minDistance) {
                minDistance = d;
                nearest = c;
            }
        }
        return nearest;
    }

    /**
     * Insert a control point into the list of control points in the correct order.
     * @param c The control point to insert.
     */
    private void insertControlPoint(ControlPoint c) {
        for(int i=0;i<controlPoints.size();++i) {
            if(controlPoints.get(i).position.x > c.position.x) {
                controlPoints.add(i,c);
                return;
            }
        }
        controlPoints.add(c);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D)g;
        g2.setRenderingHints(new RenderingHints(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON));
        g2.setRenderingHints(new RenderingHints(RenderingHints.KEY_STROKE_CONTROL,RenderingHints.VALUE_STROKE_PURE));
        g2.setRenderingHints(new RenderingHints(RenderingHints.KEY_RENDERING,RenderingHints.VALUE_RENDER_QUALITY));

        g2.setColor(Color.GRAY);
        g2.fillRect(0,0,256,256);
        g2.setColor(Color.BLACK);
        g2.drawRect(0,0,256,256);

        g2.setColor(Color.BLACK);

        drawFromEdgeToFirstControlPoint(g2);
        drawLineBetweenControlPoints(g2);
        drawFromLastControlPointToEdge(g2);
        drawAllControlPoints(g2);
        highlightSelectedControlPoint(g2);
    }

    private void highlightSelectedControlPoint(Graphics2D g2) {
        if(highlightedControlPoint != null) {
            g2.setColor(Color.GREEN);
            g2.fillOval((int)highlightedControlPoint.position.x-2,(int)highlightedControlPoint.position.y-2,4,4);
        }
    }

    private void drawAllControlPoints(Graphics2D g2) {
        for(var c : controlPoints) {
            g2.setColor(Color.RED);
            g2.fillOval((int)c.position.x-2,(int)c.position.y-2,4,4);
        }
    }

    private void drawFromLastControlPointToEdge(Graphics2D g2) {
        var last = controlPoints.getLast();
        if(last.position.x!=255) {
            g2.drawLine((int)last.position.x,(int)last.position.y,255,(int)last.position.y);
        }
    }

    private void drawLineBetweenControlPoints(Graphics2D g2) {
        if(controlPoints.size()==2) {
            g2.drawLine((int)controlPoints.get(0).position.x,(int)controlPoints.get(0).position.y,(int)controlPoints.get(1).position.x,(int)controlPoints.get(1).position.y);
        } else {
            for(int i=0;i<controlPoints.size()-1;++i) {
                var p0 = controlPoints.get(i);
                var p1 = controlPoints.get(i+1);
                g2.drawLine((int)p0.position.x,(int)p0.position.y,(int)p1.position.x,(int)p1.position.y);
            }
        }
    }

    private void drawFromEdgeToFirstControlPoint(Graphics2D g2) {
        var first = controlPoints.getFirst();
        if(first.position.x!=0) {
            g2.drawLine(0,(int)first.position.y,(int)first.position.x,(int)first.position.y);
        }
    }

    public int [] getCurve() {
        int [] curve = new int[256];
        for(int i=0;i<256;++i) {
            // on screen graphics have inverted y so flip the value.
            curve[i] = 255-getLevel(i);
        }
        return curve;
    }

    private int getLevel(int index) {
        if(index<0 || index>255) throw new IllegalArgumentException("index must be between 0 and 255, inclusive.");
        if(index < controlPoints.getFirst().position.x) {
            // left of the first control point
            return (int)controlPoints.getFirst().position.y;
        }
        if(index > controlPoints.getLast().position.x) {
            // right of the last control point
            return (int)controlPoints.getLast().position.y;
        }

        // between two points
        for(int j=0;j<controlPoints.size()-1;++j) {
            var p0 = controlPoints.get(j);
            var p1 = controlPoints.get(j+1);
            if(index >= p0.position.x && index <= p1.position.x) {
                double t = (index-p0.position.x)/(p1.position.x-p0.position.x);
                return (int)(p0.position.y*(1-t) + p1.position.y*t);
            }
        }

        return 0;
    }

    public void addCurveChangedListener(CurveChangedListener listener) {
        listeners.add(CurveChangedListener.class, listener);
    }

    public void removeCurveChangedListener(CurveChangedListener listener) {
        listeners.remove(CurveChangedListener.class, listener);
    }

    private void fireCurveChange() {
        for(var ear : listeners.getListeners(CurveChangedListener.class)) {
            ear.curveChanged(this);
        }
    }
}
