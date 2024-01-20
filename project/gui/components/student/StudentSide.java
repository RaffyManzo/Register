package project.gui.components.student;

import org.jfree.chart.ChartPanel;
import project.database.AttendanceEventDAO;
import project.database.GradeDAO;
import project.database.StudentDAO;
import project.database.TeacherDAO;
import project.database.objects.AttendanceEvent;
import project.database.objects.Grade;
import project.database.objects.Student;
import project.database.objects.Teacher;
import project.gui.components.student.GradesChartPanel;
import project.gui.components.rounded.RoundedLabel;
import project.gui.components.rounded.RoundedPanel;
import project.gui.components.rounded.RoundedScrollPane;
import project.gui.components.student.StudentProfile;
import project.util.ImageAdder;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Objects;

public class StudentSide extends JFrame {
    private Student loggedStudent;
    private GridBagConstraints gb = new GridBagConstraints();
    private static final String logoImage = "assets/logotagliato.png";

    public StudentSide(String email) {
        this.loggedStudent = new StudentDAO().findByEmail(email).get(0);
        setLayout(new GridBagLayout());

        addSidePanel();

        gb = new GridBagConstraints();
        addChart();
        gb = new GridBagConstraints();
        addWeeklyGradePanel();
        gb = new GridBagConstraints();
        addTodayEvents();


        setTitle("[%s] %s %s".formatted(loggedStudent.getMatricola(), loggedStudent.getNome(), loggedStudent.getCognome()));
        setResizable(false);
        setMinimumSize(new Dimension(1440, 800));

        setBackground(new Color(255,255,255));
        pack();


        // Try to load the icon from assets
        try {
            ClassLoader loader = Thread.currentThread().getContextClassLoader();
            setIconImage(ImageIO.read(Objects.requireNonNull(loader.getResourceAsStream(logoImage))));
        } catch (IOException e) {
            e.printStackTrace();
        }
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    private void addWeeklyGradePanel() {

        JPanel masterGradesPanel = new RoundedPanel(new GridBagLayout());
        masterGradesPanel.setBackground(Color.WHITE);
        addHead(masterGradesPanel, "Weekly grades");

        ArrayList<Grade> grades = (ArrayList<Grade>) new GradeDAO().getWeeklyStudentGrades(loggedStudent.getMatricola());
        JPanel gradesPanel = new RoundedPanel(new GridBagLayout());
        gradesPanel.setBackground(Color.WHITE);

        if(grades.isEmpty()) {
            JLabel result = new JLabel("0 grades");
            result.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 16));
            result.setForeground(new Color(51,51,51));
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

                JPanel singleGradePan = new RoundedPanel(new BorderLayout(15, 0));

                singleGradePan.setBackground(new Color(242,242,242));

                JLabel grade = new JLabel("%.2f in %s".formatted(
                        BigDecimal.valueOf(g.getVoto())
                        .setScale(2, RoundingMode.HALF_UP)
                        .doubleValue(), g.getMateria()));
                grade.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 18));
                grade.setForeground(Color.BLACK);
                grade.setAlignmentX(JLabel.WEST);

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
                gb.insets = new Insets(5, 5, 5, 5);

                gradesPanel.add(singleGradePan, gb);
                i++;
            }
        }

        JScrollPane scrollPane = new RoundedScrollPane();
        scrollPane.setBackground(Color.white);
        scrollPane.setViewportView(gradesPanel);

        gb.gridy = 1;
        gb.gridx = 0;
        gb.anchor = GridBagConstraints.LINE_START;
        gb.fill = GridBagConstraints.BOTH;
        gb.weighty = 1;
        gb.weightx = 1;
        gb.gridwidth = 1;
        gb.insets = new Insets(5,5,5,5);

        masterGradesPanel.add(scrollPane, gb);

        gb.gridy = 1;
        gb.gridx = 1;
        add(masterGradesPanel,gb);
    }

    private void addTodayEvents() {

        JPanel eventPan = new RoundedPanel(new GridBagLayout());
        eventPan.setBackground(Color.WHITE);
        addHead(eventPan, "Today");

        ArrayList<AttendanceEvent> events = (ArrayList<AttendanceEvent>) new AttendanceEventDAO().getTodayEvents(loggedStudent.getMatricola());

        if(events.isEmpty()) {
            JPanel insidePan = new RoundedPanel(new BorderLayout());
            insidePan.setBackground(Color.white);
            JLabel result = new JLabel("Nothing is happened today");
            result.setBackground(Color.white);
            result.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 16));
            result.setForeground(Color.BLACK);
            result.setHorizontalAlignment(JLabel.CENTER);

            insidePan.add(result, BorderLayout.CENTER);

            gb.gridy = 1;
            gb.gridx = 0;
            gb.weighty = 1;
            gb.weightx = 1;
            gb.fill = GridBagConstraints.BOTH;
            gb.anchor = GridBagConstraints.CENTER;
            gb.insets = new Insets(5, 5, 5, 5);

            eventPan.add(insidePan, gb);
        } else {

            for (AttendanceEvent ae : events) {
                JPanel singleEventPan = new RoundedPanel(new BorderLayout(15, 0));

                singleEventPan.setBackground(new Color(242,242,242));

                JLabel type = new JLabel(ae.getTipo());
                type.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 18));
                type.setForeground(Color.BLACK);
                type.setAlignmentX(JLabel.WEST);

                JLabel date = new JLabel(ae.getData().toString());
                date.setFont(new Font(Font.SANS_SERIF, Font.ITALIC, 14));
                date.setForeground(Color.GRAY);

                Teacher tc = new TeacherDAO().findById(ae.getTeacherId()).get(0);

                JLabel teacher = new JLabel("(added by %s %s)".formatted(tc.getCognome(), tc.getNome()));
                teacher.setFont(new Font(Font.SANS_SERIF, Font.ITALIC, 14));
                teacher.setForeground(Color.GRAY);


                singleEventPan.add(type, BorderLayout.WEST);
                singleEventPan.add(teacher, BorderLayout.CENTER);
                singleEventPan.add(date, BorderLayout.EAST);

                gb.gridy = 1;
                gb.gridx = 0;
                gb.weighty = 1;
                gb.weightx = 1;
                gb.fill = GridBagConstraints.HORIZONTAL;
                gb.anchor = GridBagConstraints.PAGE_START;
                gb.insets = new Insets(0, 20, 20, 20);

                eventPan.add(singleEventPan, gb);
            }
        }

        gb.gridy = 1;
        gb.gridx = 2;
        gb.anchor = GridBagConstraints.LINE_START;
        gb.fill = GridBagConstraints.BOTH;
        gb.weighty = 1;
        gb.weightx = 1;
        gb.gridwidth = 1;
        gb.insets = new Insets(5,5,5,5);

        add(eventPan,gb);
    }

    private void addChart() {

        ChartPanel chart = new GradesChartPanel(loggedStudent.getMatricola());


        GridBagConstraints gb = new GridBagConstraints(
                0, 1, 1, 1, 1, 1,
                GridBagConstraints.CENTER,
                GridBagConstraints.BOTH,
                new Insets(0,0,0,0),
                0, 0);

        RoundedPanel rd = new RoundedPanel(new GridBagLayout());
        rd.setBackground(Color.WHITE);


        rd.add(chart, gb);

        chart.setBackground(rd.getBackground());
        chart.setOpaque(true);

        addHead(rd, "Grade trend");

        gb.gridy = 0;
        gb.gridx = 1;
        gb.anchor = GridBagConstraints.PAGE_START;
        gb.fill = GridBagConstraints.BOTH;
        gb.weighty = 0.1;
        gb.weightx = 1;
        gb.gridwidth = 2;
        gb.insets = new Insets(5,5,5,5);

        add(rd, gb);
    }

    private void addHead(JPanel pan, String text) {
        JPanel head = new RoundedPanel(new BorderLayout());
        head.setBackground(new Color(155,81,224));
        //head.setBorder(new LineBorder(Color.RED));

        JLabel label = new JLabel(text);

        label.setForeground(new Color(242,242,242));
        label.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 18));
        label.setAlignmentX(JLabel.CENTER);
        head.add(label, BorderLayout.WEST);


        gb.gridy = 0;
        gb.gridx = 0;
        gb.weighty = 0.1;
        gb.weightx = 1;
        gb.fill = GridBagConstraints.HORIZONTAL;
        gb.anchor = GridBagConstraints.PAGE_START;
        gb.insets = new Insets(5, 5, 5, 5);

        pan.add(head, gb);

    }

    private void addWindowsListenerToFrame(JFrame frame) {
        frame.addWindowListener(new WindowListener() {
            @Override
            public void windowOpened(WindowEvent e) {
                setEnabled(false);
            }

            @Override
            public void windowClosing(WindowEvent e) {
                setEnabled(true);
            }

            @Override
            public void windowClosed(WindowEvent e) {
                setEnabled(true);
            }

            @Override
            public void windowIconified(WindowEvent e) {

            }

            @Override
            public void windowDeiconified(WindowEvent e) {

            }

            @Override
            public void windowActivated(WindowEvent e) {

            }

            @Override
            public void windowDeactivated(WindowEvent e) {

            }
        });
    }


    private void addSidePanel() {
        JPanel sidePan = new RoundedPanel(new GridBagLayout());
        sidePan.setBackground(new Color(242,242,242));

        gb.gridy = 0;
        JPanel gradesPanLink = defineSideElementPan("<HTML><U>Grades</U></HTML>", "assets/book-check.png");
        sidePan.add(gradesPanLink, gb);

        gradesPanLink.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                AllStudentGrades asg = new AllStudentGrades(loggedStudent.getMatricola());
                addWindowsListenerToFrame(asg);
            }
            @Override
            public void mousePressed(MouseEvent e) {}
            @Override
            public void mouseReleased(MouseEvent e) {}
            @Override
            public void mouseEntered(MouseEvent e) {}
            @Override
            public void mouseExited(MouseEvent e) {}
        });

        gb.gridy = 1;
        JPanel eventsPanLink = defineSideElementPan("<HTML><U>Events</U></HTML>", "assets/calendar-range.png");
        sidePan.add(eventsPanLink, gb);

        eventsPanLink.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                AllStudentEvent ase = new AllStudentEvent(loggedStudent.getMatricola());
                addWindowsListenerToFrame(ase);

            }
            @Override
            public void mousePressed(MouseEvent e) {}
            @Override
            public void mouseReleased(MouseEvent e) {}
            @Override
            public void mouseEntered(MouseEvent e) {}
            @Override
            public void mouseExited(MouseEvent e) {}
        });

        gb.gridy = 2;
        JPanel profilePanLink = defineSideElementPan("<HTML><U>Profile</U></HTML>", "assets/user-round.png");
        sidePan.add(profilePanLink, gb);

        profilePanLink.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                StudentProfile prf = new StudentProfile(loggedStudent);
                addWindowsListenerToFrame(prf);
            }
            @Override
            public void mousePressed(MouseEvent e) {}
            @Override
            public void mouseReleased(MouseEvent e) {}
            @Override
            public void mouseEntered(MouseEvent e) {}
            @Override
            public void mouseExited(MouseEvent e) {}
        });

        JLabel nullObj = new JLabel();
        gb.gridx = 0;
        gb.gridy = 3;
        gb.fill = GridBagConstraints.BOTH;
        gb.weightx = 1;
        gb.insets = new Insets(5, 5, 5, 5);
        gb.anchor = GridBagConstraints.PAGE_START;
        gb.weighty = 1;

        sidePan.add(nullObj, gb);


        gb = new GridBagConstraints();

        gb.gridy = 0;
        gb.gridx = 0;
        gb.gridheight = 2;
        gb.anchor = GridBagConstraints.PAGE_START;
        gb.fill = GridBagConstraints.VERTICAL;
        gb.weighty = 1;
        gb.weightx = 0.1;
        gb.insets = new Insets(0,0,0,0);

        add(sidePan, gb);
    }

    private JPanel defineSideElementPan(String textLabel, String imagePath) {
        JPanel panel = new RoundedPanel(new BorderLayout());
        panel.setBackground(new Color(155,81,224));

        ImageAdder image = new ImageAdder();
        image.setImage(imagePath);
        image.showImage(panel, BorderLayout.WEST);

        JLabel label = new RoundedLabel(textLabel);

        label.setBackground(panel.getBackground());
        label.setForeground(Color.white);
        label.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 20));


        panel.addMouseMotionListener(new MouseMotionListener() {
            @Override
            public void mouseDragged(MouseEvent e) {}

            @Override
            public void mouseMoved(MouseEvent e) {
                final int x = e.getX();
                final int y = e.getY();

                final Rectangle cellBounds = panel.getVisibleRect();

                if (cellBounds != null && cellBounds.contains(x, y)) {
                    panel.setCursor(new Cursor(Cursor.HAND_CURSOR));

                } else {
                    panel.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                }
            }
        });

        panel.add(label, BorderLayout.EAST);

        gb.gridx = 0;
        gb.fill = GridBagConstraints.HORIZONTAL;
        gb.weightx = 1;
        gb.insets = new Insets(0, 0, 0, 0);
        gb.anchor = GridBagConstraints.FIRST_LINE_START;
        gb.weighty = 0.05;


        return panel;
    }
}
