package project.gui.components.rounded;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

public class RoundedTextArea extends JTextArea {
    private Shape shape;

    public RoundedTextArea(String placeholder) {
        super(placeholder);

        setBorder(new EmptyBorder(10, 10, 10, 10));
        setOpaque(false); // As suggested by @AVD in comment.
        setBackground(Color.WHITE);
        setForeground(Color.DARK_GRAY);

        addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if(getText().contentEquals(placeholder)) {
                    setText("");
                    setForeground(new Color(51,51,51));
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if(getText().isEmpty()) {
                    setText(placeholder);
                    setForeground(Color.DARK_GRAY);
                }
            }
        });
    }

    protected void paintComponent(Graphics g) {
        g.setColor(getBackground());
        g.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 15, 15);
        super.paintComponent(g);
    }

    protected void paintBorder(Graphics g) {
        g.setColor(getBackground());
        g.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 15, 15);
    }
}



