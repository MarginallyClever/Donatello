package com.marginallyclever.donatello.nodes.color;

import com.marginallyclever.nodegraphcore.Node;
import com.marginallyclever.nodegraphcore.NodeVariable;

import java.awt.*;

/**
 * Separates a color into its four component RGBA channels.  Each channel is a value 0...1.
 * @author Dan Royer
 * @since 2022-03-19
 */
public class ColorToRGBA extends Node {
    private final NodeVariable<Color> color = NodeVariable.newInstance("color", Color.class, new Color(0,0,0,0),true,false);
    private final NodeVariable<Number> red = NodeVariable.newInstance("red", Number.class, 0,false,true);
    private final NodeVariable<Number> green = NodeVariable.newInstance("green", Number.class, 0,false,true);
    private final NodeVariable<Number> blue = NodeVariable.newInstance("blue", Number.class, 0,false,true);
    private final NodeVariable<Number> alpha = NodeVariable.newInstance("alpha", Number.class, 0,false,true);

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
