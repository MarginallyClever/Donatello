package com.marginallyclever.donatello.nodes.images;

import com.marginallyclever.version2.Node;
import com.marginallyclever.version2.Dock;

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
    private final Dock<String> filename = Dock.newInstance("filename",String.class," ",true,false);
    private final Dock<BufferedImage> contents = Dock.newInstance("contents", BufferedImage.class, new BufferedImage(1,1,BufferedImage.TYPE_INT_ARGB),false,true);
    private final Dock<Number> width = Dock.newInstance("width",Number.class,0,false,true);
    private final Dock<Number> height = Dock.newInstance("height",Number.class,0,false,true);

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
