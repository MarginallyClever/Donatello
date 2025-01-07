package com.marginallyclever.donatello.contextsensitivetools;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;

/**
 * A tool that is context-sensitive.  It can be activated by a key press or by clicking on the graph.
 */
public abstract class ContextSensitiveTool extends MouseAdapter {
    private boolean isActive=false;

    public abstract void paint(Graphics g);

    public void attachKeyboardAdapter() {}
    public void detachKeyboardAdapter() {}

    public void attachMouseAdapter() {}
    public void detachMouseAdapter() {}

    public void restart() {}

    public abstract String getName();

    /**
     * Returns the {@link KeyStroke} associated with activating this tool
     * @return the {@link KeyStroke} associated with activating this tool
     */
    public abstract KeyStroke getAcceleratorKey();

    public abstract Icon getSmallIcon();

    protected void setActive(boolean isActive) {
        this.isActive = isActive;
    }

    public boolean isActive() {
        return isActive;
    }

    /**
     * Returns true if the tool should activate at this point.
     * @param p the point on the graph to check
     * @return true if the tool should activate at this point.
     */
    public abstract boolean isCorrectContext(Point p);
}
