package com.marginallyclever.donatello.simplerparser;

import com.marginallyclever.donatello.simpleparser.SimpleParser;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class SimpleParserTest {
    @Test
    public void test() {
        Assertions.assertEquals(-12,SimpleParser.evaluate("3 + 5 * (2 - 8) / 2.0"));
        Assertions.assertEquals(1,SimpleParser.evaluate("sin(PI / 2)"));  // 1.0
        Assertions.assertEquals(1,SimpleParser.evaluate("cos(0)"));  // 1.0
        Assertions.assertEquals(0.999999,SimpleParser.evaluate("tan(PI / 4)"),1e-6);
        Assertions.assertEquals(0.7615941559557649,SimpleParser.evaluate("tanh(1)"),1e-6);  // Hyperbolic tangent
        Assertions.assertEquals(5,SimpleParser.evaluate("hypot(3, 4)"));  // Pythagorean theorem
        Assertions.assertEquals(5,SimpleParser.evaluate("sqrt(25)"));
        Assertions.assertEquals(-2,SimpleParser.evaluate("-5 + 3"));
        Assertions.assertEquals(1,SimpleParser.evaluate("10 % 3"));
        Assertions.assertEquals(57.29577951308232,SimpleParser.evaluate("(180 / PI)"),1e-6);  // Convert to degrees
        Assertions.assertEquals(0.017453292519943295,SimpleParser.evaluate("(PI / 180)"),1e-6);  // Convert to radians
        Assertions.assertEquals(3,SimpleParser.evaluate("floor(PI)"));
        Assertions.assertEquals(4,SimpleParser.evaluate("ceil(PI)"));
        Assertions.assertThrows(IllegalArgumentException.class,()->SimpleParser.evaluate("random"));
    }
}
