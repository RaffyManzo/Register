package project.gui.components.teacher;

import project.database.TeacherDAO;
import project.database.objects.Teacher;
import project.gui.components.rounded.RoundedButton;
import project.gui.components.rounded.RoundedLabel;
import project.gui.components.rounded.RoundedPanel;
import project.util.ImageAdder;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

public class OtherFeatures extends JFrame {
    private String selectedClass;
    private Teacher teacher;
    private GridBagConstraints gb = new GridBagConstraints();
    public OtherFeatures(String selectedClass, Teacher teacher) {
        this.selectedClass = selectedClass;
        this.teacher = teacher;

        setLayout(new GridBagLayout());

        // display the teachers with a lession in same day in all the class excect selectedClass
        JPanel exceptPan = new RoundedPanel(new GridBagLayout());

        // teacher with lesson in all x class in yy day
        JPanel divisionPan = new RoundedPanel(new GridBagLayout());

        SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd");

        addHeadAndSetPanel("Filtered list of teacher in %s for %s class".formatted(formatter.format(new Date(System.currentTimeMillis())), selectedClass), exceptPan);
        addHeadAndSetPanel("Teacher in class", divisionPan);
        setMessageBox("List of teachers who firm today in the same \nclasses as you but not in the selected one", exceptPan);
        setMessageBox("List of teachers who had lesson to all classes \nin section", divisionPan);

        gb.gridx = 0;
        gb.gridy = 0;
        gb.insets = new Insets(5,5,5,5);
        gb.weighty = 1;
        gb.weightx = 1;
        gb.fill = GridBagConstraints.BOTH;

        add(exceptPan, gb);

        gb.gridx = 1;
        gb.gridy = 0;

        add(divisionPan, gb);


        exceptQueryOutput(exceptPan);

        JComboBox<String> classesNum = new JComboBox<>();
        classesNum.addItem("Select a section number");
        classesNum.addItem("1");classesNum.addItem("2");classesNum.addItem("3");classesNum.addItem("4");classesNum.addItem("5");
        classesNum.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(),
                BorderFactory.createLineBorder(new Color(242, 242, 242), 0)));

        classesNum.setSelectedItem(-1);

        JPanel settings = new JPanel(new GridBagLayout());
        settings.setBackground(Color.WHITE);

        JButton queryBtn = new RoundedButton("Confirm");
        queryBtn.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 18));
        queryBtn.setBackground(new Color(155,81,224));
        queryBtn.setForeground(Color.WHITE);
        queryBtn.setFocusPainted(false);

        gb.gridx = 0;
        gb.gridy = 0;
        gb.insets = new Insets(5,5,5,5);
        gb.weighty = 1;
        gb.weightx = 1;
        gb.fill = GridBagConstraints.BOTH;

        settings.add(queryBtn, gb);
        gb.gridx = 1;
        settings.add(classesNum, gb);

        gb.gridx = 0;
        gb.gridy = 2;
        gb.insets = new Insets(0,0,0,0);
        gb.weighty = 0.1;
        gb.weightx = 1;
        gb.fill = GridBagConstraints.HORIZONTAL;

        divisionPan.add(settings, gb);

        JPanel list = new RoundedPanel(new GridBagLayout());
        list.setBackground(new Color(232,232,232));

        gb.gridx = 0;
        gb.gridy = 3;
        gb.insets = new Insets(0,0,0,0);
        gb.weighty = 1;
        gb.weightx = 1;
        gb.fill = GridBagConstraints.BOTH;
        divisionPan.add(list, gb);

        divisionQuery(list, 1);

        queryBtn.addActionListener(e -> {
            if(classesNum.getSelectedIndex() > 0) {
                list.removeAll();
                divisionQuery(list, Integer.parseInt((String) classesNum.getSelectedItem()));
                SwingUtilities.updateComponentTreeUI(divisionPan);
            }
        });

        setFrame();

    }

    private void exceptQueryOutput(JPanel panel) {

        JPanel list = new RoundedPanel(new GridBagLayout());
        list.setBackground(new Color(232,232,232));

        int i = 0;
        for(Teacher t : new TeacherDAO().exceptThisClass(teacher.getMatricola(), selectedClass)) {
            JLabel teacherInfo = new RoundedLabel("[%s] %s %s".formatted(t.getMatricola(), t.getCognome(), t.getNome()));
            teacherInfo.setBackground(Color.WHITE);
            teacherInfo.setForeground(new Color(51,51,51));
            teacherInfo.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 16));
            teacherInfo.setHorizontalAlignment(JLabel.LEFT);

            gb.gridx = 0;
            gb.gridy = i;
            gb.insets = new Insets(5,5,5,5);
            gb.weighty = 0.1;
            gb.weightx = 1;
            gb.fill = GridBagConstraints.HORIZONTAL;
            list.add(teacherInfo, gb);

            i++;
        }

        if(i == 0) {
            JLabel teacherInfo = new RoundedLabel("No teacher found.");
            teacherInfo.setBackground(new Color(232,232,232));
            teacherInfo.setForeground(new Color(51,51,51));
            teacherInfo.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 18));
            teacherInfo.setHorizontalAlignment(JLabel.LEFT);

            gb.gridx = 0;
            gb.gridy = i;
            gb.insets = new Insets(5,5,5,5);
            gb.weighty = 0.1;
            gb.weightx = 1;
            gb.fill = GridBagConstraints.HORIZONTAL;
            list.add(teacherInfo, gb);
        }

        gb.gridx = 0;
        gb.gridy = i+1;
        gb.insets = new Insets(5,5,5,5);
        gb.weighty = 1;
        gb.weightx = 1;
        gb.fill = GridBagConstraints.BOTH;
        JPanel placeholder = new JPanel();
        placeholder.setBackground(new Color(232,232,232));
        list.add(placeholder, gb);


        gb.gridx = 0;
        gb.gridy = 2;
        gb.insets = new Insets(0,0,0,0);
        gb.weighty = 1;
        gb.weightx = 1;
        gb.fill = GridBagConstraints.BOTH;
        panel.add(list, gb);

    }

    private void divisionQuery(JPanel panel, int selectedNum) {


        int i = 0;
        for(Teacher t : new TeacherDAO().divisionForClassNumber(teacher.getMatricola(), selectedNum)) {
            JLabel teacherInfo = new RoundedLabel("[%s] %s %s".formatted(t.getMatricola(), t.getCognome(), t.getNome()));
            teacherInfo.setBackground(Color.WHITE);
            teacherInfo.setForeground(new Color(51,51,51));
            teacherInfo.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 16));
            teacherInfo.setHorizontalAlignment(JLabel.LEFT);

            gb.gridx = 0;
            gb.gridy = i;
            gb.insets = new Insets(5,5,5,5);
            gb.weighty = 0.1;
            gb.weightx = 1;
            gb.fill = GridBagConstraints.HORIZONTAL;
            panel.add(teacherInfo, gb);

            i++;
        }

        if(i == 0) {
            JLabel teacherInfo = new RoundedLabel("No teacher found.");
            teacherInfo.setBackground(new Color(232,232,232));
            teacherInfo.setForeground(new Color(51,51,51));
            teacherInfo.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 18));
            teacherInfo.setHorizontalAlignment(JLabel.LEFT);

            gb.gridx = 0;
            gb.gridy = i;
            gb.insets = new Insets(5,5,5,5);
            gb.weighty = 0.1;
            gb.weightx = 1;
            gb.fill = GridBagConstraints.HORIZONTAL;
            panel.add(teacherInfo, gb);
        }

        gb.gridx = 0;
        gb.gridy = i+1;
        gb.insets = new Insets(5,5,5,5);
        gb.weighty = 1;
        gb.weightx = 1;
        gb.fill = GridBagConstraints.BOTH;
        JPanel placeholder = new JPanel();
        placeholder.setBackground(new Color(232,232,232));
        panel.add(placeholder, gb);




    }


    private void setMessageBox(String text, JPanel panel) {
        JPanel advPan = new RoundedPanel(new BorderLayout(40, 20));
        advPan.setBackground(new Color(108, 159, 195));

        ImageAdder img = new ImageAdder("assets/info.png");
        advPan.add(img, BorderLayout.WEST);
        JLabel alertMessage = new JLabel(text);
        alertMessage.setForeground(new Color(51,51,51));
        alertMessage.setFont(new Font(Font.SANS_SERIF, Font.ITALIC, 18));
        advPan.add(alertMessage, BorderLayout.CENTER);

        gb.gridx = 0;
        gb.gridy = 1;
        gb.insets = new Insets(5,5,5,5);
        gb.weighty = 0.1;
        gb.weightx = 1;
        gb.fill = GridBagConstraints.HORIZONTAL;

        panel.add(advPan, gb);
    }

    private void addHeadAndSetPanel(String text, JPanel panel) {

        JLabel head = new RoundedLabel(text);
        head.setBackground(new Color(155, 81, 224));
        head.setForeground(Color.WHITE);
        head.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 18));
        head.setHorizontalAlignment(JLabel.LEFT);

        gb.gridx = 0;
        gb.gridy = 0;
        gb.insets = new Insets(0,5,0,5);
        gb.weighty = 0.1;
        gb.weightx = 1;
        gb.fill = GridBagConstraints.HORIZONTAL;

        panel.add(head, gb);

        panel.setBackground(Color.WHITE);
    }


    private void setFrame() {

        setVisible(true);
        setTitle("%s %s - Other teachers info".formatted(teacher.getCognome(), teacher.getNome()));
        setMinimumSize(new Dimension(900, 800));
        setResizable(false);
        setBackground(Color.white);
        // Try to load the icon from assets
        try {
            ClassLoader loader = Thread.currentThread().getContextClassLoader();
            setIconImage(ImageIO.read(Objects.requireNonNull(loader.getResourceAsStream("assets/logotagliato.png"))));
        } catch (IOException e) {
            e.printStackTrace();
        }
        pack();
    }
}
