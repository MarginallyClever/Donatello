package com.marginallyclever.version2;

import com.marginallyclever.version2.annotatednodes.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestGraph {
    @Test
    public void helloWorld() {
        Graph graph = new Graph();
        graph.addNode(new HelloWorld());
        graph.update();
    }

    @Test
    public void testPassThroughGraph() {
        Graph graph = new Graph();
        graph.addEntryPoint(new ShippingDock("meta_in",Number.class,graph));
        graph.addExitPoint(new ReceivingDock("meta_out",Number.class,graph));
        graph.addConnection(new Connection(graph.getEntryPoint("meta_in"),graph.getExitPoint("meta_out")));
        graph.update();
    }

    @Test
    public void testAdd() {
        Graph graph = new Graph();
        graph.addEntryPoint(new ShippingDock("A",Number.class,graph));
        graph.addEntryPoint(new ShippingDock("B",Number.class,graph));
        graph.addExitPoint(new ReceivingDock("C",Number.class,graph));

        Add add = new Add();
        graph.addNode(add);

        Connection a = new Connection(graph.getEntryPoint("A"),add.getInput("A"));
        Connection b = new Connection(graph.getEntryPoint("B"),add.getInput("B"));
        Connection c = new Connection(add.getOutput("result"),graph.getExitPoint("C"));

        graph.addConnection(a);
        graph.addConnection(b);
        graph.addConnection(c);

        a.addPacket(new Packet<Double>(1.0));
        b.addPacket(new Packet<Double>(2.0));
        add.update();
        assertEquals("3.0",c.getPacket().data.toString());
        assertEquals(null,c.getPacket());
        assertFalse(a.hasPacket());
        assertFalse(b.hasPacket());
        assertFalse(c.hasPacket());
    }

    private Graph makeGraphThatDoesMath() {
        Graph graph = new Graph();
        graph.addEntryPoint(new ShippingDock("A",Number.class,graph));
        graph.addEntryPoint(new ShippingDock("B",Number.class,graph));
        graph.addExitPoint(new ReceivingDock("A+B",Number.class,graph));
        graph.addExitPoint(new ReceivingDock("A-B",Number.class,graph));
        graph.addExitPoint(new ReceivingDock("A*B",Number.class,graph));
        graph.addExitPoint(new ReceivingDock("A/B",Number.class,graph));

        Add add = new Add();
        Subtract subtract = new Subtract();
        Multiply multiply = new Multiply();
        Divide divide = new Divide();

        graph.addNode(add);
        graph.addNode(subtract);
        graph.addNode(multiply);
        graph.addNode(divide);

        graph.addConnection(new Connection(graph.getEntryPoint("A"),add.getInput("A")));
        graph.addConnection(new Connection(graph.getEntryPoint("B"),add.getInput("B")));
        graph.addConnection(new Connection(add.getOutput("result"),graph.getExitPoint("A+B")));

        graph.addConnection(new Connection(graph.getEntryPoint("A"),subtract.getInput("A")));
        graph.addConnection(new Connection(graph.getEntryPoint("B"),subtract.getInput("B")));
        graph.addConnection(new Connection(subtract.getOutput("result"),graph.getExitPoint("A-B")));

        graph.addConnection(new Connection(graph.getEntryPoint("A"),multiply.getInput("A")));
        graph.addConnection(new Connection(graph.getEntryPoint("B"),multiply.getInput("B")));
        graph.addConnection(new Connection(multiply.getOutput("result"),graph.getExitPoint("A*B")));

        graph.addConnection(new Connection(graph.getEntryPoint("A"),divide.getInput("A")));
        graph.addConnection(new Connection(graph.getEntryPoint("B"),divide.getInput("B")));
        graph.addConnection(new Connection(divide.getOutput("result"),graph.getExitPoint("A/B")));

        return graph;
    }

    @Test
    public void createGraphThatDoesSomeMath() {
        Graph graph = makeGraphThatDoesMath();
        graph.getEntryPoint("A").sendPacket(new Packet<>(1.0));
        graph.getEntryPoint("B").sendPacket(new Packet<>(2.0));
        graph.update();
        graph.update();

        assertNotNull(graph.getExitPoint("A+B"));
        assertTrue(graph.getExitPoint("A+B").hasPacket());
        assertEquals("3.0",graph.getExitPoint("A+B").getPacket().data.toString());
        assertEquals("-1.0",graph.getExitPoint("A-B").getPacket().data.toString());
        assertEquals("2.0",graph.getExitPoint("A*B").getPacket().data.toString());
        assertEquals("0.5",graph.getExitPoint("A/B").getPacket().data.toString());
    }

    @Test
    public void putAGraphInANode() {
        Graph graph = makeGraphThatDoesMath();
        NodeWhichContainsAGraph nwcag = new NodeWhichContainsAGraph(graph);
        System.out.println(nwcag.getInputs());
        System.out.println(nwcag.getOutputs());
        nwcag.update();
        nwcag.update();
    }
}
