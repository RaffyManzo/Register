package project.gui.components.teacher;

import project.database.*;
import project.database.objects.Class;
import project.database.objects.Student;
import project.database.objects.Teacher;
import project.gui.components.rounded.RoundedButton;
import project.gui.components.rounded.RoundedLabel;
import project.gui.components.rounded.RoundedPanel;
import project.util.Type;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Objects;

public class AttendanceList extends RoundedPanel {
    private final GridBagConstraints gb;

    private String selectedClass;
    private Teacher tc;

    public AttendanceList(String param) {
        super(new GridBagLayout());
        tc = new TeacherDAO().findByEmail(param).get(0);
        gb = new GridBagConstraints();

        setMinimumSize(new Dimension(440, 800));
        setMaximumSize(new Dimension(440, 800));

        JPanel pan = definePanel();

        //pan.setBorder(new LineBorder(Color.RED));
        JComboBox<String> cbox = defineComboBox(pan);

        try {
            ClassLoader loader = Thread.currentThread().getContextClassLoader();
            ImageIcon image = new ImageIcon(ImageIO.read(Objects.requireNonNull(loader.getResourceAsStream("assets/refresh-ccw.png"))));

            JButton refresh = new RoundedButton(image);
            refresh.setBackground(new Color(242, 242, 242, 1));
            refresh.setBorder(BorderFactory.createLineBorder(Color.WHITE, 1));
            refresh.setFocusPainted(false);

            refresh.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (pan.getComponents().length > 0) {
                        pan.removeAll();
                        populateAttendance(selectedClass, pan);

                        SwingUtilities.updateComponentTreeUI(pan);
                    }
                }
            });

            gb.gridx = 1;
            gb.gridy = 0;
            gb.weightx = 0.01;
            gb.gridwidth = 1;
            gb.weighty = 0.01;
            gb.anchor = GridBagConstraints.CENTER;
            gb.fill = GridBagConstraints.BOTH;
            gb.insets = new Insets(5, 5, 5, 5);

            add(refresh, gb);
        } catch (IOException e) {
            e.printStackTrace();
        }


        //cbox.setBorder(new LineBorder(Color.RED));
        addHeadList();

        //this.setBorder(new LineBorder(Color.RED));
    }

    // ----------------------------- ATTENDANCE HEAD --------------------------
    private void addHeadList() {
        gb.gridx = 0;
        gb.gridy = 1;
        gb.weightx = 1;
        gb.weighty = 0.01;
        gb.gridwidth = 2;
        gb.anchor = GridBagConstraints.PAGE_START;
        gb.fill = GridBagConstraints.BOTH;
        gb.insets = new Insets(0, 0, 5, 0);

        add(defineHead(), gb);
    }

    /**
     * Define the head panel, containing 2 labels
     *
     * @return JPanel container
     */
    private JPanel defineHead() {
        JPanel head = new RoundedPanel(new BorderLayout(10, 15));
        head.setBackground(new Color(155, 81, 224));
        //head.setBorder(new LineBorder(Color.RED));

        JLabel label = new JLabel("Attendance list");

        label.setForeground(new Color(242, 242, 242));
        label.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 20));
        label.setAlignmentX(JLabel.CENTER);
        head.add(label, BorderLayout.WEST);


        JLabel avgLab = new JLabel("Avg");
        avgLab.setForeground(new Color(242, 242, 242));
        avgLab.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 20));
        avgLab.setAlignmentX(JLabel.WEST);
        head.add(avgLab, BorderLayout.EAST);

        return head;
    }


    private JComboBox<String> defineComboBox(JPanel pan) {
        JComboBox<String> classBox = new JComboBox<>();

        classBox.setBackground(new Color(242, 242, 242));
        classBox.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(),
                BorderFactory.createLineBorder(new Color(242, 242, 242), 0)));

        gb.gridx = 0;
        gb.gridy = 0;
        gb.weightx = 1;
        gb.weighty = 0.01;
        gb.gridwidth = 1;

        gb.anchor = GridBagConstraints.PAGE_START;
        gb.fill = GridBagConstraints.BOTH;
        gb.insets = new Insets(5, 5, 5, 5);


        add(classBox, gb);

        ArrayList<Class> classes = (ArrayList<Class>) new ClassDAO().classList();

        for (project.database.objects.Class c : classes) {
            classBox.addItem(c.getNumero() + " - " + c.getSezione() + "  [" + c.getIndirizzo() + "]");
            classBox.setForeground(Color.BLACK);
            classBox.setFont(new Font(Font.SANS_SERIF, Font.ITALIC, 16));


            classBox.addItemListener(e -> {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    selectedClass = classes.get(classBox.getSelectedIndex()).getId();

                    System.out.println(selectedClass);

                    //System.out.println(classes.get(classBox.getSelectedIndex()).getId());

                    populateAttendance(classes.get(classBox.getSelectedIndex()).getId(), pan);
                    classBox.setEnabled(false);

                    JPanel dashboard = new DashboardTeacher(new GridBagLayout(), selectedClass);

                    dashboard.setMinimumSize(new Dimension(1000, 800));

                    gb.gridy = 0;
                    gb.gridx = 1;
                    gb.weighty = 1;
                    gb.weightx = 1;
                    gb.gridwidth = 2;
                    gb.insets = new Insets(15, 0, 15, 15);
                    gb.anchor = GridBagConstraints.PAGE_START;
                    gb.fill = GridBagConstraints.BOTH;

                    //dashboard.setBorder(new LineBorder(Color.RED));

                    JFrame.getFrames()[JFrame.getFrames().length - 1].add(dashboard, gb);
                    SwingUtilities.updateComponentTreeUI(JFrame.getFrames()[JFrame.getFrames().length - 1]);
                }
            });
        }

        return classBox;
    }

    /**
     * Define the master attendance panel container
     *
     * @return
     */
    private JPanel definePanel() {
        JPanel pan = new JPanel(new GridBagLayout());

        gb.gridx = 0;
        gb.gridy = 2;
        gb.weightx = 1;
        gb.gridwidth = 2;
        gb.weighty = 1;
        gb.anchor = GridBagConstraints.PAGE_START;
        gb.fill = GridBagConstraints.BOTH;

        add(pan, gb);
        //pan.setBorder(new LineBorder(Color.RED));

        return pan;
    }

    private void populateAttendance(String classId, JPanel pn) {
        int i = 0;
        ArrayList<Student> students = (ArrayList<Student>) new StudentDAO().getAllStudentByClass(classId);
        Dictionary<String, String> studentsEvent = new AttendanceEventDAO().getStudentListOfAttendanceEventByDate(classId);

        for (Student st : students) {
            JPanel stCont = new RoundedPanel(new BorderLayout());
            stCont.setBackground(Color.WHITE);
            String studentName = ((st.getCognome() + "  " + st.getNome()).toUpperCase());
            String eventsDot = null;

            if (studentsEvent.get(st.getMatricola()) != null) {
                if (Type.ASSENZA.isEquals(studentsEvent.get(st.getMatricola())))
                    eventsDot = "(%s)".formatted(Type.ASSENZA.name());
                else if (Type.RITARDO.isEquals(studentsEvent.get(st.getMatricola())))
                    eventsDot = "(%s)".formatted(Type.RITARDO.name());
                else if (Type.USCITA.isEquals(studentsEvent.get(st.getMatricola())))
                    eventsDot = "(%s)".formatted(Type.USCITA.name());
                else if (Type.ENTRATA.isEquals(studentsEvent.get(st.getMatricola())))
                    eventsDot = "(%s)".formatted(Type.ENTRATA.name());
            } else eventsDot = "";

            JLabel studentLabel = new RoundedLabel(studentName);
            studentLabel.setForeground(Color.BLACK);
            studentLabel.setBackground(Color.WHITE);
            studentLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 12));

            JLabel eventLabel = new RoundedLabel(eventsDot);
            eventLabel.setForeground(Color.BLACK);
            eventLabel.setBackground(Color.WHITE);
            eventLabel.setFont(new Font(Font.SANS_SERIF, Font.ITALIC, 10));

            stCont.add(studentLabel, BorderLayout.WEST);
            stCont.add(eventLabel, BorderLayout.EAST);

            //Border noBorder = new LineBorder(Color.WHITE, 0);

            gb.gridx = 0;
            gb.gridy = i;
            gb.weightx = 1;
            gb.gridwidth = 1;
            gb.weighty = 1;
            gb.anchor = GridBagConstraints.LINE_START;
            gb.fill = GridBagConstraints.BOTH;
            gb.insets = new Insets(0, 0, 10, 0);

            pn.add(stCont, gb);
            i++;
        }

        addAVGGrade(pn, students);


    }

    private void addAVGGrade(JPanel pan, ArrayList<Student> students) {
        int i = 0;
        for (Student s : students) {
            String studentID = s.getMatricola();
            String subject = new TeacherDAO().getSubjectFromId(tc.getMatricola());
            //System.out.println(subject);
            double avgResult = new GradeDAO().getAvgByStudentIdAndSubject(studentID, subject);
            avgResult =
                    BigDecimal.valueOf(avgResult)
                            .setScale(3, RoundingMode.HALF_UP)
                            .doubleValue();
            JLabel avg = new RoundedLabel("" + avgResult);
            avg.setFont(new Font(Font.SANS_SERIF, Font.ITALIC, 14));
            avg.setAlignmentX(JLabel.CENTER);

            if (avgResult < 6) {
                if (avgResult == 0.0)
                    avg.setBackground(Color.LIGHT_GRAY);
                else
                    avg.setBackground(new Color(238, 109, 109));
            } else
                avg.setBackground(new Color(39, 174, 96));
            gb.gridx = 1;
            gb.gridy = i;
            gb.weightx = 1;
            gb.gridwidth = 1;
            gb.weighty = 1;
            gb.anchor = GridBagConstraints.LINE_END;
            gb.fill = GridBagConstraints.HORIZONTAL;

            gb.insets = new Insets(0, 15, 10, 0);


            pan.add(avg, gb);
            i++;
        }
    }

    public String getSelectedClass() {
        return selectedClass;
    }

}
