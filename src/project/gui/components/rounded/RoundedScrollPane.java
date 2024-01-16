package project.gui.components.rounded;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class RoundedScrollPane extends JScrollPane {

    private Shape shape;
    private JPanel subPan;

    public RoundedScrollPane(Component view) {
        super(view);
        view.setBackground(getBackground());


        setBorder(new EmptyBorder(10, 10, 10, 10));
        setOpaque(false); // As suggested by @AVD in comment.
        setBackground(new Color(242,242,242,1));
        setForeground(new Color(242,242,242));
    }

    public RoundedScrollPane() {
        super();


        setBorder(new EmptyBorder(10, 10, 10, 10));
        setOpaque(false); // As suggested by @AVD in comment.
        setBackground(new Color(242,242,242,1));
        setForeground(new Color(242,242,242));
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
