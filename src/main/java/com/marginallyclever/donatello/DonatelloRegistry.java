package com.marginallyclever.donatello;

import com.marginallyclever.donatello.nodes.color.ColorDAO4JSON;
import com.marginallyclever.nodegraphcore.DAORegistry;
import com.marginallyclever.nodegraphcore.NodeFactory;
import com.marginallyclever.nodegraphcore.DAO4JSONFactory;
import com.marginallyclever.nodegraphcore.NodeRegistry;
import com.marginallyclever.donatello.nodes.images.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.awt.image.BufferedImage;

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

    /**
     * Perform the registration.
     */
    public void registerNodes() {
        logger.info("Registering donatello nodes");
        NodeFactory.registerAllNodesInPackage("com.marginallyclever.donatello.nodes");
    }

    /**
     * Perform the registration.
     */
    @Override
    public void registerDAO() {
        logger.info("Registering donatello DAOs");
        DAO4JSONFactory.registerDAO(BufferedImage.class,new BufferedImageDAO4JSON());
        DAO4JSONFactory.registerDAO(Color.class,new ColorDAO4JSON());
    }
}
