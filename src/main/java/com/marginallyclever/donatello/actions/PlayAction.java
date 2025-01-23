package com.marginallyclever.donatello.actions;

import com.marginallyclever.donatello.Donatello;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.Objects;

public class PlayAction extends AbstractAction implements EditorAction {
    private final Donatello editor;
    private final GraphUpdateAction graphUpdateAction;

    public PlayAction(String name, Donatello editor, GraphUpdateAction graphUpdateAction) {
        super(name);
        this.editor = editor;
        this.graphUpdateAction = graphUpdateAction;
        putValue(SHORT_DESCRIPTION, "Update the graph automatically.");
        updateButtonState();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        boolean keepGoing = !editor.getKeepGoing();
        editor.setKeepGoing(keepGoing);
        graphUpdateAction.setEnabled(keepGoing);
        updateButtonState();
    }

    private void updateButtonState() {
        boolean keepGoing = editor.getKeepGoing();
        this.putValue(Action.SMALL_ICON, keepGoing
                ? new ImageIcon(Objects.requireNonNull(getClass().getResource("/com/marginallyclever/donatello/icons8-pause-16.png")))
                : new ImageIcon(Objects.requireNonNull(getClass().getResource("/com/marginallyclever/donatello/icons8-play-16.png")))
        );
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
