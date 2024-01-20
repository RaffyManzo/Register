package project.gui.components.student;

import project.database.ClassDAO;
import project.database.GradeDAO;
import project.database.StudentDAO;
import project.database.objects.Grade;
import project.database.objects.Student;
import project.gui.components.rounded.RoundedLabel;
import project.gui.components.rounded.RoundedPanel;
import project.gui.components.rounded.RoundedScrollPane;
import project.util.HeadedFrame;
import project.util.ImageAdder;
import project.util.Type;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Objects;

public class AllStudentGrades extends JFrame implements HeadedFrame {
    private Student student;
    private GridBagConstraints gb = new GridBagConstraints();

    private ArrayList<Grade> grades;

    public AllStudentGrades(String studentID) {
        student = new StudentDAO().getByID(studentID);
        setLayout(new GridBagLayout());
        setTitle("%s %s - All grades".formatted(student.getCognome(), student.getNome()));
        setResizable(false);
        setMinimumSize(new Dimension(700, 800));

        setBackground(new Color(255,255,255));
        pack();


        // Try to load the icon from assets
        try {
            ClassLoader loader = Thread.currentThread().getContextClassLoader();
            setIconImage(ImageIO.read(Objects.requireNonNull(loader.getResourceAsStream("assets/logotagliato.png"))));
        } catch (IOException e) {
            e.printStackTrace();
        }


        addInfoHead(this);

        addAvgsPanel();

        addGradePanel();

        setVisible(true);
    }

    private void addGradePanel() {
        JPanel masterGradesPanel = new RoundedPanel(new GridBagLayout());
        masterGradesPanel.setBackground(getBackground());
        addHead(masterGradesPanel, "All grades");

        grades = (ArrayList<Grade>) new GradeDAO().getAllStudentGrades(student.getMatricola());
        JPanel gradesPanel = new RoundedPanel(new GridBagLayout());
        gradesPanel.setBackground(getBackground());

        if(grades.isEmpty()) {
            JLabel result = new JLabel("0 grades");
            result.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 16));
            result.setForeground(Color.BLACK);
            result.setHorizontalAlignment(JLabel.CENTER);;

            gb.gridy = 1;
            gb.gridx = 0;
            gb.weighty = 0.1;
            gb.weightx = 1;
            gb.fill = GridBagConstraints.BOTH;
            gb.anchor = GridBagConstraints.PAGE_START;
            gb.insets = new Insets(5, 5, 5, 5);

            gradesPanel.add(result, gb);
        } else {

            int i = 0;
            for (Grade g : grades) {

                JPanel singleGradePan = new RoundedPanel(new BorderLayout(15, 15));

                singleGradePan.setBackground(new Color(242,242,242));

                JLabel grade = new JLabel("%.2f in %s".formatted(
                        BigDecimal.valueOf(g.getVoto())
                                .setScale(2, RoundingMode.HALF_UP)
                                .doubleValue(), g.getMateria()));
                grade.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 18));
                grade.setForeground(Color.BLACK);
                grade.setAlignmentX(JLabel.WEST);

                if(g.getNota() != null) {
                    JLabel note = new JLabel("Comment: \"%s\"".formatted(g.getNota()));
                    note.setFont(new Font(Font.SANS_SERIF, Font.ITALIC, 12));
                    note.setForeground(Color.GRAY);

                    singleGradePan.add(note, BorderLayout.PAGE_END);
                }

                JLabel date = new JLabel(g.getData().toString());
                date.setFont(new Font(Font.SANS_SERIF, Font.ITALIC, 14));
                date.setForeground(Color.GRAY);

                JLabel type = new JLabel("(%s)".formatted(g.getTipo()));
                type.setFont(new Font(Font.SANS_SERIF, Font.ITALIC, 14));
                type.setForeground(Color.GRAY);


                singleGradePan.add(grade, BorderLayout.WEST);
                singleGradePan.add(type, BorderLayout.CENTER);
                singleGradePan.add(date, BorderLayout.EAST);

                gb.gridy = i;
                gb.gridx = 0;
                gb.weighty = 0.1;
                gb.weightx = 1;
                gb.fill = GridBagConstraints.HORIZONTAL;
                gb.anchor = GridBagConstraints.PAGE_START;
                gb.insets = new Insets(0, 5, 5, 5);

                gradesPanel.add(singleGradePan, gb);
                i++;
            }
        }

        JScrollPane scrollPane = new RoundedScrollPane();
        scrollPane.setBackground(Color.white);
        scrollPane.setViewportView(gradesPanel);

        gb.gridy = 1;
        gb.gridx = 1;
        gb.anchor = GridBagConstraints.PAGE_START;
        gb.fill = GridBagConstraints.BOTH;
        gb.weighty = 1;
        gb.weightx = 1;
        gb.gridwidth = 3;
        gb.insets = new Insets(5,5,5,5);

        masterGradesPanel.add(scrollPane, gb);

        gb.gridy = 4;
        gb.gridx = 0;
        gb.anchor = GridBagConstraints.PAGE_START;
        gb.fill = GridBagConstraints.BOTH;
        gb.weighty = 1;
        gb.weightx = 1;
        gb.gridwidth = 3;
        gb.insets = new Insets(5,5,5,5);
        add(masterGradesPanel,gb);


    }

    private void addAvgsPanel() {
        JPanel avgsPanel = new JPanel(new GridLayout(1, 3, 40, 10));



        defineAvgLabel(new GradeDAO().getAvgForType1(student.getMatricola()), avgsPanel, project.util.Type.SCRITTO.toString());
        defineAvgLabel(new GradeDAO().getAvgForType2(student.getMatricola()), avgsPanel, project.util.Type.ORALE.toString());
        defineAvgLabel(new GradeDAO().getAvg(student.getMatricola()), avgsPanel, "Generale");

        gb.gridy = 3;
        gb.gridx = 1;
        gb.anchor = GridBagConstraints.PAGE_START;
        gb.fill = GridBagConstraints.HORIZONTAL;
        gb.weighty = 0.1;
        gb.weightx = 1;
        gb.gridwidth = 3;
        gb.insets = new Insets(5,20,5,20);

        add(avgsPanel, gb);

    }

    private void defineAvgLabel(double grade, JPanel parent, String toAdd) {

        double truncated = BigDecimal.valueOf(grade)
                .setScale(2, RoundingMode.HALF_UP)
                .doubleValue();
        JLabel lb = new RoundedLabel(toAdd + ": " + truncated);

        if(grade < 6) {
            if (grade == 0)
                lb.setBackground(Color.LIGHT_GRAY);
            else
                lb.setBackground(new Color(238, 109, 109));
        } else
            lb.setBackground(new Color(39,174,96));

        lb.setForeground(Color.WHITE);
        lb.setHorizontalAlignment(JLabel.CENTER);
        lb.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 18));

        parent.add(lb);
    }

    private void addHead(JPanel pan, String text) {
        JPanel head = new RoundedPanel(new BorderLayout());
        head.setBackground(new Color(155,81,224));
        //head.setBorder(new LineBorder(Color.RED));

        JLabel label = new JLabel(text);

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

        pan.add(head, gb);

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
}
