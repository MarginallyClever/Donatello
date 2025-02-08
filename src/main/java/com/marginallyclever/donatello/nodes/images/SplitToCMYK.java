package com.marginallyclever.donatello.nodes.images;

import com.marginallyclever.donatello.ports.InputImage;
import com.marginallyclever.donatello.ports.OutputImage;
import com.marginallyclever.nodegraphcore.Node;

import java.awt.image.BufferedImage;

/**
 * Split a {@link BufferedImage} into separate CMYK channels.
 * @author Dan Royer
 * @since 2022-02-23
 */
public class SplitToCMYK extends Node {
    private final InputImage image = new InputImage("image");
    private final OutputImage cyan    = new OutputImage("Cyan");
    private final OutputImage magenta = new OutputImage("Magenta");
    private final OutputImage yellow  = new OutputImage("Yellow");
    private final OutputImage black   = new OutputImage("Black");

    /**
     * Constructor for subclasses to call.
     */
    public SplitToCMYK() {
        super("SplitToCMYK");
        addPort(image);
        addPort(cyan);
        addPort(magenta);
        addPort(yellow);
        addPort(black);
    }

    @Override
    public void update() {
        BufferedImage src = image.getValue();
        int h = src.getHeight();
        int w = src.getWidth();
        int [] pixel = new int[src.getColorModel().getNumComponents()];
        var rasterS = src.getRaster();

        BufferedImage channelCyan    = new BufferedImage(w,h,src.getType());
        BufferedImage channelMagenta = new BufferedImage(w,h,src.getType());
        BufferedImage channelYellow  = new BufferedImage(w,h,src.getType());
        BufferedImage channelBlack   = new BufferedImage(w,h,src.getType());

        for (int py = 0; py < h; ++py) {
            for (int px = 0; px < w; ++px) {
                rasterS.getPixel(px,py,pixel);

                double [] cmyk = pixelToCMYK(pixel);
                int c = (int)cmyk[0];
                int m = (int)cmyk[1];
                int y = (int)cmyk[2];
                int k = (int)cmyk[3];
                channelCyan   .setRGB(px, py, ColorHelper.RGBToInt(c,c,c,c));
                channelMagenta.setRGB(px, py, ColorHelper.RGBToInt(m,m,m,m));
                channelYellow .setRGB(px, py, ColorHelper.RGBToInt(y,y,y,y));
                channelBlack  .setRGB(px, py, ColorHelper.RGBToInt(k,k,k,k));
            }
        }

        cyan   .send(channelCyan);
        magenta.send(channelMagenta);
        yellow .send(channelYellow);
        black  .send(channelBlack);
    }

    public static double[] pixelToCMYK(int [] pixel) {
        //double a = 255-((pixel>>24) & 0xff);
        double r = 1.0 - (pixel[0] / 255.0);
        double g = 1.0 - (pixel[1] / 255.0);
        double b = 1.0 - (pixel[2] / 255.0);
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
}
