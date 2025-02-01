package com.marginallyclever.donatello.nodes.images;

import com.marginallyclever.donatello.Filename;
import com.marginallyclever.nodegraphcore.*;
import com.marginallyclever.nodegraphcore.port.Input;
import com.marginallyclever.nodegraphcore.port.Output;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

/**
 * This {@link Node} can load an AWT {@link BufferedImage}.
 * @author Dan Royer
 * @since 2022-02-23
 */
public class LoadImage extends Node {
    private static final Logger logger = LoggerFactory.getLogger(LoadImage.class);

    private final Input<Filename> filename = new Input<>("filename",Filename.class,new Filename(""));
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
        String filenameValue = filename.getValue().get();
        if(filenameValue==null || filenameValue.isEmpty()) {
            contents.send(null);
            width.send(0);
            height.send(0);
            return;
        }

        try {
            System.out.println("loading "+filenameValue);
            BufferedImage image = ImageIO.read(new File(filenameValue));
            if(image!=null) {
                contents.send(image);
                width.send(image.getWidth());
                height.send(image.getHeight());
            }
        } catch (Exception e) {
            logger.error("Failed to load image from "+filenameValue,e);
            e.printStackTrace();
        }
    }
}
