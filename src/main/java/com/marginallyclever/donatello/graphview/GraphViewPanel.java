package com.marginallyclever.donatello.graphview;

import com.marginallyclever.donatello.bezier.Bezier;
import com.marginallyclever.donatello.bezier.Point2D;
import com.marginallyclever.nodegraphcore.*;
import com.marginallyclever.nodegraphcore.port.Port;
import com.marginallyclever.nodegraphcore.port.Input;
import com.marginallyclever.nodegraphcore.port.Output;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * {@link GraphViewPanel} visualizes the contents of a {@link Graph} with Java Swing.
 * It can call on {@link GraphViewListener}s to add additional flavor.
 * Override this to implement a unique look and feel.
 * @author Dan Royer
 * @since 2022-02-11
 */
public class GraphViewPanel extends JPanel {
    private final Logger logger = LoggerFactory.getLogger(GraphViewPanel.class);
    private static final String SETTINGS_FILENAME = "graphViewSettings.json";
    /**
     * Controls horizontal text alignment within a {@link Node} or {@link Port}.
     * See {@link #paintText(Graphics, String, Rectangle, int, int)} for more information.
     */
    public static final int ALIGN_LEFT=0;
    /**
     * Controls horizontal text alignment within a {@link Node} or {@link Port}.
     * See {@link #paintText(Graphics, String, Rectangle, int, int)} for more information.
     */
    public static final int ALIGN_RIGHT=1;
    /**
     * Controls horizontal or vertical text alignment within a {@link Node} or {@link Port}.
     * See {@link #paintText(Graphics, String, Rectangle, int, int)} for more information.
     */
    public static final int ALIGN_CENTER=2;

    /**
     * Controls vertical text alignment within a {@link Node} or {@link Port}.
     * See {@link #paintText(Graphics, String, Rectangle, int, int)} for more information.
     */
    public static final int ALIGN_TOP=0;
    /**
     * Controls vertical text alignment within a {@link Node} or {@link Port}.
     * See {@link #paintText(Graphics, String, Rectangle, int, int)} for more information.
     */
    public static final int ALIGN_BOTTOM=1;

    /**
     * the {@link Graph} to edit.
     */
    private final Graph model;

    private final Point camera = new Point();

    private final Point previousMouse = new Point();

    /**
     * Larger number means zooming further out
     */
    private double zoom = 1.0;

    private final GraphViewSettings settings = new GraphViewSettings();

    /**
     * Constructs one new instance of {@link GraphViewPanel}.
     * @param model the {@link Graph} model to paint.
     */
    public GraphViewPanel(Graph model) {
        super();
        this.model=model;
        this.setBackground(settings.getPanelColorBackground());
        this.setFocusable(true);

        loadSettings();
        addCameraControls();
    }

    public void loadSettings() {
        logger.debug("loading settings");
        File f = new File(SETTINGS_FILENAME);
        if(!f.exists()) return;

        try {
            String content = new String(Files.readAllBytes(f.toPath()));
            getSettings().fromJSON(new JSONObject(content));
        }
        catch (IOException e) {
            logger.error("Error loading settings.", e);
        }
    }

    public void saveSettings() {
        logger.debug("saving settings");
        try {
            Files.write(Paths.get(SETTINGS_FILENAME),getSettings().toJSON().toString().getBytes());
        } catch (IOException e) {
            logger.error("Error saving settings.", e);
        }
    }

    /**
     * Scroll wheel to zoom
     * click+drag scroll wheel to move camera.
     */
    private void addCameraControls() {
        final boolean[] middlePressed = {false};

        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                middlePressed[0] = SwingUtilities.isMiddleMouseButton(e);
                if(middlePressed[0]) previousMouse.setLocation(e.getX(),e.getY());
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                middlePressed[0] = !SwingUtilities.isMiddleMouseButton(e);
            }
        });

        this.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                Point now = new Point(e.getX(),e.getY());
                if(SwingUtilities.isMiddleMouseButton(e)) {
                    Point delta = new Point(now.x- previousMouse.x,now.y- previousMouse.y);
                    camera.x -= (int)(zoom * delta.x);
                    camera.y -= (int)(zoom * delta.y);
                    repaint();
                }
                previousMouse.setLocation(now);
                repaint();
                super.mouseDragged(e);
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                previousMouse.setLocation(e.getX(),e.getY());
                repaint();
                super.mouseMoved(e);
            }
        });

        this.addMouseWheelListener(e -> {
            // adjust the camera position based on the mouse position (zoom to cursor)
            Point before = transformScreenToWorldPoint(e.getPoint());
            setZoom(getZoom() - e.getWheelRotation() * 0.1);
            Point after = transformScreenToWorldPoint(e.getPoint());

            camera.x -= after.x - before.x;
            camera.y -= after.y - before.y;

            repaint();
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        updatePaintAreaBounds();

        Graphics2D g2 = (Graphics2D)g;
        setHints(g2);

        setBackground(settings.getPanelColorBackground());
        super.paintComponent(g2);

        g2.transform(getTransform());

        if(settings.getDrawBackgroundGrid()) paintBackgroundGrid(g2);
        paintNodesInBackground(g2);

        for(Node n : model.getNodes()) {
            paintNode(g2,n);
        }

        g2.setColor(settings.getConnectionColor());
        for(Connection c : model.getConnections()) paintConnection(g2,c);

        if(settings.getDrawCursor()) paintCursor(g2);
        if(settings.getDrawOrigin()) paintOrigin(g2);

        firePaintEvent(g2);
    }

    public static void setHints(Graphics2D g2) {
        //g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_RENDERING,RenderingHints.VALUE_RENDER_QUALITY);
        //g2.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL,RenderingHints.VALUE_STROKE_PURE);
        g2.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING,RenderingHints.VALUE_COLOR_RENDER_QUALITY);
    }

    private void paintBackgroundGrid(Graphics g) {
        g.setColor(settings.getPanelGridColor());

        var gs = settings.getGridSize();
        Rectangle r = getBounds();
        int width = (int)( r.getWidth()*zoom )+gs*2;
        int height = (int)( r.getHeight()*zoom )+gs*2;
        int size = Math.max(width,height);
        int startX = camera.x - width/2 - gs;
        int startY = camera.y - height/2 - gs;

        startX -= startX % gs;
        startY -= startY % gs;

        for(int i = 0; i <= size; i+=gs) {
            g.drawLine(startX+i,startY,startX+i,startY+height);
            g.drawLine(startX,startY+i,startX+width,startY+i);
        }
    }

    private void paintCursor(Graphics2D g2) {
        g2.setColor(Color.WHITE);
        int z = (int)(zoom*10);
        Point transformed = transformScreenToWorldPoint(previousMouse);
        g2.translate(transformed.x,transformed.y);
        g2.drawOval(-z,-z,z*2,z*2);
        g2.translate(-transformed.x,-transformed.y);
    }

    private void paintOrigin(Graphics2D g2) {
        g2.setColor(Color.RED);
        g2.drawLine(0,0,10*(int)Math.ceil(zoom),0);
        g2.setColor(Color.GREEN);
        g2.drawLine(0,0,0,10*(int)Math.ceil(zoom));
    }

    AffineTransform getTransform() {
        Rectangle r = getBounds();
        int w2 = (int)(r.getWidth()/2.0);
        int h2 = (int)(r.getHeight()/2.0);
        AffineTransform tx = new AffineTransform();
        double dx=camera.x-w2*zoom;
        double dy=camera.y-h2*zoom;
        tx.scale(1.0/zoom, 1.0/zoom);
        tx.translate(-dx,-dy);
        return tx;
    }

    public Point transformScreenToWorldPoint(Point point) {
        AffineTransform tf = getTransform();
        java.awt.geom.Point2D from = new java.awt.geom.Point2D.Double(point.x,point.y);
        java.awt.geom.Point2D to = new java.awt.geom.Point2D.Double();
        try {
            tf.inverseTransform(from,to);
        } catch (NoninvertibleTransformException e) {
            e.printStackTrace();
        }

        return new Point((int)to.getX(),(int)to.getY());
    }

    /**
     * Paint all {@link Node}s that implement the {@link PrintWithGraphics} interface.
     * @param g the {@link Graphics} context.
     */
    private void paintNodesInBackground(Graphics g) {
        for(Node n : model.getNodes()) {
            if(n instanceof PrintWithGraphics) {
                ((PrintWithGraphics) n).print(g);
            }
        }
    }

    /**
     * Update the bounds of every node in the model {@link Graph}.
     */
    public void updatePaintAreaBounds() {
        Rectangle r = this.getBounds();
        for(Node n : model.getNodes()) {
            n.updateBounds();
            Rectangle other = new Rectangle(n.getRectangle());
            //other.grow(100,100);
            r.add(other.getMinX(),other.getMinY());
            r.add(other.getMaxX(),other.getMaxY());
        }
        Dimension d = new Dimension(r.width,r.height);
        this.setMinimumSize(d);
        this.setMaximumSize(d);
        this.setPreferredSize(d);
    }

    /**
     * Paint one {@link Node}
     * @param g the {@link Graphics} context
     * @param n the {@link Node} to paint.
     */
    public void paintNode(Graphics g, Node n) {
        g.setColor(settings.getNodeColorBackground());
        paintNodeBackground(g,n);
        paintNodeTitleBar(g, n);
        paintAllDocks(g, n);
        g.setColor(settings.getNodeColorBorder());
        paintNodeBorder(g, n);
    }

    /**
     * Paint the background of one {@link Node}
     * @param g the {@link Graphics} context
     * @param n the {@link Node} to paint.
     */
    public void paintNodeBackground(Graphics g, Node n) {
        Rectangle r = n.getRectangle();
        var cr = settings.getCornerRadius();
        g.fillRoundRect(r.x, r.y, r.width, r.height, cr, cr);
    }

    /**
     * Paint the title bar of one {@link Node}.
     * @param g the {@link Graphics} context
     * @param n the {@link Node} to paint.
     */
    public void paintNodeTitleBar(Graphics g, Node n) {
        Rectangle r = n.getRectangle();
        var cr = settings.getCornerRadius();
        g.setClip(r.x,r.y,r.width,Node.TITLE_HEIGHT);
        g.setColor(settings.getNodeColorTitleBackground());
        g.fillRoundRect(r.x, r.y, r.width, cr*2, cr, cr);
        g.fillRect(r.x, r.y+cr, r.width+1, Node.TITLE_HEIGHT -cr);
        g.setClip(null);

        paintProgressBar(g,n,r);

        Rectangle box = getNodeInternalBounds(n.getRectangle());
        g.setColor(settings.getNodeColorTitleFont());
        box.height = Node.TITLE_HEIGHT;
        paintText(g,n.getLabel(),box,ALIGN_LEFT,ALIGN_CENTER);
        paintText(g,n.getName(),box,ALIGN_RIGHT,ALIGN_CENTER);
    }

    private void paintProgressBar(Graphics g, Node n,Rectangle r) {
        float complete = n.getComplete() * 0.01f;
        var cr = settings.getCornerRadius();
        g.setClip(r.x,r.y,(int)(r.width * complete),Node.TITLE_HEIGHT);
        g.setColor(settings.getNodeColorProgressBar());
        g.fillRoundRect(r.x, r.y, r.width, cr*2, cr, cr);
        g.setClip(null);
    }

    /**
     * Paint all the {@link Port}s in one {@link Node}.
     * @param g the {@link Graphics} context
     * @param n the {@link Node} to paint.
     */
    private void paintAllDocks(Graphics g, Node n) {
        for(int i=0;i<n.getNumVariables();++i) {
            paintOneDock(g,n.getVariable(i));
        }
    }

    /**
     * Paint one {@link Port}.
     * @param g the {@link Graphics} context
     * @param v the {@link Port} to paint.
     */
    public void paintOneDock(Graphics g, Port<?> v) {
        final int MAX_CHARS = 10;

        Rectangle box = v.getRectangle();
        Rectangle insideBox = getNodeInternalBounds(box);

        Object vObj = v.getValue();
        if(vObj != null) {
            if(vObj instanceof BufferedImage img) {
                paintDockBufferedImage(g,img,box);
            } else {
                String val;
                if (vObj instanceof String
                        || vObj instanceof Number
                        || vObj instanceof Boolean
                        || vObj instanceof Character
                        || vObj instanceof Enum) {
                    val = vObj.toString();
                } else {
                    val = v.getTypeName();
                }
                if (val.length() > MAX_CHARS) val = val.substring(0, MAX_CHARS) + "...";
                g.setColor(settings.getNodeColorFontClean());
                paintText(g, val, insideBox, ALIGN_RIGHT, ALIGN_TOP);
            }
        }


        // label
        g.setColor(settings.getNodeColorFontClean());
        paintText(g,v.getName(),insideBox,ALIGN_LEFT,ALIGN_TOP);

        // internal border
        g.setColor(settings.getNodeColorInternalBorder());
        g.drawLine((int)box.getMinX(),(int)box.getMinY(),(int)box.getMaxX(),(int)box.getMinY());

        // connection points
        g.setColor(settings.getConnectionPointColor());
        paintVariableConnectionPoints(g,v);
    }

    private void paintDockBufferedImage(Graphics g, BufferedImage img, Rectangle insideBox) {
        int w = img.getWidth();
        int h = img.getHeight();
        int maxW = (int)insideBox.getWidth();
        int maxH = (int)insideBox.getHeight();
        if (w > maxW) {
            h = h * maxW / w;
            w = maxW;
        }
        int x = (int)insideBox.getX();
        int y = (int)insideBox.getY();
        g.drawImage(img, x, y, w, h, null);

        //g.setColor(Color.RED);
        //g.drawRect(insideBox.x,insideBox.y,insideBox.width,insideBox.height);
    }

    /**
     * Returns the adjusted inner bounds of a {@link Node}.
     * Nodes have a left and right margin useful for printing labels and values without overlapping the {@link Connection} points.
     * these edges form an inner bound.  Given a {@link Port#getRectangle()}, this
     * @param r the outer bounsd of the node.
     * @return the adjusted inner bounds of a {@link Node}.
     */
    public Rectangle getNodeInternalBounds(Rectangle r) {
        Rectangle r2 = new Rectangle(r);
        int padding = (int)Connection.DEFAULT_RADIUS+4;
        r2.x += padding;
        r2.width -= padding*2;
        return r2;
    }

    /**
     * Paint the outside border of one {@link Node}.
     * @param g the {@link Graphics} context
     * @param n the {@link Node} to paint.
     */
    public void paintNodeBorder(Graphics g,Node n) {
        Rectangle r = n.getRectangle();
        var cr = settings.getCornerRadius();
        g.drawRoundRect(r.x, r.y, r.width, r.height,cr,cr);
    }

    /**
     * Paint the female end of connection points of one {@link Port}.
     * @param g the {@link Graphics} context
     * @param v the {@link Port} to paint.
     */
    public void paintVariableConnectionPoints(Graphics g, Port<?> v) {
        if(v instanceof Input) {
            Point p = v.getInPosition();
            int radius = (int)Connection.DEFAULT_RADIUS+2;
            g.drawOval(p.x-radius,p.y-radius,radius*2,radius*2);
        }
        if(v instanceof Output) {
            Point p = v.getOutPosition();
            int radius = (int)Connection.DEFAULT_RADIUS+2;
            g.drawOval(p.x-radius,p.y-radius,radius*2,radius*2);
        }
    }

    /**
     * Use the graphics context to paint text within a box with the provided alignment.
     * @param g the graphics context
     * @param str the text to paint
     * @param box the bounding limits
     * @param alignH the desired horizontal alignment.  Can be any one of {@link GraphViewPanel#ALIGN_LEFT}, {@link GraphViewPanel#ALIGN_RIGHT}, or {@link GraphViewPanel#ALIGN_CENTER}
     * @param alignV the desired vertical alignment.  Can be any one of {@link GraphViewPanel#ALIGN_TOP}, {@link GraphViewPanel#ALIGN_BOTTOM}, or {@link GraphViewPanel#ALIGN_CENTER}
     */
    public void paintText(Graphics g,String str,Rectangle box,int alignH,int alignV) {
        if(str==null || str.isEmpty()) return;

        FontRenderContext frc = new FontRenderContext(null, false, false);
        TextLayout layout = new TextLayout(str,g.getFont(),frc);
        FontMetrics metrics = g.getFontMetrics();
        int h = metrics.getHeight();
        int w = metrics.stringWidth(str);
        int md = metrics.getMaxDescent();

        int x,y;
        x = switch (alignH) {
            default -> (int) box.getMinX();
            case ALIGN_RIGHT -> (int) (box.getMaxX() - w);
            case ALIGN_CENTER -> (int) (box.getMinX() + (box.getWidth() - w) / 2);
        };
        y = switch (alignV) {
            default -> (int) (box.getMinY() + h-md);
            case ALIGN_BOTTOM -> (int) (box.getMaxY());
            case ALIGN_CENTER -> (int) (box.getMinY()-md + (box.getHeight() + h) / 2);
        };
        layout.draw((Graphics2D)g,x,y);
    }

    /**
     * Paint the male end of connection points at this {@link Port}.
     * @param g the {@link Graphics} context
     * @param c the {@link Port} to paint.
     */
    public void paintConnection(Graphics g, Connection c) {
        Point to = c.getOutPosition();
        Point from = c.getInPosition();
        paintBezierBetweenTwoPoints(g,from,to);

        if(c.isToValid()) paintConnectionAtPoint(g,to);
        if(c.isFromValid()) paintConnectionAtPoint(g,from);
    }

    /**
     * Paint the male end of one connection point.
     * @param g the {@link Graphics} context
     * @param p the center of male end to paint.
     */
    public void paintConnectionAtPoint(Graphics g,Point p) {
        int radius = (int) Connection.DEFAULT_RADIUS;
        g.fillOval( p.x - radius, p.y - radius, radius * 2, radius * 2);
    }

    /**
     * Paint a cubic bezier using {@link Graphics} from p0 to p3.
     * @param g the {@link Graphics} painting tool.
     * @param p0 the first point of the cubic bezier spline.
     * @param p3 the last point of the cubic bezier spline.
     */
    public void paintBezierBetweenTwoPoints(Graphics g,Point p0, Point p3) {
        Point p1 = new Point(p0);
        Point p2 = new Point(p3);

        int d=Math.abs(p3.x-p1.x)/2;
        p1.x+=d;
        p2.x-=d;

        Bezier b = new Bezier(
                p0.x,p0.y,
                p1.x,p1.y,
                p2.x,p2.y,
                p3.x,p3.y);
        drawBezier(g,b);
    }

    private void drawBezier(Graphics g, Bezier b) {
        List<Point2D> points = b.generateCurvePoints(0.2);
        int len=points.size();
        int [] x = new int[len];
        int [] y = new int[len];
        for(int i=0;i<len;++i) {
            Point2D p = points.get(i);
            x[i]=(int)p.x;
            y[i]=(int)p.y;
        }
        g.drawPolyline(x,y,len);
    }

    /**
     * listener pattern for painting via {@link GraphViewListener#paint(Graphics, GraphViewPanel)}.
     */
    private final List<GraphViewListener> listeners = new ArrayList<>();

    /**
     * {@link GraphViewListener}s register here.
     * @param p the {@link GraphViewListener} to register.
     */
    public void addViewListener(GraphViewListener p) {
        listeners.add(p);
    }

    /**
     * {@link GraphViewListener}s unregister here.
     * @param p the {@link GraphViewListener} to unregister.
     */
    public void removeViewListener(GraphViewListener p) {
        listeners.remove(p);
    }

    private void firePaintEvent(Graphics g) {
        for( GraphViewListener p : listeners ) {
            p.paint(g, this);
        }
    }

    /**
     * Sets the Graphics context line width.
     * @param g the {@link Graphics} context
     * @param r thew new line width.
     */
    public void setLineWidth(Graphics g,float r) {
        Graphics2D g2 = (Graphics2D)g;
        g2.setStroke(new BasicStroke(r));
    }

    /**
     * Returns the current scale
     * @return the current scale
     */
    public double getZoom() {
        return zoom;
    }

    /**
     * Sets the scale.  Must be greater than or equal to 1.
     * @param zoom  Must be greater than or equal to 1.
     */
    public void setZoom(double zoom) {
        this.zoom = Math.max(1, zoom);
    }

    public GraphViewSettings getSettings() {
        return settings;
    }

    /**
     * pan and zoom the camera to fit the rectangle in the view.
     * @param rectangle the rectangle to fit.
     */
    public void moveAndZoomToFit(Rectangle rectangle) {
        camera.x = (int)rectangle.getCenterX();
        camera.y = (int)rectangle.getCenterY();
        Rectangle bounds= getBounds();
        double sw = rectangle.getWidth() / bounds.getWidth();
        double sh = rectangle.getHeight() / bounds.getHeight();
        double s = Math.max(sw, sh);
        setZoom(s);
    }

    /**
     * pan and zoom the camera such that it can see all the selected nodes.
     * @param selectedNodes the nodes to fit.  if there are none, allow all.
     */
    public void moveAndZoomToFit(List<Node> selectedNodes) {
        Rectangle r = new Rectangle();
        if(selectedNodes.isEmpty()) {
            // consider all
            for(Node n : model.getNodes()) {
                r.add(n.getRectangle());
            }
        } else {
            // add selected
            for(Node n : model.getNodes()) {
                if (selectedNodes.contains(n)) {
                    r.add(n.getRectangle());
                }
            }
        }
        moveAndZoomToFit(r);
    }
}
