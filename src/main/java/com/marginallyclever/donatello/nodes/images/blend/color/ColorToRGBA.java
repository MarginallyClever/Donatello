package com.marginallyclever.donatello.nodes.images.blend.color;

import com.marginallyclever.version2.Node;
import com.marginallyclever.version2.Dock;
import com.marginallyclever.version2.Packet;
import com.marginallyclever.version2.nodes.InPort;
import com.marginallyclever.version2.nodes.OutPort;

import java.awt.*;

/**
 * Separates a color into its four component RGBA channels.  Each channel is a value 0...1.
 * @author Dan Royer
 * @since 2022-03-19
 */
@InPort(name="color",type=Color.class)
@OutPort(name="red",type=Number.class)
@OutPort(name="green",type=Number.class)
@OutPort(name="blue",type=Number.class)
@OutPort(name="alpha",type=Number.class)
public class ColorToRGBA extends Node {
    @Override
    public void update() {
        if(!getInput("color").hasPacket()) return;

        Color c = (Color)getInput("color").getPacket().data;
        getOutput("red"  ).sendPacket(new Packet(c.getRed()  /255.0));
        getOutput("green").sendPacket(new Packet(c.getGreen()/255.0));
        getOutput("blue" ).sendPacket(new Packet(c.getBlue() /255.0));
        getOutput("alpha").sendPacket(new Packet(c.getAlpha()/255.0));
    }
}
