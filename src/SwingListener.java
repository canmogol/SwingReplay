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
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                myPanel.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                myPanel.setBounds(200, 200, 400, 400);
                myPanel.setVisible(true);
            }
        });

        EventPlayer eventPlayer = new EventPlayer(myPanel);
        myPanel.addMouseListener(eventPlayer);
        eventPlayer.addMouseListenerToAll(myPanel);
        eventPlayer.listen();
    }



}


