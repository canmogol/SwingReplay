package com.swingreplay;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class EventPoster {
    public static void main(String[] args) {
        EventPoster.startMonitoringEventDispatchQueue();
        checkThread();

        JButton northButton = new JButton("Click Me North");
        northButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                System.out.println("--- The north button was clicked.");
            }
        });

        JButton southButton = new JButton("Click Me South");
        southButton.addKeyListener(new KeyListener() {
            @Override
            public void keyPressed(KeyEvent arg0) {
                System.out.println("--- The south button was sent a key.");
            }

            @Override
            public void keyReleased(KeyEvent arg0) {
            }

            @Override
            public void keyTyped(KeyEvent arg0) {
            }
        });

        JFrame frame = new JFrame();
        frame.setLayout(new BorderLayout());
        frame.setLocation(200, 200);
        frame.setSize(300, 300);
        frame.add(northButton, BorderLayout.NORTH);
        frame.add(southButton, BorderLayout.SOUTH);
        frame.setVisible(true);


        MouseListener mouseListener = new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
//                System.out.println("mouseClicked");
            }

            @Override
            public void mousePressed(MouseEvent e) {
                int x = e.getX();
                int y = e.getY();
                System.out.println(x + " " + y + " " + e.getSource());
//                Component component = findComponentAt(x, y);
//                System.out.println(component.getClass().getSimpleName());
            }

            @Override
            public void mouseReleased(MouseEvent e) {
//                System.out.println("mouseReleased");
            }

            @Override
            public void mouseEntered(MouseEvent e) {
//                System.out.println("mouseEntered");
            }

            @Override
            public void mouseExited(MouseEvent e) {
//                System.out.println("mouseExited");
            }
        };
        frame.addMouseListener(mouseListener);
        addMouseListenerToAll(frame, mouseListener);

        mouseSingleLeftClick(frame, northButton);
        //mouseSingleLeftClick(frame, southButton); // Needs focus to send a Key
        //keyTypeControlV(frame); // Focus events need to be dispatched and processed before this call
    }


    private static void addMouseListenerToAll(Container container, MouseListener mouseListener) {
        for (Component component : container.getComponents()) {
            component.addMouseListener(mouseListener);
            if (component instanceof Container) {
                addMouseListenerToAll((Container) component, mouseListener);
            }
        }
    }

    private static void mouseSingleLeftClick(Window window, JButton button) {
        //System.out.println("First:"+MouseEvent.MOUSE_PRESSED+" Second:"+MouseEvent.MOUSE_RELEASED+" Third:"+MouseEvent.MOUSE_CLICKED);
        long when = 0;
        int modifiers = InputEvent.BUTTON1_MASK; // Left Button
        int x = button.getLocationOnScreen().x - window.getLocationOnScreen().x + 1;
        int y = button.getLocationOnScreen().y - window.getLocationOnScreen().y + 1;
        System.out.println("****** will click at x: " + x + " y: " + y);
        int clickCount = 1;
        boolean popupTrigger = false;
        MouseEvent mEventPressed = new MouseEvent(window, MouseEvent.MOUSE_PRESSED, when, modifiers, x, y, clickCount, popupTrigger);
        myEventQueue.postEvent(mEventPressed);
        MouseEvent mEventReleased = new MouseEvent(window, MouseEvent.MOUSE_RELEASED, when, modifiers, x, y, clickCount, popupTrigger);
        myEventQueue.postEvent(mEventReleased);
        MouseEvent mEventClicked = new MouseEvent(window, MouseEvent.MOUSE_CLICKED, when, modifiers, x, y, clickCount, popupTrigger);
        myEventQueue.postEvent(mEventClicked);
        waitForQueue();
    }

    private static void keyTypeControlV(Window window) {
        //System.out.println("First:"+KeyEvent.KEY_PRESSED+" Second:"+KeyEvent.KEY_TYPED+" Third:"+KeyEvent.KEY_RELEASED);
        int when = 0;
        int modifiers = InputEvent.CTRL_MASK;
        int keyCode = KeyEvent.VK_V;
        char ch = InputEvent.CTRL_MASK & KeyEvent.VK_V;
        KeyEvent kEventPressed = new KeyEvent(window, KeyEvent.KEY_PRESSED, when, modifiers, keyCode, ch);
        myEventQueue.postEvent(kEventPressed);
        KeyEvent kEventTyped = new KeyEvent(window, KeyEvent.KEY_TYPED, when, modifiers, 0, ch);
        myEventQueue.postEvent(kEventTyped);
        KeyEvent kEventReleased = new KeyEvent(window, KeyEvent.KEY_RELEASED, when, modifiers, keyCode, ch);
        myEventQueue.postEvent(kEventReleased);
        waitForQueue();
    }

    public static void checkThread() {
        if (EventQueue.isDispatchThread()) {
            System.out.println(">Current Thread is the Event Dispatch Thread");
        } else {
            System.out.println(">Current Thread is NOT the Event Dispatch Thread");
        }
    }

    private static MyEventQueue myEventQueue = new MyEventQueue();

    public static void startMonitoringEventDispatchQueue() {
        EventQueue eventQueue = Toolkit.getDefaultToolkit().getSystemEventQueue();
        eventQueue.push(myEventQueue);
    }

    /**
     * Note that just because there are no events in the queue,
     * the handler for the dispatched events may still be being processed.
     * In general it is a good idea to have a thread manager that tracks all the threads.
     * This function may leave when the queue is empty but if another thread is handling
     * the dispatched event, that thread may end up posting additional threads via
     * processes like repaint();
     */
    public static void waitForQueue() {
        while (myEventQueue.peekEvent() != null) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    private static class MyEventQueue extends EventQueue {
        public void postEvent(AWTEvent event) {
            if (event instanceof InvocationEvent) {
                InvocationEvent iEvent = (InvocationEvent) event;
                //System.out.println("postEvent InvocationEvent:"+iEvent.getSource());
            } else {
                //System.out.println("postEvent AWTEvent:"+event.getClass());
            }
            super.postEvent(event);
        }

        public void dispatchEvent(AWTEvent event) {
            if (event instanceof InvocationEvent) {
                InvocationEvent iEvent = (InvocationEvent) event;
                //System.out.println("dispatchEvent InvocationEvent:"+iEvent.getSource());
            } else if (event instanceof MouseEvent) {
                MouseEvent mEvent = (MouseEvent) event;
                if (mEvent.getID() == MouseEvent.MOUSE_PRESSED) {
                    System.out.println("dispatchEvent MouseEvent: Source:" + mEvent.getSource().getClass()
                            + " ID:" + mEvent.getID()
                            + " When:" + mEvent.getWhen()
                            + " Modifiers:" + mEvent.getModifiers()
                            + " X:" + mEvent.getX()
                            + " Y:" + mEvent.getY()
                            + " ClickCount:" + mEvent.getClickCount());
                }
            } else if (event instanceof KeyEvent) {
                KeyEvent kEvent = (KeyEvent) event;
                if (kEvent.getID() == KeyEvent.KEY_PRESSED) {
                    System.out.println("dispatchEvent KeyEvent: Source:" + kEvent.getSource().getClass()
                            + " ID:" + kEvent.getID()
                            + " When:" + kEvent.getWhen()
                            + " Modifiers:" + kEvent.getModifiers()
                            + " KeyCode:" + kEvent.getKeyCode()
                            + " Char:" + kEvent.getKeyChar());
                }
            } else {
                //System.out.println("dispatchEvent AWTEvent:"+event.getClass());
            }
            super.dispatchEvent(event);
        }
    }
}