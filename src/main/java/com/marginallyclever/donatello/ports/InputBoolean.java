package com.marginallyclever.donatello.ports;

import com.marginallyclever.donatello.SwingProvider;
import com.marginallyclever.donatello.select.Select;
import com.marginallyclever.donatello.select.SelectBoolean;
import com.marginallyclever.nodegraphcore.port.Input;

import java.awt.*;

public class InputBoolean extends Input<Boolean> implements SwingProvider {
    private SelectBoolean selectBoolean;

    public InputBoolean(String name, Boolean startingValue) throws IllegalArgumentException {
        super(name, Boolean.class, startingValue);
    }

    @Override
    public Select getSwingComponent(Component parent) {
        if(selectBoolean==null) {
            selectBoolean = new SelectBoolean(name,name,value);
            selectBoolean.addSelectListener( evt -> {
                setValue(evt.getNewValue());
            });
        }
        return selectBoolean;
    }
}
