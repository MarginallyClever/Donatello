package com.marginallyclever.donatello;

import com.marginallyclever.nodegraphcore.Node;

import java.util.EventListener;

/**
 * For listening to the {@link NodeFactoryPanel}.
 */
public interface AddNodeListener extends EventListener {
    void addNode(Node node);
}
