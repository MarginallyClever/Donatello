package com.marginallyclever.donatello;

import com.marginallyclever.nodegraphcore.*;
import com.marginallyclever.donatello.actions.SaveGraphAction;
import com.marginallyclever.donatello.nodes.images.LoadImage;
import com.marginallyclever.donatello.nodes.images.PrintImage;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test the NodeGraphSwing elements.
 * @author Dan Royer
 * @since 2022-02-21
 */
public class TestNodeGraphSwing {
    private static NodeGraph model = new NodeGraph();

    @BeforeAll
    public static void beforeAll() {
        DonatelloRegistry r = new DonatelloRegistry();
        r.registerNodes();
        r.registerDAO();
    }

    @AfterAll
    public static void afterAll() {
        NodeFactory.clear();
        DAO4JSONFactory.clear();
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
        assertNotEquals(0,NodeFactory.getNames().length);
        for(String s : NodeFactory.getNames()) {
            System.out.println(s);
            assertNotNull(NodeFactory.createNode(s));
        }
    }

    @Test
    public void testImages() {
        LoadImage img2 = new LoadImage();
        img2.getVariable(0).setValue("doesNotExist.png");
        img2.update();

        LoadImage img = new LoadImage();
        img.getVariable(0).setValue("src/test/resources/test.png");
        img.update();

        PrintImage printer = new PrintImage();
        NodeConnection c = new NodeConnection(img,0,printer,0);
    }

    @Test
    public void testAddExtension() {
        SaveGraphAction actionSaveGraph = new SaveGraphAction("Save",null);
        assertEquals("test.graph",actionSaveGraph.addExtensionIfNeeded("test"));
        assertEquals("test.graph",actionSaveGraph.addExtensionIfNeeded("test.graph"));
    }
}
