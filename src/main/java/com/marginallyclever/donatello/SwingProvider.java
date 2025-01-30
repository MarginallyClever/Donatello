package com.marginallyclever.donatello;

import java.awt.*;

/**
 * Interface for classes that provide a Swing component.
 */
public interface SwingProvider {
    /**
     * Get the Swing component.
     * @return the Swing component or null.
     */
    Component getSwingComponent(Component parent);
}
