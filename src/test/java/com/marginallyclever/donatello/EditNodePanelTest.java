package com.marginallyclever.donatello;

import com.marginallyclever.donatello.nodes.Calculate;
import com.marginallyclever.nodegraphcore.Node;

public class EditNodePanelTest {
    /**
     * main entry point.  Good for independent test.
     * @param args command line arguments.
     */
    public static void main(String[] args) {
        // a test case
        Node node = new Calculate();
        EditNodePanel.runAsDialog(node,null,null);
    }
}
