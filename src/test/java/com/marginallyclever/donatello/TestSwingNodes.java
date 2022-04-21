package com.marginallyclever.donatello;

import com.marginallyclever.version2.*;
import com.marginallyclever.donatello.actions.SaveGraphAction;
import com.marginallyclever.donatello.nodes.images.LoadImage;
import com.marginallyclever.donatello.nodes.images.PrintImage;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test the GraphSwing elements.
 * @author Dan Royer
 * @since 2022-02-21
 */
public class TestSwingNodes {
    private static Graph model = new Graph();

    @BeforeAll
    public static void beforeAll() {
        DonatelloRegistry r = new DonatelloRegistry();
        r.registerNodes();
    }

    @AfterAll
    public static void afterAll() {
        NodeFactory.clear();
    }

    /**
     * Reset
     */
    @BeforeEach
    public void beforeEach() {
        model.clear();
    }

    /**
     * Make sure all nodes introduced in this package can be created.
     */
    @Test
    public void testFactoryCreatesAllSwingTypes() {
        assertNotEquals(0,NodeFactory.getNames().size());
        for(String s : NodeFactory.getNames()) {
            System.out.println(s);
            assertNotNull(NodeFactory.create(s));
        }
    }

    @Test
    public void testAddExtension() {
        SaveGraphAction actionSaveGraph = new SaveGraphAction("Save",null);
        assertEquals("test.graph",actionSaveGraph.addExtensionIfNeeded("test"));
        assertEquals("test.graph",actionSaveGraph.addExtensionIfNeeded("test.graph"));
    }
}
