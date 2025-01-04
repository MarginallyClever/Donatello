package com.marginallyclever.donatello.actions.undoable;

import com.marginallyclever.donatello.actions.EditorAction;
import com.marginallyclever.donatello.actions.GraphCopyAction;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * Performs an {@link GraphCopyAction} and then an {@link NodeDeleteAction}.
 * @author Dan Royer
 * @since 2022-02-21
 */
public class NodeCutAction extends AbstractAction implements EditorAction {
    /**
     * The delete action on which this action depends.
     */
    private final NodeDeleteAction actionDeleteGraph;
    /**
     * The copy action on which this action depends.
     */
    private final GraphCopyAction actionCopyGraph;

    /**
     * Constructor for subclasses to call.
     * @param name the name of this action visible on buttons and menu items.
     * @param actionDeleteGraph the delete action to use
     * @param actionCopyGraph the copy action to use
     */
    public NodeCutAction(String name, NodeDeleteAction actionDeleteGraph, GraphCopyAction actionCopyGraph) {
        super(name);
        this.actionDeleteGraph = actionDeleteGraph;
        this.actionCopyGraph = actionCopyGraph;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        actionCopyGraph.actionPerformed(e);
        actionDeleteGraph.actionPerformed(e);
    }

    @Override
    public void updateEnableStatus() {
        setEnabled(actionDeleteGraph.isEnabled());
    }
}
