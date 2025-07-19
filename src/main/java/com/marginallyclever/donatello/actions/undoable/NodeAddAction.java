package com.marginallyclever.donatello.actions.undoable;

import com.marginallyclever.donatello.Donatello;
import com.marginallyclever.donatello.nodefactorypanel.NodeFactoryPanel;
import com.marginallyclever.donatello.edits.NodeAddEdit;
import com.marginallyclever.nodegraphcore.Graph;
import com.marginallyclever.nodegraphcore.Node;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
    private static final Logger logger = LoggerFactory.getLogger(NodeAddAction.class);

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

        System.out.println("Adding "+n.getName() + " ("+n.getUniqueID()+")");
        n.setPosition(p);
        n.updateBounds();
        editor.addEdit(new NodeAddEdit((String)this.getValue(Action.NAME),editor,n));
    }

    public NodeFactoryPanel getNodeFactoryPanel() {
        return nodeFactoryPanel;
    }
}
