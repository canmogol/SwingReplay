package com.swingreplay;

import javax.swing.*;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * canm
 */
public class EventPlayer {

    private final Object lock = new Object();
    private final List<EventSource> eventSourceList = Collections.synchronizedList(new LinkedList<EventSource>());
    private boolean running = true;
    private boolean replaying = false;

    private JPanel glass = null;
    private JFrame frame = null;
    private EventQueue eventQueue = new EventQueue();

    public EventPlayer(JFrame frame) {
        this.frame = frame;
    }

    public void ready() {
        try {
            while (running) {
                synchronized (lock) {
                    replaying = false;
                    lock.wait();
                    glass = (JPanel) frame.getGlassPane();
                    glass.setBackground(Color.darkGray);
                    glass.setLayout(null);
                    glass.setVisible(true);
                    replaying = true;
                    Iterator<EventSource> iterator = eventSourceList.iterator();
                    while (iterator.hasNext()) {
                        EventSource eventSource = iterator.next();
                        iterator.remove();
                        if (eventSource instanceof MouseEventSource) {
                            MouseEventSource e = (MouseEventSource) eventSource;
                            e.getComponent().requestFocus();

                            CirclePanel circlePanel = new CirclePanel();
                            circlePanel.setBounds(e.getX() - 30, e.getY() - 50, 100, 100);
                            glass.removeAll();
                            glass.add(circlePanel);
                            glass.repaint();

                            if(e.getComponent() instanceof AbstractButton){
                                AbstractButton button = ((AbstractButton)e.getComponent());
                                for (ActionListener action : button.getActionListeners()) {
                                    action.actionPerformed(new ActionEvent(e.getComponent(), ActionEvent.ACTION_FIRST, "test"));
                                }
                            }else{
                                long when = 1;
                                int modifiers = InputEvent.BUTTON1_MASK;
                                int clickCount = 1;
                                MouseEvent mEventPressed = new MouseEvent(frame, MouseEvent.MOUSE_PRESSED, when, modifiers, e.getX(), e.getY(), clickCount, false);
                                eventQueue.postEvent(mEventPressed);
                            }
                            waitForQueue();
                        } else if (eventSource instanceof KeyEventSource) {
                            KeyEventSource keyEventSource = (KeyEventSource) eventSource;
                            if (keyEventSource.getComponent() instanceof JTextComponent) {
                                JTextComponent jTextComponent = (JTextComponent) keyEventSource.getComponent();
                                jTextComponent.setText(jTextComponent.getText() + keyEventSource.getKey());
                            }
                        }
                        Thread.sleep(500);
                    }
                    glass.setVisible(false);
                    glass.removeAll();
                    glass.repaint();
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void waitForQueue() {
        while (eventQueue.peekEvent() != null) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void add(EventSource eventSource) {
        if(!replaying){
            this.eventSourceList.add(eventSource);
        }
    }

    public JFrame getFrame() {
        return frame;
    }

    public void stop() {
        synchronized (lock) {
            running = false;
            lock.notify();
        }
    }

    public void play() {
        synchronized (lock) {
            lock.notify();
        }
    }
}
