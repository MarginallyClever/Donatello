package com.marginallyclever.donatello.nodes.images;

import com.marginallyclever.donatello.ports.InputFilename;
import com.marginallyclever.donatello.ports.OutputImage;
import com.marginallyclever.donatello.ports.OutputInt;
import com.marginallyclever.nodegraphcore.Node;
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

    private final InputFilename filename = new InputFilename("filename","");
    private final OutputImage contents = new OutputImage("contents");
    private final OutputInt width = new OutputInt("width",0);
    private final OutputInt height = new OutputInt("height",0);

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
            this.updateBounds();
            return;
        }

        try {
            System.out.println("loading "+filenameValue);
            BufferedImage image = ImageIO.read(new File(filenameValue));
            if(image!=null) {
                contents.send(image);
                width.send(image.getWidth());
                height.send(image.getHeight());
                this.updateBounds();
            }
        } catch (Exception e) {
            logger.error("Failed to load image from "+filenameValue,e);
            e.printStackTrace();
        }
    }
}
