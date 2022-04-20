package com.marginallyclever.version2;

import com.marginallyclever.version2.nodes.InPort;
import com.marginallyclever.version2.nodes.OutPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.io.Serial;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.LinkedList;
import java.util.List;

/**
 * <p>The {@link Node} interface describes a node in a graph.  Each Node is equivalent to a method in <a
 * href='https://en.wikipedia.org/wiki/Functional_programming'>functional program</a> and a first-class citizen.</p>
 * <p>Nodes do not contain any state information.  They are simply a way to describe a computation.</p>
 */
public abstract class Node extends AbstractNamedEntity {
    @Serial
    private static final long serialVersionUID = 6772685119452110491L;
    /**
     * The default height of the title bar.
     */
    public static final int TITLE_HEIGHT = 25;

    private static final Logger logger = LoggerFactory.getLogger(Node.class);

    /**
     * All inputs to this node.
     */
    private final List<ReceivingDock> inputs = new LinkedList<>();

    /**
     * All outputs from this node.
     */
    private final List<ShippingDock> outputs = new LinkedList<>();

    private final Rectangle bounds = new Rectangle();

    public Node() {
        super();
        parseInPorts();
        parseOutPorts();
    }

    /**
     * Read all annotations for {@link InPort} and create a {@link ReceivingDock} for each.
     */
    private void parseInPorts() {
        InPort [] inPorts = this.getClass().getAnnotationsByType(InPort.class);
        for( InPort p : inPorts ) {
            logger.debug("{}.{} input type {}",getName(),p.name(),p.type().getName());
            addDock(new ReceivingDock(p.name(), p.type(), this));
        }
    }

    /**
     * Read all annotations for {@link OutPort} and create a {@link ShippingDock} for each.
     */
    private void parseOutPorts() {
        OutPort [] outPorts = this.getClass().getAnnotationsByType(OutPort.class);
        for( OutPort p : outPorts ) {
            logger.debug("{}.{} output type {}",getName(),p.name(),p.type().getName());
            addDock(new ShippingDock(p.name(), p.type(), this));
        }
    }

    /**
     * <p>This is where the {@link Node} should</p>
     * <ul>
     *     <li>check for valid inputs</li>
     *     <li>perform calculations</li>
     *     <li>update the state of the outputs</li>
     * </ul>
     * <p>Note that all inputs should be treated in a read-only manner.</p>
     */
    public abstract void update();

    public void addDock(Dock dock) {
        assertUniqueDockName(dock.getName());

        if(dock instanceof ShippingDock) {
            outputs.add((ShippingDock)dock);
        } else if(dock instanceof ReceivingDock) {
            inputs.add((ReceivingDock)dock);
        } else {
            throw new InvalidParameterException("dock must of type ShippingDock or ReceivingDock.");
        }
    }

    private void assertUniqueDockName(String name) throws InvalidParameterException {
        assertUniqueInputName(name);
        assertUniqueOutputName(name);
    }

    private void assertUniqueInputName(String name) {
        List<ReceivingDock> list = getInputs();
        for( ReceivingDock d : list ) {
            if(name.equals(d.getName())) throw new InvalidParameterException("Dock name already exists: " + name);
        }
    }

    private void assertUniqueOutputName(String name) {
        List<ShippingDock> list = getOutputs();
        for( ShippingDock d : list ) {
            if(name.equals(d.getName())) throw new InvalidParameterException("Dock name already exists: " + name);
        }
    }

    public void removeDock(Dock dock) {
        outputs.remove(dock);
        inputs.remove(dock);
    }

    /**
     * @param name the name of the input to get.
     * @return the input found or null.
     */
    public ReceivingDock getInput(String name) {
        return (ReceivingDock)getDock(name, getInputs());
    }

    /**
     * @param name the name of the output to get.
     * @return the input found or null.
     */
    public ShippingDock getOutput(String name) {
        return (ShippingDock)getDock(name, getOutputs());
    }

    private Dock getDock(String name, List<? extends Dock> list) {
        for( Dock d : list ) {
            if(d.getName().equals(name)) return d;
        }
        return null;
    }

    @Override
    public String toString() {
        return "Node{" +
                "name='" + getName() + '\'' +
                ", uniqueID='" + getUniqueID() + '\'' +
                ", inputs=[" + getInputsAsString() + ']' +
                ", outputs=[" + getOutputsAsString() + ']' +
                '}';
    }

    public String getInputsAsString() {
        return getDocksAsString(getInputs());
    }

    public String getOutputsAsString() {
        return getDocksAsString(getOutputs());
    }

    public String getDocksAsString(List<? extends Dock> docks) {
        StringBuilder result = new StringBuilder();
        String add="";
        for( Dock dock: docks ) {
            result.append(add).append(dock.toString());
            add=",";
        }
        return result.toString();
    }

    public List<ReceivingDock> getInputs() {
        return inputs;
    }

    public List<ShippingDock> getOutputs() {
        return outputs;
    }

    public void moveRelative(int dx, int dy) {
        bounds.x+=dx;
        bounds.y+=dy;
    }

    public Rectangle getRectangle() {
        return bounds;
    }

    public void setPosition(Point p) {
        bounds.setLocation(p);
    }

    public void updateBounds() {
        int w=(int)bounds.getWidth();
        int h=Node.TITLE_HEIGHT;
        int x=getRectangle().x;
        int y=getRectangle().y;
        for(Dock v : getAllDocks()) {
            Rectangle r = v.getBounds();
            r.y=h+y;
            r.x=x;
            if(w < r.width) w = r.width;
            h += r.height;
            Point p = v.getConnectionPoint();
            p.y = r.y+r.height/2;
            p.x = x;
            if(v instanceof ShippingDock) {
                p.x += r.width;
            }
        }
        bounds.width=w;
        bounds.height=h;
    }

    private List<Dock> getAllDocks() {
        List<Dock> list = new ArrayList<>();
        list.addAll(getInputs());
        list.addAll(getOutputs());
        return list;
    }
}
