package com.marginallyclever.donatello.ports;

import com.marginallyclever.donatello.SwingProvider;
import com.marginallyclever.donatello.select.Select;
import com.marginallyclever.donatello.select.SelectSlider;
import com.marginallyclever.nodegraphcore.port.Input;

import java.awt.*;

/**
 * An input port that allows the user to select a range of values using a slider.
 */
public class InputRange extends Input<Integer> implements SwingProvider {
    private SelectSlider selectSlider;
    private int top, bottom;

    public InputRange(String name) {
        this(name,0);
    }

    public InputRange(String name, Integer startingValue) throws IllegalArgumentException {
        this(name, startingValue,100,0);
    }

    public InputRange(String name, Integer startingValue, int top, int bottom) throws IllegalArgumentException {
        super(name, Integer.class, startingValue);
        this.top = top;
        this.bottom = bottom;
    }

    @Override
    public Select getSwingComponent(Component parent) {
        if(selectSlider==null) {
            selectSlider = new SelectSlider(name,name,top,bottom,value);
            selectSlider.addSelectListener( evt -> setValue(evt.getNewValue()) );
        }
        return selectSlider;
    }

    public int getTop() {
        return top;
    }

    public int getBottom() {
        return bottom;
    }

    public void setTop(int top) {
        this.top = top;
        if(selectSlider!=null) {
            selectSlider.setTop(top);
        }
    }

    public void setBottom(int bottom) {
        this.bottom = bottom;
        if(selectSlider!=null) {
            selectSlider.setBottom(bottom);
        }
    }
}
