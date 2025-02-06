package com.marginallyclever.donatello.actions.undoable;

import com.marginallyclever.donatello.Donatello;
import com.marginallyclever.donatello.NodeFactoryPanel;
import com.marginallyclever.donatello.edits.NodeAddEdit;
import com.marginallyclever.nodegraphcore.Graph;
import com.marginallyclever.nodegraphcore.Node;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.security.InvalidParameterException;

/**
 * Launches the "Add Node" dialog.  If the user clicks "Ok" then the selected {@link Node} type is added to the
 * current editor {@link Graph}.
 * @author Dan Royer
 * @since 2022-02-21
 */
public class NodeAddAction extends AbstractAction {
    /**
     * The editor being affected.
     */
    private final Donatello editor;
    private final NodeFactoryPanel nodeFactoryPanel;

    /**
     * Constructor for subclasses to call.
     * @param name the name of this action visible on buttons and menu items.
     */
    public NodeAddAction(String name, Donatello editor, NodeFactoryPanel nodeFactoryPanel) {
        super(name);
        this.editor = editor;
        this.nodeFactoryPanel = nodeFactoryPanel;

        nodeFactoryPanel.addListener(e->{
            var p = editor.getPopupPoint();
            if(p!=null) p = editor.getPaintArea().transformScreenToWorldPoint(p);
            else p = editor.getPaintArea().getCameraPosition();

            commitAdd(e,p);
        });
    }

    /**
     * Launches the "Add Node" dialog.
     * @param e the event to be processed
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        JFrame parentFrame = (JFrame)SwingUtilities.getWindowAncestor((Component)e.getSource());
        JDialog dialog = new JDialog(parentFrame, "Add Node", true);
        dialog.add(nodeFactoryPanel);
        dialog.pack();
        dialog.setLocationRelativeTo(parentFrame);
        dialog.setVisible(true);
    }

    /**
     * Adds the given node to the editor's graph and creates an undoable edit.
     * @param n the node to add.
     */
    public void commitAdd(Node n, Point p) {
        if(n==null) throw new InvalidParameterException("NodeAddAction.commitAdd(null)");

        n.setPosition(p);
        n.updateBounds();
        editor.addEdit(new NodeAddEdit((String)this.getValue(Action.NAME),editor,n));
    }

    public NodeFactoryPanel getNodeFactoryPanel() {
        return nodeFactoryPanel;
    }
}
