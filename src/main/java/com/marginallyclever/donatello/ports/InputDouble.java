package com.marginallyclever.donatello.ports;

import com.marginallyclever.donatello.SwingProvider;
import com.marginallyclever.donatello.select.Select;
import com.marginallyclever.donatello.select.SelectDouble;
import com.marginallyclever.nodegraphcore.port.Input;

import java.awt.*;

public class InputDouble extends Input<Double> implements SwingProvider {
    private SelectDouble selectDouble;

    public InputDouble(String name) throws IllegalArgumentException {
        super(name, Double.class, 0.0);
    }

    public InputDouble(String name, Double startingValue) throws IllegalArgumentException {
        super(name, Double.class, startingValue);
    }

    @Override
    public Select getSwingComponent(Component parent) {
        if (selectDouble == null) {
            selectDouble = new SelectDouble(name, name, value);
            selectDouble.addSelectListener(evt -> {
                setValue(evt.getNewValue());
            });
        }
        return selectDouble;
    }

    @Override
    public boolean isValidType(Object arg0) {
        if(arg0 instanceof Number) return true;
        return super.isValidType(arg0);
    }

    @Override
    public void setValue(Object arg0) {
        if(arg0 instanceof Number) {
            super.setValue(((Number)arg0).doubleValue());
            return;
        }
        super.setValue(arg0);
    }
}
