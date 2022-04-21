package com.marginallyclever.donatello.nodes.images.blend.color;

import com.marginallyclever.version2.Node;
import com.marginallyclever.version2.Dock;
import com.marginallyclever.version2.Packet;
import com.marginallyclever.version2.nodes.InPort;
import com.marginallyclever.version2.nodes.OutPort;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Loads a {@link Color} into the graph.
 * @author Dan Royer
 * @since 2022-03-19
 */
@InPort(name="red",type=Number.class)
@InPort(name="green",type=Number.class)
@InPort(name="blue",type=Number.class)
@InPort(name="alpha",type=Number.class)
@OutPort(name="color",type=Color.class)
public class LoadColor extends Node {
    @Override
    public void update() {
        if(!getInput("red"  ).hasPacket()) return;
        if(!getInput("green").hasPacket()) return;
        if(!getInput("blue" ).hasPacket()) return;
        if(!getInput("alpha").hasPacket()) return;

        int rr = (int)getInput("red"  ).getPacket().data;
        int gg = (int)getInput("green").getPacket().data;
        int bb = (int)getInput("blue" ).getPacket().data;
        int aa = (int)getInput("alpha").getPacket().data;
        getOutput("color").sendPacket(new Packet<>(new Color(rr,gg,bb,aa)));
    }
}
