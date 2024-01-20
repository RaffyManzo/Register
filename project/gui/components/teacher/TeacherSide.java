package project.gui.components.teacher;

import project.database.TeacherDAO;
import project.database.objects.Teacher;
import project.gui.components.rounded.RoundedButton;
import project.gui.components.rounded.RoundedLabel;
import project.gui.components.rounded.RoundedPanel;
import project.gui.components.teacher.AttendanceList;
import project.util.ImageAdder;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;
import java.util.Objects;

public class TeacherSide extends JFrame {
    private static final String logoImage = "assets/logotagliato.png";
    private Teacher teacher;

    public TeacherSide(String text) {
        super(text);
        accountInfo();


        // Define the termination of program on close
        //setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


        setResizable(false);
        setMinimumSize(new Dimension(1440, 800));
        //loginFrame.setSize(new Dimension(1440, 1000));

        setLayout(new GridBagLayout());
        setBackground(new Color(242,242,242));
        getContentPane().setBackground(getBackground());
        //setForeground(Color.WHITE);
        pack();
        ((JComponent) getContentPane()).setBorder(BorderFactory.createEmptyBorder(-5, 0, -1, 0));


        // Try to load the icon from assets
        try {
            ClassLoader loader = Thread.currentThread().getContextClassLoader();
            setIconImage(ImageIO.read(Objects.requireNonNull(loader.getResourceAsStream(logoImage))));
        } catch (IOException e) {
            e.printStackTrace();
        }

        //-------------------------------- CREATING COMPONENTS -----------------------------------

        GridBagConstraints gb = new GridBagConstraints();

        JPanel dashPlaceholder = new RoundedPanel(new GridBagLayout());


        dashPlaceholder.setBackground(new Color(235,235,235));

        gb.gridy = 0;
        gb.gridx = 0;
        gb.weighty = 0.2;
        gb.weightx = 0.5;
        gb.anchor = GridBagConstraints.LINE_START;
        gb.fill = GridBagConstraints.BOTH;
        gb.insets = new Insets(5,5,5,5);

        JPanel insidePlaceholder1 = new RoundedPanel(new BorderLayout());
        insidePlaceholder1.setBackground(new Color(220, 220, 220));

        dashPlaceholder.add(insidePlaceholder1, gb);

        gb.gridy = 0;
        gb.gridx = 1;
        gb.weighty = 0.2;
        gb.weightx = 0.5;
        gb.anchor = GridBagConstraints.LINE_START;
        gb.fill = GridBagConstraints.BOTH;
        gb.insets = new Insets(5,5,5,5);

        JPanel insidePlaceholder2 = new RoundedPanel(new BorderLayout());
        insidePlaceholder2.setBackground(new Color(220, 220, 220));

        dashPlaceholder.add(insidePlaceholder2, gb);

        gb.gridy = 1;
        gb.gridx = 0;
        gb.weighty = 0.8;
        gb.weightx = 0.5;
        gb.anchor = GridBagConstraints.LINE_START;
        gb.fill = GridBagConstraints.BOTH;
        gb.insets = new Insets(5,5,5,5);
        JPanel insidePlaceholder3 = new RoundedPanel(new BorderLayout());
        insidePlaceholder3.setBackground(new Color(220, 220, 220));

        dashPlaceholder.add(insidePlaceholder3, gb);

        gb.gridy = 1;
        gb.gridx = 1;
        gb.weighty = 0.8;
        gb.weightx = 0.5;
        gb.anchor = GridBagConstraints.LINE_START;
        gb.fill = GridBagConstraints.BOTH;
        gb.insets = new Insets(5,5,5,5);
        JPanel insidePlaceholder4 = new RoundedPanel(new BorderLayout());
        insidePlaceholder4.setBackground(new Color(220, 220, 220));

        dashPlaceholder.add(insidePlaceholder4, gb);



        AttendanceList atList = new AttendanceList(teacher.getEmail(), dashPlaceholder);



        gb.gridy = 0;
        gb.gridx = 0;
        gb.weighty = 1;
        gb.weightx = 0.05;
        gb.anchor = GridBagConstraints.LINE_START;
        gb.fill = GridBagConstraints.VERTICAL;
        gb.insets = new Insets(5,5,5,5);
        add(atList, gb);

        gb = new GridBagConstraints();

        gb.gridy = 0;
        gb.gridx = 1;
        gb.weighty = 1;
        gb.weightx = 1;
        gb.anchor = GridBagConstraints.LINE_START;
        gb.fill = GridBagConstraints.BOTH;
        gb.insets = new Insets(5,5,5,5);

        add(dashPlaceholder, gb);



        getRootPane().setBorder(BorderFactory.createMatteBorder(5,5,5,5, new Color(242,242,242)));


        // ---------------------------------------------------------------------------------------

        setVisible(true);
    }

    private void accountInfo() {
       teacher = new TeacherDAO().getInfo(getTitle());
       setTitle("[" + teacher.getMatricola() + "] " + teacher.getCognome() + " - " + teacher.getNome());
    }
}
