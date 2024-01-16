package project.gui.components.teacher;

import project.gui.components.rounded.RoundedPanel;

import javax.swing.*;
import java.awt.*;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class AvgCircle extends JPanel {
    private double percentage;

    public AvgCircle(double percentage) {
        super(new BorderLayout(5,5));
        this.percentage = percentage;
        setBackground(new Color(220, 220, 220));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        int width = getWidth();
        int height = getHeight();

        int diameter = Math.min(width, height);
        int radius = diameter / 2;

        int x = (width - diameter) / 2;
        int y = (height - diameter) / 2;

        g.setColor(new Color(229,229,229));
        g.fillOval(x, y, diameter, diameter);

        double avgResult =
                BigDecimal.valueOf(percentage/10)
                        .setScale(1, RoundingMode.HALF_UP)
                        .doubleValue();




        g.setColor(avgResult < 6 ? new Color(238, 109, 109) : new Color(39,174,96));
        g.fillArc(x, y, diameter, diameter, 90, (int) (360 * (percentage / 100.0)));

        // Draw centered text
        FontMetrics fm = g.getFontMetrics();



        double textWidth = fm.getStringBounds(Double.toString(avgResult), g).getWidth();
        g.setColor(new Color(51,51,51));
        g.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 12));
        g.drawString("Class avg: ".concat(Double.toString(avgResult)).toUpperCase(), (int) ((x + radius/3) - textWidth/2),
                (int) ((y + radius) + fm.getMaxAscent() / 2));


    }
}