package com.marginallyclever.donatello.nodes.images.blend.color;

import com.marginallyclever.donatello.nodes.images.ColorHelper;
import com.marginallyclever.version2.Node;
import com.marginallyclever.version2.Dock;
import com.marginallyclever.version2.Packet;
import com.marginallyclever.version2.nodes.InPort;
import com.marginallyclever.version2.nodes.OutPort;

import java.awt.*;

/**
 * Separates a color into its four component CMYK channels.  Each channel is a value 0...1.
 * @author Dan Royer
 * @since 2022-03-19
 */
@InPort(name="color",type=Color.class)
@OutPort(name="cyan",type=Number.class)
@OutPort(name="magenta",type=Number.class)
@OutPort(name="yellow",type=Number.class)
@OutPort(name="black",type=Number.class)
public class ColorToCMYK extends Node {
    @Override
    public void update() {
        if(!getInput("color").hasPacket()) return;

        Color c = (Color)getInput("color").getPacket().data;
        double [] cmyk = ColorHelper.IntToCMYK(ColorHelper.ColorToInt(c));
        getOutput("cyan").sendPacket(new Packet<>(   cmyk[0]/255.0));
        getOutput("magenta").sendPacket(new Packet<>(cmyk[1]/255.0));
        getOutput("yellow").sendPacket(new Packet<>( cmyk[2]/255.0));
        getOutput("black").sendPacket(new Packet<>(  cmyk[3]/255.0));
    }
}
