package com.marginallyclever.donatello.ports;

import com.marginallyclever.donatello.graphview.GraphViewProvider;
import com.marginallyclever.nodegraphcore.port.Output;

import java.awt.*;
import java.awt.image.BufferedImage;

public class OutputImage extends Output<BufferedImage> implements GraphViewProvider {
    public OutputImage(String _name) throws IllegalArgumentException {
        super(_name, BufferedImage.class, new BufferedImage(1,1,BufferedImage.TYPE_INT_ARGB));
    }

    @Override
    public Rectangle getRectangle() {
        double w = value.getWidth();
        double h = value.getHeight();
        if(w<rectangle.width && h<DEFAULT_HEIGHT) {
            rectangle.setSize(DEFAULT_WIDTH,DEFAULT_HEIGHT);
            return rectangle;
        }
        double ratio = h/w;
        var newHeight = (double)rectangle.width * ratio;
        rectangle.setSize(rectangle.width,(int)Math.max(newHeight,DEFAULT_HEIGHT));

        return rectangle;
    }

    @Override
    public void paint(Graphics g, Rectangle box) {
        var img = value;
        if(img==null) return;
        int w = img.getWidth();
        int h = img.getHeight();
        int maxW = (int)box.getWidth();
        if (w > maxW) {
            h = h * maxW / w;
            w = maxW;
        }
        int x = (int)box.getX();
        int y = (int)box.getY();
        g.drawImage(img, x, y, w, h, null);
    }
}
