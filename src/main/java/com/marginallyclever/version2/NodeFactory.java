package com.marginallyclever.version2;

import com.marginallyclever.version2.Node;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

import static org.reflections.scanners.Scanners.SubTypes;

/**
 *
 */
public class NodeFactory {
    private static final Logger logger = LoggerFactory.getLogger(NodeFactory.class);
    private static final Map<String, Class<? extends Node>> suppliers = new LinkedHashMap<>();

    public static void registerAllNodesInPackage(String packageName) throws IllegalArgumentException {
        Reflections reflections = new Reflections(packageName);
        Set<Class<?>> subTypes = reflections.get(SubTypes.of(Node.class).asClass());
        for(Class<?> typeFound : subTypes) {
            registerNode(typeFound.getSimpleName(), typeFound);
        }
    }

    /**
     * Register a new {@link Node} class with the core.
     * @param name the unique name of the Node.
     * @param supplier the {@link Node} class which must have a default no-arg constructor.
     */
    public static void registerNode(String name, Class<?> supplier) throws IllegalArgumentException {
        verifyValidName(name);
        verifyUnregisteredName(name);
        logger.debug("Registering node "+name);
        verifyTypeNotRegistered(supplier);
        verifyTypeConstructor(supplier);

        suppliers.put(name,(Class<? extends Node>)supplier);
    }

    private static void verifyUnregisteredName(String name) {
        if(suppliers.containsKey(name)) {
            throw new IllegalArgumentException("Cannot register two nodes with name "+name);
        }
    }

    private static void verifyValidName(String name) {
        if(name==null || name.isEmpty() || name.isBlank()) {
            throw new IllegalArgumentException("Name cannot be empty or blank");
        }
    }

    private static void verifyTypeNotRegistered(Class<?> typeFound) {
        if(suppliers.containsValue(typeFound)) {
            throw new IllegalArgumentException("A supplier for that node already exists.");
        }
    }

    private static void verifyTypeConstructor(Class<?> typeFound) {
        boolean valid=false;
        for( Constructor<?> constructor : typeFound.getDeclaredConstructors() ) {
            if(constructor.getParameterCount()==0) {
                valid=true;
                break;
            }
        }
        if(!valid) throw new IllegalArgumentException("Node "+typeFound.getName()+" must have public zero argument constructor.");
    }

    /**
     *
     * @param name the name of the node to create.
     * @return one new instance of the named Node, or null.
     */
    public static Node create(String name) {
        Class<? extends Node> clazz = suppliers.get(name);
        if(clazz==null) {
            throw new IllegalArgumentException("NodeFactory does not recognize '"+name+"'.");
        }
        Node instance;
        try {
            instance = (Node)clazz.getDeclaredConstructors()[0].newInstance();
            instance.updateBounds();
        } catch (InvocationTargetException | InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        return instance;
    }

    /**
     * @return a list of all registered {@link Node} names.
     */
    public static List<String> getNames() {
        return new ArrayList<>(suppliers.keySet());
    }

    public static void clear() {
        suppliers.clear();
    }

    public static boolean knowsAbout(String name) {
        return suppliers.containsKey(name);
    }

    /**
     * Initializes the {@link NodeFactory} by scanning the classpath for {@link Node}s.
     * Be sure to call {@link ServiceLoaderHelper#addFile(String)} before calling this method.
     * @throws Exception
     */
    public static void loadRegistries() {
        ServiceLoaderHelper helper = new ServiceLoaderHelper();
        loadRegistries(helper.getExtensionClassLoader());
    }

    public static void loadRegistries(ClassLoader classLoader) {
        ServiceLoader<NodeRegistry> serviceLoader = ServiceLoader.load(NodeRegistry.class, classLoader);
        for (NodeRegistry registry : serviceLoader) {
            try {
                logger.info("Loading node registry: "+registry.getClass().getName());
                registry.registerNodes();
            } catch(NoSuchMethodError e) {
                logger.warn("Plugin out of date: {}", registry.getClass().getName());
                // TODO which plugin?
            }
        }
    }
}