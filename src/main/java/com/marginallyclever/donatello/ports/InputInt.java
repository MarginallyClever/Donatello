package com.marginallyclever.donatello.ports;

import com.marginallyclever.donatello.SwingProvider;
import com.marginallyclever.donatello.select.Select;
import com.marginallyclever.donatello.select.SelectInteger;
import com.marginallyclever.nodegraphcore.port.Input;

import java.awt.*;

public class InputInt extends Input<Integer> implements SwingProvider {
    private SelectInteger selectInteger;

    public InputInt(String name) {
        this(name, 0);
    }

    public InputInt(String name, Integer startingValue) throws IllegalArgumentException {
        super(name, Integer.class, startingValue);
    }

    @Override
    public Select getSwingComponent(Component parent) {
        if(selectInteger==null) {
            selectInteger = new SelectInteger(name,name,value);
            selectInteger.addSelectListener( evt -> setValue(evt.getNewValue()) );
        }
        return selectInteger;
    }

    @Override
    public boolean isValidType(Object arg0) {
        if(arg0 instanceof Number) return true;
        return super.isValidType(arg0);
    }

    @Override
    public void setValue(Object arg0) {
        if(arg0 instanceof Number) {
            super.setValue(((Number)arg0).intValue());
            return;
        }
        super.setValue(arg0);
    }
}
