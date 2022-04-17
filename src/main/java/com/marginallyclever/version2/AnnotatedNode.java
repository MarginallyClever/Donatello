package com.marginallyclever.version2;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This node reads and loads annotations.
 */
public abstract class AnnotatedNode extends AbstractNode {
    private static final Logger logger = LoggerFactory.getLogger(AnnotatedNode.class);

    public AnnotatedNode() {
        super();
        parseInPorts();
        parseOutPorts();
    }

    /**
     * Read all annotations for {@link InPort} and create a {@link ReceivingDock} for each.
     */
    private void parseInPorts() {
        InPort [] inPorts = this.getClass().getAnnotationsByType(InPort.class);
        for( InPort p : inPorts ) {
            logger.debug("{}.{} input type {}",getName(),p.name(),p.type().getName());
            addDock(new ReceivingDock(p.name(), p.type(), this));
        }
    }

    /**
     * Read all annotations for {@link OutPort} and create a {@link ShippingDock} for each.
     */
    private void parseOutPorts() {
        OutPort [] outPorts = this.getClass().getAnnotationsByType(OutPort.class);
        for( OutPort p : outPorts ) {
            logger.debug("{}.{} output type {}",getName(),p.name(),p.type().getName());
            addDock(new ShippingDock(p.name(), p.type(), this));
        }
    }
}
