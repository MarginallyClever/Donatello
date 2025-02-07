package com.marginallyclever.donatello.ports;

import com.marginallyclever.nodegraphcore.port.Output;

import java.awt.*;
import java.awt.image.BufferedImage;

public class OutputImage extends Output<BufferedImage> {
    public OutputImage(String _name) throws IllegalArgumentException {
        super(_name, BufferedImage.class, new BufferedImage(1,1,BufferedImage.TYPE_INT_ARGB));
    }

    @Override
    public Rectangle getRectangle() {
        double w = value.getWidth();
        double h = value.getHeight();
        if(w<rectangle.width && h<DEFAULT_HEIGHT) return rectangle;
        double ratio = h/w;
        var newh = (double)rectangle.width * ratio;
        rectangle.setSize(rectangle.width,(int)Math.max(newh,DEFAULT_HEIGHT));

        return super.getRectangle();
    }
}
