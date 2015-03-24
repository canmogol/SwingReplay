import javax.swing.*;
import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.InvocationEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

/**
 * canm
 */
public class SwingListener {

    public static void main(String[] args) {
        SwingListener swingListener = new SwingListener();
        swingListener.show();
    }

    private void show() {
        final MyPanel myPanel = new MyPanel();
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                myPanel.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                myPanel.setBounds(200, 200, 400, 400);
                myPanel.setVisible(true);
            }
        });

        startMonitoringEventDispatchQueue();
        checkThread();

        int x = 211;
        int y = 56;
        long when = 0;
        int modifiers = InputEvent.BUTTON1_MASK; // Left Button
        System.out.println("****** will click at x: " + x + " y: " + y);
        int clickCount = 1;
        MouseEvent mEventPressed = new MouseEvent(myPanel, MouseEvent.MOUSE_PRESSED, when, modifiers, x, y, clickCount, false);
        myEventQueue.postEvent(mEventPressed);
//        MouseEvent mEventReleased = new MouseEvent(myPanel, MouseEvent.MOUSE_RELEASED, when, modifiers, x, y, clickCount, false);
//        myEventQueue.postEvent(mEventReleased);
//        MouseEvent mEventClicked = new MouseEvent(myPanel, MouseEvent.MOUSE_CLICKED, when, modifiers, x, y, clickCount, false);
//        myEventQueue.postEvent(mEventClicked);
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



}


