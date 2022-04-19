package com.marginallyclever.version2.nodes;

import com.marginallyclever.version2.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serial;

/**
 * This node reads and loads {@link InPort} and {@link OutPort} annotations.
 */
public abstract class AbstractAnnotatedNode extends AbstractNode {
    @Serial
    private static final long serialVersionUID = 650325937917108534L;

    private static final Logger logger = LoggerFactory.getLogger(AbstractAnnotatedNode.class);

    public AbstractAnnotatedNode() {
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
