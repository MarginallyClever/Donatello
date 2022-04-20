package com.marginallyclever.donatello.edits;

import com.marginallyclever.version2.Node;
import com.marginallyclever.donatello.Donatello;

import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by the {@link com.marginallyclever.donatello.contextsensitivetools.NodeMoveTool} so that reorganizations
 * can be undone.  This edit is created when a move is complete and should record only the significant relative change
 * in position.  Since the tool already completed the move there is no need to apply the move in the constructor.
 * @author Dan Royer
 * @since 2022-03-11
 */
public class MoveNodesEdit extends SignificantUndoableEdit {
    private final String name;
    private final Donatello editor;
    private final int dx,dy;
    private final List<Node> selected = new ArrayList<>();

    public MoveNodesEdit(String name, Donatello editor, int dx, int dy) {
        super();
        this.name = name;
        this.editor = editor;
        this.dx = dx;
        this.dy = dy;
        this.selected.addAll(editor.getSelectedNodes());
    }

    @Override
    public String getPresentationName() {
        return name;
    }

    @Override
    public void undo() throws CannotUndoException {
        editor.setSelectedNodes(selected);
        editor.moveSelectedNodes(-dx,-dy);
        super.undo();
    }

    @Override
    public void redo() throws CannotRedoException {
        editor.setSelectedNodes(selected);
        editor.moveSelectedNodes(dx,dy);
        super.redo();
    }
}
