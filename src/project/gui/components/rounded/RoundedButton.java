package project.gui.components.rounded;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

public class RoundedButton extends JButton {
    private final String placeholder;
    private Shape shape;

    public RoundedButton(String placeholder) {
        super(placeholder);
        this.placeholder = placeholder;
        setForeground(Color.GRAY);

        setBorder(new EmptyBorder(10, 10, 10, 10));
        setOpaque(false); // As suggested by @AVD in comment.

    }

    public RoundedButton(ImageIcon image) {
        super(image);
        this.placeholder = "";
        setForeground(Color.GRAY);

        setBorder(new EmptyBorder(10, 10, 10, 10));
        setOpaque(false); // As suggested by @AVD in comment.

    }

    public static void customizeButton(JButton btn) {
        btn.setBackground(new Color(155, 81, 224));
        btn.setForeground(new Color(242, 242, 242));
        btn.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 20));
        Dimension d = new Dimension(465, 105);
        btn.setHorizontalAlignment(JTextField.CENTER);
        btn.setMinimumSize(d);
        btn.setMaximumSize(d);

        //btn.setBorder(new LineBorder(Color.RED));

        btn.setFocusPainted(false);
        btn.setOpaque(false);
    }

    protected void paintComponent(Graphics g) {
        g.setColor(getBackground());
        g.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 15, 15);
        super.paintComponent(g);
        setText(placeholder);
    }

    protected void paintBorder(Graphics g) {
        g.setColor(getBackground());
        g.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 15, 15);
    }
}
