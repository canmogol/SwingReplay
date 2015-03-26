package com.swingreplay;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * canm
 */
public class EventListener implements MouseListener {

    private EventPlayer player;

    public EventListener(EventPlayer player) {
        this.player = player;
    }

    public void listen() {
        KeyboardFocusManager.getCurrentKeyboardFocusManager()
                .addKeyEventDispatcher(new KeyEventDispatcher() {
                    @Override
                    public boolean dispatchKeyEvent(KeyEvent event) {
                        if (event.getID() == KeyEvent.KEY_PRESSED) {
                            System.out.println("char: " + event.getKeyChar() + " code: " + event.getKeyCode());
                            Component component = (Component) event.getSource();
                            JFrame jFrame = findFrame(component);
                            player.add(new KeyEventSource(event.getKeyChar(), component, jFrame));
                            if (event.getKeyChar() == 'r') {
                                new Thread() {
                                    @Override
                                    public void run() {
                                        player.play();
                                    }
                                }.start();
                            }
                        }
                        return false;
                    }
                });
    }


    public void setPlayer(EventPlayer player) {
        this.player = player;
    }

    public EventPlayer getPlayer() {
        return player;
    }


    private JFrame findFrame(Component component) {
        try {
            if (component instanceof JFrame) {
                return (JFrame) component;
            }
            return findFrame(component.getParent());
        } catch (ClassCastException e) {
            return null;
        }
    }

    public void addMouseListenerToAll(Container container) {
        for (Component component : container.getComponents()) {
            component.addMouseListener(this);
            if (component instanceof Container) {
                addMouseListenerToAll((Container) component);
            }
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        Component component = (Component) e.getSource();
        JFrame jFrame = findFrame(component);
        Point point = jFrame.getMousePosition();
        if (point != null) {
            final int x = (int) point.getX();
            final int y = (int) point.getY();
            System.out.println(x + " " + y + " " + e.getSource());
            player.add(new MouseEventSource(x, y, component, jFrame));
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }


}
