package com.marginallyclever.donatello.nodes.images;

import com.marginallyclever.donatello.ports.InputFilename;
import com.marginallyclever.donatello.ports.OutputImage;
import com.marginallyclever.donatello.ports.OutputInt;
import com.marginallyclever.nodegraphcore.Node;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
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
        addPort(filename);
        addPort(contents);
        addPort(width);
        addPort(height);

        var ff = new FileNameExtensionFilter("Images",ImageIO.getReaderFileSuffixes());

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fileChooser.setAcceptAllFileFilterUsed(false);
        fileChooser.setFileFilter(ff);
        filename.setFileChooser(fileChooser);
    }

    @Override
    public void update() {
        String filenameValue = filename.getValue().get();
        if(filenameValue==null || filenameValue.isEmpty()) {
            contents.setValue(null);
            width.setValue(0);
            height.setValue(0);
            return;
        }

        try {
            System.out.println("loading "+filenameValue);
            BufferedImage image = ImageIO.read(new File(filenameValue));
            if(image!=null) {
                contents.setValue(image);
                width.setValue(image.getWidth());
                height.setValue(image.getHeight());
            }
        } catch (Exception e) {
            logger.error("Failed to load image from "+filenameValue,e);
        }
    }
}
