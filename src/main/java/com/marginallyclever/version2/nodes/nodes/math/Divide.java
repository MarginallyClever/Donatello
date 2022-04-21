package com.marginallyclever.version2.nodes.nodes.math;

import com.marginallyclever.version2.*;
import com.marginallyclever.version2.nodes.InPort;
import com.marginallyclever.version2.nodes.OutPort;

@InPort(name="A",type=Number.class)
@InPort(name="B",type=Number.class)
@OutPort(name="result",type=Number.class)
public class Divide extends Node {
    private static final long serialVersionUID = 4424796154614276674L;

    public Divide() {
        super();
    }

    @Override
    public void update() {
        if(!getInput("A").hasPacket()) return;
        if(!getInput("B").hasPacket()) return;

        Double a = (Double)getInput("A").getPacket().data;
        Double b = (Double)getInput("B").getPacket().data;
        getOutput("result").sendPacket(new Packet<>(a/b));
    }
}
