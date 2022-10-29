package com.marginallyclever.donatello.actions.undoable;

import com.marginallyclever.nodegraphcore.Node;
import com.marginallyclever.nodegraphcore.Graph;
import com.marginallyclever.donatello.AddNodePanel;
import com.marginallyclever.donatello.Donatello;
import com.marginallyclever.donatello.edits.AddNodeEdit;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * Launches the "Add Node" dialog.  If the user clicks "Ok" then the selected {@link Node} type is added to the
 * current editor {@link Graph}.
 * @author Dan Royer
 * @since 2022-02-21
 */
public class AddNodeAction extends AbstractAction {
    /**
     * The editor being affected.
     */
    private final Donatello editor;

    /**
     * Constructor for subclasses to call.
     * @param name the name of this action visible on buttons and menu items.
     * @param icon the small icon of this action visible on buttons and menu items.
     * @param editor the editor affected by this Action.
     */
    public AddNodeAction(String name, Donatello editor) {
        super(name);
        this.editor = editor;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Node n = AddNodePanel.runAsDialog((JFrame)SwingUtilities.getWindowAncestor(editor));
        if(n!=null) {
            n.setPosition(editor.getPaintArea().transformMousePoint(editor.getPopupPoint()));
            editor.addEdit(new AddNodeEdit((String)this.getValue(Action.NAME),editor,n));
        }
    }
}
