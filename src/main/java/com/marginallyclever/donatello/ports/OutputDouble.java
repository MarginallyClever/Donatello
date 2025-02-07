package com.marginallyclever.donatello.ports;

import com.marginallyclever.nodegraphcore.port.Output;

public class OutputDouble extends Output<Double> {
    public OutputDouble(String name) {
        this(name, 0d);
    }

    public OutputDouble(String name, Double startingValue) throws IllegalArgumentException {
        super(name, Double.class, startingValue);
    }
}
