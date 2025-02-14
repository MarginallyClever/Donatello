package com.marginallyclever.donatello.ports;

import com.marginallyclever.donatello.SwingProvider;
import com.marginallyclever.donatello.select.Select;
import com.marginallyclever.donatello.select.SelectTextField;
import com.marginallyclever.nodegraphcore.port.Input;

import java.awt.*;

public class InputString extends Input<String> implements SwingProvider {
    private SelectTextField selectTextField;

    public InputString(String name, String startingValue) throws IllegalArgumentException {
        super(name, String.class, startingValue);
    }

    @Override
    public Select getSwingComponent(Component parent) {
        if(selectTextField==null) {
            selectTextField = new SelectTextField(name,name,value);
            selectTextField.addSelectListener( evt -> {
                setValue(evt.getNewValue());
            });
        }
        return selectTextField;
    }
}
