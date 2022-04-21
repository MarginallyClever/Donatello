package com.marginallyclever.donatello;

import com.marginallyclever.version2.NodeFactory;
import com.marginallyclever.version2.NodeRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Registers Swing {@link com.marginallyclever.version2.Node}s to the {@link NodeFactory}.
 * Registers Swing types with the JSON DAO factory.
 * @author Dan Royer
 * @since 2022-02-11
 */
public class DonatelloRegistry implements NodeRegistry {
    private static final Logger logger = LoggerFactory.getLogger(DonatelloRegistry.class);

    public String getName() {
        return "Donatello";
    }

    /**
     * Perform the registration.
     */
    public void registerNodes() {
        logger.info("Registering donatello nodes");
        NodeFactory.registerAllNodesInPackage("com.marginallyclever.donatello.nodes");
    }
}
