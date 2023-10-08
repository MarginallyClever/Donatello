package com.marginallyclever.donatello;

import com.github.sarxos.webcam.Webcam;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledIf;

import java.awt.*;

/**
 * Test the camera feed.
 */
@DisabledIf(value = "java.awt.GraphicsEnvironment#isHeadless", disabledReason = "headless environment")
public class TestCameraFeed {
    @Test
    public void detectCamera() {
        Webcam webcam = Webcam.getDefault();
        if (webcam != null) {
            System.out.println("Webcam: " + webcam.getName());
        } else {
            System.out.println("No webcam detected");
        }
    }
}
