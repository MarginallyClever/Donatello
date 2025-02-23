package com.marginallyclever.donatello.nodes;

import com.marginallyclever.nodegraphcore.DAO4JSONFactory;
import org.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class CalculateTest {
    /**
     * Create a Calculate node.  set the inputCount to 2.  set the inputExpression to "a+b".
     * set the inputA to 1.0.  set the inputB to 2.0.  update the node.  check that the output is 3.0.
     * save the Calculate node to a JSONObject.  load the Calculate node from the JSONObject.
     * check that the inputCount is 2.
     */
    @Test
    public void testCalculate() {
        DAO4JSONFactory.loadRegistries();

        Calculate before = new Calculate();
        before.getPort("count").setValue(2);
        before.getPort("expression").setValue("a+b");
        before.update();
        before.getPort("a").setValue(1.0);
        before.getPort("b").setValue(2.0);
        before.update();
        Assertions.assertEquals(3.0, (double)before.getPort("result").getValue(), 0.0001);

        JSONObject json = before.toJSON();
        Calculate after = new Calculate();
        after.parseJSON(json);
        Assertions.assertEquals(2, after.getPort("count").getValue());
        after.update();
        Assertions.assertEquals(3.0, after.getPort("result").getValue());
    }
}
