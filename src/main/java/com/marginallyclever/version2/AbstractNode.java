package com.marginallyclever.version2;

import java.security.InvalidParameterException;
import java.util.*;

public abstract class AbstractNode implements Node {
    private final String name;
    private final String uniqueID = UUID.randomUUID().toString();
    private final List<Dock> allDocks = new ArrayList<>();

    private final List<ReceivingDock> inputs = new LinkedList<>();
    private final List<ShippingDock> outputs = new LinkedList<>();

    public AbstractNode() {
        name = this.getClass().getSimpleName();
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

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getUniqueID() {
        return uniqueID;
    }

    /**
     * @param name the name of the input to get.
     * @return the input found or null.
     */
    @Override
    public ReceivingDock getInput(String name) {
        for( ReceivingDock d : getInputs() ) {
            if(d.getName().equals(name)) return d;
        }
        return null;
    }

    /**
     * @param name the name of the output to get.
     * @return the input found or null.
     */
    @Override
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
        String result = "";
        String add="";
        for( Dock dock: docks ) {
            result += add + dock.toString();
            add=",";
        }
        return result;
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
