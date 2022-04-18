package com.marginallyclever.version2;

import java.util.List;

/**
 * <p>The {@link Node} interface describes a node in a graph.  Each Node is equivalent to a method in <a
 * href='https://en.wikipedia.org/wiki/Functional_programming'>functional program</a> and a first-class citizen.</p>
 * <p>Nodes do not contain any state information.  They are simply a way to describe a computation.</p>
 */
public interface Node extends NamedEntity {
    /**
     * <p>This is where the {@link Node} should</p>
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
