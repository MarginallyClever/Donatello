package com.marginallyclever.version2.nodes.nodes.logicaloperators;

import com.marginallyclever.version2.Node;
import com.marginallyclever.version2.Packet;
import com.marginallyclever.version2.nodes.InPort;
import com.marginallyclever.version2.nodes.OutPort;

/**
 * C=(!A)
 * @author Dan Royer
 * @since 2022-02-01
 */

@InPort(name="A",type=Boolean.class)
@OutPort(name="C",type=Boolean.class)
public class LogicalNot extends Node {

    @Override
    public void update() {
        if(!getInput("A").hasPacket()) return;

        boolean av = (boolean)getInput("A").getPacket().data;
        getOutput("C").sendPacket(new Packet(!av));
    }
}
