package com.marginallyclever.donatello.port;

import com.marginallyclever.donatello.ports.InputInt;
import com.marginallyclever.donatello.ports.OutputDouble;
import com.marginallyclever.nodegraphcore.Connection;
import com.marginallyclever.nodegraphcore.Node;
import org.junit.jupiter.api.Test;

public class InputIntTest {
    // test that InputDouble can receive from an OutputInt
    @Test
    public void testDoubleToInt() {
        // Create an instance of InputDouble
        Node outputNode = new Node("output") {
            final OutputDouble outputDouble = new OutputDouble("TestOutput", 1.0);
            {
                addPort(outputDouble);
            }

            @Override
            public void update() {}
        };

        Node inputNode = new Node("input") {
            final InputInt inputInt = new InputInt("TestInput", 0);
            {
                addPort(inputInt);
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
