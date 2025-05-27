package com.marginallyclever.donatello.actions;

import com.marginallyclever.donatello.Donatello;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.swing.*;

public class ActionRegistryTest {
    // test that action accelerators can be saved, loaded, and reset
    @Test
    public void testActionAccelerators() {
        // This test will check if the actions can be registered, their accelerators can be set,
        var donatello = new Donatello();
        var action = new GraphNewAction("New",donatello);
        KeyStroke defaultKs = (KeyStroke)action.getValue(Action.ACCELERATOR_KEY);
        ActionRegistry.register("New", action);
        KeyStroke currentKs = (KeyStroke) action.getValue(Action.ACCELERATOR_KEY);

        // Assert that the action can be retrieved and its accelerator is set correctly.
        Action retrievedAction = ActionRegistry.get("New");
        Assertions.assertEquals(defaultKs, retrievedAction.getValue(Action.ACCELERATOR_KEY));
        Assertions.assertEquals(defaultKs, ActionRegistry.getDefaultAccelerator(action));

        // Change the accelerator to a new value.
        KeyStroke newKs = KeyStroke.getKeyStroke("control N");
        retrievedAction.putValue(Action.ACCELERATOR_KEY, newKs);
        ActionRegistry.saveUserAcceleratorForAction("New", newKs);

        // Assert that the action's accelerator has been updated.
        var middle = ActionRegistry.get("New");
        Assertions.assertEquals(newKs, middle.getValue(Action.ACCELERATOR_KEY));
        Assertions.assertNotEquals(newKs,ActionRegistry.getDefaultAccelerator(action));

        // Reset the action's accelerator to the current.
        retrievedAction.putValue(Action.ACCELERATOR_KEY, currentKs);
        ActionRegistry.saveUserAcceleratorForAction("New", newKs);

        // Assert that the action's accelerator is back to the current (test should not change settings).
        Assertions.assertEquals(currentKs, retrievedAction.getValue(Action.ACCELERATOR_KEY));
    }
}
