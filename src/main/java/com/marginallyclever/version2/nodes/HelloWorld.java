package com.marginallyclever.version2.nodes;

import com.marginallyclever.version2.AbstractNode;

public class HelloWorld extends AbstractNode {
    private static final long serialVersionUID = -8114694939008347416L;
    public HelloWorld() {
        super();
    }

    @Override
    public void update() {
        System.out.println("Hello World!");
    }
}
