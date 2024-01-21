package project.gui.components.teacher;

import project.database.AttendanceEventDAO;
import project.database.GradeDAO;
import project.database.StudentDAO;
import project.database.TeacherDAO;
import project.database.objects.AttendanceEvent;
import project.database.objects.Grade;
import project.database.objects.Student;
import project.database.objects.Teacher;
import project.util.ImageAdder;
import project.util.Type;
import project.gui.components.rounded.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Ellipse2D;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

public class DashboardTeacher extends RoundedPanel {

    private final Teacher teacher ;
    GridBagConstraints gb = new GridBagConstraints();
    String selectedClassId;
    JPanel avgClassPan;
    public DashboardTeacher(GridBagLayout gbl, String selectedClassId) {
        super(gbl);


        this.teacher = new TeacherDAO().findById(JFrame.getFrames()[1].getTitle().substring(1, 5));
        this.selectedClassId = selectedClassId;


        setBackground(new Color(235,235,235));

        addDashboardHead();
        insertGrade();
        insertEvents();


        JPanel buttonsPan = new JPanel(new GridLayout(3, 1, 20, 20));
        buttonsPan.setBackground(getBackground());
        addProfileBtn(buttonsPan);
        addActivitiesList(buttonsPan);
        addLessionRecap(buttonsPan);


        gb.gridy = 1;
        gb.gridx = 0;
        gb.weighty = 0.3;
        gb.weightx = 1;
        gb.gridwidth = 1;
        gb.fill = GridBagConstraints.BOTH;
        gb.anchor = GridBagConstraints.CENTER;
        gb.insets = new Insets(10, 10, 30, 10);

        add(buttonsPan, gb);

        JPanel avgClassContainer = new RoundedPanel(new BorderLayout());
        double classAvg = new GradeDAO().getClassAvg(teacher.getMateria(), selectedClassId);

        avgClassPan = new AvgCircle(classAvg*10);

        avgClassContainer.add(avgClassPan);

        gb.gridy = 1;
        gb.gridx = 1;
        gb.weighty = 0.7;
        gb.weightx = 1;
        gb.gridwidth = 1;
        gb.fill = GridBagConstraints.BOTH;
        gb.anchor = GridBagConstraints.CENTER;
        gb.insets = new Insets(10, 10, 30, 10);

        add(avgClassContainer, gb);


        Thread refreshT = new Thread(new Runnable() {
            @Override
            public void run() {
                while(true) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    double localAvg = new GradeDAO().getClassAvg(teacher.getMateria(), selectedClassId);
                    if (localAvg != classAvg) {
                        avgClassContainer.removeAll();


                        avgClassPan = new AvgCircle(localAvg*10);

                        avgClassContainer.add(avgClassPan);

                        SwingUtilities.updateComponentTreeUI(avgClassContainer);
                    }
                }
            }
        });

        try {
            refreshT.setName("refreshAvg");
            refreshT.join();
            refreshT.start();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }


    }




    private void insertGrade() {
        class InsertGradePanel extends RoundedPanel{
            private final List<Student> students = new StudentDAO().getAllStudentByClass(selectedClassId);
            private String selectedStudent;
            private final GridBagConstraints gb = new GridBagConstraints();

            JLabel head;
            JComboBox<String> studentBox;
            JComboBox<String> typeBox;
            JTextField grade;
            JTextArea note;
            InsertGradePanel(GridBagLayout gb) {
                super(gb);
                setBackground(new Color(220, 220, 220));
                addHead();
                addStudentComboBox();
                addTypeAndGradeSelection();
                addTextArea();
                addQueryButton();
            }

            private void addHead() {
                head = new RoundedLabel("Insert a grade");
                head.setBackground(new Color(155,81,224));

                head.setForeground(new Color(242,242,242));
                head.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 16));
                head.setAlignmentX(JLabel.CENTER);

                gb.gridy = 0;
                gb.gridx = 0;
                gb.weightx = 1;
                gb.weighty = 0.01;
                gb.gridwidth = 4;
                gb.anchor = GridBagConstraints.PAGE_START;
                gb.fill = GridBagConstraints.HORIZONTAL;
                gb.insets = new Insets(5, 5, 15, 5);

                add(head, gb);
            }

            private void addStudentComboBox() {
                studentBox = new JComboBox<>();

                studentBox.setBackground(new Color(242, 242,242));
                studentBox.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(),
                        BorderFactory.createLineBorder(new Color(242, 242,242), 0)));

                for(Student s: students) {
                    studentBox.addItem("[%s]".formatted(s.getMatricola()) + " %s  %s".formatted(s.getCognome(), s.getNome()));
                }

                studentBox.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        selectedStudent = students.get(studentBox.getSelectedIndex()).getMatricola();
                    }
                });

                gb.gridy = 1;
                gb.gridx = 0;
                gb.anchor = GridBagConstraints.LINE_START;
                gb.weighty = 0.01;
                gb.weightx = 0.5;
                gb.fill = GridBagConstraints.BOTH;
                gb.gridwidth = 2;
                gb.insets = new Insets(5, 5, 5, 5);

                add(studentBox, gb);
            }

            private void addTypeAndGradeSelection() {
                typeBox = new JComboBox<>();

                typeBox.setBackground(new Color(242, 242,242));
                typeBox.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(),
                        BorderFactory.createLineBorder(new Color(242, 242,242), 0)));

                typeBox.addItem(Type.SCRITTO.toString()); typeBox.addItem(Type.ORALE.toString());

                gb.gridy = 1;
                gb.gridx = 2;
                gb.anchor = GridBagConstraints.LINE_START;
                gb.weighty = 0.01;
                gb.weightx = 0.5;
                gb.fill = GridBagConstraints.BOTH;
                gb.gridwidth = 1;
                gb.insets = new Insets(5, 5, 5, 5);

                add(typeBox, gb);

                grade = new RoundedJTextField("");
                grade.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        boolean isValid = true;

                        try {
                            int intVal = Integer.parseInt(grade.getText());
                            if (intVal < 0 || intVal > 10) {
                                isValid = false;
                            }
                        } catch (NumberFormatException x) {
                            // user entered something which is not an int
                            isValid = false;
                        }
                    }
                });

                gb.gridy = 1;
                gb.gridx = 3;
                gb.anchor = GridBagConstraints.LINE_START;
                gb.weighty = 0.01;
                gb.weightx = 0.1;
                gb.fill = GridBagConstraints.BOTH;
                gb.gridwidth = 1;
                gb.insets = new Insets(5, 5, 5, 5);

                add(grade, gb);
            }

            private void addTextArea() {

                note = new RoundedTextArea("Insert a comment");
                note.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 12));
                note.setRows(2);
                note.setMaximumSize(new Dimension(getPreferredSize().width, 200));
                JScrollPane notePan = new RoundedScrollPane(note);

                gb.gridy = 2;
                gb.gridx = 0;
                gb.weightx = 1;
                gb.weighty = 0.01;
                gb.gridwidth = 3;
                gb.anchor = GridBagConstraints.PAGE_START;
                gb.fill = GridBagConstraints.BOTH;
                gb.insets = new Insets(5, 5, 5, 5);


                add(notePan, gb);
            }

            private void addQueryButton() {
                JButton queryBtn = new RoundedButton("Confirm");
                queryBtn.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 18));
                queryBtn.setBackground(new Color(155,81,224));
                queryBtn.setForeground(Color.WHITE);
                queryBtn.setFocusPainted(false);


                gb.gridy = 2;
                gb.gridx = 3;
                gb.weightx = 0.1;
                gb.weighty = 0.01;
                gb.gridwidth = 1;
                gb.anchor = GridBagConstraints.PAGE_START;
                gb.fill = GridBagConstraints.BOTH;
                gb.insets = new Insets(5, 5, 5, 5);


                add(queryBtn, gb);

                queryBtn.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        try {
                            new GradeDAO().insertInto(
                                    new Grade(
                                            getGrade(),
                                            getSelectedType(),
                                            getTextAreaContent(),
                                            new java.sql.Date(new java.util.Date().getTime()),
                                            selectedStudent,
                                            new TeacherDAO().getSubjectFromId(teacher.getMatricola())
                                    )
                            );
                            note.setText("Insert a comment");
                            grade.setText("");
                            //scrollPane.updateUI();

                        } catch (SQLException ex) {
                            throw new RuntimeException(ex);
                        }


                    }
                });
            }

            public double getGrade() {
                return Double.parseDouble(grade.getText());
            }

            public String getSelectedStudent() {
                return selectedStudent;
            }

            public String getSelectedType() {
                return Objects.requireNonNull(typeBox.getSelectedItem()).toString();
            }

            public String getTextAreaContent() {
                return (!note.getText().contentEquals("Insert a comment")) ? note.getText() : null;
            }

        }

        gb.gridy = 2;
        gb.gridx = 0;
        gb.weighty = 1;
        gb.weightx = 1;
        gb.gridwidth = 2;
        gb.fill = GridBagConstraints.HORIZONTAL;
        gb.anchor = GridBagConstraints.CENTER;
        gb.insets = new Insets(0, 10, 10, 10);

        add(new InsertGradePanel(new GridBagLayout()), gb);
    }

    private void insertEvents() {
        class InsertEventsPanel extends RoundedPanel{
            private final List<Student> students = new StudentDAO().getAllStudentByClass(selectedClassId);
            private String selectedStudent;
            private final GridBagConstraints gb = new GridBagConstraints();

            JLabel head;
            JComboBox<String> studentBox;
            JComboBox<String> typeBox;
            JTextArea note;
            InsertEventsPanel(GridBagLayout gb) {
                super(gb);
                setBackground(new Color(220, 220, 220));
                addHead();
                addStudentComboBox();
                addTypeSelection();
                addQueryButton();
            }

            private void addHead() {
                head = new RoundedLabel("Insert a events");
                head.setBackground(new Color(155,81,224));

                head.setForeground(new Color(242,242,242));
                head.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 16));
                head.setAlignmentX(JLabel.CENTER);

                gb.gridy = 0;
                gb.gridx = 0;
                gb.weightx = 1;
                gb.weighty = 0.01;
                gb.gridwidth = 4;
                gb.anchor = GridBagConstraints.PAGE_START;
                gb.fill = GridBagConstraints.HORIZONTAL;
                gb.insets = new Insets(5, 5, 15, 5);


                add(head, gb);
            }

            private void addStudentComboBox() {
                studentBox = new JComboBox<>();

                studentBox.setBackground(new Color(242, 242,242));
                studentBox.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(),
                        BorderFactory.createLineBorder(new Color(242, 242,242), 0)));

                for(Student s: students) {
                    studentBox.addItem("[%s]".formatted(s.getMatricola()) + " %s  %s".formatted(s.getCognome(), s.getNome()));
                }

                studentBox.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        selectedStudent = students.get(studentBox.getSelectedIndex()).getMatricola();
                    }
                });

                gb.gridy = 1;
                gb.gridx = 0;
                gb.anchor = GridBagConstraints.LINE_START;
                gb.weighty = 0.01;
                gb.weightx = 0.5;
                gb.fill = GridBagConstraints.BOTH;
                gb.gridwidth = 2;
                gb.insets = new Insets(5, 5, 5, 5);

                add(studentBox, gb);
            }

            private void addTypeSelection() {
                typeBox = new JComboBox<>();

                typeBox.setBackground(new Color(242, 242,242));
                typeBox.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(),
                        BorderFactory.createLineBorder(new Color(242, 242,242), 0)));

                typeBox.addItem(Type.ASSENZA.toString()); typeBox.addItem(Type.RITARDO.toString());
                typeBox.addItem(Type.USCITA.toString()); typeBox.addItem(Type.ENTRATA.toString());


                gb.gridy = 2;
                gb.gridx = 0;
                gb.anchor = GridBagConstraints.LINE_START;
                gb.weighty = 0.01;
                gb.weightx = 0.5;
                gb.fill = GridBagConstraints.BOTH;
                gb.gridwidth = 2;
                gb.insets = new Insets(5, 5, 5, 5);

                add(typeBox, gb);
            }

            private void addQueryButton() {
                JButton queryBtn = new RoundedButton("Confirm");
                queryBtn.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 18));
                queryBtn.setBackground(new Color(155,81,224));
                queryBtn.setForeground(Color.WHITE);
                queryBtn.setFocusPainted(false);


                gb.gridy = 1;
                gb.gridx = 2;
                gb.weightx = 0.1;
                gb.weighty = 0.01;
                gb.gridwidth = 1;
                gb.gridheight = 2;
                gb.anchor = GridBagConstraints.PAGE_START;
                gb.fill = GridBagConstraints.BOTH;
                gb.insets = new Insets(5, 5, 5, 5);


                add(queryBtn, gb);
                gb.gridheight = 1;

                // JPanel master = this;
                queryBtn.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        try {
                            new AttendanceEventDAO().insertInto(
                                    new AttendanceEvent(
                                            Objects.requireNonNull(typeBox.getSelectedItem()).toString(),
                                            new java.sql.Date(new java.util.Date().getTime()),
                                            selectedStudent,
                                            teacher.getMatricola())
                            );

                        } catch (SQLException ex) {
                            throw new RuntimeException(ex);
                        }

                    }
                });
            }


            public String getSelectedStudent() {
                return selectedStudent;
            }

            public String getSelectedType() {
                return Objects.requireNonNull(typeBox.getSelectedItem()).toString();
            }


        }

        gb.gridy = 3;
        gb.gridx = 0;
        gb.weighty = 1;
        gb.weightx = 1;
        gb.gridwidth = 2;
        gb.fill = GridBagConstraints.HORIZONTAL;
        gb.anchor = GridBagConstraints.CENTER;
        gb.insets = new Insets(10, 10, 10, 10);

        add(new InsertEventsPanel(new GridBagLayout()), gb);
    }

    private void addDashboardHead() {
        JPanel head = new RoundedPanel(new BorderLayout(20, 0));
        head.setBackground(new Color(155,81,224));
        //head.setBorder(new LineBorder(Color.RED));

        JLabel label = new JLabel("DASHBOARD");

        label.setForeground(new Color(242,242,242));
        label.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 22));
        label.setAlignmentX(JLabel.CENTER);
        head.add(label, BorderLayout.WEST);


        JLabel avgLab = new JLabel("YOUR SUBJECT: %s".formatted(new TeacherDAO().getSubjectFromId(teacher.getMatricola())));
        avgLab.setForeground(new Color(242,242,242));
        avgLab.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 14));
        avgLab.setAlignmentX(JLabel.WEST);
        head.add(avgLab, BorderLayout.EAST);


        gb.gridy = 0;
        gb.gridx = 0;
        gb.weighty = 0.1;
        gb.weightx = 1;
        gb.gridwidth = 2;
        gb.fill = GridBagConstraints.HORIZONTAL;
        gb.anchor = GridBagConstraints.PAGE_START;
        gb.insets = new Insets(10, 10, 10, 10);

        add(head, gb);
    }

    private void addProfileBtn(JPanel pan) {
        ImageAdder imgLabel = new ImageAdder("assets/user-round.png");

        JPanel profilePan = new RoundedPanel(new BorderLayout(20, 20));
        profilePan.add(imgLabel, BorderLayout.WEST);
        JLabel label = new JLabel("<HTML><BODY><u>Profile</u></BODY></HTML>".toUpperCase());
        label.setForeground(new Color(242,242,242));
        label.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 14));
        profilePan.add(label);

        addPointerUpdate(profilePan);
        profilePan.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                TeacherProfile profile = new TeacherProfile(new TeacherDAO().findById(teacher.getMatricola()));

                addWindowsListenerToNewFrane(profile);
            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });
        profilePan.setBackground(new Color(155,81,224));
        pan.add(profilePan);
    }

    private void addLessionRecap(JPanel pan) {
        ImageAdder imgLabel = new ImageAdder("assets/presentation.png");

        JPanel panel = new RoundedPanel(new BorderLayout(20, 20));
        panel.add(imgLabel, BorderLayout.WEST);
        JLabel label = new JLabel("<HTML><BODY><u>Today Lession</u></BODY></HTML>".toUpperCase());
        label.setForeground(new Color(242,242,242));
        label.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 14));
        panel.add(label);

        addPointerUpdate(panel);
        panel.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                LessionMenu lsm = new LessionMenu(teacher, selectedClassId);

                addWindowsListenerToNewFrane(lsm);
            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });

        panel.setBackground(new Color(155,81,224));
        pan.add(panel);
    }

    private void addActivitiesList(JPanel pan) {
        ImageAdder imgLabel = new ImageAdder("assets/notebook-pen.png");

        JPanel panel = new RoundedPanel(new BorderLayout(20, 20));
        panel.add(imgLabel, BorderLayout.WEST);
        JLabel label = new JLabel("<HTML><BODY><u>Last activities</u></BODY></HTML>".toUpperCase());
        label.setForeground(new Color(242,242,242));
        label.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 14));
        panel.add(label);

        addPointerUpdate(panel);
        panel.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                LastActivitiesList lst = new LastActivitiesList(teacher.getMatricola(), selectedClassId);

                addWindowsListenerToNewFrane(lst);
            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });

        panel.setBackground(new Color(155,81,224));
        pan.add(panel);
    }

    private void addWindowsListenerToNewFrane(JFrame f) {
        f.addWindowListener(new WindowListener() {
            @Override
            public void windowOpened(WindowEvent e) {
                setEnabled(false);
            }

            @Override
            public void windowClosing(WindowEvent e) {
                f.dispose();
                setEnabled(true);
            }

            @Override
            public void windowClosed(WindowEvent e) {

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

    private void addPointerUpdate(JPanel panel) {
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
    }
}
