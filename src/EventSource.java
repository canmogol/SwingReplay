import javax.swing.*;
import java.awt.*;

/**
 * canm
 */
public abstract class EventSource {

    private Component component;
    private JFrame frame;

    public EventSource(Component component, JFrame frame) {
        this.component = component;
        this.frame = frame;
    }

    public Component getComponent() {
        return component;
    }

    public JFrame getFrame() {
        return frame;
    }


}
