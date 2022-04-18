package com.marginallyclever.version2.nodes.annotatednodes;

import com.marginallyclever.version2.*;
import com.marginallyclever.version2.nodes.AbstractAnnotatedNode;
import com.marginallyclever.version2.nodes.InPort;
import com.marginallyclever.version2.nodes.OutPort;

@InPort(name="A",type=Number.class)
@InPort(name="B",type=Number.class)
@OutPort(name="result",type=Number.class)
public class Subtract extends AbstractAnnotatedNode {
    private static final long serialVersionUID = 3630806965554256225L;

    public Subtract() {
        super();
    }

    @Override
    public void update() {
        if(!getInput("A").hasPacket()) return;
        if(!getInput("B").hasPacket()) return;

        Double a = (Double)getInput("A").getPacket().data;
        Double b = (Double)getInput("B").getPacket().data;
        getOutput("result").sendPacket(new Packet<>(a-b));
    }
}
