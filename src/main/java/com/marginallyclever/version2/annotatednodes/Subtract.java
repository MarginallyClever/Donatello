package com.marginallyclever.version2.annotatednodes;

import com.marginallyclever.version2.*;

@InPort(name="A",type=Double.class)
@InPort(name="B",type=Double.class)
@OutPort(name="result",type=Double.class)
public class Subtract extends AnnotatedNode {
    public Subtract() {
        super();
    }

    @Override
    public void update() {
        if(!getInput("A").hasPacket()) return;
        if(!getInput("B").hasPacket()) return;

        Packet<Double> a = getInput("A").getPacket();
        Packet<Double> b = getInput("B").getPacket();
        getOutput("result").sendPacket(new Packet(a.data-b.data));
    }
}
