package com.marginallyclever.donatello;

import com.marginallyclever.donatello.actions.GraphSaveAsAction;
import com.marginallyclever.donatello.nodes.images.LoadImage;
import com.marginallyclever.donatello.nodes.images.PrintImage;
import com.marginallyclever.nodegraphcore.Connection;
import com.marginallyclever.nodegraphcore.DAO4JSONFactory;
import com.marginallyclever.nodegraphcore.NodeFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test the GraphSwing elements.
 * @author Dan Royer
 * @since 2022-02-21
 */
public class TestGraphSwing {
    @BeforeAll
    public static void beforeAll() {
        NodeFactory.loadRegistries();
        DAO4JSONFactory.loadRegistries();
    }

    @AfterAll
    public static void afterAll() {
        NodeFactory.clear();
        DAO4JSONFactory.clear();
    }

    /**
     * Make sure all nodes introduced in this package can be created.
     */
    @Test
    public void testFactoryCreatesAllSwingTypes() {
        assertNotEquals(0,NodeFactory.getNames().length);
        System.out.print("Create all Swing types: ");
        String add = "";
        for(String s : NodeFactory.getNames()) {
            System.out.print(add+s);
            add=", ";
            assertNotNull(NodeFactory.createNode(s));
        }
        System.out.println();
    }

    @Test
    public void testImages() {
        LoadImage img2 = new LoadImage();
        img2.getPort(0).setValue(new Filename("doesNotExist.png"));
        img2.update();

        LoadImage img = new LoadImage();
        // get filename of test image
        String fname = Objects.requireNonNull(TestGraphSwing.class.getResource("test.png")).getPath();
        img.getPort(0).setValue(new Filename(fname));
        img.update();

        PrintImage printer = new PrintImage();
        Connection c = new Connection(img,1,printer,0);
    }

    @Test
    public void testAddExtension() {
        GraphSaveAsAction actionSaveGraph = new GraphSaveAsAction(null,"Save",null);
        assertEquals("test.graph",actionSaveGraph.addExtensionIfNeeded("test"));
        assertEquals("test.graph",actionSaveGraph.addExtensionIfNeeded("test.graph"));
    }
}
