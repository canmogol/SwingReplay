import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * canm
 */
public class EventPlayer implements MouseListener {

    private JFrame frame;
    private JPanel glass;

    public EventPlayer(JFrame frame) {
        this.frame = frame;
    }

    private void replay() {
        glass = (JPanel) frame.getGlassPane();
        glass.setBackground(Color.red);
        glass.setSize(frame.getSize());
        glass.setPreferredSize(frame.getPreferredSize());
        glass.setVisible(true);
        glass.setLayout(null);
    }

    public void listen() {
        startMonitoringEventDispatchQueue();
        checkThread();

        int x = 211;
        int y = 56;
        long when = 0;
        int modifiers = InputEvent.BUTTON1_MASK; // Left Button
        System.out.println("****** will click at x: " + x + " y: " + y);
        int clickCount = 1;
        MouseEvent mEventPressed = new MouseEvent(frame, MouseEvent.MOUSE_PRESSED, when, modifiers, x, y, clickCount, false);
        myEventQueue.postEvent(mEventPressed);
        waitForQueue();
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

    public void startMonitoringEventDispatchQueue() {
        EventQueue eventQueue = Toolkit.getDefaultToolkit().getSystemEventQueue();
        eventQueue.push(myEventQueue);
    }

    public static void checkThread() {
        if (EventQueue.isDispatchThread()) {
            System.out.println(">Current Thread is the Event Dispatch Thread");
        } else {
            System.out.println(">Current Thread is NOT the Event Dispatch Thread");
        }
    }


    @Override
    public void mouseClicked(MouseEvent e) {
//                System.out.println("mouseClicked");
    }

    @Override
    public void mousePressed(MouseEvent e) {
        final int x = e.getX();
        final int y = e.getY();
        System.out.println(x + " " + y + " " + e.getSource());
        JComponent jComponent = (JComponent) e.getSource();

        CirclePanel circlePanel = new CirclePanel();
        circlePanel.setBounds(x - 30, y - 30, 100, 100);
//        glass.removeAll();
//        glass.add(circlePanel);
//        glass.repaint();
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

class MouseEventSource{

    private int x;
    private int y;
    private JComponent component;
    private JFrame frame;

}