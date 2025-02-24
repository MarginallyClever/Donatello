package com.marginallyclever.donatello.colorspace;

public interface ColorSpace {
    /**
     * Converts RGB values (0-1 range) to the target color space.
     * @param r Red component (0-1)
     * @param g Green component (0-1)
     * @param b Blue component (0-1)
     * @return Array of converted color space values.
     */
    float[] fromRGB(float r, float g, float b);

    /**
     * Converts from the target color space back to RGB (0-1 range).
     * @param c1 First component of color space.
     * @param c2 Second component of color space.
     * @param c3 Third component of color space.
     * @return Array with RGB values (0-1 range).
     */
    float[] toRGB(float c1, float c2, float c3);
}