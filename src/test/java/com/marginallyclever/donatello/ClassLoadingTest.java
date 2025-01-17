package com.marginallyclever.donatello;

import com.marginallyclever.nodegraphcore.*;
import org.junit.jupiter.api.Test;
import org.reflections.Reflections;
import org.reflections.scanners.ResourcesScanner;
import org.reflections.scanners.Scanner;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.FileSystems;
import java.util.Arrays;
import java.util.ServiceLoader;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ClassLoadingTest {
    @Test
    public void listAllNodes() throws Exception {
        NodeFactory.loadRegistries();
        System.out.println("List all nodes: "+ Arrays.toString(NodeFactory.getNames()));
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
        System.out.println("List all Donatello DAOs: "+Arrays.toString(DAO4JSONFactory.getNames()));
        DAO4JSONFactory.clear();
    }

    @Test
    public void whoIsMyClassLoader() throws Exception {
        ServiceLoaderHelper helper = new ServiceLoaderHelper();
        ClassLoader classLoader = helper.getExtensionClassLoader();
        NodeFactory.loadRegistries();
        if(NodeFactory.knowsAbout("PrintTurtle")) {
            Node pt = NodeFactory.createNode("PrintTurtle");
            ClassLoader addLoader = pt.getClass().getClassLoader();
            NodeFactory.clear();
            assertEquals(addLoader, classLoader);
        } else {
            System.out.println("Did not thoroughly run this test");
        }
        NodeFactory.clear();
    }

    @Test
    public void testLoadingDonatelloExtensionsIfAvailable() throws Exception {
        System.out.println("Loading Donatello extensions: "+FileHelper.getExtensionPath());

        ServiceLoaderHelper.addAllPathFiles(FileHelper.getExtensionPath());

        NodeFactory.loadRegistries();
        System.out.println("All Donatello nodes: "+Arrays.toString(NodeFactory.getNames()));
        NodeFactory.clear();
    }
}
