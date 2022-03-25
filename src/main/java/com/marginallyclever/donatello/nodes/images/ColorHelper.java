package com.marginallyclever.donatello.nodes.images;

import java.awt.*;

public class ColorHelper {
    public static double[] IntToCMYK(int pixel) {
        //double a = 255-((pixel>>24) & 0xff);
        double r = 1.0-(double)((pixel >> 16) & 0xff) / 255.0;
        double g = 1.0-(double)((pixel >>  8) & 0xff) / 255.0;
        double b = 1.0-(double)((pixel      ) & 0xff) / 255.0;
        // now convert to cmyk
        double k = Math.min(Math.min(r,g),b);   // should be Math.max(Math.max(r,g),b) but colors are inverted.
        double ik = 1.0 - k;

        //if(ik<1.0/255.0) {
        //  c=m=y=0;
        //} else {
        int c = (int)Math.max(0,Math.min(255, 255 * (r-k) / k ));
        int m = (int)Math.max(0,Math.min(255, 255 * (g-k) / k ));
        int y = (int)Math.max(0,Math.min(255, 255 * (b-k) / k ));
        int k2 = (int)Math.max(0,Math.min(255, 255 * ik ));
        //}
        return new double[]{c,m,y,k2};
    }

    public static int RGBToInt(int r, int g, int b, int a) {
        a = ( a & 0xff ) << 24;
        r = ( r & 0xff ) << 16;
        g = ( g & 0xff ) << 8;
        b = ( b & 0xff );
        return a|r|g|b;
    }

    public static int ColorToInt(Color color) {
        int a = ( color.getAlpha() & 0xff ) << 24;
        int r = ( color.getRed() & 0xff ) << 16;
        int g = ( color.getGreen() & 0xff ) << 8;
        int b = ( color.getBlue() & 0xff );
        return a|r|g|b;
    }
}
