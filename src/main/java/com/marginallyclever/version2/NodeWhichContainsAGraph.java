package com.marginallyclever.version2;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class NodeWhichContainsAGraph extends AbstractNode {
    private final Graph myGraph;

    private final Map<ReceivingDock,ShippingDock> myOutputGlue = new java.util.HashMap<>();
    private final Map<ReceivingDock,ShippingDock> myInputGlue = new java.util.HashMap<>();

    public NodeWhichContainsAGraph(Graph g) {
        super();
        if(g==null) throw new IllegalArgumentException("Graph cannot be null.");

        myGraph = g;

        // connect my inputs to the graph entry points
        for(ShippingDock output : myGraph.getEntryPoints()) {
            ReceivingDock input = new ReceivingDock(output.getName(),output.getType(),this);
            addDock(output);
            myInputGlue.put(input,output);
        }

        // connect my outputs to the graph exit points
        for(ReceivingDock input : myGraph.getExitPoints()) {
            ShippingDock output = new ShippingDock(input.getName(),input.getType(),this);
            addDock(output);
            myOutputGlue.put(input,output);
        }
    }

    /**
     * This node contains a graph which may contain other graphs and so on.  This node transfers
     * waiting {@link Packet}s into the {@link Graph} only when there is one {@link Packet} waiting on each available
     * input.  This node will transfer out {@link Packet}s as soon as there is room on the {@link Connection}s.
     */
    @Override
    public void update() {
        readInputs();

        myGraph.update();

        writeOutputs();
    }

    private boolean hasOnePacketWaitingOnEach( Map<ReceivingDock,ShippingDock> map ) {
        for (ReceivingDock a : map.keySet()) {
            if (!a.hasPacket()) return false;
        }
        return true;
    }

    private boolean hasRoomForPackets( Map<ReceivingDock,ShippingDock> map ) {
        for(ShippingDock a : map.values()) {
            if(a.hasPacket()) return false;
        }
        return true;
    }

    private void readInputs() {
        if(hasOnePacketWaitingOnEach(myInputGlue) && hasRoomForPackets(myInputGlue)) {
            for (ReceivingDock a : myInputGlue.keySet()) {
                myInputGlue.get(a).sendPacket(a.getPacket());
            }
        }
    }

    private void writeOutputs() {
        for (ReceivingDock a : myOutputGlue.keySet()) {
            ShippingDock out = myOutputGlue.get(a);
            if(a.hasPacket() && !out.hasPacket() ) {
                out.sendPacket(a.getPacket());
            }
        }
    }

    @Override
    public List<ReceivingDock> getInputs() {
        return new ArrayList<>(myInputGlue.keySet());
    }

    @Override
    public List<ShippingDock> getOutputs() {
        return new ArrayList<>(myOutputGlue.values());
    }
}
