package com.marginallyclever.version2;

public class DefaultRegistry implements NodeRegistry {
    @Override
    public String getName() {
        return "default";
    }

    @Override
    public void registerNodes() {
        NodeFactory.registerAllNodesInPackage("com.marginallyclever.version2.nodes");
    }
}
