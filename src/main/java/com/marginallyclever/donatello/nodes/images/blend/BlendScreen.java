package com.marginallyclever.donatello.nodes.images.blend;

import com.marginallyclever.nodegraphcore.*;
import com.marginallyclever.nodegraphcore.dock.Input;
import com.marginallyclever.nodegraphcore.dock.Output;

import java.awt.image.BufferedImage;

/**
 * <a href='https://en.wikipedia.org/wiki/Blend_modes'>Blend screen</a>
 * @author Dan Royer
 * @since 2022-02-23
 */
public class BlendScreen extends Node {
    private final Input<BufferedImage> a = new Input<>("a", BufferedImage.class,new BufferedImage(1,1,BufferedImage.TYPE_INT_ARGB));
    private final Input<BufferedImage> b = new Input<>("b", BufferedImage.class,new BufferedImage(1,1,BufferedImage.TYPE_INT_ARGB));
    private final Output<BufferedImage> output = new Output<>("output", BufferedImage.class,new BufferedImage(1,1,BufferedImage.TYPE_INT_ARGB));

    /**
     * Constructor for subclasses to call.
     */
    public BlendScreen() {
        super("BlendScreen");
        addVariable(a);
        addVariable(b);
        addVariable(output);
    }

    @Override
    public void update() {
        BufferedImage A = a.getValue();
        BufferedImage B = b.getValue();

        int w = Math.min(A.getWidth(),B.getWidth());
        int h = Math.min(A.getHeight(),B.getHeight());
        BufferedImage C = new BufferedImage(w,h,A.getType());

        for(int y=0;y<h;++y) {
            for(int x=0;x<w;++x) {
                int cA = A.getRGB(x,y);
                int cB = B.getRGB(x,y);
                double ar = (( cA >> 16 ) & 0xff)/255.0;
                double ag = (( cA >>  8 ) & 0xff)/255.0;
                double ab = (( cA       ) & 0xff)/255.0;
                double br = (( cB >> 16 ) & 0xff)/255.0;
                double bg = (( cB >>  8 ) & 0xff)/255.0;
                double bb = (( cB       ) & 0xff)/255.0;

                int dr = boundColorRange(1 - (1-ar) * (1-br));
                int dg = boundColorRange(1 - (1-ag) * (1-bg));
                int db = boundColorRange(1 - (1-ab) * (1-bb));
                int cC = (dr << 16) | (dg << 8) | db;
                C.setRGB(x,y,cC);
            }
        }
        output.send(C);
    }

    /**
     * Returns input scaled to 0...255
     * @param input value from 0...1 inclusive
     * @return input scaled to 0...255
     */
    int boundColorRange(double input) {
        return (int)Math.max(0,Math.min(255.0,input*255.0));
    }
}
