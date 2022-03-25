package com.marginallyclever.donatello;

import java.awt.*;

/**
 * Used by any class wanting to add decorations to a {@link NodeGraphViewPanel}.
 * @author Dan Royer
 * @since 2022-02-11
 */
public interface NodeGraphViewListener {
    /**
     * Called when the {@link NodeGraphViewPanel} has completed painting itself.
     * Useful for then adding highlights and extra annotation.
     * @param g the graphics context used to paint the panel
     * @param panel the caller
     */
    void paint(Graphics g, NodeGraphViewPanel panel);
}
