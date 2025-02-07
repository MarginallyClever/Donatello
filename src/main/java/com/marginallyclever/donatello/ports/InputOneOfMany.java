package com.marginallyclever.donatello.ports;

import com.marginallyclever.donatello.SwingProvider;
import com.marginallyclever.donatello.select.SelectOneOfMany;

import java.awt.*;

public class InputOneOfMany extends InputInt implements SwingProvider {
    private SelectOneOfMany selectOneOfMany;
    private String [] options;

    public InputOneOfMany(String name) {
        this(name,0);
    }

    public InputOneOfMany(String name, Integer startingValue) throws IllegalArgumentException {
        super(name, startingValue);
    }

    @Override
    public Component getSwingComponent(Component parent) {
        if(selectOneOfMany == null) {
            selectOneOfMany = new SelectOneOfMany(name,name);
            if(options!=null) setOptions(options);
            selectOneOfMany.addSelectListener(evt-> setValue(evt.getNewValue()) );
        }
        return selectOneOfMany;
    }

    public void setOptions(String[] options) {
        this.options = options;
        if(selectOneOfMany!=null) {
            selectOneOfMany.setNewList(options);
            selectOneOfMany.setSelectedIndex(getValue());
        }
    }

    @Override
    public void setValue(Object arg0) {
        super.setValue(arg0);
        if(selectOneOfMany!=null) {
            selectOneOfMany.setSelectedIndex(getValue());
        }
    }
}
