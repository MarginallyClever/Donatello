package com.marginallyclever.donatello.actions;

import com.marginallyclever.donatello.Donatello;
import com.marginallyclever.donatello.organize.OrganizeGraphPanel;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class OrganizeGraphAction extends AbstractAction {
    /**
     * The editor being affected.
     */
    private final Donatello editor;

    /**
     * Constructor for subclasses to call.
     * @param name the name of this action visible on buttons and menu items.
     * @param editor the editor affected by this Action.
     */
    public OrganizeGraphAction(String name, Donatello editor) {
        super(name);
        this.editor = editor;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        OrganizeGraphPanel.runAsDialog((String)this.getValue(Action.NAME),editor);
    }
}