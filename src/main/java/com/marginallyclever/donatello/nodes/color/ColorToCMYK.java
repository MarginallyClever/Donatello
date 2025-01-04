package com.marginallyclever.donatello.nodes.color;

import com.marginallyclever.donatello.nodes.images.ColorHelper;
import com.marginallyclever.nodegraphcore.*;
import com.marginallyclever.nodegraphcore.dock.Input;
import com.marginallyclever.nodegraphcore.dock.Output;

import java.awt.*;

/**
 * Separates a color into its four component CMYK channels.  Each channel is a value 0...1.
 * @author Dan Royer
 * @since 2022-03-19
 */
public class ColorToCMYK extends Node {
    private final Input<Color> color = new Input<>("color", Color.class, new Color(0,0,0,0));
    private final Output<Number> cyan = new Output<>("cyan", Number.class, 0);
    private final Output<Number> magenta = new Output<>("magenta", Number.class, 0);
    private final Output<Number> yellow = new Output<>("yellow", Number.class, 0);
    private final Output<Number> black = new Output<>("black", Number.class, 0);

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
        if(color.hasPacketWaiting()) color.receive();

        Color c = color.getValue();
        double [] cmyk = ColorHelper.IntToCMYK(ColorHelper.ColorToInt(c));
        cyan.send(new Packet<>(   cmyk[0]/255.0));
        magenta.send(new Packet<>(cmyk[1]/255.0));
        yellow.send(new Packet<>( cmyk[2]/255.0));
        black.send(new Packet<>(  cmyk[3]/255.0));
    }
}
