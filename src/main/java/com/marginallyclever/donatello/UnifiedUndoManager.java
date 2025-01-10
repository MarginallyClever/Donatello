package com.marginallyclever.donatello;

import javax.swing.undo.*;

public class UnifiedUndoManager {
    private static final UndoManager undoManager = new UndoManager();

    public static UndoManager getInstance() {
        return undoManager;
    }
}
