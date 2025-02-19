package com.marginallyclever.donatello.graphview;

import java.awt.*;

/**
 * Ports which implement this interface will provide custom displays in the graph view.
 */
public interface GraphViewProvider {
    /**
     * Display the port in the graph view.
     * @param g The graphics context to draw on.
     * @param box The bounding box to draw in.
     */
    void paint(Graphics g, Rectangle box);
}
