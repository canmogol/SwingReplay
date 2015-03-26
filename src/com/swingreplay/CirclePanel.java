package com.swingreplay;

import javax.swing.*;
import java.awt.*;

/**
 * canm
 */
public class CirclePanel extends JPanel {

    public CirclePanel() {
        setPreferredSize(new Dimension(100, 100));
        setBackground(Color.white);
    }

    public void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setComposite(AlphaComposite.getInstance(
                AlphaComposite.SRC_OVER, 0.3f));
        g2d.setColor(Color.yellow);
        int r = 20;
        g.fillOval(30 - r, 30 - r, 2 * r, 2 * r);
    }

}

