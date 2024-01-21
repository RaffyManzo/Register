package project.gui.components.teacher;

import project.database.ClassDAO;
import project.database.LessionDAO;
import project.database.StudentDAO;
import project.database.TeacherDAO;
import project.database.objects.Class;
import project.database.objects.Lession;
import project.database.objects.Student;
import project.database.objects.Teacher;
import project.gui.components.rounded.RoundedLabel;
import project.gui.components.rounded.RoundedPanel;
import project.gui.components.rounded.RoundedScrollPane;
import project.util.HeadedFrame;
import project.util.ImageAdder;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Objects;

public class LessionMenu extends JFrame implements HeadedFrame {

    private Teacher teacher;
    private String selectedClass;
    private JPanel lessionPan;
    private LessionHour current;

    public LessionMenu(Teacher teacher, String selectedClass) {
        super();
        this.teacher = teacher;
        this.selectedClass = selectedClass;
        try {
            current = LessionHour.getHourByTime();

            setTitle("%s %s - Current Lession".formatted(teacher.getCognome(), teacher.getNome()));
            setResizable(false);
            setVisible(true);
            setMinimumSize(new Dimension(900, 800));
            setLayout(new GridBagLayout());
            //setBackground(new Color(255,255,255));
            pack();

            addInfoHead(this, "Teacher");


            JPanel pan = new JPanel(new GridLayout(1, 2, 20, 20));
            //pan.setBackground(getBackground());

            addDeleteButton(pan);
            addFirmButton(pan);

            JPanel advPan = new RoundedPanel(new BorderLayout(40, 20));
            advPan.setBackground(new Color(240,230,140));

            ImageAdder img = new ImageAdder("assets/message-square-warning.png");
            advPan.add(img, BorderLayout.WEST);
            JLabel alertMessage = new JLabel("Firm before to see the lession info.");
            alertMessage.setForeground(new Color(51,51,51));
            alertMessage.setFont(new Font(Font.SANS_SERIF, Font.ITALIC, 18));
            advPan.add(alertMessage, BorderLayout.CENTER);

            gb.gridy = 2;
            gb.gridx = 0;
            gb.gridwidth = 3;
            gb.fill = GridBagConstraints.BOTH;
            gb.anchor = GridBagConstraints.PAGE_START;
            gb.weightx = 1;
            gb.weighty = 0.01;
            gb.insets = new Insets(10,10,10,10);
            add(advPan, gb);


            lessionPan = new RoundedPanel(new GridBagLayout());
            lessionPan.setBackground(new Color(220,220,220));
            if(!(new LessionDAO().getCurrentLession(current.getValue(), selectedClass, teacher.getMatricola()).isEmpty()))
                addLessionInfo();

            gb.gridy = 3;
            gb.gridx = 0;
            gb.fill = GridBagConstraints.BOTH;
            gb.anchor = GridBagConstraints.CENTER;
            gb.weightx = 1;
            gb.weighty = 1;
            gb.gridwidth = 3;
            gb.insets = new Insets(10,10,0,10);

            add(lessionPan, gb);

            gb.gridy = 4;
            gb.gridx = 0;
            gb.fill = GridBagConstraints.NONE;
            gb.anchor = GridBagConstraints.LAST_LINE_END;
            gb.weightx = 1;
            gb.weighty = 0.1;
            gb.gridwidth = 3;
            gb.insets = new Insets(10,10,10,10);

            add(pan, gb);

            // Try to load the icon from assets
            try {
                ClassLoader loader = Thread.currentThread().getContextClassLoader();
                setIconImage(ImageIO.read(Objects.requireNonNull(loader.getResourceAsStream("assets/logotagliato.png"))));
            } catch (IOException e) {
                e.printStackTrace();
            }

        } catch(Exception e) {

            JOptionPane.showMessageDialog(this,
                    "Check the current hour.\nTry again into an interval between 9 am and 4 pm",
                    "Invalid lession hour",
                    JOptionPane.ERROR_MESSAGE);
            setVisible(false); //you can't see me!
            dispose(); //Destroy the JFrame object
            e.printStackTrace();
        }
        // --------------- JFRAME SETTING ----------------




    }

    private void addLessionInfo() {
        JPanel attendanceList = new RoundedPanel(new GridBagLayout());
        //attendanceList.setBorder(BorderFactory.createEmptyBorder());
        attendanceList.setBackground(new Color(230,230,230));
        addHead("Present today", attendanceList);

        JPanel lessionInfo = new RoundedPanel(new GridBagLayout());
        lessionInfo.setBackground(new Color(230,230,230));
        addHead("Lession info", lessionInfo);


        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.setBackground(new Color(230,230,230));

        addStudentsList(scrollPane);

        gb.gridwidth = 1;
        // ADD SCROLLPANE TO ATTENDANCE LIST
        gb.gridy = 1;
        gb.gridx = 0;
        gb.weightx = 1;
        gb.weighty = 1;
        gb.anchor = GridBagConstraints.PAGE_START;
        gb.fill = GridBagConstraints.BOTH;
        gb.insets = new Insets(5, 5, 15, 5);

        attendanceList.add(scrollPane, gb);

        gb.gridy = 0;
        gb.weighty = 0.01;
        gb.weightx = 0.5;
        gb.gridx = 1;
        gb.fill = GridBagConstraints.HORIZONTAL;
        gb.insets = new Insets(5,5,5,5);

        lessionPan.add(lessionInfo, gb);
        addLessionInfo(lessionInfo);

        gb.gridy = 0;
        gb.weighty = 1;
        gb.weightx = 0.5;
        gb.gridx = 0;
        gb.fill = GridBagConstraints.BOTH;
        gb.insets = new Insets(5,5,5,5);

        lessionPan.add(attendanceList, gb);
    }

    private void addLessionInfo(JPanel lessionPan) {
        JLabel hour = new RoundedLabel("Ora: %d°".formatted(current.getValue()));
        hour.setBackground(new Color(242, 242, 242));

        gb.gridy = 1;
        gb.gridx = 0;
        gb.weighty = 0.1;
        gb.weightx = 1;
        gb.fill = GridBagConstraints.BOTH;
        gb.anchor = GridBagConstraints.PAGE_START;
        gb.insets = new Insets(5, 5, 5, 5);

        lessionPan.add(hour, gb);

        Class c = new ClassDAO().getClassID(selectedClass);
        JLabel classLabel = new RoundedLabel("Class: %s %s - %s".formatted(c.getNumero(), c.getSezione(), c.getIndirizzo()));
        classLabel.setBackground(new Color(242, 242, 242));

        gb.gridy = 2;
        lessionPan.add(classLabel, gb);

        JLabel teacherLabel = new RoundedLabel("Teacher: %s %s".formatted(teacher.getCognome(), teacher.getNome()));
        teacherLabel.setBackground(new Color(242, 242, 242));

        gb.gridy = 3;
        lessionPan.add(teacherLabel, gb);

        JLabel subject = new RoundedLabel("Subject: %s ".formatted(new TeacherDAO().getSubjectFromId(teacher.getMatricola())));
        subject.setBackground(new Color(242, 242, 242));

        gb.gridy = 4;
        lessionPan.add(subject, gb);
    }

    private void addStudentsList(JScrollPane scrollPane) {
        try{
            ArrayList<Student> students = (ArrayList<Student>) new StudentDAO().getPresentToday(selectedClass, current.getValue());
            JPanel list = new JPanel(new GridBagLayout());
            list.setBackground(scrollPane.getBackground());

            if(students.isEmpty()) {
                JLabel result = new JLabel("Today they are all absent :(");
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

                list.add(result, gb);
            } else {
                int i = 1;
                for(Student s : students) {
                    JLabel studentLabel = new RoundedLabel("[%s] %s %s".formatted(
                            s.getMatricola(),
                            s.getCognome(),
                            s.getNome()));
                    studentLabel.setBackground(new Color(242, 242, 242));

                    gb.gridy = i;
                    gb.gridx = 0;
                    gb.weighty = 0.1;
                    gb.weightx = 1;
                    gb.fill = GridBagConstraints.BOTH;
                    gb.anchor = GridBagConstraints.PAGE_START;
                    gb.insets = new Insets(5, 5, 5, 5);

                    list.add(studentLabel, gb);

                    i++;
                }

                scrollPane.setViewportView(list);
            }
        } catch(Exception e) {
            e.printStackTrace();
        }

    }

    private void addHead(String txt, JPanel panel) {
        JLabel head = new RoundedLabel("<HTML><BODY><u>%s</u></BODY></HTML>".formatted(txt));
        head.setBackground(new Color(155,81,224));

        head.setForeground(new Color(242,242,242));
        head.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 16));
        head.setAlignmentX(JLabel.CENTER);

        gb.gridy = 0;
        gb.gridx = 0;
        gb.weightx = 1;
        gb.weighty = 0.01;
        gb.anchor = GridBagConstraints.PAGE_START;
        gb.fill = GridBagConstraints.HORIZONTAL;
        gb.insets = new Insets(5, 5, 15, 5);

        panel.add(head, gb);
    }

    private void addDeleteButton(JPanel addHere) {
        ImageAdder imgLabel = new ImageAdder("assets/trash.png");

        JPanel panel = new RoundedPanel(new BorderLayout(20, 20));
        panel.setPreferredSize(new Dimension(150, 50));
        panel.add(imgLabel, BorderLayout.WEST);
        JLabel label = new JLabel("<HTML><BODY><u>Delete</u></BODY></HTML>".toUpperCase());
        label.setForeground(new Color(242,242,242));
        label.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 14));
        panel.add(label);

        addPointerUpdate(panel);


        panel.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                removeLession();
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
        addHere.add(panel);
    }

    private void addFirmButton(JPanel addHere) {
        ImageAdder imgLabel = new ImageAdder("assets/pen-line.png");

        JPanel panel = new RoundedPanel(new BorderLayout(20, 20));
        panel.setPreferredSize(new Dimension(150, 50));
        panel.add(imgLabel, BorderLayout.WEST);
        JLabel label = new JLabel("<HTML><BODY><u>Firm</u></BODY></HTML>".toUpperCase());
        label.setForeground(new Color(242,242,242));
        label.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 14));
        panel.add(label);

        addPointerUpdate(panel);


        panel.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                firmAction();
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
        addHere.add(panel);
    }

    private void firmAction()  {
        int hour = current.getValue();
        new LessionDAO().removeByAtt(hour, selectedClass, teacher.getMatricola());
        new LessionDAO().insertInto(new Lession(hour, selectedClass, teacher.getMatricola()));
        lessionPan.removeAll();
        addLessionInfo();
        SwingUtilities.updateComponentTreeUI(lessionPan);

    }

    private void removeLession() {
        int hour = current.getValue();
        new LessionDAO().removeByAtt(hour, selectedClass, teacher.getMatricola());
        lessionPan.removeAll();
        SwingUtilities.updateComponentTreeUI(lessionPan);
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
