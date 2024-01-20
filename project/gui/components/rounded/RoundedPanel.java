package project.gui.components.rounded;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class RoundedPanel extends JPanel {
    private Shape shape;

    public RoundedPanel(GridBagLayout gb) {
        super(gb);

        setBorder(new EmptyBorder(10, 10, 10, 10));
        setOpaque(false); // As suggested by @AVD in comment.
        setBackground(new Color(242,242,242,1));
        setForeground(new Color(242,242,242));
    }

    public RoundedPanel(GridLayout gb) {
        super(gb);

        setBorder(new EmptyBorder(10, 10, 10, 10));
        setOpaque(false); // As suggested by @AVD in comment.
        setBackground(new Color(242,242,242,1));
        setForeground(new Color(242,242,242));
    }

    public RoundedPanel(BorderLayout bl) {
        super(bl);

        setBorder(new EmptyBorder(10, 10, 10, 10));
        setOpaque(false); // As suggested by @AVD in comment.
        setBackground(new Color(242,242,242,1));
        setForeground(new Color(242,242,242));
    }

    protected void paintComponent(Graphics g) {
        g.setColor(getBackground());
        g.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 12, 12);
        super.paintComponent(g);
    }

    protected void paintBorder(Graphics g) {
        g.setColor(getBackground());
        g.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 12, 12);
    }
}


