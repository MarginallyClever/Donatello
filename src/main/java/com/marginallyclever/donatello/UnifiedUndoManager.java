package com.marginallyclever.donatello;

import javax.swing.undo.*;

/**
 * A singleton class that provides a single instance of {@link UndoManager} for the entire application.
 */
public class UnifiedUndoManager {
    private static final UndoManager undoManager = new UndoManager();

    public static UndoManager getInstance() {
        return undoManager;
    }
}
