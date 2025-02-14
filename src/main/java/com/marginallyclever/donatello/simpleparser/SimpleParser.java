package com.marginallyclever.donatello.simpleparser;

import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;
import net.objecthunter.exp4j.function.Function;

/**
 * A simple parser that evaluates a mathematical expression.  Supports the use of the constant PI and the hypot function.
 */
public class SimpleParser {
    private static final Function hypot = new Function("hypot",2) {
        @Override
        public double apply(double... doubles) {
            return Math.hypot(doubles[0], doubles[1]);
        }
    };
    private static final Function atan2 = new Function("atan2",2) {
        @Override
        public double apply(double... doubles) {
            return Math.atan2(doubles[0], doubles[1]);
        }
    };

    public static double evaluate(String expression) {
        Expression e = new ExpressionBuilder(expression)
            .variables("PI") // Declare the variable
            .function(hypot)
            .function(atan2)
            .build()
            .setVariable("PI", Math.PI); // Assign value to variable
        return e.evaluate();
    }
}
