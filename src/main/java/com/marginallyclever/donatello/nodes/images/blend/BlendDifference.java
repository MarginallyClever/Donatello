package com.marginallyclever.donatello.nodes.images.blend;

import com.marginallyclever.nodegraphcore.*;
import com.marginallyclever.nodegraphcore.dock.Input;
import com.marginallyclever.nodegraphcore.dock.Output;

import java.awt.image.BufferedImage;

/**
 * <a href='https://en.wikipedia.org/wiki/Blend_modes'>blend difference</a>
 * @author Dan Royer
 * @since 2022-02-23
 */
public class BlendDifference extends Node {
    private final Input<BufferedImage> a = new Input<>("a", BufferedImage.class,new BufferedImage(1,1,BufferedImage.TYPE_INT_ARGB));
    private final Input<BufferedImage> b = new Input<>("b", BufferedImage.class,new BufferedImage(1,1,BufferedImage.TYPE_INT_ARGB));
    private final Output<BufferedImage> output = new Output<>("output", BufferedImage.class,new BufferedImage(1,1,BufferedImage.TYPE_INT_ARGB));

    /**
     * Constructor for subclasses to call.
     */
    public BlendDifference() {
        super("BlendDifference");
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
                int ar = ( cA >> 16 ) & 0xff;
                int ag = ( cA >>  8 ) & 0xff;
                int ab = ( cA       ) & 0xff;
                int br = ( cB >> 16 ) & 0xff;
                int bg = ( cB >>  8 ) & 0xff;
                int bb = ( cB       ) & 0xff;
                int dr = Math.abs(ar - br);
                int dg = Math.abs(ag - bg);
                int db = Math.abs(ab - bb);
                int cC = (dr << 16) | (dg << 8) | db;
                C.setRGB(x,y,cC);
            }
        }
        output.send(C);
    }

}
