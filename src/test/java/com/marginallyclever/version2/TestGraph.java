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
        graph.addDock(new ShippingDock("meta_in",Number.class,graph));
        graph.addDock(new ReceivingDock("meta_out",Number.class,graph));
        graph.addConnection(new Connection(graph.getOutput("meta_in"),graph.getInput("meta_out")));
        graph.update();
    }

    @Test
    public void testAdd() {
        Graph graph = new Graph();
        graph.addDock(new ShippingDock("A",Number.class,graph));
        graph.addDock(new ShippingDock("B",Number.class,graph));
        graph.addDock(new ReceivingDock("C",Number.class,graph));

        Add add = new Add();
        graph.addNode(add);

        Connection a = new Connection(graph.getOutput("A"),add.getInput("A"));
        Connection b = new Connection(graph.getOutput("B"),add.getInput("B"));
        Connection c = new Connection(add.getOutput("result"),graph.getInput("C"));

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

    @Test void createGraphThatDoesSomeMath() {
        Graph graph = new Graph();
        graph.addDock(new ShippingDock("A",Number.class,graph));
        graph.addDock(new ShippingDock("B",Number.class,graph));
        graph.addDock(new ReceivingDock("A+B",Number.class,graph));
        graph.addDock(new ReceivingDock("A-B",Number.class,graph));
        graph.addDock(new ReceivingDock("A*B",Number.class,graph));
        graph.addDock(new ReceivingDock("A/B",Number.class,graph));

        Add add = new Add();
        Subtract subtract = new Subtract();
        Multiply multiply = new Multiply();
        Divide divide = new Divide();

        graph.addNode(add);
        graph.addNode(subtract);
        graph.addNode(multiply);
        graph.addNode(divide);

        graph.addConnection(new Connection(graph.getOutput("A"),add.getInput("A")));
        graph.addConnection(new Connection(graph.getOutput("B"),add.getInput("B")));
        graph.addConnection(new Connection(add.getOutput("result"),graph.getInput("A+B")));

        graph.addConnection(new Connection(graph.getOutput("A"),subtract.getInput("A")));
        graph.addConnection(new Connection(graph.getOutput("B"),subtract.getInput("B")));
        graph.addConnection(new Connection(subtract.getOutput("result"),graph.getInput("A-B")));

        graph.addConnection(new Connection(graph.getOutput("A"),multiply.getInput("A")));
        graph.addConnection(new Connection(graph.getOutput("B"),multiply.getInput("B")));
        graph.addConnection(new Connection(multiply.getOutput("result"),graph.getInput("A*B")));

        graph.addConnection(new Connection(graph.getOutput("A"),divide.getInput("A")));
        graph.addConnection(new Connection(graph.getOutput("B"),divide.getInput("B")));
        graph.addConnection(new Connection(divide.getOutput("result"),graph.getInput("A/B")));

        graph.getOutput("A").sendPacket(new Packet<Double>(1.0));
        graph.getOutput("B").sendPacket(new Packet<Double>(2.0));
        graph.update();
        graph.update();

        assertNotNull(graph.getInput("A+B"));
        assertTrue(graph.getInput("A+B").hasPacket());
        assertEquals("3.0",graph.getInput("A+B").getPacket().data.toString());
        assertEquals("-1.0",graph.getInput("A-B").getPacket().data.toString());
        assertEquals("2.0",graph.getInput("A*B").getPacket().data.toString());
        assertEquals("0.5",graph.getInput("A/B").getPacket().data.toString());
    }
}
