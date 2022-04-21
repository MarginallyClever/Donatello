package com.marginallyclever.version2.nodes.nodes;

import com.marginallyclever.version2.Node;
import com.marginallyclever.version2.nodes.InPort;

/**
 * sends A as a string to <pre>System.out.println()</pre>.
 * @author Dan Royer
 * @since 2022-02-01
 */
@InPort(name ="input",type = Object.class)
public class PrintToStdOut extends Node {
    @Override
    public void update() {
        if(!getInput("input").hasPacket()) return;

        Object var = getInput("input").getPacket().data;
        String output = (var!=null) ? var.toString() : "null";
        System.out.println(getUniqueID()+": "+output);
    }
}
