package com.marginallyclever.donatello;

import com.marginallyclever.donatello.select.Select;

import java.awt.*;

/**
 * Interface for classes that provide a Swing component.
 */
public interface SwingProvider {
    /**
     * @return the Select component or null.
     */
    Select getSwingComponent(Component parent);
}
