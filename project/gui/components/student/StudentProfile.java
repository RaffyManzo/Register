package project.gui.components.student;

import project.database.objects.Student;
import project.database.objects.Teacher;
import project.gui.components.rounded.RoundedPanel;
import project.util.HeadedFrame;
import project.util.Profile;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.Objects;

public class StudentProfile extends JFrame implements Profile, HeadedFrame {

    private Student student;
    private GridBagConstraints gb = new GridBagConstraints();

    public StudentProfile(Student student) {
        this.student = student;
        JPanel master = new JPanel(new GridBagLayout());
        setContentPane(master);
        setTitle("%s %s - Profile".formatted(student.getCognome(), student.getNome()));
        setResizable(false);
        setMinimumSize(new Dimension(700, 800));
        getRootPane().setBorder(BorderFactory.createMatteBorder(15,15,15,15, getBackground()));


        pack();


        // Try to load the icon from assets
        try {
            ClassLoader loader = Thread.currentThread().getContextClassLoader();
            setIconImage(ImageIO.read(Objects.requireNonNull(loader.getResourceAsStream("assets/logotagliato.png"))));
        } catch (IOException e) {
            e.printStackTrace();
        }

        JPanel profileInfoPan = new RoundedPanel(new GridBagLayout());
        profileInfoPan.setBackground(new Color(220,220,220));



        addInfoHead(this);

        addHead("Profile info:", profileInfoPan);

        addInfo(profileInfoPan);

        gb.gridy = 3;
        gb.gridx = 0;
        gb.weighty = 0.8;
        gb.weightx = 1;
        gb.anchor = GridBagConstraints.PAGE_START;
        gb.fill = GridBagConstraints.BOTH;
        gb.insets = new Insets(5,5,5,5);

        add(profileInfoPan, gb);

        setVisible(true);
    }


    @Override
    public String getNome() {
        return student.getNome();
    }

    @Override
    public String getCognome() {
        return student.getCognome();
    }

    @Override
    public String getMatricola() {
        return student.getMatricola();
    }

    @Override
    public java.sql.Date getData() {
        return student.getDataDiNascita();
    }

    @Override
    public String getResidenza() {
        return student.getResidenza();
    }

    @Override
    public String getEmail() {
        return student.getEmail();
    }

}
