package com.marginallyclever.donatello.ports;

import com.marginallyclever.donatello.SwingProvider;
import com.marginallyclever.donatello.graphview.GraphViewPanel;
import com.marginallyclever.donatello.graphview.GraphViewProvider;
import com.marginallyclever.donatello.select.Select;
import com.marginallyclever.donatello.select.SelectOneOfMany;

import java.awt.*;

public class InputOneOfMany extends InputInt implements SwingProvider, GraphViewProvider {
    private SelectOneOfMany selectOneOfMany;
    private String [] options;

    public InputOneOfMany(String name) {
        this(name,0);
    }

    public InputOneOfMany(String name, Integer startingValue) throws IllegalArgumentException {
        super(name, startingValue);
    }

    @Override
    public Select getSwingComponent(Component parent) {
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

    @Override
    public void paint(Graphics g, Rectangle box) {
        String val = options[getValue()];
        if (val.length() > GraphViewPanel.MAX_CHARS) val = val.substring(0, GraphViewPanel.MAX_CHARS) + "...";
        GraphViewPanel.paintText(g, val, box, GraphViewPanel.ALIGN_RIGHT, GraphViewPanel.ALIGN_TOP);
    }
}
