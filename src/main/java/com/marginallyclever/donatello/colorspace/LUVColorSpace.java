package com.marginallyclever.donatello.colorspace;

/**
 * LUV color space conversion.
 * https://framewave.sourceforge.net/Manual/fw_function_020_0060_00330.html
 * preserve the D65 white point.
 */
public class LUVColorSpace implements ColorSpace {
    @Override
    public float[] fromRGB(float r, float g, float b) {
        float x = r * 0.4124564f + g * 0.3575761f + b * 0.1804375f;
        float y = r * 0.2126729f + g * 0.7151522f + b * 0.0721750f;
        float z = r * 0.0193339f + g * 0.1191920f + b * 0.9503041f;
        return new float[]{x,y,z};
    }

    @Override
    public float[] toRGB(float x, float y, float z) {
        float r = x *  3.2404542f + y * -1.5371385f + z * -0.4985314f;
        float g = x * -0.9692660f + y *  1.8760108f + z *  0.0415560f;
        float b = x *  0.0556434f + y * -0.2040259f + z *  1.0572252f;
        return new float[]{r,g,b};
    }
}
