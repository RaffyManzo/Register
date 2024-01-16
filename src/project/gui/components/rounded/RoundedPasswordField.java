package project.gui.components.rounded;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

public class RoundedPasswordField extends JPasswordField {
    private Shape shape;

    public RoundedPasswordField(String placeholder) {
        super(placeholder);
        setEchoChar((char)0);
        setForeground(Color.GRAY);

        setBorder(new EmptyBorder(10, 10, 10, 10));
        setOpaque(false); // As suggested by @AVD in comment.

        addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                String pass = new String(getPassword());
                setForeground(new Color(51,51,51));
                if (pass.contentEquals(placeholder)) {
                    setText("");
                    setEchoChar('•');

                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if(getPassword().length == 0) {
                    setText(placeholder);
                    setEchoChar((char)0);
                    setForeground(Color.GRAY);
                } else {
                    setEchoChar('•');
                    setForeground(new Color(51,51,51));
                }

            }
        });

        Border empty = BorderFactory.createLineBorder(new Color(242,242,242,1));

        setHorizontalAlignment(JTextField.CENTER);
        setMinimumSize(getPreferredSize());
        setMaximumSize(getPreferredSize());
        //txt.setBorder(new LineBorder(Color.RED));

        setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 18));
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
