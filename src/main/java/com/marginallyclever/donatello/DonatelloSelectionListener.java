package com.marginallyclever.donatello;

import java.util.EventListener;

/**
 * Interface for listening to changes in the selected nodes of a {@link Donatello}.
 */
public interface DonatelloSelectionListener extends EventListener {
    /**
     * Called when the selection of a {@link Donatello} changes.
     * @param e the {@link Donatello} that changed.
     */
    void selectionChanged(Donatello e);
}
