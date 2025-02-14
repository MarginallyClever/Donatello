package com.marginallyclever.donatello.ports;

import com.marginallyclever.donatello.SwingProvider;
import com.marginallyclever.donatello.select.Select;
import com.marginallyclever.donatello.select.SelectColor;
import com.marginallyclever.nodegraphcore.port.Input;

import java.awt.*;

public class InputColor extends Input<Color> implements SwingProvider {
    private SelectColor selectColor;

    public InputColor(String name, Color startingValue) throws IllegalArgumentException {
        super(name, Color.class, startingValue);
    }

    @Override
    public Select getSwingComponent(Component parent) {
        if(selectColor==null) {
            selectColor = new SelectColor(name,name,value,parent);
            selectColor.addSelectListener( evt -> {
                setValue(evt.getNewValue());
            });
        }
        return selectColor;
    }
}
