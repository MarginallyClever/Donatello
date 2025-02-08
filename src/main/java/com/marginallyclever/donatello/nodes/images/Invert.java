package com.marginallyclever.donatello.nodes.images;

import com.marginallyclever.donatello.ports.InputImage;
import com.marginallyclever.donatello.ports.OutputImage;
import com.marginallyclever.nodegraphcore.Node;

import java.awt.image.BufferedImage;
import java.util.stream.IntStream;

/**
 * Invert the colors in an image.
 */
public class Invert extends Node {
    private final InputImage before = new InputImage("before");
    private final OutputImage after = new OutputImage("after");
    
    public Invert() {
        super("Invert");
        addPort(before);
        addPort(after);
    }
    
    @Override
    public void update() {
        var img = before.getValue();
        if (img == null) {
            after.send(new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB));
            return;
        }

        int width = img.getWidth();
        int height = img.getHeight();
        var dst = new BufferedImage(width, height, img.getType());

        IntStream.range(0,height).parallel().forEach(y->{
            IntStream.range(0,width).parallel().forEach(x->{
                int rgb = img.getRGB(x, y);
                int r = 255 - ((rgb >> 16) & 0xFF);
                int g = 255 - ((rgb >> 8) & 0xFF);
                int b = 255 - (rgb & 0xFF);
                int invertedRGB = (rgb & 0xFF000000) | (r << 16) | (g << 8) | b;
                dst.setRGB(x, y, invertedRGB);
            });
        });

        after.send(dst);
    }
}
