package project.gui.components.student;

import project.database.*;
import project.database.objects.AttendanceEvent;
import project.database.objects.Grade;
import project.database.objects.Student;
import project.database.objects.Teacher;
import project.gui.components.rounded.RoundedLabel;
import project.gui.components.rounded.RoundedPanel;
import project.gui.components.rounded.RoundedScrollPane;
import project.util.HeadedFrame;
import project.util.ImageAdder;
import project.util.Type;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Objects;

public class AllStudentEvent extends JFrame implements HeadedFrame {

    private Student student;
    private GridBagConstraints gb = new GridBagConstraints();

    private ArrayList<AttendanceEvent> events;

    public AllStudentEvent(String studentID) {
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


        addEventsCount();
        addEventsPanel();


        setVisible(true);
    }

    private void addEventsPanel() {
        JPanel masterEventsPanel = new RoundedPanel(new GridBagLayout());
        masterEventsPanel.setBackground(getBackground());
        addHead(masterEventsPanel, "All events");

        events = (ArrayList<AttendanceEvent>) new AttendanceEventDAO().getAllStudentEvents(student.getMatricola());
        JPanel eventsPanel = new RoundedPanel(new GridBagLayout());
        eventsPanel.setBackground(getBackground());

        if(events.isEmpty()) {
            JLabel result = new JLabel("0 events");
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

            eventsPanel.add(result, gb);
        } else {

            int i = 0;
            for (AttendanceEvent aet : events) {

                JPanel singleEventPan = new RoundedPanel(new BorderLayout(15, 0));

                singleEventPan.setBackground(new Color(242,242,242));

                Teacher tc = new TeacherDAO().findById(aet.getTeacherId()).get(0);

                JLabel event = new JLabel("%s".formatted(aet.getTipo().toUpperCase()));
                event.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 18));
                event.setForeground(Color.BLACK);
                event.setAlignmentX(JLabel.WEST);

                JLabel date = new JLabel(aet.getData().toString());
                date.setFont(new Font(Font.SANS_SERIF, Font.ITALIC, 14));
                date.setForeground(Color.GRAY);

                JLabel type = new JLabel("(added by %s %s)".formatted(tc.getCognome(), tc.getNome()));
                type.setFont(new Font(Font.SANS_SERIF, Font.ITALIC, 14));
                type.setForeground(Color.GRAY);


                singleEventPan.add(event, BorderLayout.WEST);
                singleEventPan.add(type, BorderLayout.CENTER);
                singleEventPan.add(date, BorderLayout.EAST);

                gb.gridy = i;
                gb.gridx = 0;
                gb.weighty = 0.1;
                gb.weightx = 1;
                gb.fill = GridBagConstraints.HORIZONTAL;
                gb.anchor = GridBagConstraints.PAGE_START;
                gb.insets = new Insets(0, 5, 5, 5);

                eventsPanel.add(singleEventPan, gb);
                i++;
            }
        }

        JScrollPane scrollPane = new RoundedScrollPane();
        scrollPane.setBackground(Color.white);
        scrollPane.setViewportView(eventsPanel);

        gb.gridy = 1;
        gb.gridx = 1;
        gb.anchor = GridBagConstraints.PAGE_START;
        gb.fill = GridBagConstraints.BOTH;
        gb.weighty = 1;
        gb.weightx = 1;
        gb.gridwidth = 3;
        gb.insets = new Insets(5,5,5,5);

        masterEventsPanel.add(scrollPane, gb);

        gb.gridy = 4;
        gb.gridx = 0;
        gb.anchor = GridBagConstraints.PAGE_START;
        gb.fill = GridBagConstraints.BOTH;
        gb.weighty = 1;
        gb.weightx = 1;
        gb.gridwidth = 3;
        gb.insets = new Insets(5,5,5,5);
        add(masterEventsPanel,gb);


    }

    private void addEventsCount() {
        JPanel countEventPanel = new JPanel(new GridLayout(1, 4, 20, 10));

        defineCountLabel(countEventPanel,
                new AttendanceEventDAO().countTypeById(student.getMatricola(), project.util.Type.ASSENZA),
                project.util.Type.ASSENZA);
        defineCountLabel(countEventPanel,
                new AttendanceEventDAO().countTypeById(student.getMatricola(), project.util.Type.RITARDO),
                project.util.Type.RITARDO);
        defineCountLabel(countEventPanel,
                new AttendanceEventDAO().countTypeById(student.getMatricola(), project.util.Type.USCITA),
                project.util.Type.USCITA);
        defineCountLabel(countEventPanel,
                new AttendanceEventDAO().countTypeById(student.getMatricola(), project.util.Type.ENTRATA),
                project.util.Type.ENTRATA);

        gb.gridy = 3;
        gb.gridx = 1;
        gb.anchor = GridBagConstraints.PAGE_START;
        gb.fill = GridBagConstraints.HORIZONTAL;
        gb.weighty = 0.1;
        gb.weightx = 1;
        gb.gridwidth = 3;
        gb.insets = new Insets(5,20,5,20);

        add(countEventPanel, gb);

    }

    private void defineCountLabel(JPanel parent, int toAdd, project.util.Type enumType) {

        JPanel singleCountContainer = new RoundedPanel(new BorderLayout());

        JLabel type = new JLabel(enumType.name() + ": ");

        type.setForeground(new Color(51,51,51));
        type.setHorizontalAlignment(JLabel.CENTER);
        type.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 16));

        JLabel lb = new RoundedLabel(toAdd + "");

        lb.setBackground(new Color(242,242,242));
        lb.setForeground(new Color(51,51,51));
        lb.setHorizontalAlignment(JLabel.CENTER);
        lb.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 16));

        singleCountContainer.setBackground(Color.WHITE);
        singleCountContainer.add(type, BorderLayout.WEST);
        singleCountContainer.add(lb, BorderLayout.EAST);

        parent.add(singleCountContainer);
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
