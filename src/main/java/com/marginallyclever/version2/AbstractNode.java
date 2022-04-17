package com.marginallyclever.version2;

import java.security.InvalidParameterException;
import java.util.*;

/**
 * Implementation of {@link Node} interface.
 */
public abstract class AbstractNode extends AbstractNamedEntity implements Node {
    /**
     * All inputs to this node.
     */
    private final List<ReceivingDock> inputs = new LinkedList<>();

    /**
     * All outputs from this node.
     */
    private final List<ShippingDock> outputs = new LinkedList<>();

    /**
     * All inputs and outputs together in one list for convenience.
     */
    private final List<Dock> allDocks = new ArrayList<>();

    public AbstractNode() {
        super();
    }

    public void addDock(Dock dock) {
        allDocks.add(dock);
        if(dock instanceof ShippingDock) {
            outputs.add((ShippingDock)dock);
        } else if(dock instanceof ReceivingDock) {
            inputs.add((ReceivingDock)dock);
        } else {
            throw new InvalidParameterException("dock must of type ShippingDock or ReceivingDock.");
        }
    }

    public void removeDock(Dock dock) {
        allDocks.remove(dock);
        outputs.remove(dock);
        inputs.remove(dock);
    }

    @Override
    public List<Dock> getDocks() {
        return allDocks;
    }

    /**
     * @param name the name of the input to get.
     * @return the input found or null.
     */
    public ReceivingDock getInput(String name) {
        List<ReceivingDock> list = getInputs();
        for( ReceivingDock d : list ) {
            if(d.getName().equals(name)) return d;
        }
        return null;
    }

    /**
     * @param name the name of the output to get.
     * @return the input found or null.
     */
    public ShippingDock getOutput(String name) {
        List<ShippingDock> list = getOutputs();
        for( ShippingDock d : list ) {
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

    @Override
    public List<ReceivingDock> getInputs() {
        return inputs;
    }

    @Override
    public List<ShippingDock> getOutputs() {
        return outputs;
    }
}
