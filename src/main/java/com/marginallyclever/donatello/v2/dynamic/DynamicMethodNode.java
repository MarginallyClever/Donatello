package com.marginallyclever.donatello.v2.dynamic;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * A node that can invoke a method dynamically at runtime. The method can be static or non-static.
 */
public class DynamicMethodNode implements Node {
    private final Method method;
    private Object[] inputs;
    private Object output;
    private Object instance; // Only needed for non-static methods

    public DynamicMethodNode(String className, String methodName) throws ClassNotFoundException, NoSuchMethodException {
        Class<?> clazz = Class.forName(className);
        method = findMethod(clazz, methodName);

        // If the method is not static, create an instance of the class
        if (!java.lang.reflect.Modifier.isStatic(method.getModifiers())) {
            try {
                instance = clazz.getDeclaredConstructor().newInstance();
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                throw new RuntimeException("Unable to create an instance of the class", e);
            }
        }
    }

    private Method findMethod(Class<?> clazz, String methodName) throws NoSuchMethodException {
        for (Method method : clazz.getMethods()) {
            if (method.getName().equals(methodName)) {
                return method;
            }
        }
        throw new NoSuchMethodException("Method not found: " + methodName);
    }

    @Override
    public void setInput(Object input) {
        if (input instanceof Object[]) {
            inputs = (Object[]) input;
        } else {
            throw new IllegalArgumentException("Input should be an array of objects");
        }
    }

    @Override
    public Object getOutput() {
        return output;
    }

    @Override
    public void process() {
        try {
            output = method.invoke(instance, inputs);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException("Unable to invoke the method", e);
        }
    }

    public Method getMethod() {
        return method;
    }
}