package com.marginallyclever.donatello.contextsensitivetools;

import com.marginallyclever.donatello.KeyStateMemory;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * Remembers the state of a key for other actions, such as "is shift being held down?" for selection tools.
 * @author Dan Royer
 * @since 2022-03-08
 */
public class RememberKeyStateAction extends AbstractAction {
    private final KeyStateMemory memory;
    private final boolean onRelease;
    private final int keyCode;

    public RememberKeyStateAction(KeyStateMemory memory, int keyCode, boolean onRelease) {
        super();
        this.memory=memory;
        this.onRelease = onRelease;
        this.keyCode = keyCode;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(onRelease) {
            memory.keyReleased(keyCode);
        } else {
            memory.keyPressed(keyCode);
        }
    }
}
