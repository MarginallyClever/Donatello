package com.marginallyclever.donatello.ports;

import com.marginallyclever.donatello.SwingProvider;
import com.marginallyclever.donatello.select.Select;
import com.marginallyclever.donatello.select.SelectDouble;
import com.marginallyclever.nodegraphcore.port.Input;

import java.awt.*;

/**
 * {@link InputNumber} exists only so that OutputInteger and OutputDouble can both connect to it.
 * It is problematic because it's the exact same as an InputDouble, but with a different parameter class.
 */
public class InputNumber extends Input<Number> implements SwingProvider {
    private SelectDouble selectDouble;

    public InputNumber(String name) {
        this(name, 0);
    }

    public InputNumber(String name, Number startingValue) throws IllegalArgumentException {
        super(name, Number.class, startingValue);
    }

    @Override
    public Select getSwingComponent(Component parent) {
        if (selectDouble == null) {
            selectDouble = new SelectDouble(name, name, value.doubleValue());
            selectDouble.addSelectListener(evt -> {
                setValue(evt.getNewValue());
            });
        }
        return selectDouble;
    }
}
