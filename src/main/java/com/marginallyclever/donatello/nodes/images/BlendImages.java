package com.marginallyclever.donatello.nodes.images;

import com.marginallyclever.donatello.ports.InputImage;
import com.marginallyclever.donatello.ports.InputOneOfMany;
import com.marginallyclever.donatello.ports.OutputImage;
import com.marginallyclever.nodegraphcore.Node;

import java.awt.image.BufferedImage;

/**
 * <a href='https://en.wikipedia.org/wiki/Blend_modes'>blend difference</a>
 * @author Dan Royer
 * @since 2022-02-23
 */
public class BlendImages extends Node {
    private final InputImage a = new InputImage("a");
    private final InputImage b = new InputImage("b");
    private final InputOneOfMany style = new InputOneOfMany("style",0);
    private final OutputImage output = new OutputImage("output");

    /**
     * Constructor for subclasses to call.
     */
    public BlendImages() {
        super("BlendImages");
        style.setOptions(new String[]{"Difference","Multiply","Screen"});
        addVariable(a);
        addVariable(b);
        addVariable(style);
        addVariable(output);
    }

    @Override
    public void update() {
        BufferedImage A = a.getValue();
        BufferedImage B = b.getValue();
        int mode = style.getValue();

        int w = Math.min(A.getWidth(),B.getWidth());
        int h = Math.min(A.getHeight(),B.getHeight());
        BufferedImage C = new BufferedImage(w,h,A.getType());

        // TODO check A and B have the same pixel format?
        int components = A.getColorModel().getNumComponents();
        float [] pa = new float[components];
        float [] pb = new float[components];

        var ar = A.getRaster();
        var br = B.getRaster();
        var cr = C.getRaster();

        for(int y=0;y<h;++y) {
            for(int x=0;x<w;++x) {
                ar.getPixel(x, y, pa);
                br.getPixel(x, y, pb);

                // normalize
                for(int j=0;j<components;++j) {
                    pa[j]/=255f;
                    pb[j]/=255f;
                }

                switch(mode) {
                    case 0: { // difference
                        pa[0] = Math.abs(pa[0] - pb[0]);
                        pa[1] = Math.abs(pa[1] - pb[1]);
                        pa[2] = Math.abs(pa[2] - pb[2]);
                    }
                    break;
                    case 1: { // multiply
                        pa[0] = pa[0] * pb[0];
                        pa[1] = pa[1] * pb[1];
                        pa[2] = pa[2] * pb[2];
                    }
                    break;
                    case 2: { // screen
                        pa[0] = 1.0f - (1.0f - pa[0]) * (1.0f - pb[0]);
                        pa[1] = 1.0f - (1.0f - pa[1]) * (1.0f - pb[1]);
                        pa[2] = 1.0f - (1.0f - pa[2]) * (1.0f - pb[2]);
                    }
                    break;
                }

                for(int j=0;j<components;++j) {
                    pa[j] = (int)Math.max( 0, Math.min( 255.0, pa[j]*255 ) );
                }

                cr.setPixel(x, y, pa);
            }
        }
        output.send(C);
        updateBounds();
    }
}
