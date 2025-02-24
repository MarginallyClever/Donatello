package com.marginallyclever.donatello.colorspace;

public class HSVColorSpace implements ColorSpace {
    @Override
    public float[] fromRGB(float r, float g, float b) {
        float max = Math.max(r, Math.max(g, b));
        float min = Math.min(r, Math.min(g, b));
        float delta = max - min;

        float h = 0f;
        if (delta != 0) {
            if (max == r) h = ((g - b) / delta) % 6;
            else if (max == g) h = ((b - r) / delta) + 2;
            else h = ((r - g) / delta) + 4;
            h /= 6f;
            if (h < 0) h += 1;
        }

        float s = (max == 0) ? 0 : (delta / max);
        float v = max;
        return new float[]{h, s, v};
    }

    @Override
    public float[] toRGB(float h, float s, float v) {
        float c = v * s;
        float x = c * (1 - Math.abs((h * 6) % 2 - 1));
        float m = v - c;

        float r = 0, g = 0, b = 0;
        int sector = (int)(h * 6) % 6;
        switch (sector) {
            case 0: r = c; g = x; break;
            case 1: r = x; g = c; break;
            case 2: g = c; b = x; break;
            case 3: g = x; b = c; break;
            case 4: r = x; b = c; break;
            case 5: r = c; b = x; break;
        }
        return new float[]{r + m, g + m, b + m};
    }
}
