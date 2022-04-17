package com.marginallyclever.version2.annotatednodes;

import com.marginallyclever.version2.AbstractNode;

public class HelloWorld extends AbstractNode {
    public HelloWorld() {
        super();
    }

    @Override
    public void update() {
        System.out.println("Hello World!");
    }
}
