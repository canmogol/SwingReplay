import javax.swing.*;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * canm
 */
public class EventPlayer implements MouseListener {

    private JFrame frame = null;
    private JPanel glass = null;

    private boolean isPlaying = false;

    private List<EventSource> eventSourceList = new ArrayList<EventSource>();

    public EventPlayer(JFrame frame) {
        this.frame = frame;
        glass = (JPanel) frame.getGlassPane();
        glass.setBackground(Color.darkGray);
        glass.setVisible(true);
        //glass.setOpaque(true);
        glass.setLayout(null);
    }

    private void replay() {
        isPlaying = true;
        Iterator<EventSource> iterator = eventSourceList.iterator();
        while (iterator.hasNext()) {
            EventSource eventSource = iterator.next();
            iterator.remove();
            if (eventSource instanceof MouseEventSource) {

                MouseEventSource e = (MouseEventSource) eventSource;
                CirclePanel circlePanel = new CirclePanel();
                circlePanel.setBounds(e.getX() - 30, e.getY() - 30, 100, 100);
                glass.removeAll();
                glass.add(circlePanel);
                glass.repaint();
                long when = 0;
                int modifiers = InputEvent.BUTTON1_MASK;
                int clickCount = 1;
                MouseEvent mEventPressed = new MouseEvent(frame, MouseEvent.MOUSE_PRESSED, when, modifiers, e.getX(), e.getY(), clickCount, false);
                myEventQueue.postEvent(mEventPressed);
                waitForQueue();
            } else if (eventSource instanceof KeyEventSource) {
                KeyEventSource keyEventSource = (KeyEventSource) eventSource;
                if(keyEventSource.getComponent() instanceof JTextComponent){
                    JTextComponent jTextComponent = (JTextComponent) keyEventSource.getComponent();
                    jTextComponent.setText(jTextComponent.getText() + keyEventSource.getKey());
                }
            }
            try {
                Thread.sleep(500);
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }
        }
        isPlaying = false;
    }

    public void listen() {
//        startMonitoringEventDispatchQueue();
//        checkThread();
        KeyboardFocusManager.getCurrentKeyboardFocusManager()
                .addKeyEventDispatcher(new KeyEventDispatcher() {
                    @Override
                    public boolean dispatchKeyEvent(KeyEvent event) {
                        if (event.getID() == KeyEvent.KEY_PRESSED) {
                            System.out.println("char: " + event.getKeyChar() + " code: " + event.getKeyCode());
                            Component component = (Component) event.getSource();
                            JFrame jFrame = findFrame(component);
                            if (!isPlaying) {
                                eventSourceList.add(new KeyEventSource(event.getKeyChar(), component, jFrame));
                            }
                            if (event.getKeyChar() == 'r') {
                                //replay();

                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
                                            Thread.sleep(500);
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        }
                                        replay();
                                    }
                                }).start();
                            }
                        }
                        return false;
                    }
                });
    }


    private MyEventQueue myEventQueue = new MyEventQueue();

    private class MyEventQueue extends EventQueue {
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

    public void waitForQueue() {
        while (myEventQueue.peekEvent() != null) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

//    public void startMonitoringEventDispatchQueue() {
//        EventQueue eventQueue = Toolkit.getDefaultToolkit().getSystemEventQueue();
//        eventQueue.push(myEventQueue);
//    }

//    public static void checkThread() {
//        if (EventQueue.isDispatchThread()) {
//            System.out.println(">Current Thread is the Event Dispatch Thread");
//        } else {
//            System.out.println(">Current Thread is NOT the Event Dispatch Thread");
//        }
//    }


    @Override
    public void mouseClicked(MouseEvent e) {
//                System.out.println("mouseClicked");
    }

    @Override
    public void mousePressed(MouseEvent e) {
        final int x = e.getX();
        final int y = e.getY();
        System.out.println(x + " " + y + " " + e.getSource());
        Component component = (Component) e.getSource();
        JFrame jFrame = findFrame(component);
        if (!isPlaying) {
            eventSourceList.add(new MouseEventSource(x, y, component, jFrame));
        }
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


}
