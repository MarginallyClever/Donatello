package com.marginallyclever.donatello.nodes.images;

import com.marginallyclever.donatello.ports.InputImage;
import com.marginallyclever.donatello.ports.InputInt;
import com.marginallyclever.donatello.ports.OutputColor;
import com.marginallyclever.nodegraphcore.Node;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Calculates the color of a {@link BufferedImage} at a given point.  Does nothing if the requested point is out of bounds.
 * The sampling area is a square <code>1+samplesize</code> pixels on each side.<br/>
 *
 * The sampling is evenly weighted - that is to say the
 * <a href='https://en.wikipedia.org/wiki/Kernel_(image_processing)#Convolution'>convolution matrix</a> is all 1s.
 * @author Dan Royer
 * @since 2022-02-23
 */
public class ColorAtPoint extends Node {
    private final InputImage image = new InputImage("image");
    private final InputInt cx = new InputInt("x", 0);
    private final InputInt cy = new InputInt("y", 0);
    private final InputInt sampleSize = new InputInt("sampleSize", 0);
    private final OutputColor output = new OutputColor("output", Color.BLACK);

    /**
     * Constructor for subclasses to call.
     */
    public ColorAtPoint() {
        super("ColorAtPoint");
        addPort(image);
        addPort(cx);
        addPort(cy);
        addPort(sampleSize);
        addPort(output);
    }

    @Override
    public void update() {
        BufferedImage src = image.getValue();
        int h = src.getHeight();
        int w = src.getWidth();

        int sample = sampleSize.getValue().intValue();
        int sampleSize = 1 + 2 * sample;
        int startX = cx.getValue().intValue() - sample - 1;
        int startY = cy.getValue().intValue() - sample - 1;
        int endX = startX + sampleSize;
        int endY = startY + sampleSize;
        startX = Math.max(startX,0);
        startY = Math.max(startY,0);
        endX = Math.min(endX,w);
        endY = Math.min(endY,h);

        if(startX!=endX && startY!=endY) {
            int sumCount=0;
            double sumA=0;
            double sumR=0;
            double sumG=0;
            double sumB=0;

            for (int y = startY; y < endY; ++y) {
                for (int x = startX; x < endX; ++x) {
                    int pixel = src.getRGB(x,y);
                    sumA += ((pixel >> 24) & 0xff);
                    sumR += ((pixel >> 16) & 0xff);
                    sumG += ((pixel >>  8) & 0xff);
                    sumB += ((pixel      ) & 0xff);
                    sumCount++;
                }
            }

            sumA /= sumCount;
            sumR /= sumCount;
            sumG /= sumCount;
            sumB /= sumCount;
            output.setValue(new Color((int)sumR, (int)sumG, (int)sumB, (int)sumA));
        }
    }
}
