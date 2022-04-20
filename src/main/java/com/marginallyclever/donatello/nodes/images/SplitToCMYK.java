package com.marginallyclever.donatello.nodes.images;

import com.marginallyclever.version2.Node;
import com.marginallyclever.version2.Dock;
import com.marginallyclever.version2.Packet;
import com.marginallyclever.version2.nodes.InPort;
import com.marginallyclever.version2.nodes.OutPort;

import java.awt.image.BufferedImage;

/**
 * Split a {@link BufferedImage} into separate CMYK channels.
 * @author Dan Royer
 * @since 2022-02-23
 */
@InPort(name="image",type=BufferedImage.class)
@OutPort(name="Cyan",   type=BufferedImage.class)
@OutPort(name="Magenta",type=BufferedImage.class)
@OutPort(name="Yellow", type=BufferedImage.class)
@OutPort(name="Black",  type=BufferedImage.class)
public class SplitToCMYK extends Node {
    @Override
    public void update() {
        if(!getInput("image").hasPacket()) return;

        BufferedImage src = (BufferedImage)getInput("image").getPacket().data;
        int h = src.getHeight();
        int w = src.getWidth();

        BufferedImage channelCyan    = new BufferedImage(w,h,src.getType());
        BufferedImage channelMagenta = new BufferedImage(w,h,src.getType());
        BufferedImage channelYellow  = new BufferedImage(w,h,src.getType());
        BufferedImage channelBlack   = new BufferedImage(w,h,src.getType());

        for (int py = 0; py < h; ++py) {
            for (int px = 0; px < w; ++px) {
                int pixel = src.getRGB(px,py);
                double [] cmyk = ColorHelper.IntToCMYK(pixel);
                int c=(int)cmyk[0];
                int m=(int)cmyk[1];
                int y=(int)cmyk[2];
                int k=(int)cmyk[3];
                channelCyan   .setRGB(px, py, ColorHelper.RGBToInt(c,c,c,c));
                channelMagenta.setRGB(px, py, ColorHelper.RGBToInt(m,m,m,m));
                channelYellow .setRGB(px, py, ColorHelper.RGBToInt(y,y,y,y));
                channelBlack  .setRGB(px, py, ColorHelper.RGBToInt(k,k,k,k));
            }
        }

        getOutput("cyan").sendPacket(new Packet(channelCyan));
        getOutput("magenta").sendPacket(new Packet(channelMagenta));
        getOutput("yellow").sendPacket(new Packet(channelYellow));
        getOutput("black").sendPacket(new Packet(channelBlack));
    }
}
