package project.gui.components.teacher;

import project.database.AttendanceEventDAO;
import project.database.GradeDAO;
import project.database.StudentDAO;
import project.database.TeacherDAO;
import project.database.objects.AttendanceEvent;
import project.database.objects.Grade;
import project.database.objects.Student;
import project.database.objects.Teacher;
import project.gui.components.rounded.RoundedButton;
import project.gui.components.rounded.RoundedLabel;
import project.gui.components.rounded.RoundedPanel;
import project.gui.components.rounded.RoundedScrollPane;
import project.util.HeadedFrame;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

public class LastActivitiesList extends JFrame implements HeadedFrame {
    GridBagConstraints gb = new GridBagConstraints();
    Teacher teacher;
    String classID;

    JScrollPane scrollPane;

    public LastActivitiesList(String teacherID, String selectedClass) {
        super();
        setLayout(new GridBagLayout());

        this.teacher = new TeacherDAO().findById(teacherID);
        this.classID = selectedClass;

        setTitle("%s %s - Last activities".formatted(teacher.getCognome(), teacher.getNome()));
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

        addInfoHead(this, "Teacher");


        JPanel master = new RoundedPanel(new GridBagLayout());
        master.setBackground(new Color(220,220,220));


        gb.gridx = 0;
        gb.gridy = 3;
        gb.weightx = 1;
        gb.weighty = 1;
        gb.gridwidth = 3;
        gb.fill = GridBagConstraints.BOTH;
        gb.anchor = GridBagConstraints.PAGE_START;
        gb.insets = new Insets(3, 3, 3, 3);

        add(master, gb);

        JPanel head = new RoundedPanel(new BorderLayout());
        head.setBackground(new Color(155,81,224));
        //head.setBorder(new LineBorder(Color.RED));

        JLabel label = new JLabel("<HTML><BODY><u>Last activities</u></BODY></HTML> :");

        label.setForeground(new Color(242,242,242));
        label.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 18));
        label.setHorizontalAlignment(JLabel.CENTER);
        head.add(label, BorderLayout.WEST);

        gb.gridy = 0;
        gb.gridx = 0;
        gb.weighty = 0.1;
        gb.weightx = 1;
        gb.gridwidth = 3;
        gb.fill = GridBagConstraints.HORIZONTAL;
        gb.anchor = GridBagConstraints.PAGE_START;
        gb.insets = new Insets(5, 5, 5, 5);

        master.add(head, gb);
        addLastActivies(master);

        setVisible(true);

    }

    private void listElement() {
        scrollPane.getViewport().removeAll();
        scrollPane.setViewportView(null);

        ArrayList<Grade> grades = (ArrayList<Grade>) new GradeDAO().listOfGradeOrderByDate(teacher.getMateria(), classID);
        ArrayList<AttendanceEvent> events = (ArrayList<AttendanceEvent>) new AttendanceEventDAO().listOfEventsOrderByDate(teacher.getMatricola(), classID);

        JPanel scrollPaneView = new JPanel(new GridBagLayout());

        scrollPaneView.setBackground(new Color(220,220,220));
        int i = 0;

        insertIntoPanelEvent(events, scrollPaneView, i);
        i = events.size();
        insertIntoPanelGrade(grades, scrollPaneView, i);


        scrollPane.setViewportView(scrollPaneView);
        SwingUtilities.updateComponentTreeUI(this);
    }

    private void addLastActivies(JPanel master) {
        scrollPane = new RoundedScrollPane();

        listElement();

        scrollPane.setBackground(new Color(220,220,220));
        gb.gridy = 1;
        gb.gridx = 0;
        gb.weighty = 1;
        gb.weightx = 1;
        gb.gridwidth = 3;
        gb.fill = GridBagConstraints.BOTH;
        gb.anchor = GridBagConstraints.PAGE_START;
        gb.insets = new Insets(5, 5, 5, 5);
        master.add(scrollPane, gb);
    }

    private void insertIntoPanelGrade(ArrayList<Grade> grades, JPanel scrollPaneView, int i) {
        for(Grade g : grades) {
            gb.gridy = i;
            gb.gridx = 0;
            gb.weighty = 0.1;
            gb.weightx = 1;
            gb.fill = GridBagConstraints.HORIZONTAL;
            gb.anchor = GridBagConstraints.PAGE_START;
            gb.insets = new Insets(5, 5, 5, 5);

            scrollPaneView.add(createLabel(g), gb);
            i++;
        }
    }

    private void insertIntoPanelEvent(ArrayList<AttendanceEvent> events, JPanel scrollPaneView, int i) {
        for (AttendanceEvent event : events) {
            gb.gridy = i;
            gb.gridx = 0;
            gb.weighty = 0.1;
            gb.weightx = 1;
            gb.fill = GridBagConstraints.HORIZONTAL;
            gb.anchor = GridBagConstraints.PAGE_START;
            gb.insets = new Insets(5, 5, 5, 5);

            scrollPaneView.add(createLabel(event), gb);
            i++;
        }
    }


    private JPanel createLabel(Grade g) {
        JPanel pan = new RoundedPanel(new BorderLayout(10,10));
        pan.setBackground(Color.WHITE);

        Student student = new StudentDAO().getByID(g.getStudenteId());

        JLabel info = new JLabel("Grade: %s: %s %s".formatted(g.getVoto(), student.getCognome(), student.getNome()));

        if(g.getNota() != null) {
            JLabel note = new JLabel("Comment: \"%s\"".formatted(g.getNota()));
            note.setFont(new Font(Font.SANS_SERIF, Font.ITALIC, 12));
            note.setForeground(Color.GRAY);

            pan.add(note, BorderLayout.PAGE_END);
        }

        info.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 16));
        info.setForeground(new Color(51,51,51));

        JPanel actionPanel = new JPanel(new GridLayout(1, 2, 5, 5));
        actionPanel.setBackground(Color.WHITE);
        JButton modify = null;
        JButton delete = null;

        try {

            ClassLoader loader = Thread.currentThread().getContextClassLoader();
            // get images
            ImageIcon modifyImg = new ImageIcon(ImageIO.read(
                    Objects.requireNonNull(
                            loader.getResourceAsStream(
                                    "assets/pen-line.png"
                            ))));
            ImageIcon deleteImg = new ImageIcon(ImageIO.read(
                    Objects.requireNonNull(
                            loader.getResourceAsStream(
                                    "assets/trash.png"
                            ))));

            modify = new RoundedButton(modifyImg);
            delete = new RoundedButton(deleteImg);

            modify.setBackground(new Color(155,81,224));
            delete.setBackground(new Color(155,81,224));

            modify.setFocusPainted(false);
            modify.setBorderPainted(false);
            delete.setFocusPainted(false);
            delete.setBorderPainted(false);
            delete.addActionListener(e -> removeGradeAndUpdate(g.getID()));
        } catch (IOException e) {
           e.printStackTrace();
        }

        //actionPanel.add(modify);
        actionPanel.add(delete);

        pan.add(info, BorderLayout.WEST);
        pan.add(defineDateLabel("%s, %S".formatted(g.getTipo(), g.getData().toString())), BorderLayout.CENTER);
        pan.add(actionPanel, BorderLayout.EAST);


        return pan;
    }

    private JLabel defineDateLabel(String date) {
        JLabel dateLabel = new JLabel("(%s)".formatted(date));
        dateLabel.setFont(new Font(Font.SANS_SERIF, Font.ITALIC, 12));
        dateLabel.setForeground(Color.GRAY);

        return dateLabel;
    }

    private JPanel createLabel(AttendanceEvent ate) {
        JPanel pan = new RoundedPanel(new BorderLayout(10,10));
        pan.setBackground(Color.WHITE);

        Student student = new StudentDAO().getByID(ate.getStudentId());

        JLabel info = new JLabel(ate.getTipo() + ": " + student.getCognome() + " " + student.getNome());
        info.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 16));
        info.setForeground(new Color(51,51,51));

        JPanel actionPanel = new JPanel(new GridLayout(1, 2, 5, 5));
        actionPanel.setBackground(Color.WHITE);
        JButton modify = null;
        JButton delete = null;

        try {

            ClassLoader loader = Thread.currentThread().getContextClassLoader();
            // get images
            ImageIcon modifyImg = new ImageIcon(ImageIO.read(
                    Objects.requireNonNull(
                            loader.getResourceAsStream(
                                    "assets/pen-line.png"
                            ))));
            ImageIcon deleteImg = new ImageIcon(ImageIO.read(
                    Objects.requireNonNull(
                            loader.getResourceAsStream(
                                    "assets/trash.png"
                            ))));

            modify = new RoundedButton(modifyImg);
            delete = new RoundedButton(deleteImg);

            modify.setBackground(new Color(155,81,224));
            delete.setBackground(new Color(155,81,224));

            modify.setFocusPainted(false);
            modify.setBorderPainted(false);
            delete.setFocusPainted(false);
            delete.setBorderPainted(false);

            delete.addActionListener(e -> removeEventAndUpdate(ate.getID()));
        } catch (IOException e) {
            e.printStackTrace();
        }

        //actionPanel.add(modify);
        actionPanel.add(delete);

        pan.add(info, BorderLayout.WEST);
        pan.add(defineDateLabel(ate.getData().toString()), BorderLayout.CENTER);
        pan.add(actionPanel, BorderLayout.EAST);


        return pan;
    }


    private void removeEventAndUpdate(int id) {
        new AttendanceEventDAO().removeByID(id);
        listElement();
    }

    private void removeGradeAndUpdate(int id) {
        new GradeDAO().removeByID(id);
        listElement();
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
}
