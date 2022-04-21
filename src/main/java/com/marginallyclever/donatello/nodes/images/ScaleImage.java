package com.marginallyclever.donatello.nodes.images;

import com.marginallyclever.version2.Node;
import com.marginallyclever.version2.Dock;
import com.marginallyclever.version2.Packet;
import com.marginallyclever.version2.nodes.InPort;
import com.marginallyclever.version2.nodes.OutPort;

import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;

/**
 * Resize an {@link BufferedImage} to the new desired size
 * @author Dan Royer
 * @since 2022-02-23
 */
@InPort(name="image",type=BufferedImage.class)
@InPort(name="width",type=Integer.class)
@InPort(name="height",type=Integer.class)
@OutPort(name="output",type=BufferedImage.class)
public class ScaleImage extends Node {
    @Override
    public void update() {
        if(!getInput("image").hasPacket()) return;
        if(!getInput("width").hasPacket()) return;
        if(!getInput("height").hasPacket()) return;

        int w = Math.max(1,(int)getInput("width").getPacket().data);
        int h = Math.max(1,(int)getInput("height").getPacket().data);
        BufferedImage input = (BufferedImage)getInput("image").getPacket().data;
        BufferedImage result = new BufferedImage(w,h,input.getType());

        AffineTransform at = new AffineTransform();
        at.scale((double)w/(double)input.getWidth(), (double)h/(double)input.getHeight());
        AffineTransformOp scaleOp = new AffineTransformOp(at, AffineTransformOp.TYPE_BICUBIC);
        scaleOp.filter(input, result);
        getOutput("output").sendPacket(new Packet<>(result));
    }
}
