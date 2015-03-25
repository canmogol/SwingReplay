import javax.swing.*;
import java.awt.*;

/**
 * canm
 */
public class KeyEventSource extends EventSource {

    private final char key;

    public KeyEventSource(char key, Component component, JFrame frame) {
        super(component, frame);
        this.key = key;
    }

    public char getKey() {
        return key;
    }
}
