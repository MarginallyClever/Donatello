package com.marginallyclever.donatello.port;

import com.marginallyclever.donatello.ports.InputDouble;
import com.marginallyclever.donatello.ports.OutputInt;
import com.marginallyclever.nodegraphcore.Connection;
import com.marginallyclever.nodegraphcore.Node;
import org.junit.jupiter.api.Test;

public class InputDoubleTest {
    // test that InputDouble can receive from an OutputInt
    @Test
    public void testIntToDouble() {
        // Create an instance of InputDouble
        Node outputNode = new Node("Test") {
            OutputInt outputInt = new OutputInt("TestOutput", 1);
            {
                addPort(outputInt);
            }

            @Override
            public void update() {}
        };

        Node inputNode = new Node("input") {
            InputDouble inputDouble = new InputDouble("TestInput", 0.0);
            {
                addPort(inputDouble);
            }
            @Override
            public void update() {}
        };

        // make a Connection
        Connection connection = new Connection();
        connection.setFrom(outputNode,0);
        connection.setTo(inputNode,0);
        // check that the connection is valid
        assert connection.isValidDataType();
    }
}
