package com.marginallyclever.donatello.nodes.images;

import com.marginallyclever.donatello.ports.InputImage;
import com.marginallyclever.donatello.ports.InputInt;
import com.marginallyclever.donatello.ports.OutputImage;
import com.marginallyclever.nodegraphcore.Node;

import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;

/**
 * Resize an {@link BufferedImage} to the new desired size
 * @author Dan Royer
 * @since 2022-02-23
 */
public class ScaleImage extends Node {
    private final InputImage image = new InputImage("image");
    private final InputInt width = new InputInt("width",1);
    private final InputInt height = new InputInt("height",1);
    private final OutputImage output = new OutputImage("output");

    /**
     * Constructor for subclasses to call.
     */
    public ScaleImage() {
        super("ScaleImage");
        addPort(image);
        addPort(width);
        addPort(height);
        addPort(output);
    }

    @Override
    public void update() {
        int w = Math.max(1,width.getValue());
        int h = Math.max(1,height.getValue());

        BufferedImage input = image.getValue();
        BufferedImage result = new BufferedImage(w,h,input.getType());

        AffineTransform at = new AffineTransform();
        at.scale((double)w/(double)input.getWidth(), (double)h/(double)input.getHeight());
        AffineTransformOp scaleOp = new AffineTransformOp(at, AffineTransformOp.TYPE_BICUBIC);
        scaleOp.filter(input, result);
        output.setValue(result);
    }
}
