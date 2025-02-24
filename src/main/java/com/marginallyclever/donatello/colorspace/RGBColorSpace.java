package com.marginallyclever.donatello.colorspace;

/**
 * A color space that converts between RGB and RGB.
 */
public class RGBColorSpace implements ColorSpace {
    @Override
    public float[] fromRGB(float r, float g, float b) {
        return new float[]{r, g, b};
    }

    @Override
    public float[] toRGB(float c1, float c2, float c3) {
        return new float[]{c1, c2, c3};
    }
}
