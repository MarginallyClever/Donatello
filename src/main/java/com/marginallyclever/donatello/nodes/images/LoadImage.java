package com.marginallyclever.donatello.nodes.images;

import com.marginallyclever.nodegraphcore.*;
import com.marginallyclever.nodegraphcore.dock.Input;
import com.marginallyclever.nodegraphcore.dock.Output;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

/**
 * This {@link Node} can load a Swing {@link BufferedImage}.
 * @author Dan Royer
 * @since 2022-02-23
 */
public class LoadImage extends Node {
    private String previousFilename = "";
    private final Input<String> filename = new Input<>("filename",String.class,"");
    private final Output<BufferedImage> contents = new Output<>("contents", BufferedImage.class, new BufferedImage(1,1,BufferedImage.TYPE_INT_ARGB));
    private final Output<Number> width = new Output<>("width",Number.class,0);
    private final Output<Number> height = new Output<>("height",Number.class,0);

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

    @Override
    public void update() {
        String filenameValue = filename.getValue();
        if(filenameValue!=null && !filenameValue.isEmpty() && !filenameValue.equals(previousFilename)) {
            try {
                previousFilename = filenameValue;
                System.out.println("loading "+filenameValue);
                BufferedImage image = ImageIO.read(new File(filenameValue));
                if(image!=null) {
                    contents.send(image);
                    width.send(image.getWidth());
                    height.send(image.getHeight());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
