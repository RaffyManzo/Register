package project.util;

import project.gui.components.rounded.RoundedLabel;
import project.gui.components.rounded.RoundedPanel;


import javax.swing.*;
import java.awt.*;

public interface Profile {
    GridBagConstraints gb = new GridBagConstraints();

    default void addHead(String text, JPanel master) {
        JPanel head = new RoundedPanel(new BorderLayout());
        head.setBackground(new Color(155,81,224));
        //head.setBorder(new LineBorder(Color.RED));

        JLabel label = new JLabel("<HTML><BODY><u>%s</u></BODY></HTML>".formatted(text));

        label.setForeground(new Color(242,242,242));
        label.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 18));
        label.setHorizontalAlignment(JLabel.CENTER);
        head.add(label, BorderLayout.WEST);

        gb.gridy = 3;
        gb.gridx = 0;
        gb.weighty = 0.1;
        gb.weightx = 1;
        gb.gridwidth = 3;
        gb.fill = GridBagConstraints.HORIZONTAL;
        gb.anchor = GridBagConstraints.PAGE_START;
        gb.insets = new Insets(5, 5, 5, 5);

        master.add(head, gb);
    }

     default void addInfo(Container pan) {
        gb.anchor = GridBagConstraints.CENTER;
        gb.gridx = 0;
        gb.insets = new Insets(5,0,5,0);
        gb.fill = GridBagConstraints.BOTH;
        gb.weightx = 1;
        gb.weighty = 0.01;

        gb.gridy = 0;
        pan.add(createLayerComponent("ID", getMatricola()), gb);
        gb.gridy = 1;
        pan.add(createLayerComponent("Name", getNome()), gb);
        gb.gridy = 2;
        pan.add(createLayerComponent("Surname", getCognome()), gb);
        gb.gridy = 3;
        pan.add(createLayerComponent("Birth date", getData().toString()), gb);
        gb.gridy = 4;
        pan.add(createLayerComponent("Adress", getResidenza()), gb);

        gb.weighty = 1;
        gb.gridy = 5;
        pan.add(new JLabel(), gb);
     }

     private JPanel createLayerComponent(String leftString, String rightString) {
        JPanel pan = new RoundedPanel(new BorderLayout(0,20));
        JLabel left = new JLabel(leftString);
        JLabel right = new JLabel(rightString);
        pan.setBackground(Color.WHITE);
        adjustInfoLabel(left, new Color(51,51, 51));
        adjustInfoLabel(right, Color.GRAY);

        pan.add(left, BorderLayout.WEST);
        pan.add(right, BorderLayout.EAST);

        return pan;
     }

     private void adjustInfoLabel(JLabel label, Color c) {
         label.setForeground(c);
         label.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 14));

     }

    String getNome();

    String getCognome();

    public String getMatricola();

    public java.sql.Date getData();
    public String getResidenza();
}
