package com.marginallyclever.donatello.curveeditor;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

public class CurveEditorTest {
    public static void main(String[] args) {
        var resource = Objects.requireNonNull(CurveEditor.class.getResource("/com/marginallyclever/donatello/mona-lisa-full.jpg"));
        BufferedImage originalImage = null;
        try {
            originalImage = ImageIO.read(resource);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        // scale image to 256x256
        var scaledImage = new BufferedImage(256, 256, originalImage.getType());
        var g = scaledImage.createGraphics();
        g.drawImage(originalImage, 0, 0, 256, 256, null);
        g.dispose();

        var ce = new CurveEditor();
        JPanel panel = new JPanel(new BorderLayout());

        JPanel displayImage = new JPanel(new BorderLayout());
        displayImage.add(new JLabel(new ImageIcon(scaledImage)));
        ce.addCurveChangedListener((curveEditor) -> {
            BufferedImage adjustedImage = adjustImageIntensity(scaledImage, ce.getCurve());
            // display the image.
            displayImage.removeAll();
            displayImage.add(new JLabel(new ImageIcon(adjustedImage)));
            displayImage.revalidate();
            displayImage.repaint();
        });

        panel.add(ce,BorderLayout.WEST);
        panel.add(displayImage, BorderLayout.EAST);

        JFrame frame = new JFrame("Curve Editor");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.add(panel);
        frame.pack();
        frame.setVisible(true);
    }

    private static BufferedImage adjustImageIntensity(BufferedImage originalImage, int[] curve) {
        BufferedImage adjustedImage = new BufferedImage(originalImage.getWidth(), originalImage.getHeight(), originalImage.getType());
        for (int y = 0; y < originalImage.getHeight(); y++) {
            for (int x = 0; x < originalImage.getWidth(); x++) {
                int rgb = originalImage.getRGB(x, y);
                Color color = new Color(rgb, true);
                int red = curve[color.getRed()];
                int green = curve[color.getGreen()];
                int blue = curve[color.getBlue()];
                Color adjustedColor = new Color(red, green, blue, color.getAlpha());
                adjustedImage.setRGB(x, y, adjustedColor.getRGB());
            }
        }
        return adjustedImage;
    }
}
