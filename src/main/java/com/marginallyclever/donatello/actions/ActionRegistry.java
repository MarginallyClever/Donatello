package com.marginallyclever.donatello.actions;

import javax.swing.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.prefs.Preferences;

/**
 * Coordinates the actions available in the application.
 */
public class ActionRegistry {
    private static final Preferences pref = Preferences.userNodeForPackage(ActionRegistry.class);
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
        loadUserAcceleratorForAction(id, action);
    }

    /**
     * Load user's preferred accelerator key for the action from Preferences.
     * @param id The unique identifier for the action.
     * @param action The action to load settings for.
     */
    private static void loadUserAcceleratorForAction(String id, Action action) {
        String key = "action." + id + ".accelerator";
        String accelerator = pref.get(key, null);
        if (accelerator != null) {
            KeyStroke ks = KeyStroke.getKeyStroke(accelerator);
            action.putValue(Action.ACCELERATOR_KEY, ks);
        }
    }

    public static void saveUserAcceleratorForAction(String id, KeyStroke ks) {
        String key = "action." + id + ".accelerator";
        pref.put(key, toHumanReadable(ks));
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
     * @return The default KeyStroke for the action or null if no default accelerator is registered.
     */
    public static KeyStroke getDefaultAccelerator(Action id) {
        KeyStroke ks = (KeyStroke)id.getValue(Action.ACCELERATOR_KEY);
        if(ks == null) return null;

        // Find the key that matches the KeyStroke in the default accelerators map.
        AtomicReference<String> key = new AtomicReference<>("");
        defaultAccelerators.forEach((k, v) -> {
            if(ks.equals(v)) {
                key.set(k);
            }
        });

        // If no matching key is found, return null.
        String value = key.get();
        if(value==null || value.isEmpty()) return null;

        // If a matching key is found, return the corresponding KeyStroke.
        return defaultAccelerators.get(value);
    }

    /**
     * Convert a {@link KeyStroke} to a human-readable string that matches the format used in
     * {@link KeyStroke#getKeyStroke(String)}
     * @param ks the KeyStroke to convert
     * @return a human-readable string representation of the {@link KeyStroke}
     */
    public static String toHumanReadable(KeyStroke ks) {
        if (ks == null) return "";
        StringBuilder sb = new StringBuilder();
        int modifiers = ks.getModifiers();
        if (modifiers != 0) {
            sb.append(InputEvent.getModifiersExText(modifiers).toLowerCase().replace("+"," ")).append(" ");
        }
        sb.append(KeyEvent.getKeyText(ks.getKeyCode()));
        return sb.toString();
    }
}