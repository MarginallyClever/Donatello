package com.marginallyclever.donatello;

import java.awt.*;

/**
 * Used by any class wanting to add decorations to a {@link GraphViewPanel}.
 * @author Dan Royer
 * @since 2022-02-11
 */
public interface GraphViewListener {
    /**
     * Called when the {@link GraphViewPanel} has completed painting itself.
     * Useful for then adding highlights and extra annotation.
     * @param g the graphics context used to paint the panel
     * @param panel the caller
     */
    void paint(Graphics g, GraphViewPanel panel);
}
