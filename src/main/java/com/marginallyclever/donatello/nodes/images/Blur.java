package com.marginallyclever.donatello.nodes.images;

import com.marginallyclever.donatello.ports.InputImage;
import com.marginallyclever.donatello.ports.InputInt;
import com.marginallyclever.donatello.ports.OutputImage;
import com.marginallyclever.nodegraphcore.Node;

import java.awt.image.BufferedImage;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;

public class Blur extends Node {
    private final InputImage src = new InputImage("source");
    private final InputInt radiusAmount = new InputInt("radius",1);
    private final OutputImage dst = new OutputImage("result");
    
    public Blur() {
        super("Blur");
        addPort(src);
        addPort(radiusAmount);
        addPort(dst);
    }
    
    @Override
    public void update() {
        BufferedImage iSrc = src.getValue();
        var radius = radiusAmount.getValue();
        BufferedImage result = filter(iSrc,radius);
        dst.send(result);
    }

    public BufferedImage filter(BufferedImage src, int radius) {
        BufferedImage after = new BufferedImage(src.getWidth(), src.getHeight(), BufferedImage.TYPE_INT_ARGB);
        BufferedImage inter = new BufferedImage(src.getWidth(), src.getHeight(), src.getType());
        getGaussianBlurFilter(true,radius).filter(src,inter);
        getGaussianBlurFilter(false,radius).filter(inter,after);
        return after;
    }

    public ConvolveOp getGaussianBlurFilter(boolean horizontal,int radius) {
        int size = radius * 2 + 1;
        float[] data = new float[size];

        // sigma here is the lowercase σ, not the uppercase Σ.
        float sigma = radius / 3.0f;
        float twoSigmaSquare = 2.0f * sigma * sigma;
        float sigmaRoot = (float) Math.sqrt(twoSigmaSquare * Math.PI);
        float total = 0.0f;

        for (int i = -radius; i <= radius; i++) {
            float distance = i * i;
            int index = i + radius;
            data[index] = (float) Math.exp(-distance / twoSigmaSquare) / sigmaRoot;
            total += data[index];
        }

        for (int i = 0; i < data.length; i++) {
            data[i] /= total;
        }

        Kernel kernel = null;
        if (horizontal) {
            kernel = new Kernel(size, 1, data);
        } else {
            kernel = new Kernel(1, size, data);
        }
        return new ConvolveOp(kernel, ConvolveOp.EDGE_NO_OP, null);
    }
}
