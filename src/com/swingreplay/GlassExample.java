package com.swingreplay;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/*
 * Demonstrate use of GlassPane in JWindow & friends. Buttons enable/disable it.
 * @author Eckstein et al, in the O'Reilly book "Java Swing"
 */
public class GlassExample {

    /** Construct a Splash screen with the given image */
    public static void main(String[] args) {
        JFrame f = new JFrame("GlassPane");

        final JPanel p1 = new JPanel();
        p1.add(new JLabel("GlassPane Example"));
        JButton show = new JButton("Show");
        p1.add(show);
        p1.add(new JButton("No-op"));
        f.getContentPane().add(p1);

        final JPanel glass = (JPanel) f.getGlassPane();

        glass.setVisible(true);
        glass.setLayout(new GridBagLayout());
        JButton glassButton = new JButton("Hide");
        glass.add(glassButton);

        f.setSize(150, 80);
        f.setVisible(true);

        System.out.println("Button is " + glassButton);
        System.out.println("GlassPane is " + glass);

        // Add actions to the buttons...

        // show button (re-)shows the glass pane.
        show.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                glass.setVisible(true);
                p1.repaint();
            }
        });
        // hide button hides the Glass Pane to show what's under.
        glassButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                glass.setVisible(false);
                p1.repaint();
            }
        });
    }

}
