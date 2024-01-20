package project.util;

import project.database.ClassDAO;
import project.gui.components.rounded.RoundedLabel;
import project.gui.components.rounded.RoundedPanel;

import javax.swing.*;
import java.awt.*;

public interface HeadedFrame {

    GridBagConstraints gb = new GridBagConstraints();

    default void addInfoHead(Container c) {
        ImageAdder img = new ImageAdder();
        img.setImage("assets/logotagliato.png");

        gb.gridy = 0;
        gb.gridx = 1;
        gb.weighty = 0.01;
        gb.weightx = 1;
        gb.anchor = GridBagConstraints.PAGE_START;
        gb.fill = GridBagConstraints.BOTH;

        c.add(img, gb);
        img.setHorizontalAlignment(JLabel.CENTER);

        JPanel studentInfoPan = new RoundedPanel(new GridBagLayout());
        JLabel registry = new RoundedLabel("Student: %s %s".formatted(getNome(), getCognome()).toUpperCase());
        registry.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 22));
        registry.setHorizontalAlignment(JLabel.CENTER);

        project.database.objects.Class studentClass = new ClassDAO().getClassByStudentID(getMatricola());

        JLabel classLabel = new RoundedLabel("CLASS: %s%s - %s".formatted(studentClass.getNumero(), studentClass.getSezione(), studentClass.getIndirizzo()));
        classLabel.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 18));
        classLabel.setForeground(Color.GRAY);
        classLabel.setHorizontalAlignment(JLabel.CENTER);


        gb.gridy = 0;
        gb.insets = new Insets(0,0,0,0);
        studentInfoPan.add(registry, gb);
        gb.gridy = 1;
        gb.anchor = GridBagConstraints.PAGE_START;
        studentInfoPan.add(classLabel, gb);

        gb.gridy = 1;
        gb.gridx = 1;
        gb.weighty = 0.01;
        gb.weightx = 0.1;
        gb.anchor = GridBagConstraints.PAGE_START;
        gb.fill = GridBagConstraints.BOTH;

        c.add(studentInfoPan, gb);
    }

    default void addInfoHead(Container c, String txt) {
        ImageAdder img = new ImageAdder();
        img.setImage("assets/logotagliato.png");

        gb.gridy = 0;
        gb.gridx = 1;
        gb.weighty = 0.01;
        gb.weightx = 1;
        gb.anchor = GridBagConstraints.PAGE_START;
        gb.fill = GridBagConstraints.BOTH;

        c.add(img, gb);
        img.setHorizontalAlignment(JLabel.CENTER);

        JPanel infoPan = new RoundedPanel(new GridBagLayout());
        JLabel registry = new RoundedLabel("%s: %s %s".formatted(txt, getNome(), getCognome()).toUpperCase());
        registry.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 20));
        registry.setHorizontalAlignment(JLabel.CENTER);

        gb.gridy = 0;
        gb.anchor = GridBagConstraints.CENTER;
        gb.insets = new Insets(0,0,0,0);
        infoPan.add(registry, gb);

        gb.gridy = 1;
        gb.gridx = 1;
        gb.weighty = 0.01;
        gb.weightx = 0.1;
        gb.anchor = GridBagConstraints.PAGE_START;
        gb.fill = GridBagConstraints.BOTH;

        c.add(infoPan, gb);
    }

    public String getNome();
    public String getCognome();
    public String getMatricola();
}
