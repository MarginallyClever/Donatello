package com.marginallyclever.donatello.nodes.color;

import com.marginallyclever.nodegraphcore.*;
import com.marginallyclever.nodegraphcore.port.Input;
import com.marginallyclever.nodegraphcore.port.Output;

import java.awt.*;

/**
 * Separates a color into its four component RGBA channels.  Each channel is a value 0...1.
 * @author Dan Royer
 * @since 2022-03-19
 */
public class ColorToRGBA extends Node {
    private final Input<Color> color = new Input<>("color", Color.class, new Color(0,0,0,0));
    private final Output<Number> red = new Output<>("red", Number.class, 0);
    private final Output<Number> green = new Output<>("green", Number.class, 0);
    private final Output<Number> blue = new Output<>("blue", Number.class, 0);
    private final Output<Number> alpha = new Output<>("alpha", Number.class, 0);

    /**
     * Constructor for subclasses to call.
     */
    public ColorToRGBA() {
        super("ColorToRGBA");
        addPort(color);
        addPort(red  );
        addPort(green);
        addPort(blue );
        addPort(alpha);
    }

    @Override
    public void update() {
        Color c = color.getValue();
        red  .send(c.getRed()  /255.0);
        green.send(c.getGreen()/255.0);
        blue .send(c.getBlue() /255.0);
        alpha.send(c.getAlpha()/255.0);
    }
}
