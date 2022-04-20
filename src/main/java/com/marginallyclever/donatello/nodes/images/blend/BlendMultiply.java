package com.marginallyclever.donatello.nodes.images.blend;

import com.marginallyclever.version2.Node;
import com.marginallyclever.version2.Dock;

import java.awt.image.BufferedImage;

/**
 * <a href='https://en.wikipedia.org/wiki/Blend_modes'>Blend multiply</a>
 * @author Dan Royer
 * @since 2022-02-23
 */
public class BlendMultiply extends Node {
    private final Dock<BufferedImage> a = Dock.newInstance("a", BufferedImage.class,new BufferedImage(1,1,BufferedImage.TYPE_INT_ARGB),true,false);
    private final Dock<BufferedImage> b = Dock.newInstance("b", BufferedImage.class,new BufferedImage(1,1,BufferedImage.TYPE_INT_ARGB),true,false);
    private final Dock<BufferedImage> output = Dock.newInstance("output", BufferedImage.class,new BufferedImage(1,1,BufferedImage.TYPE_INT_ARGB),false,true);

    /**
     * Constructor for subclasses to call.
     */
    public BlendMultiply() {
        super("BlendMultiply");
        addVariable(a);
        addVariable(b);
        addVariable(output);
    }

    @Override
    public void update() {
        BufferedImage A = a.getValue();
        BufferedImage B = b.getValue();

        int w = (int)Math.min(A.getWidth(),B.getWidth());
        int h = (int)Math.min(A.getHeight(),B.getHeight());
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
                int dr = (int)Math.max(0,Math.min(255.0,(ar * br)*255.0));
                int dg = (int)Math.max(0,Math.min(255.0,(ag * bg)*255.0));
                int db = (int)Math.max(0,Math.min(255.0,(ab * bb)*255.0));
                int cC = (dr << 16) | (dg << 8) | db;
                C.setRGB(x,y,cC);
            }
        }
        output.setValue(C);
        cleanAllInputs();
    }

}
