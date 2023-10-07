package com.marginallyclever.donatello.actions;

import com.marginallyclever.donatello.Donatello;
import com.marginallyclever.donatello.UnicodeIcon;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class PlayAction extends AbstractAction implements EditorAction {
    private final Donatello editor;
    private final UpdateGraphAction updateGraphAction;

    public PlayAction(String name, Donatello editor, UpdateGraphAction updateGraphAction) {
        super(name);
        this.editor = editor;
        this.updateGraphAction = updateGraphAction;
        updateButtonState();
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        boolean keepGoing = !editor.getKeepGoing();
        editor.setKeepGoing(keepGoing);
        updateGraphAction.setEnabled(keepGoing);
        updateButtonState();
    }

    private void updateButtonState() {
        boolean keepGoing = editor.getKeepGoing();
        this.putValue(Action.SMALL_ICON, keepGoing ? new UnicodeIcon("⏸") : new UnicodeIcon("▶"));
        this.putValue(Action.NAME, keepGoing ? "Pause" : "Play");
    }

    /**
     * Called by the {@link Donatello} when the editor believes it is time to confirm enable status.
     */
    @Override
    public void updateEnableStatus() {
        setEnabled(!editor.getGraph().isEmpty());
    }
}
