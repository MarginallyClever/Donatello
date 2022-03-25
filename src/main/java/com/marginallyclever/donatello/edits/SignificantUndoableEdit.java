package com.marginallyclever.donatello.edits;

import javax.swing.undo.AbstractUndoableEdit;

public class SignificantUndoableEdit extends AbstractUndoableEdit {
    private boolean isSignificant = true;

    @Override
    public boolean isSignificant() {
        return isSignificant;
    }

    public void setSignificant(boolean significant) {
        isSignificant = significant;
    }
}
