import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.HashMap;

public class TransparentKeyboardOverlay {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            KeyboardOverlay overlay = new KeyboardOverlay();
            overlay.setVisible(true);
        });
    }
}

class KeyboardOverlay extends JWindow {
    private Point dragStartPoint;
    private HashMap<String, JLabel> keyMap;

    public KeyboardOverlay() {
        setAlwaysOnTop(true);
        setLayout(null);
        setSize(800, 400);
        setLocationRelativeTo(null);
        setBackground(new Color(0, 0, 0, 0)); // Fully transparent
        keyMap = new HashMap<>();

        addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                dragStartPoint = e.getPoint();
            }
        });

        addMouseMotionListener(new MouseMotionAdapter() {
            public void mouseDragged(MouseEvent e) {
                Point p = getLocation();
                setLocation(p.x + e.getX() - dragStartPoint.x, p.y + e.getY() - dragStartPoint.y);
            }
        });

        createKeyboardLayout();
        addKeyListener(new KeyPressHandler());
        setFocusable(true); // Ensures the window receives keyboard events
    }

    private void createKeyboardLayout() {
        String[] keys = {
            "Q", "W", "E", "R", "T", "Y", "U", "I", "O", "P",
            "A", "S", "D", "F", "G", "H", "J", "K", "L",
            "Z", "X", "C", "V", "B", "N", "M"
        };

        int x = 50, y = 50, width = 50, height = 50;
        for (String key : keys) {
            JLabel keyLabel = new JLabel(key, SwingConstants.CENTER);
            keyLabel.setBounds(x, y, width, height);
            keyLabel.setOpaque(true);
            keyLabel.setBackground(new Color(255, 255, 255, 180)); // Semi-transparent
            keyLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            add(keyLabel);
            keyMap.put(key.toUpperCase(), keyLabel); // Map keys to labels

            x += width + 10;
            if (key.equals("P") || key.equals("L")) {
                x = 50;
                y += height + 10;
            }
        }
    }

    private class KeyPressHandler extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            String keyChar = KeyEvent.getKeyText(e.getKeyCode()).toUpperCase();
            JLabel keyLabel = keyMap.get(keyChar);
            if (keyLabel != null) {
                keyLabel.setBackground(new Color(0, 255, 0, 180)); // Highlight key in green
            }
        }

        @Override
        public void keyReleased(KeyEvent e) {
            String keyChar = KeyEvent.getKeyText(e.getKeyCode()).toUpperCase();
            JLabel keyLabel = keyMap.get(keyChar);
            if (keyLabel != null) {
                keyLabel.setBackground(new Color(255, 255, 255, 180)); // Reset to original color
            }
        }
    }
}
