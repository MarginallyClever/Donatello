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
                if(image==null) return;

                if(contents.outputHasRoom()) {
                    contents.send(new Packet<>(image));
                }
                if(width.outputHasRoom()) {
                    width.send(new Packet<>(image.getWidth()));
                }
                if(height.outputHasRoom()) {
                    height.send(new Packet<>(image.getHeight()));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        //}
    }

    private BufferedImage captureFrame() {
        BufferedImage image = null;
        try {
            Webcam webcam = Webcam.getDefault();
            webcam.open();
            image = webcam.getImage();
        }
        catch(Exception ignored) {}

        return image;
    }
}
