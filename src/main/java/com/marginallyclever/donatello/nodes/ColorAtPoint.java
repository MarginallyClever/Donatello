package com.marginallyclever.donatello.nodes;

import com.marginallyclever.version2.Node;
import com.marginallyclever.version2.Dock;
import com.marginallyclever.version2.Packet;
import com.marginallyclever.version2.nodes.InPort;
import com.marginallyclever.version2.nodes.OutPort;

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
@InPort(name="image",type=BufferedImage.class)
@InPort(name="x",type=Number.class)
@InPort(name="y",type=Number.class)
@InPort(name="samplesize",type=Number.class)
@OutPort(name="color",type=Color.class)
public class ColorAtPoint extends Node {
    @Override
    public void update() {
        if(!getInput("image").hasPacket()) return;
        if(!getInput("sampleSize").hasPacket()) return;
        if(!getInput("x").hasPacket()) return;
        if(!getInput("y").hasPacket()) return;

        BufferedImage src = (BufferedImage)getInput("image").getPacket().data;
        int h = src.getHeight();
        int w = src.getWidth();

        int sample = (int)getInput("sampleSize").getPacket().data;
        int sampleSize = 1 + 2 * sample;
        int startX = (int)getInput("s").getPacket().data - sample - 1;
        int startY = (int)getInput("y").getPacket().data - sample - 1;
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
                    sumA += (double)((pixel >> 24) & 0xff);
                    sumR += (double)((pixel >> 16) & 0xff);
                    sumG += (double)((pixel >>  8) & 0xff);
                    sumB += (double)((pixel      ) & 0xff);
                    sumCount++;
                }
            }

            sumA /= sumCount;
            sumR /= sumCount;
            sumG /= sumCount;
            sumB /= sumCount;
            getOutput("color").sendPacket(new Packet(new Color((int)sumA,(int)sumR,(int)sumG,(int)sumB)));
        }
    }
}
