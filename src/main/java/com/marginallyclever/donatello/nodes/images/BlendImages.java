package com.marginallyclever.donatello.nodes.images;

import com.marginallyclever.donatello.ports.InputImage;
import com.marginallyclever.donatello.ports.InputOneOfMany;
import com.marginallyclever.donatello.ports.OutputImage;
import com.marginallyclever.nodegraphcore.Node;

import java.awt.image.BufferedImage;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

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
        style.setOptions(new String[]{"Difference","Multiply","Screen","Add","Subtract"});
        addPort(a);
        addPort(b);
        addPort(style);
        addPort(output);
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

        var ar = A.getRaster();
        var br = B.getRaster();
        var cr = C.getRaster();

        setComplete(0);
        int total = w*h;
        AtomicInteger count = new AtomicInteger();

        IntStream.range(0,h).parallel().forEach(y -> {
            float [] pa = new float[components];
            float [] pb = new float[components];
            for(int x=0;x<w;++x) {
                ar.getPixel(x, y, pa);
                br.getPixel(x, y, pb);

                // normalize
                for(int j=0;j<components;++j) {
                    pa[j]/=255f;
                    pb[j]/=255f;
                }

                switch(mode) {
                    case 0:  difference(pa,pb);  break;
                    case 2:  screen(pa,pb);  break;
                    case 3:  add(pa,pb);  break;
                    case 4:  subtract(pa,pb);  break;
                    default:  multiply(pa,pb);  break;
                }

                for(int j=0;j<components;++j) {
                    pa[j] = (int)Math.max( 0, Math.min( 255.0, pa[j]*255 ) );
                }

                cr.setPixel(x, y, pa);
            }
            setComplete((int)(100.0 * count.addAndGet(w) /total));
        });
        setComplete(100);
        output.setValue(C);
    }

    private void difference(float [] pa, float [] pb) {
        pa[0] = Math.abs(pa[0] - pb[0]);
        pa[1] = Math.abs(pa[1] - pb[1]);
        pa[2] = Math.abs(pa[2] - pb[2]);
    }

    private void multiply(float [] pa, float [] pb) {
        pa[0] = pa[0] * pb[0];
        pa[1] = pa[1] * pb[1];
        pa[2] = pa[2] * pb[2];
    }

    private void screen(float [] pa, float [] pb) {
        pa[0] = 255 - (255 - pa[0]) * (255 - pb[0]);
        pa[1] = 255 - (255 - pa[1]) * (255 - pb[1]);
        pa[2] = 255 - (255 - pa[2]) * (255 - pb[2]);
    }

    private void add(float [] pa, float [] pb) {
        pa[0] = Math.min(255, pa[0] + pb[0]);
        pa[1] = Math.min(255, pa[1] + pb[1]);
        pa[2] = Math.min(255, pa[2] + pb[2]);
    }

    private void subtract(float [] pa, float [] pb) {
        pa[0] = Math.max(0, pa[0] - pb[0]);
        pa[1] = Math.max(0, pa[1] - pb[1]);
        pa[2] = Math.max(0, pa[2] - pb[2]);
    }
}
