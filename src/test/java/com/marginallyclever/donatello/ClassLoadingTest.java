package com.marginallyclever.donatello;

import com.marginallyclever.nodegraphcore.*;
import org.junit.jupiter.api.Test;

import java.util.ServiceLoader;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ClassLoadingTest {
    @Test
    public void listAllNodes() {
        ServiceLoaderHelper helper = new ServiceLoaderHelper();
        ClassLoader classLoader = helper.getExtensionClassLoader();
        ServiceLoader<NodeRegistry> loader = ServiceLoader.load(NodeRegistry.class, classLoader);
        for (NodeRegistry registry : loader) {
            registry.registerNodes();
        }
        String add = "Nodes: ";
        for (String n : NodeFactory.getNames()) {
            System.out.print(add + n);
            add = ", ";
        }
        System.out.println(".");
        NodeFactory.clear();
    }

    @Test
    public void listAllDAOs() {
        ServiceLoaderHelper helper = new ServiceLoaderHelper();
        ClassLoader classLoader = helper.getExtensionClassLoader();
        ServiceLoader<DAORegistry> loader = ServiceLoader.load(DAORegistry.class, classLoader);
        for (DAORegistry registry : loader) {
            registry.registerDAO();
        }
        String add="DAOs: ";
        for(String n : DAO4JSONFactory.getNames()) {
            System.out.print(add + n);
            add = ", ";
        }
        System.out.println(".");
        DAO4JSONFactory.clear();
    }

    @Test
    public void whoIsMyClassLoader() {
        ServiceLoaderHelper helper = new ServiceLoaderHelper();
        ClassLoader classLoader = helper.getExtensionClassLoader();
        NodeFactory.loadRegistries();
        if(NodeFactory.knowsAbout("PrintTurtle")) {
            Node add = NodeFactory.createNode("PrintTurtle");
            ClassLoader addLoader = add.getClass().getClassLoader();
            NodeFactory.clear();
            assertEquals(addLoader, classLoader);
        } else {
            System.out.println("Did not thoroughly run this test");
        }
    }
}
