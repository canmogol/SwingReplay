import javax.swing.*;
import java.awt.*;
import java.awt.event.*;


/**
 * canm
 */
public class MyPanel extends JFrame
        implements ActionListener {
    private static final long serialVersionUID = -5482850214654836564L;

    public MyPanel() {
        KeyboardFocusManager.getCurrentKeyboardFocusManager()
                .addKeyEventDispatcher(new KeyEventDispatcher() {
                    @Override
                    public boolean dispatchKeyEvent(KeyEvent event) {
                        if (event.getID() == KeyEvent.KEY_PRESSED) {
                            System.out.println("char: " + event.getKeyChar() + " code: " + event.getKeyCode());
                        }
                        return false;
                    }
                });

        // Create the labels and set alignment
        JLabel label1 = new JLabel("BottomRight", SwingConstants.RIGHT);
        JLabel label2 = new JLabel("CenterLeft", SwingConstants.LEFT);
        JLabel label3 = new JLabel("TopCenter", SwingConstants.CENTER);
        label1.setVerticalAlignment(SwingConstants.BOTTOM);
        label2.setVerticalAlignment(SwingConstants.CENTER);
        label3.setVerticalAlignment(SwingConstants.TOP);

        // Add borders to the labels . . . more on Borders later in the book!
        label1.setBorder(BorderFactory.createLineBorder(Color.black));
        label2.setBorder(BorderFactory.createLineBorder(Color.black));
        label3.setBorder(BorderFactory.createLineBorder(Color.black));

        // Put it all together . . .
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        JPanel p = new JPanel(new GridLayout(3, 1, 8, 8));
        p.add(label1);
        p.add(label2);
        p.add(label3);
        p.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        this.setContentPane(p);
        this.setSize(200, 200);
        this.setVisible(true);


        final JPanel glass = (JPanel) this.getGlassPane();
        glass.setBackground(Color.red);
        glass.setSize(this.getSize());
        glass.setPreferredSize(this.getPreferredSize());
        glass.setVisible(true);
        glass.setLayout(null);


        MouseListener mouseListener = new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
//                System.out.println("mouseClicked");
            }

            @Override
            public void mousePressed(MouseEvent e) {
                final int x = e.getX();
                final int y = e.getY();
                System.out.println(x + " " + y + " " + e.getSource());

                CirclePanel circlePanel = new CirclePanel();
                circlePanel.setBounds(x-30, y-30, 100, 100);
                glass.removeAll();
                glass.add(circlePanel);
                glass.repaint();
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
        this.addMouseListener(mouseListener);
        addMouseListenerToAll(this, mouseListener);

    }

    private void addMouseListenerToAll(Container container, MouseListener mouseListener) {
        for (Component component : container.getComponents()) {
            component.addMouseListener(mouseListener);
            if (component instanceof Container) {
                addMouseListenerToAll((Container) component, mouseListener);
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        System.out.println(e);
    }

}

class CirclePanel extends JPanel {

    public CirclePanel() {
        setPreferredSize(new Dimension(100, 100));
        setBackground(Color.white);
    }

    public void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setComposite(AlphaComposite.getInstance(
                AlphaComposite.SRC_OVER, 0.3f));
        g2d.setColor(Color.yellow);
        int r = 20;
        g.fillOval(30 - r, 30 - r, 2 * r, 2 * r);
    }

}
