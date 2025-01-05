package com.marginallyclever.donatello.nodes.images;

import com.marginallyclever.nodegraphcore.dock.Input;
import com.marginallyclever.nodegraphcore.dock.Output;
import com.marginallyclever.nodegraphcore.Node;
import com.github.sarxos.webcam.Webcam;

import java.awt.image.BufferedImage;

/**
 * This {@link Node} can load a Swing {@link BufferedImage}.
 * @author Dan Royer
 * @since 2022-02-23
 */
public class CameraFeed extends Node {
    private final Input<String> filename = new Input<>("src",String.class,"");
    private final Output<BufferedImage> contents = new Output<>("contents", BufferedImage.class, new BufferedImage(1,1,BufferedImage.TYPE_INT_ARGB));
    private final Output<Number> width = new Output<>("width",Number.class,0);
    private final Output<Number> height = new Output<>("height",Number.class,0);

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

                contents.send(image);
                width.send(image.getWidth());
                height.send(image.getHeight());
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
