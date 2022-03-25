package com.marginallyclever.donatello;

import java.awt.event.KeyEvent;

/**
 * Remembers the state of important keys.
 * @author Dan Royer
 * @since 2022-03-08
 */
public class KeyStateMemory {
    private boolean shiftKeyDown = false;

    public void keyPressed(int keyCode) {
        if(keyCode==KeyEvent.VK_SHIFT) shiftKeyDown = true;
    }

    public void keyReleased(int keyCode) {
        if(keyCode==KeyEvent.VK_SHIFT) shiftKeyDown = false;
    }

    public boolean isShiftKeyDown() {
        return shiftKeyDown;
    }
}
