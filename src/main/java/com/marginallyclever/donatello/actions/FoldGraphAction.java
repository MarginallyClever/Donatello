package com.marginallyclever.donatello.actions;

import com.marginallyclever.version2.GraphHelper;
import com.marginallyclever.version2.Node;
import com.marginallyclever.version2.Graph;
import com.marginallyclever.donatello.Donatello;
import com.marginallyclever.donatello.actions.undoable.CutGraphAction;
import com.marginallyclever.version2.nodes.NodeWhichContainsAGraph;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * Collapses the editor's selected {@link Node}s into a new sub-graph.
 * @author Dan Royer
 * @since 2022-02-21
 */
public class FoldGraphAction extends AbstractAction implements EditorAction {
    /**
     * The editor being affected.
     */
    private final Donatello editor;
    /**
     * The cut action on which this action depends.
     */
    private final CutGraphAction cutGraphAction;

    /**
     * Constructor for subclasses to call.
     * @param name the name of this action visible on buttons and menu items.
     * @param editor the editor affected by this Action.
     * @param CutGraphAction the cut action to use with this Action.
     */
    public FoldGraphAction(String name, Donatello editor, CutGraphAction CutGraphAction) {
        super(name);
        this.editor = editor;
        this.cutGraphAction = CutGraphAction;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Graph preserveCopyBehaviour = GraphHelper.deepCopy(editor.getCopiedGraph());

        cutGraphAction.actionPerformed(e);
        Graph justCut = GraphHelper.deepCopy(editor.getCopiedGraph());
        Node n = new NodeWhichContainsAGraph(justCut);
        editor.getGraph().add(n);
        n.setPosition(editor.getPopupPoint());

        editor.setCopiedGraph(preserveCopyBehaviour);
    }

    @Override
    public void updateEnableStatus() {
        setEnabled(!editor.getSelectedNodes().isEmpty());
    }
}
