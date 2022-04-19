package com.marginallyclever.version2.nodes;

import com.marginallyclever.version2.AbstractNamedEntity;
import com.marginallyclever.version2.Node;

import java.io.Serial;

public class HelloWorld extends Node {
    @Serial
    private static final long serialVersionUID = -8114694939008347416L;
    public HelloWorld() {
        super();
    }

    @Override
    public void update() {
        System.out.println("Hello World!");
    }
}
