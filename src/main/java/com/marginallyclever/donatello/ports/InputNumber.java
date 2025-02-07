package com.marginallyclever.donatello.ports;

import com.marginallyclever.nodegraphcore.port.Input;

public class InputNumber extends Input<Number> {
    public InputNumber(String name) {
        this(name, 0);
    }

    public InputNumber(String name, Number startingValue) throws IllegalArgumentException {
        super(name, Number.class, startingValue);
    }
}
