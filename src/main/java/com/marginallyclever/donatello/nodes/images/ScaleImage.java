package com.marginallyclever.donatello.nodes.images;

import com.marginallyclever.nodegraphcore.Node;
import com.marginallyclever.nodegraphcore.NodeVariable;

import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;

/**
 * Resize an {@link BufferedImage} to the new desired size
 * @author Dan Royer
 * @since 2022-02-23
 */
public class ScaleImage extends Node {
    private final NodeVariable<BufferedImage> image = NodeVariable.newInstance("image", BufferedImage.class,new BufferedImage(1,1,BufferedImage.TYPE_INT_ARGB),true,false);
    private final NodeVariable<Number> width = NodeVariable.newInstance("width",Number.class,256,true,false);
    private final NodeVariable<Number> height = NodeVariable.newInstance("height",Number.class,256,true,false);
    private final NodeVariable<BufferedImage> output = NodeVariable.newInstance("output", BufferedImage.class,new BufferedImage(1,1,BufferedImage.TYPE_INT_ARGB),false,true);

    /**
     * Constructor for subclasses to call.
     */
    public ScaleImage() {
        super("ScaleImage");
        addVariable(image);
        addVariable(width);
        addVariable(height);
        addVariable(output);
    }

    @Override
    public void update() {
        int w = Math.max(1,width.getValue().intValue());
        int h = Math.max(1,height.getValue().intValue());
        BufferedImage input = image.getValue();
        BufferedImage result = new BufferedImage(w,h,input.getType());

        AffineTransform at = new AffineTransform();
        at.scale((double)w/(double)input.getWidth(), (double)h/(double)input.getHeight());
        AffineTransformOp scaleOp = new AffineTransformOp(at, AffineTransformOp.TYPE_BICUBIC);
        scaleOp.filter(input, result);
        output.setValue(result);

        cleanAllInputs();
    }
}
