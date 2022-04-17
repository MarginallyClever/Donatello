package com.marginallyclever.version2;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class NodeWhichContainsAGraph extends AbstractNode {
    private final Graph myGraph;

    private final Map<ReceivingDock,ShippingDock> myReceivingDock = new java.util.HashMap<>();
    private final Map<ReceivingDock,ShippingDock> myShippingDock = new java.util.HashMap<>();

    public NodeWhichContainsAGraph(Graph g) {
        super();
        if(g==null) throw new IllegalArgumentException("Graph cannot be null.");

        myGraph = g;

        for(ReceivingDock input : myGraph.getInputs()) {
            ShippingDock output = new ShippingDock(input.getName(),input.getType(),this);
            addDock(output);
            myReceivingDock.put(input,output);
        }
        for(ShippingDock output : myGraph.getOutputs()) {
            ReceivingDock input = new ReceivingDock(output.getName(),output.getType(),this);
            addDock(output);
            myShippingDock.put(input,output);
        }
    }

    /**
     * NodeWhichContainsAGraph will only
     */
    @Override
    public void update() {
        if(hasOnePacketWaitingOnEachInput() && hasRoomForInputPackets()) {
            readInputs();
        }

        myGraph.update();

        if(hasOnePacketWaitingOnEachOutput() && hasRoomForOutputPackets()) {
            writeOutputs();
        }
    }

    private boolean hasOnePacketWaitingOnEachInput() {
        for (ReceivingDock a : myReceivingDock.keySet()) {
            if (!a.hasPacket()) return false;
        }
        return true;
    }

    private boolean hasRoomForInputPackets() {
        for(ShippingDock a : myReceivingDock.values()) {
            if(a.hasPacket()) return false;
        }
        return true;
    }

    private void readInputs() {
        for (ReceivingDock a : myReceivingDock.keySet()) {
            myReceivingDock.get(a).sendPacket(a.getPacket());
        }
    }

    private boolean hasOnePacketWaitingOnEachOutput() {
        for (ReceivingDock a : myShippingDock.keySet()) {
            if (!a.hasPacket()) return false;
        }
        return true;
    }

    private boolean hasRoomForOutputPackets() {
        for(ShippingDock a : myShippingDock.values()) {
            if(a.hasPacket()) return false;
        }
        return true;
    }

    private void writeOutputs() {
        for (ReceivingDock a : myShippingDock.keySet()) {
            myShippingDock.get(a).sendPacket(a.getPacket());
        }
    }

    @Override
    public List<ReceivingDock> getInputs() {
        return new ArrayList<>(myReceivingDock.keySet());
    }

    @Override
    public List<ShippingDock> getOutputs() {
        return new ArrayList<>(myShippingDock.values());
    }

    @Override
    public ReceivingDock getInput(String name) {
        for( ReceivingDock d : getInputs() ) {
            if(d.getName().equals(name)) return d;
        }
        return null;
    }

    @Override
    public ShippingDock getOutput(String name) {
        for( ShippingDock d : getOutputs() ) {
            if(d.getName().equals(name)) return d;
        }
        return null;
    }
}
