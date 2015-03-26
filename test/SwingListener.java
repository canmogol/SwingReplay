import com.swingreplay.EventListener;
import com.swingreplay.EventPlayer;
import com.testui.MyPanel;

import javax.swing.*;
import java.awt.*;

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

        final EventPlayer eventPlayer = new EventPlayer(myPanel);
        final EventListener eventListener = new EventListener(eventPlayer);

        myPanel.addMouseListener(eventListener);
        eventListener.addMouseListenerToAll(myPanel);
        eventListener.setPlayer(eventPlayer);
        eventListener.listen();

        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                myPanel.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                myPanel.setBounds(200, 200, 400, 400);
                myPanel.setVisible(true);
            }
        });

        new Thread(){
            @Override
            public void run() {
                eventPlayer.ready();
            }
        }.start();

    }



}


