package com.marginallyclever.donatello.ports;

import com.marginallyclever.nodegraphcore.port.Input;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * An input port that accepts a {@link BufferedImage}.
 */
public class InputImage extends Input<BufferedImage> {
    public InputImage(String name) throws IllegalArgumentException {
        super(name, BufferedImage.class, new BufferedImage(1,1,BufferedImage.TYPE_INT_ARGB));
    }

    @Override
    public Rectangle getRectangle() {
        rectangle.setSize(DEFAULT_WIDTH,DEFAULT_HEIGHT);
        return super.getRectangle();
    }
}
