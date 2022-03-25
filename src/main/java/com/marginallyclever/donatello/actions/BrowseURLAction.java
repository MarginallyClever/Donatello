package com.marginallyclever.donatello.actions;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.net.URI;

/**
 * Use the desktop browser to open a URL.
 * @author Dan Royer
 * @since 2022-03-14
 */
public class BrowseURLAction extends AbstractAction {
    private final String address;

    public BrowseURLAction(String label, String address) {
        super(label);
        this.address = address;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
                java.awt.Desktop.getDesktop().browse(URI.create(address));
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
