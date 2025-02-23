package com.marginallyclever.donatello.nodes;

import com.marginallyclever.donatello.ports.*;
import com.marginallyclever.nodegraphcore.Node;
import com.marginallyclever.nodegraphcore.port.Input;
import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;
import net.objecthunter.exp4j.function.Function;
import org.json.JSONException;
import org.json.JSONObject;

public class Calculate extends Node {
    private final InputInt inputCount = new InputInt("count");
    private final InputString inputExpression = new InputString("expression","");
    private final OutputDouble output = new OutputDouble("result");

    private final int DEFAULT_PORTS = 3;

    public Calculate() {
        super("Calculate");
        addPort(inputCount);
        addPort(inputExpression);
        addPort(output);
    }

    @Override
    public void update() {
        updateInputCount(Math.max(0,inputCount.getValue()));
        try {
            double result = evaluate(inputExpression.getValue());
            output.setValue(result);
        } catch (IllegalArgumentException e) {
            output.setValue(0);
        }
    }

    private void updateInputCount(int count) {
        // count the number of ports of type Input.
        int inputPortCount = 0;
        // skip port 0, the "count" port.
        for(int i=DEFAULT_PORTS; i<getNumPorts(); i++) {
            if(getPort(i) instanceof Input<?>) inputPortCount++;
        }
        // if the count is different, add or remove ports.
        inputPortCount = Math.min(26, inputPortCount);
        boolean changed = false;
        if(inputPortCount < count) {
            for(int i=inputPortCount; i<count; i++) {
                // name should be ('a'+i)
                String name = ""+(char)('a'+i);
                addPort(new InputDouble(name));
                changed = true;
            }
        } else if(inputPortCount > count) {
            int toRemove = inputPortCount - count;
            for(int i=getNumPorts()-1; i>=0; i--) {
                if(getPort(i) instanceof Input<?>) {
                    removePort(getPort(i));
                    changed = true;
                    toRemove--;
                    if(toRemove==0) break;
                }
            }
        }
        if(changed) {
            updateBounds();
        }
    }

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

    public double evaluate(String expression) {
        // build the expression
        ExpressionBuilder eb = new ExpressionBuilder(expression)
                .variables("PI") // Declare the variable
                .function(hypot)
                .function(atan2);
        // add the variables from the input ports
        for(int i=DEFAULT_PORTS; i<getNumPorts(); i++) {
            if(getPort(i) instanceof InputDouble input) {
                eb.variable(input.getName());
            }
        }
        Expression e = eb.build();
        // set the variables
        e.setVariable("PI", Math.PI); // Assign value to variable
        for(int i=DEFAULT_PORTS; i<getNumPorts(); i++) {
            if(getPort(i) instanceof InputDouble input) {
                e.setVariable(input.getName(), input.getValue());
            }
        }
        // evaluate the expression
        return e.evaluate();
    }

    /**
     * Parse the JSON object into this object.  Because the number of inputs may vary,
     * we need to update the number of input ports first and then read in the rest.
     * @param jo the JSON object to parse
     * @throws JSONException if there is an error parsing the JSON
     */
    @Override
    public void parseJSON(JSONObject jo) throws JSONException {
        super.parseJSON(jo);
        updateInputCount(Math.max(0,inputCount.getValue()));
        this.parseAllPortsFromJSON(jo.getJSONArray("variables"));
    }
}
