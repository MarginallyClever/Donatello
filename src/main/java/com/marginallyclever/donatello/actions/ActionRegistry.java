package com.marginallyclever.donatello.actions;

import javax.swing.*;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Coordinates the actions available in the application.
 */
public class ActionRegistry {
    private static final Map<String, Action> actions = new LinkedHashMap<>();
    private static final Map<String, KeyStroke> defaultAccelerators = new HashMap<>();

    /**
     * Registers an action with the given id.
     * @param id The unique identifier for the action.
     * @param action The action to register.
     * @throws IllegalArgumentException if an action with the same id is already registered.
     */
    public static void register(String id, Action action) {
        actions.put(id, action);
        defaultAccelerators.put(id, (KeyStroke) action.getValue(Action.ACCELERATOR_KEY));
    }

    /**
     * Gets the action with the given id.
     * @param id The unique identifier for the action.
     * @return The action associated with the given id.
     * @throws IllegalArgumentException if no action with the given id is registered.
     */
    public static Action get(String id) {
        if(!actions.containsKey(id)) {
            throw new IllegalArgumentException("Action with id '" + id + "' is not registered.");
        }
        return actions.get(id);
    }

    public static ArrayList<Action> getAll() {
        return new ArrayList<>(actions.values());
    }

    public static Set<Map.Entry<String, Action>> entrySet() {
        return actions.entrySet();
    }

    /**
     * Returns the default accelerator for the action with the given id.
     * @param id The id of the action.
     * @return The default KeyStroke for the action.
     * @throws IllegalArgumentException if no default accelerator is registered for the action.
     */
    public static KeyStroke getDefaultAccelerator(Action id) {
        KeyStroke ks = (KeyStroke)id.getValue(Action.ACCELERATOR_KEY);
        if(ks == null) return null;
        AtomicReference<String> key = new AtomicReference<>("");

        defaultAccelerators.forEach((k, v) -> {
            if(ks.equals(v)) {
                key.set(k);
            }
        });
        String value = key.get();
        if(value.isEmpty()) {
            throw new IllegalArgumentException("No default accelerator for action with id '" + id + "'.");
        }
        return defaultAccelerators.get(value);
    }
}