package io.github.srcimon.metalrenderingbug;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

public class DemoApp extends Canvas {
    public static void main(String[] args) {
        //CHANGE TO sun.java2d.opengl to workaround bug
        System.setProperty("sun.java2d.metal", "true");
        BufferedImage spriteImage = createTestSprite(32, 32);

        final BufferedImage finalImage = spriteImage;
        final int scaleFactor = 12;
        JFrame frame = new JFrame("Resize window to experience bug");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        BuggyCanvas canvas = new BuggyCanvas(finalImage, scaleFactor);
        frame.add(canvas);

        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    static class BuggyCanvas extends JPanel {
        private final BufferedImage sprite;
        private final int scale;

        public BuggyCanvas(BufferedImage sprite, int scale) {
            this.sprite = sprite;
            this.scale = scale;
            int width = sprite.getWidth() * scale;
            int height = sprite.getHeight() * scale;
            setPreferredSize(new Dimension(width, height));
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            AffineTransform tx = new AffineTransform();
            tx.scale(20, 20);
            tx.translate(15, 20);
            g2d.setTransform(tx);
            g2d.drawImage(sprite, 10, 0, null);

        }
    }

    private static BufferedImage createTestSprite(int w, int h) {
        BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                int color = ((x + y) % 2 == 0) ? 0xFF0000 : 0x0000FF;
                if (x == 0 || x == w - 1 || y == 0 || y == h - 1) {
                    color = 0xFFFFFF; // White
                }
                img.setRGB(x, y, color);
            }
        }
        return img;
    }
}
