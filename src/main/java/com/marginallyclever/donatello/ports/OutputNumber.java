package com.marginallyclever.donatello.ports;

import com.marginallyclever.nodegraphcore.port.Output;

public class OutputNumber extends Output<Number> {
    public OutputNumber(String name) {
        this(name, 0);
    }
    
    public OutputNumber(String name, Number startingValue) throws IllegalArgumentException {
        super(name, Number.class, startingValue);
    }
}
