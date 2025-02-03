package com.marginallyclever.donatello.ports;

import com.marginallyclever.nodegraphcore.port.Output;

import java.awt.*;

public class OutputColor extends Output<Color> {
    public OutputColor(String _name, Color startingValue) throws IllegalArgumentException {
        super(_name, Color.class, startingValue);
    }
}
