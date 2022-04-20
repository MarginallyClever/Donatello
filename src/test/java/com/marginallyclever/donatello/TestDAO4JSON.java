package com.marginallyclever.donatello;

import com.marginallyclever.version2.DAO4JSONFactory;
import com.marginallyclever.version2.NodeFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.awt.image.BufferedImage;


/**
 * Test JSON Data Access Objects.
 * @author Dan Royer
 * @since 2022-03-07
 */
public class TestDAO4JSON {
    @BeforeAll
    public static void beforeAll() throws Exception {
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
