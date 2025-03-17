package com.marginallyclever.donatello;

import com.marginallyclever.nodegraphcore.DAO4JSONFactory;
import com.marginallyclever.nodegraphcore.NodeFactory;
import com.marginallyclever.donatello.nodes.images.BufferedImageDAO4JSON;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.awt.image.BufferedImage;


/**
 * Test JSON Data Access Objects.
 * @author Dan Royer
 * @since 2022-03-07
 */
public class TestDAO4JSON {
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

    private boolean bufferedImagesEqual(BufferedImage img1, BufferedImage img2) {
        if(img1.getType() != img2.getType()
            || img1.getWidth() != img2.getWidth()
            || img1.getHeight() != img2.getHeight()) {
            return false;
        }

        for (int x = 0; x < img1.getWidth(); x++) {
            for (int y = 0; y < img1.getHeight(); y++) {
                if (img1.getRGB(x, y) != img2.getRGB(x, y))
                    return false;
            }
        }

        return true;
    }

    @Disabled("Work in IntelliJ, not in Maven.  Investigate!")
    @Test
    public void isRegistered() {
        assert(DAO4JSONFactory.isRegistered(BufferedImage.class));
    }

    /**
     * Test {@link BufferedImage}.
     */
    @Test
    public void testBufferedImageDAO() {
        BufferedImageDAO4JSON dao = new BufferedImageDAO4JSON();
        BufferedImage r1 = new BufferedImage(2,3,BufferedImage.TYPE_INT_ARGB);
        BufferedImage r2=dao.fromJSON(dao.toJSON(r1));
        assert(bufferedImagesEqual(r1,r2));
    }
}
