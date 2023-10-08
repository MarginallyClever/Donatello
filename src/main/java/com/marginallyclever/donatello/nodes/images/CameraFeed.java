package com.marginallyclever.donatello.nodes.images;

import com.marginallyclever.nodegraphcore.DockReceiving;
import com.marginallyclever.nodegraphcore.DockShipping;
import com.marginallyclever.nodegraphcore.Node;
import com.marginallyclever.nodegraphcore.Packet;
import com.github.sarxos.webcam.Webcam;

import java.awt.image.BufferedImage;

/**
 * This {@link Node} can load a Swing {@link BufferedImage}.
 * @author Dan Royer
 * @since 2022-02-23
 */
public class CameraFeed extends Node {
    private final DockReceiving<String> filename = new DockReceiving<>("src",String.class,"");
    private final DockShipping<BufferedImage> contents = new DockShipping<>("contents", BufferedImage.class, new BufferedImage(1,1,BufferedImage.TYPE_INT_ARGB));
    private final DockShipping<Number> width = new DockShipping<>("width",Number.class,0);
    private final DockShipping<Number> height = new DockShipping<>("height",Number.class,0);

    /**
     * Constructor for subclasses to call.
     */
    public CameraFeed() {
        super("CameraFeed");
        addVariable(filename);
        addVariable(contents);
        addVariable(width);
        addVariable(height);
    }

    @Override
    public void update() {
        //if(filename.hasPacketWaiting()) filename.receive();

        //String filenameValue = filename.getValue();
        //if(filenameValue!=null && !filenameValue.isEmpty()) {
            try {
                //System.out.println("loading "+filenameValue);
                BufferedImage image = captureFrame();
                if(contents.outputHasRoom()) {
                    contents.send(new Packet<>(image));
                }
                width.send(new Packet<>(image.getWidth()));
                height.send(new Packet<>(image.getHeight()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        //}
    }

    private BufferedImage captureFrame() {
        // get default webcam and open it
        Webcam webcam = Webcam.getDefault();
        webcam.open();

        // get image
        BufferedImage image = webcam.getImage();

        return image;
    }
}
