package com.marginallyclever.donatello.colorspace;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ColorSpaceTest {
    @Test
    public void testToAndFromRGB() {
        testToAndFrom(new RGBColorSpace());
    }

    @Test
    public void testToAndFromLUT() {
        testToAndFrom(new LUVColorSpace());
    }

    @Test
    public void testToAndFromHSV() {
        testToAndFrom(new HSVColorSpace());
    }

    private void testToAndFrom(ColorSpace colorSpace) {
        for (int r = 0; r < 256; r++) {
            for (int g = 0; g < 256; g++) {
                for (int b = 0; b < 256; b++) {
                    float r2 = r / 255f;
                    float g2 = g / 255f;
                    float b2 = b / 255f;
                    float[] converted = colorSpace.fromRGB(r2, g2, b2);
                    float[] after = colorSpace.toRGB(converted[0], converted[1], converted[2]);
                    Assertions.assertEquals(after[0], r2, 0.01, r+","+g+","+b);
                    Assertions.assertEquals(after[1], g2, 0.01, r+","+g+","+b);
                    Assertions.assertEquals(after[2], b2, 0.01, r+","+g+","+b);
                }
            }
        }
    }
}
