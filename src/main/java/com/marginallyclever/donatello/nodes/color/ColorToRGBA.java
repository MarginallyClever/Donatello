package com.marginallyclever.donatello.nodes.color;

import com.marginallyclever.version2.Node;
import com.marginallyclever.version2.Dock;

import java.awt.*;

/**
 * Separates a color into its four component RGBA channels.  Each channel is a value 0...1.
 * @author Dan Royer
 * @since 2022-03-19
 */
public class ColorToRGBA extends Node {
    private final Dock<Color> color = Dock.newInstance("color", Color.class, new Color(0,0,0,0),true,false);
    private final Dock<Number> red = Dock.newInstance("red", Number.class, 0,false,true);
    private final Dock<Number> green = Dock.newInstance("green", Number.class, 0,false,true);
    private final Dock<Number> blue = Dock.newInstance("blue", Number.class, 0,false,true);
    private final Dock<Number> alpha = Dock.newInstance("alpha", Number.class, 0,false,true);

    /**
     * Constructor for subclasses to call.
     */
    public ColorToRGBA() {
        super("ColorToRGBA");
        addVariable(color);
        addVariable(red  );
        addVariable(green);
        addVariable(blue );
        addVariable(alpha);
    }

    @Override
    public void update() {
        Color c = color.getValue();
        red  .setValue(c.getRed()  /255.0);
        green.setValue(c.getGreen()/255.0);
        blue .setValue(c.getBlue() /255.0);
        alpha.setValue(c.getAlpha()/255.0);
        cleanAllInputs();
    }
}
