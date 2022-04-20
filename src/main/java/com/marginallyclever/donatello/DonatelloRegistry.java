package com.marginallyclever.donatello;

import com.marginallyclever.version2.DAORegistry;
import com.marginallyclever.version2.NodeFactory;
import com.marginallyclever.version2.DAO4JSONFactory;
import com.marginallyclever.version2.NodeRegistry;
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
public class DonatelloRegistry implements NodeRegistry {
    private static final Logger logger = LoggerFactory.getLogger(DonatelloRegistry.class);

    public String getName() {
        return "Donatello";
    }

    /**
     * Perform the registration.
     */
    public void registerNodes() {
        logger.info("Registering donatello annotatednodes");
        NodeFactory.registerAllNodesInPackage("com.marginallyclever.donatello.annotatednodes");
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
