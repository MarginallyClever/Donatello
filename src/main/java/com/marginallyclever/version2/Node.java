package com.marginallyclever.version2;

import java.util.List;

public interface Node extends NamedEntity {
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

    /**
     * @return all {@link ReceivingDock} for this {@link Node}.
     */
    List<ReceivingDock> getInputs();

    /**
     * @return all {@link ShippingDock} for this {@link Node}.
     */
    List<ShippingDock> getOutputs();
}
