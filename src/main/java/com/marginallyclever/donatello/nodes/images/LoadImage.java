package com.marginallyclever.donatello.nodes.images;

import com.marginallyclever.version2.Node;
import com.marginallyclever.version2.Dock;
import com.marginallyclever.version2.Packet;
import com.marginallyclever.version2.nodes.InPort;
import com.marginallyclever.version2.nodes.OutPort;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * This {@link Node} can load a Swing {@link BufferedImage}.
 * @author Dan Royer
 * @since 2022-02-23
 */
@InPort(name="filename",type=String.class)
@OutPort(name="contents",type=BufferedImage.class)
@OutPort(name="width",type=Integer.class)
@OutPort(name="height",type=Integer.class)
public class LoadImage extends Node {
    @Override
    public void update() {
        String filenameValue = (String)getInput("filename").getPacket().data;

        try {
            if(filenameValue!=null && !filenameValue.isEmpty()) {
                File f = new File(filenameValue);
                if (f.exists()) {
                    BufferedImage image = ImageIO.read(f);
                    getOutput("contents").sendPacket(new Packet<>(image));
                    getOutput("width").sendPacket(new Packet<>(image.getWidth()));
                    getOutput("height").sendPacket(new Packet<>(image.getHeight()));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
