package com.marginallyclever.donatello.nodes.color;

import com.marginallyclever.donatello.nodes.images.ColorHelper;
import com.marginallyclever.version2.Node;
import com.marginallyclever.version2.Dock;

import java.awt.*;

/**
 * Separates a color into its four component CMYK channels.  Each channel is a value 0...1.
 * @author Dan Royer
 * @since 2022-03-19
 */
public class ColorToCMYK extends Node {
    private final Dock<Color> color = Dock.newInstance("color", Color.class, new Color(0,0,0,0),true,false);
    private final Dock<Number> cyan = Dock.newInstance("cyan", Number.class, 0,false,true);
    private final Dock<Number> magenta = Dock.newInstance("magenta", Number.class, 0,false,true);
    private final Dock<Number> yellow = Dock.newInstance("yellow", Number.class, 0,false,true);
    private final Dock<Number> black = Dock.newInstance("black", Number.class, 0,false,true);

    /**
     * Constructor for subclasses to call.
     */
    public ColorToCMYK() {
        super("ColorToCMYK");
        addVariable(color);
        addVariable(cyan);
        addVariable(magenta);
        addVariable(yellow);
        addVariable(black);
    }

    @Override
    public void update() {
        Color c = color.getValue();
        double [] cmyk = ColorHelper.IntToCMYK(ColorHelper.ColorToInt(c));
        cyan.setValue(   cmyk[0]/255.0);
        magenta.setValue(cmyk[1]/255.0);
        yellow.setValue( cmyk[2]/255.0);
        black.setValue(  cmyk[3]/255.0);
        cleanAllInputs();
    }
}
