package com.marginallyclever.donatello;

import com.marginallyclever.donatello.nodes.color.ColorDAO4JSON;
import com.marginallyclever.donatello.nodes.images.BufferedImageDAO4JSON;
import com.marginallyclever.nodegraphcore.DAO4JSONFactory;
import com.marginallyclever.nodegraphcore.DAORegistry;
import com.marginallyclever.nodegraphcore.NodeFactory;
import com.marginallyclever.nodegraphcore.NodeRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Registers Swing {@link com.marginallyclever.nodegraphcore.Node}s to the {@link NodeFactory}.
 * Registers Swing types with the JSON DAO factory.
 * @author Dan Royer
 * @since 2022-02-11
 */
public class DonatelloRegistry implements NodeRegistry, DAORegistry {
    private static final Logger logger = LoggerFactory.getLogger(DonatelloRegistry.class);

    public String getName() {
        return "Donatello";
    }

    public void registerNodes() {
        logger.info("Registering donatello nodes");
        NodeFactory.registerAllNodesInPackage("com.marginallyclever.donatello.nodes");
    }

    @Override
    public void registerDAO() {
        logger.info("Registering donatello DAOs");
        DAO4JSONFactory.registerDAO(new BufferedImageDAO4JSON());
        DAO4JSONFactory.registerDAO(new ColorDAO4JSON());
        DAO4JSONFactory.registerDAO(new FilenameDAO4JSON());
    }
}
