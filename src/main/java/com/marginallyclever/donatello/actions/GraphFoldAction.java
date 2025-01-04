package com.marginallyclever.donatello.actions;

import com.marginallyclever.nodegraphcore.Node;
import com.marginallyclever.nodegraphcore.Graph;
import com.marginallyclever.nodegraphcore.Subgraph;
import com.marginallyclever.donatello.Donatello;
import com.marginallyclever.donatello.actions.undoable.NodeCutAction;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * Collapses the editor's selected {@link Node}s into a new sub-graph.
 * @author Dan Royer
 * @since 2022-02-21
 */
public class GraphFoldAction extends AbstractAction implements EditorAction {
    /**
     * The editor being affected.
     */
    private final Donatello editor;
    /**
     * The cut action on which this action depends.
     */
    private final NodeCutAction nodeCutAction;

    /**
     * Constructor for subclasses to call.
     * @param name the name of this action visible on buttons and menu items.
     * @param editor the editor affected by this Action.
     * @param NodeCutAction the cut action to use with this Action.
     */
    public GraphFoldAction(String name, Donatello editor, NodeCutAction NodeCutAction) {
        super(name);
        this.editor = editor;
        this.nodeCutAction = NodeCutAction;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Graph preserveCopyBehaviour = editor.getCopiedGraph().deepCopy();

        nodeCutAction.actionPerformed(e);
        Graph justCut = editor.getCopiedGraph().deepCopy();
        Node n = editor.getGraph().add(new Subgraph(justCut));
        n.setPosition(editor.getPopupPoint());

        editor.setCopiedGraph(preserveCopyBehaviour);
    }

    @Override
    public void updateEnableStatus() {
        setEnabled(!editor.getSelectedNodes().isEmpty());
    }
}
