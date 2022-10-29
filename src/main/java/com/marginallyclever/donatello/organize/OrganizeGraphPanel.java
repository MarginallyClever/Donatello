package com.marginallyclever.donatello.organize;

import com.marginallyclever.nodegraphcore.Node;
import com.marginallyclever.nodegraphcore.NodeFactory;
import com.marginallyclever.nodegraphcore.Graph;
import com.marginallyclever.donatello.AddNodePanel;
import com.marginallyclever.donatello.Donatello;
import com.marginallyclever.donatello.edits.ReorderEdit;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

public class OrganizeGraphPanel extends JPanel {
    private final Donatello editor;
    private final Graph myGraph;

    /**
     * list model controls the contents of the list.  This is needed to add/remove as the search field is changed.
     */
    private final DefaultListModel<Node> listModel = new DefaultListModel<>();

    /**
     * the list of names from the {@link NodeFactory}.
     */
    private final JList<Node> myList = new JList<>(listModel);


    public OrganizeGraphPanel(Donatello editor) {
        super(new BorderLayout());
        this.editor = editor;
        this.myGraph = editor.getGraph();

        myList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        JScrollPane listScroller = new JScrollPane(myList);
        listScroller.setPreferredSize(new Dimension(250, 250));
        this.add(listScroller, BorderLayout.CENTER);

        myList.setCellRenderer(new NodeListCellRenderer());

        setupReorder();
        populateList();
    }

    private void populateList() {
        for(Node n : myGraph.getNodes()) {
            listModel.addElement(n);
        }
    }

    private void setupReorder() {
        myList.setDragEnabled(true);
        myList.setDropMode(DropMode.ON);

        myList.setTransferHandler(new TransferHandler() {
            int index;

            @Override
            public int getSourceActions(JComponent comp) {
                return COPY_OR_MOVE;
            }

            @Override
            public Transferable createTransferable(JComponent comp) {
                index = myList.getSelectedIndex();
                return new TransferableNode(myList.getSelectedValue());
            }


            @Override
            public void exportDone( JComponent comp, Transferable trans, int action ) {
                if (action==MOVE) {
                    listModel.remove(index);
                }
            }

            @Override
            public boolean canImport(TransferHandler.TransferSupport support) {
                return support.isDataFlavorSupported(TransferableNode.nodeFlavor);
            }

            @Override
            public boolean importData(TransferHandler.TransferSupport support) {
                try {
                    // convert data to string
                    Node node = (Node)support.getTransferable().getTransferData(TransferableNode.nodeFlavor);
                    JList.DropLocation dl = (JList.DropLocation)support.getDropLocation();
                    listModel.add(dl.getIndex(),node);

                    editor.addEdit(new ReorderEdit("Reorder",editor,node,dl.getIndex()));

                    return true;
                }
                catch (UnsupportedFlavorException e) {}
                catch (IOException e) {}

                return false;
            }
        });
    }

    /**
     * Runs the panel as a dialog.
     * @param title the parent frame.
     * @param editor the editor that owns this dialog
     */
    public static void runAsDialog(String title,Donatello editor) {
        Frame frame = (JFrame)SwingUtilities.getWindowAncestor(editor);
        JDialog dialog = new JDialog(frame,title, Dialog.ModalityType.DOCUMENT_MODAL);

        JPanel panel = new OrganizeGraphPanel(editor);
        dialog.add(panel);
        dialog.pack();
        dialog.setLocationRelativeTo(frame);
        dialog.setVisible(true);
    }

    /**
     * main entry point.  Good for independent test.
     * @param args command line arguments.
     */
    public static void main(String[] args) throws Exception {
        NodeFactory.loadRegistries();

        JFrame frame = new JFrame(AddNodePanel.class.getSimpleName());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.add(new AddNodePanel());
        frame.pack();
        frame.setVisible(true);
    }
}
