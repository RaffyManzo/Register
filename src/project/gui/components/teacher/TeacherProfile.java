package project.gui.components.teacher;

import project.database.objects.Teacher;
import project.gui.components.rounded.RoundedPanel;
import project.util.HeadedFrame;
import project.util.Profile;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.Objects;

public class TeacherProfile extends JFrame implements Profile, HeadedFrame {

    private Teacher teacher;
    private GridBagConstraints gb = new GridBagConstraints();

    public TeacherProfile(Teacher teacher) {
        this.teacher = teacher;
        JPanel master = new JPanel(new GridBagLayout());
        setContentPane(master);
        setTitle("%s %s - Profile".formatted(teacher.getCognome(), teacher.getNome()));
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



        addInfoHead(this, "Teacher");

        addHead("Profile info:", master);

        addInfo(profileInfoPan);

        gb.gridy = 4;
        gb.gridx = 1;
        gb.weighty = 0.8;
        gb.weightx = 1;
        gb.anchor = GridBagConstraints.PAGE_START;
        gb.fill = GridBagConstraints.BOTH;
        gb.insets = new Insets(5,0,5,0);

        add(profileInfoPan, gb);

        setVisible(true);
    }


    @Override
    public String getNome() {
        return teacher.getNome();
    }

    @Override
    public String getCognome() {
        return teacher.getCognome();
    }

    @Override
    public String getMatricola() {
        return teacher.getMatricola();
    }

    @Override
    public java.sql.Date getData() {
        return teacher.getDataDiNascita();
    }

    @Override
    public String getResidenza() {
        return teacher.getResidenza();
    }
}
