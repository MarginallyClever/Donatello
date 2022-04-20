package com.marginallyclever.donatello;

import com.marginallyclever.version2.DAO4JSONFactory;
import com.marginallyclever.version2.NodeFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.List;

/**
 * Convenient methods for dealing with system properties.
 * @author Dan Royer
 * @since 2022-02-01
 */
public class PropertiesHelper {
    private static final Logger logger = LoggerFactory.getLogger(PropertiesHelper.class);

    public static void showProperties() {
        logger.debug("------------------------------------------------");
        Properties p = System.getProperties();
        List<String> names = new ArrayList<>(p.stringPropertyNames());
        Collections.sort(names);
        for (String name : names) {
            logger.debug("{} = {}", name, p.get(name));
        }
        logger.debug("------------------------------------------------");
    }

    public static void listAllNodes() {
        String sum = "";
        String add = "";
        for (String n : NodeFactory.getNames()) {
            sum += add + n;
            add = ", ";
        }
        logger.debug("Nodes: {}.",sum);
    }
}
