package com.marginallyclever.donatello.graphview;

import com.marginallyclever.donatello.IconHelper;
import com.marginallyclever.donatello.bezier.Bezier;
import com.marginallyclever.nodegraphcore.Connection;
import com.marginallyclever.nodegraphcore.Graph;
import com.marginallyclever.nodegraphcore.Node;
import com.marginallyclever.nodegraphcore.PrintWithGraphics;
import com.marginallyclever.nodegraphcore.port.Input;
import com.marginallyclever.nodegraphcore.port.Output;
import com.marginallyclever.nodegraphcore.port.Port;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.vecmath.Point2d;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static com.marginallyclever.donatello.IconHelper.scaleIcon;

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
     * The maximum number of characters to display in a {@link Port}.
     */
    public static final int MAX_CHARS_PER_RORT = 10;

    private static final double BEZIER_TOLERANCE = 0.2;

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
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D)g.create();
        setHints(g2);

        setBackground(settings.getPanelColorBackground());
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

        g2.dispose();
    }

    /**
     * Set rendering hints for the {@link Graphics2D} context.
     * @param g2 the {@link Graphics2D} context.
     */
    public static void setHints(Graphics2D g2) {
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_DITHERING,RenderingHints.VALUE_DITHER_DISABLE);
        g2.setRenderingHint(RenderingHints.KEY_RENDERING,RenderingHints.VALUE_RENDER_QUALITY);
        g2.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL,RenderingHints.VALUE_STROKE_NORMALIZE);
        g2.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING,RenderingHints.VALUE_COLOR_RENDER_QUALITY);
        g2.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS,RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION,RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
    }

    /**
     * Paint the background grid across the entire JPanel
     * @param g the {@link Graphics} context.
     */
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

    private AffineTransform getTransform() {
        Rectangle r = getBounds();
        var w2 = r.getWidth()/2.0;
        var h2 = r.getHeight()/2.0;
        AffineTransform tx = new AffineTransform();
        double dx = camera.x - w2 * zoom;
        double dy = camera.y - h2 * zoom;
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
        List<PrintWithGraphics> found = new ArrayList<>();
        for(Node n : model.getNodes()) {
            if(n instanceof PrintWithGraphics pwg) {
                found.add(pwg);
            }
        }

        found.sort(Comparator.comparing(PrintWithGraphics::getLayer));
        for( var pwg : found) pwg.print(g);
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
        paintAllPorts(g, n);
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

        g.setColor(settings.getNodeColorTitleBackground());
        g.fillRoundRect(r.x, r.y, r.width, cr*2, cr, cr);
        g.fillRect(r.x, r.y+cr, r.width, Node.TITLE_HEIGHT -cr);

        paintProgressBar(g,n,r);

        Rectangle box = getNodeInternalBounds(n.getRectangle());
        g.setColor(settings.getNodeColorTitleFont());
        box.height = Node.TITLE_HEIGHT;
        paintText(g,n.getLabel(),box,ALIGN_LEFT,ALIGN_CENTER);
        paintText(g,n.getName(),box,ALIGN_RIGHT,ALIGN_CENTER);
    }

    private void paintProgressBar(Graphics g, Node n,Rectangle r) {
        int width = (int)(r.width * n.getComplete() / 100.0);
        var cr = settings.getCornerRadius();
        g.setColor(settings.getNodeColorProgressBar());
        g.fillRoundRect(r.x, r.y, width, cr*2, cr, cr);
    }

    /**
     * Paint all the {@link Port}s in one {@link Node}.
     * @param g the {@link Graphics} context
     * @param n the {@link Node} to paint.
     */
    private void paintAllPorts(Graphics g, Node n) {
        for(int i = 0; i<n.getNumPorts(); ++i) {
            paintOnePort(g,n.getPort(i));
        }
    }

    /**
     * Paint one {@link Port}.
     * @param g the {@link Graphics} context
     * @param v the {@link Port} to paint.
     */
    public void paintOnePort(Graphics g, Port<?> v) {
        Rectangle box = v.getRectangle();
        Rectangle insideBox = getNodeInternalBounds(box);

        Object vObj = v.getValue();
        g.setColor(settings.getNodeColorFontClean());

        if(v instanceof GraphViewProvider gvp) {
            gvp.paint(g,box);
        } else {
            String val;
            if (vObj instanceof String
                    || vObj instanceof Number
                    || vObj instanceof Boolean
                    || vObj instanceof Character
                    || vObj instanceof Enum) {
                val = vObj.toString();
            } else if(vObj != null) {
                val = v.getTypeName();
            } else val = "null";
            if (val.length() > MAX_CHARS_PER_RORT) val = val.substring(0, MAX_CHARS_PER_RORT) + "...";
            paintText(g, val, insideBox, ALIGN_RIGHT, ALIGN_TOP);
        }

        // label
        g.setColor(settings.getNodeColorFontClean());
        paintText(g,v.getName(),insideBox,ALIGN_LEFT,ALIGN_TOP);

        // internal border
        g.setColor(settings.getNodeColorInternalBorder());
        g.drawLine(box.x,box.y,box.x+box.width,box.y);

        // connection points
        g.setColor(settings.getConnectionPointColor());
        paintPortConnectionPoints(g,v);
    }

    /**
     * Returns the adjusted inner bounds of a {@link Node}.
     * Nodes have a left and right margin useful for printing labels and values without overlapping the {@link Connection} points.
     * these edges form an inner bound.  Given a {@link Port#getRectangle()}, this
     * @param r the outer bounsd of the node.
     * @return the adjusted inner bounds of a {@link Node}.
     */
    public static Rectangle getNodeInternalBounds(Rectangle r) {
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
    public void paintPortConnectionPoints(Graphics g, Port<?> v) {
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
    public static void paintText(Graphics g,String str,Rectangle box,int alignH,int alignV) {
        if(str==null || str.isEmpty()) return;

        FontRenderContext frc = new FontRenderContext(null, false, false);
        TextLayout layout = new TextLayout(str,g.getFont(),frc);
        FontMetrics metrics = g.getFontMetrics();
        int h = metrics.getHeight();
        int w = metrics.stringWidth(str);
        int md = metrics.getMaxDescent();

        int x,y;
        x = switch (alignH) {
            case ALIGN_RIGHT -> (int) (box.getMaxX() - w);
            case ALIGN_CENTER -> (int) (box.getMinX() + (box.getWidth() - w) / 2);
            default -> (int) box.getMinX();
        };
        y = switch (alignV) {
            case ALIGN_BOTTOM -> (int) (box.getMaxY());
            case ALIGN_CENTER -> (int) (box.getMinY()-md + (box.getHeight() + h) / 2);
            default -> (int) (box.getMinY() + h-md);
        };
        layout.draw((Graphics2D)g,x,y);
    }

    public void paintIcon(Graphics g, Node n, Rectangle box, int alignH, int alignV) {
        Icon icon = scaleIcon(n.getIcon(), IconHelper.ICON_SIZE, IconHelper.ICON_SIZE);
        //Icon icon = n.getIcon();
        if(icon == null) return;

        int x,y;
        if(alignH == ALIGN_LEFT) {
            x = box.x;
        } else if(alignH == ALIGN_RIGHT) {
            x = box.x + box.width - icon.getIconWidth();
        } else {
            x = box.x + (box.width - icon.getIconWidth()) / 2;
        }

        if(alignV == ALIGN_TOP) {
            y = box.y;
        } else if(alignV == ALIGN_BOTTOM) {
            y = box.y + box.height - icon.getIconHeight();
        } else {
            y = box.y + (box.height - icon.getIconHeight()) / 2;
        }

        icon.paintIcon(this, g, x, y);
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

        if(c.isToASaneInput()) paintConnectionAtPoint(g,to);
        if(c.isFromASaneOutput()) paintConnectionAtPoint(g,from);
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
     * Paint a cubic bezier using {@link Graphics} from p0 to p3.  The X difference between p0 and p3 is used to
     * calculate p1 and p2.
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
        List<Point2d> points = b.generateCurvePoints(BEZIER_TOLERANCE);
        int len = points.size();
        int [] x = new int[len];
        int [] y = new int[len];
        for(int i=0;i<len;++i) {
            Point2d p = points.get(i);
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

    /**
     * @return a copy of the current camera position.  it is in absolute world coordinates.
     */
    public Point getCameraPosition() {
        return new Point(camera);
    }

    public Point getPreviousMousePosition() {
        return previousMouse;
    }
}
