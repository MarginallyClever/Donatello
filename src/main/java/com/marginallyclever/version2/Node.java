package com.marginallyclever.version2;

import java.util.List;

public interface Node {
    void addDock(Dock dock);

    void removeDock(Dock dock);

    List<Dock> getDocks();

    /**
     * <p>Called once every tick.  This is where annotatednodes should</p>
     * <ul>
     *     <li>check for valid inputs</li>
     *     <li>perform calculations</li>
     *     <li>update the state of the outputs</li>
     * </ul>
     * <p>Note that all inputs should be treated in a read-only manner.</p>
     */
    void update();

    String getName();

    String getUniqueID();

    ReceivingDock getInput(String name);

    ShippingDock getOutput(String name);

    List<ReceivingDock> getInputs();

    List<ShippingDock> getOutputs();
}