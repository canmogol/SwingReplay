package com.testui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;


/**
 * canm
 */
public class MyPanel extends JFrame
        implements ActionListener {

    private static final long serialVersionUID = -5482850214654836564L;

    public MyPanel() {

        // Create the labels and set alignment
        JLabel label1 = new JLabel("BottomRight", SwingConstants.RIGHT);
        JLabel label2 = new JLabel("CenterLeft", SwingConstants.LEFT);
        JTextField jTextField = new JTextField();
        label1.setVerticalAlignment(SwingConstants.BOTTOM);
        label2.setVerticalAlignment(SwingConstants.CENTER);


        // Add borders to the labels . . . more on Borders later in the book!
        label1.setBorder(BorderFactory.createLineBorder(Color.black));
        label2.setBorder(BorderFactory.createLineBorder(Color.black));


        // Put it all together . . .
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        JPanel p = new JPanel(new GridLayout(4, 1, 8, 8));
        p.add(label1);
        p.add(label2);
        p.add(jTextField);
        JButton jButton = new JButton("ok");
        jButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("-- button clicked");
            }
        });

        p.add(jButton);
        p.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        this.setContentPane(p);
        this.setSize(200, 200);
        this.setVisible(true);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        System.out.println(e);
    }

}
