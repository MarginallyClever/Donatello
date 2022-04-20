package com.marginallyclever.version2;

import com.marginallyclever.version2.nodes.HelloWorld;
import com.marginallyclever.version2.nodes.NodeWhichContainsAGraph;
import com.marginallyclever.version2.nodes.annotatednodes.*;
import org.junit.jupiter.api.Test;

import java.io.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestGraph {
    @Test
    public void helloWorld() {
        Graph graph = new Graph();
        graph.add(new HelloWorld());
        graph.update();
    }

    @Test
    public void testPassThroughGraph() {
        Graph graph = new Graph();
        graph.addEntryPoint(new ShippingDock("meta_in",Number.class,graph));
        graph.addExitPoint(new ReceivingDock("meta_out",Number.class,graph));
        graph.add(new Connection(graph.getEntryPoint("meta_in"),graph.getExitPoint("meta_out")));
        graph.update();
    }

    @Test
    public void testAdd() {
        Graph graph = new Graph();
        graph.addEntryPoint(new ShippingDock("A",Number.class,graph));
        graph.addEntryPoint(new ShippingDock("B",Number.class,graph));
        graph.addExitPoint(new ReceivingDock("C",Number.class,graph));

        Add add = new Add();
        graph.add(add);

        Connection a = new Connection(graph.getEntryPoint("A"),add.getInput("A"));
        Connection b = new Connection(graph.getEntryPoint("B"),add.getInput("B"));
        Connection c = new Connection(add.getOutput("result"),graph.getExitPoint("C"));

        graph.add(a);
        graph.add(b);
        graph.add(c);

        a.addPacket(new Packet<>(1.0));
        b.addPacket(new Packet<>(2.0));
        add.update();
        assertEquals("3.0",c.getPacket().data.toString());
        assertNull(c.getPacket());
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

        graph.add(add);
        graph.add(subtract);
        graph.add(multiply);
        graph.add(divide);

        graph.add(new Connection(graph.getEntryPoint("A"),add.getInput("A")));
        graph.add(new Connection(graph.getEntryPoint("B"),add.getInput("B")));
        graph.add(new Connection(add.getOutput("result"),graph.getExitPoint("A+B")));

        graph.add(new Connection(graph.getEntryPoint("A"),subtract.getInput("A")));
        graph.add(new Connection(graph.getEntryPoint("B"),subtract.getInput("B")));
        graph.add(new Connection(subtract.getOutput("result"),graph.getExitPoint("A-B")));

        graph.add(new Connection(graph.getEntryPoint("A"),multiply.getInput("A")));
        graph.add(new Connection(graph.getEntryPoint("B"),multiply.getInput("B")));
        graph.add(new Connection(multiply.getOutput("result"),graph.getExitPoint("A*B")));

        graph.add(new Connection(graph.getEntryPoint("A"),divide.getInput("A")));
        graph.add(new Connection(graph.getEntryPoint("B"),divide.getInput("B")));
        graph.add(new Connection(divide.getOutput("result"),graph.getExitPoint("A/B")));

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

    public Node createANodeWhichContainsAGraphWhichDoesMath() {
        Graph inner = makeGraphThatDoesMath();
        return new NodeWhichContainsAGraph(inner);
    }

    private Graph makeGraphInsideNodeInsideGraph() {
        Node container = createANodeWhichContainsAGraphWhichDoesMath();
        assertEquals(2, container.getInputs().size());
        assertEquals(4, container.getOutputs().size());

        Graph outer = new Graph();
        outer.add(container);
        outer.addEntryPoint(new ShippingDock("A",Number.class,outer));
        outer.addEntryPoint(new ShippingDock("B",Number.class,outer));
        outer.addExitPoint(new ReceivingDock("A",Number.class,outer));
        outer.addExitPoint(new ReceivingDock("B",Number.class,outer));
        outer.addExitPoint(new ReceivingDock("C",Number.class,outer));
        outer.addExitPoint(new ReceivingDock("D",Number.class,outer));

        outer.add(new Connection(outer.getEntryPoint("A"),container.getInput("A")));
        outer.add(new Connection(outer.getEntryPoint("B"),container.getInput("B")));

        outer.add(new Connection(container.getOutput("A+B"),outer.getExitPoint("A")));
        outer.add(new Connection(container.getOutput("A-B"),outer.getExitPoint("B")));
        outer.add(new Connection(container.getOutput("A*B"),outer.getExitPoint("C")));
        outer.add(new Connection(container.getOutput("A/B"),outer.getExitPoint("D")));
        return outer;
    }

    @Test
    public void putAGraphInANode() {
        Graph outer = makeGraphInsideNodeInsideGraph();
        outer.getEntryPoint("A").sendPacket(new Packet<>(1.0));
        outer.getEntryPoint("B").sendPacket(new Packet<>(2.0));
        outer.update();
        outer.update();

        assertEquals("3.0",outer.getExitPoint("A").getPacket().data.toString());
        assertEquals("-1.0",outer.getExitPoint("B").getPacket().data.toString());
        assertEquals("2.0",outer.getExitPoint("C").getPacket().data.toString());
        assertEquals("0.5",outer.getExitPoint("D").getPacket().data.toString());
    }

    @Test
    public void saveAndLoadAGraph() throws IOException, ClassNotFoundException {
        Graph first = makeGraphThatDoesMath();

        File temp = File.createTempFile("hello", ".file");
        temp.deleteOnExit();

        GraphWriter writer = new GraphWriter();
        writer.write(first, new FileOutputStream(temp));

        GraphReader reader = new GraphReader();
        Graph second = reader.parse(new FileInputStream(temp));

        String s0 = first.toString();
        String s1 = second.toString();
        assertEquals(s0,s1);
    }

    @Test
    public void saveAndLoadANestedGraph() throws IOException, ClassNotFoundException {
        Graph first = makeGraphInsideNodeInsideGraph();

        File temp = File.createTempFile("hello", ".ser");
        temp.deleteOnExit();

        GraphWriter writer = new GraphWriter();
        writer.write(first, new FileOutputStream(temp));

        GraphReader reader = new GraphReader();
        Graph second = reader.parse(new FileInputStream(temp));

        String s0 = first.toString();
        String s1 = second.toString();
        assertEquals(s0,s1);
    }

    @Test
    public void makingInvalidConnectionsShouldFail() {
        Graph graph = new Graph();
        graph.add(new Add());
        graph.addEntryPoint(new ShippingDock("A",String.class,graph));
        assertThrows(IllegalArgumentException.class,
                ()->graph.add(new Connection(graph.getEntryPoint("A"),graph.getNodes().get(0).getInputs().get(0)))
        );
        graph.addExitPoint(new ReceivingDock("B",String.class,graph));
        assertThrows(IllegalArgumentException.class,
                ()->graph.add(new Connection(graph.getNodes().get(0).getOutputs().get(0),graph.getExitPoint("B")))
        );
    }
}
