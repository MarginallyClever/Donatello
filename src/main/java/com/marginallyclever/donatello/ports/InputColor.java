package com.marginallyclever.donatello.ports;

import com.marginallyclever.donatello.SwingProvider;
import com.marginallyclever.donatello.graphview.GraphViewProvider;
import com.marginallyclever.donatello.select.Select;
import com.marginallyclever.donatello.select.SelectColor;
import com.marginallyclever.nodegraphcore.port.Input;

import java.awt.*;

public class InputColor extends Input<Color> implements SwingProvider, GraphViewProvider {
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

    @Override
    public void paint(Graphics g, Rectangle box) {
        int w = (int)box.getWidth();
        int h = (int)box.getHeight();
        int x = (int)box.getX();
        int y = (int)box.getY();
        Color prev = g.getColor();
        g.setColor(getValue());
        g.fillRect(x, y, w, h);
        g.setColor(prev);
    }
}
