package com.marginallyclever.donatello.actions;

import com.marginallyclever.donatello.Donatello;

import javax.swing.*;

/**
 * {@link AbstractAction}s in the {@link Donatello} implement this interface to update their own
 * {@link AbstractAction#setEnabled(boolean)} status.
 * @author Dan Royer
 * @since 2022-02-23
 */
public interface EditorAction {
    /**
     * Called by the {@link Donatello} when the editor believes it is time to confirm enable status.
     */
    void updateEnableStatus();
}
