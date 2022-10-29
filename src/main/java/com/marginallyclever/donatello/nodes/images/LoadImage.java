package com.marginallyclever.donatello.nodes.images;

import com.marginallyclever.nodegraphcore.*;

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
    private final DockReceiving<String> filename = new DockReceiving<>("filename",String.class,"");
    private final DockShipping<BufferedImage> contents = new DockShipping<>("contents", BufferedImage.class, new BufferedImage(1,1,BufferedImage.TYPE_INT_ARGB));
    private final DockShipping<Number> width = new DockShipping<>("width",Number.class,0);
    private final DockShipping<Number> height = new DockShipping<>("height",Number.class,0);

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
        if(!filename.hasConnection() || !filename.hasPacketWaiting()) return;
        filename.receive();
        try {
            String filenameValue = filename.getValue();
            if(filenameValue!=null && !filenameValue.isEmpty()) {
                File f = new File(filenameValue);
                if (f.exists()) {
                    System.out.println("loading "+filenameValue);
                    BufferedImage image = ImageIO.read(f);
                    contents.send(new Packet<>(image));
                    width.send(new Packet<>(image.getWidth()));
                    height.send(new Packet<>(image.getHeight()));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
