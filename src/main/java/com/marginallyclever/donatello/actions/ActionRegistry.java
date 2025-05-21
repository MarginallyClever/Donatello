package com.marginallyclever.donatello.actions;

import javax.swing.*;
import java.util.*;

/**
 * Coordinates the actions available in the application.
 */
public class ActionRegistry {
    private static final Map<String, Action> actions = new LinkedHashMap<>();

    public static void register(String id, Action action) {
        actions.put(id, action);
    }

    public static Action get(String id) {
        return actions.get(id);
    }

    public static Collection<Action> getAll() {
        return actions.values();
    }

    public static Set<Map.Entry<String, Action>> entrySet() {
        return actions.entrySet();
    }
}