package com.marginallyclever.donatello.nodes.images;

import com.marginallyclever.nodegraphcore.Node;
import com.marginallyclever.nodegraphcore.NodeVariable;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * This {@link Node} can load a Swing {@link BufferedImage}.
 * @author Dan Royer
 * @since 2022-02-23
 */
public class LoadImage extends Node {
    private final NodeVariable<String> filename = NodeVariable.newInstance("filename",String.class," ",true,false);
    private final NodeVariable<BufferedImage> contents = NodeVariable.newInstance("contents", BufferedImage.class, new BufferedImage(1,1,BufferedImage.TYPE_INT_ARGB),false,true);
    private final NodeVariable<Number> width = NodeVariable.newInstance("width",Number.class,0,false,true);
    private final NodeVariable<Number> height = NodeVariable.newInstance("height",Number.class,0,false,true);

    /**
     * Constructor for subclasses to call.
     */
    public LoadImage() {
        super("LoadImage");
        addVariable(filename);
        addVariable(contents);
        addVariable(width);
        addVariable(height);
    }

    /**
     * Constructor that sets a starting value
     * @param filename the starting value.
     */
    public LoadImage(String filename) {
        this();
        this.filename.setValue(filename);
    }

    @Override
    public Node create() {
        return new LoadImage();
    }

    @Override
    public void update() {
        try {
            String filenameValue = filename.getValue();
            if(filenameValue!=null && !filenameValue.isEmpty()) {
                File f = new File(filenameValue);
                if (f.exists()) {
                    BufferedImage image = ImageIO.read(f);
                    contents.setValue(image);
                    width.setValue(image.getWidth());
                    height.setValue(image.getHeight());
                    cleanAllInputs();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
