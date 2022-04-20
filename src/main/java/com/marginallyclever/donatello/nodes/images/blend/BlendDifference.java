package com.marginallyclever.donatello.nodes.images.blend;

import com.marginallyclever.version2.Node;
import com.marginallyclever.version2.Dock;
import com.marginallyclever.version2.Packet;
import com.marginallyclever.version2.nodes.InPort;
import com.marginallyclever.version2.nodes.OutPort;

import java.awt.image.BufferedImage;

/**
 * <a href='https://en.wikipedia.org/wiki/Blend_modes'>blend difference</a>
 * @author Dan Royer
 * @since 2022-02-23
 */
@InPort(name="a",type=BufferedImage.class)
@InPort(name="b",type=BufferedImage.class)
@OutPort(name="output",type=BufferedImage.class)
public class BlendDifference extends Node {
    @Override
    public void update() {
        if(!getInput("a").hasPacket()) return;
        if(!getInput("b").hasPacket()) return;
        BufferedImage A = (BufferedImage)getInput("a").getPacket().data;
        BufferedImage B = (BufferedImage)getInput("b").getPacket().data;

        int w = (int)Math.min(A.getWidth(),B.getWidth());
        int h = (int)Math.min(A.getHeight(),B.getHeight());
        BufferedImage C = new BufferedImage(w,h,A.getType());

        for(int y=0;y<h;++y) {
            for(int x=0;x<w;++x) {
                int cA = A.getRGB(x,y);
                int cB = B.getRGB(x,y);
                int ar = ( cA >> 16 ) & 0xff;
                int ag = ( cA >>  8 ) & 0xff;
                int ab = ( cA       ) & 0xff;
                int br = ( cB >> 16 ) & 0xff;
                int bg = ( cB >>  8 ) & 0xff;
                int bb = ( cB       ) & 0xff;
                int dr = Math.abs(ar - br);
                int dg = Math.abs(ag - bg);
                int db = Math.abs(ab - bb);
                int cC = (dr << 16) | (dg << 8) | db;
                C.setRGB(x,y,cC);
            }
        }
        getOutput("output").sendPacket(new Packet(C));
    }

}
