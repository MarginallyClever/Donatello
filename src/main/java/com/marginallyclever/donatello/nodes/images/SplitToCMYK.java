package com.marginallyclever.donatello.nodes.images;

import com.marginallyclever.nodegraphcore.Node;
import com.marginallyclever.nodegraphcore.NodeVariable;

import java.awt.image.BufferedImage;

/**
 * Split a {@link BufferedImage} into separate CMYK channels.
 * @author Dan Royer
 * @since 2022-02-23
 */
public class SplitToCMYK extends Node {
    private final NodeVariable<BufferedImage> image   = NodeVariable.newInstance("image",   BufferedImage.class, new BufferedImage(1,1,BufferedImage.TYPE_INT_ARGB),true,false);
    private final NodeVariable<BufferedImage> cyan    = NodeVariable.newInstance("Cyan",    BufferedImage.class, new BufferedImage(1,1,BufferedImage.TYPE_INT_ARGB),false,true);
    private final NodeVariable<BufferedImage> magenta = NodeVariable.newInstance("Magenta", BufferedImage.class, new BufferedImage(1,1,BufferedImage.TYPE_INT_ARGB),false,true);
    private final NodeVariable<BufferedImage> yellow  = NodeVariable.newInstance("Yellow",  BufferedImage.class, new BufferedImage(1,1,BufferedImage.TYPE_INT_ARGB),false,true);
    private final NodeVariable<BufferedImage> black   = NodeVariable.newInstance("Black",   BufferedImage.class, new BufferedImage(1,1,BufferedImage.TYPE_INT_ARGB),false,true);

    /**
     * Constructor for subclasses to call.
     */
    public SplitToCMYK() {
        super("SplitToCMYK");
        addVariable(image);
        addVariable(cyan);
        addVariable(magenta);
        addVariable(yellow);
        addVariable(black);
    }

    /**
     * Constructor that sets starting values.
     * @param img the starting value.
     */
    public SplitToCMYK(BufferedImage img) {
        this();
        this.image.setValue(img);
    }

    @Override
    public Node create() {
        return new SplitToCMYK();
    }

    @Override
    public void update() {
        BufferedImage src = image.getValue();
        int h = src.getHeight();
        int w = src.getWidth();

        BufferedImage channelCyan    = new BufferedImage(w,h,src.getType());
        BufferedImage channelMagenta = new BufferedImage(w,h,src.getType());
        BufferedImage channelYellow  = new BufferedImage(w,h,src.getType());
        BufferedImage channelBlack   = new BufferedImage(w,h,src.getType());

        for (int py = 0; py < h; ++py) {
            for (int px = 0; px < w; ++px) {
                int pixel = src.getRGB(px,py);
                double [] cmyk = ColorHelper.IntToCMYK(pixel);
                int c=(int)cmyk[0];
                int m=(int)cmyk[1];
                int y=(int)cmyk[2];
                int k=(int)cmyk[3];
                channelCyan   .setRGB(px, py, ColorHelper.RGBToInt(c,c,c,c));
                channelMagenta.setRGB(px, py, ColorHelper.RGBToInt(m,m,m,m));
                channelYellow .setRGB(px, py, ColorHelper.RGBToInt(y,y,y,y));
                channelBlack  .setRGB(px, py, ColorHelper.RGBToInt(k,k,k,k));
            }
        }

        cyan   .setValue(channelCyan);
        magenta.setValue(channelMagenta);
        yellow .setValue(channelYellow);
        black  .setValue(channelBlack);

        cleanAllInputs();
    }
}
