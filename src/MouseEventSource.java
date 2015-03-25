import javax.swing.*;
import java.awt.*;

/**
 * canm
 */
public class MouseEventSource extends EventSource {

    private int x;
    private int y;

    public MouseEventSource(int x, int y, Component component, JFrame frame) {
        super(component, frame);
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

}