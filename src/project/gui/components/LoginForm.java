package project.gui.components;

import org.jetbrains.annotations.NotNull;
import project.database.StudentDAO;
import project.database.TeacherDAO;
import project.database.UserDAO;
import project.gui.components.student.StudentSide;
import project.gui.components.teacher.TeacherSide;
import project.gui.components.rounded.RoundedButton;
import project.gui.components.rounded.RoundedJTextField;
import project.gui.components.rounded.RoundedPasswordField;
import project.util.ImageAdder;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class LoginForm extends JPanel implements ActionListener {

    private final JTextField userField;
    private final JPasswordField passwordField;
    private final JButton loginButton;

    public LoginForm(JPanel container) {

        //setBorder(new LineBorder(Color.RED));

        // set a new layout manager
        setLayout(new GridBagLayout());

        setMinimumSize(new Dimension(500, 500));
        Dimension d = new Dimension(getPreferredSize().width, 100);

        // Creating a panel is necessary to apply the border style

        // User field panel definition
        userField = new RoundedJTextField("Username");

        JPanel userContainer = new JPanel(new GridLayout(1, 1));

        userContainer.setMinimumSize(d);
        userContainer.setMaximumSize(d);
        //userContainer.setBorder(new LineBorder(Color.RED));
        userContainer.add(userField);

        // Password field panel definition
        passwordField = new RoundedPasswordField("Password");

        /*try {
            ClassLoader loader = Thread.currentThread().getContextClassLoader();
            JButton showPassword = new JButton(());
        }catch (IOException e) {
            e.printStackTrace();
        }*/

        JPanel passwordContainer = new JPanel(new GridLayout(1, 1));

        passwordContainer.setMinimumSize(d);
        passwordContainer.setMaximumSize(d);

        //passwordContainer.setBorder(new LineBorder(Color.RED));
        passwordContainer.add(passwordField);


        loginButton = getLoginButton(userField, passwordField);

        //loginButton.setBorder(new LineBorder(Color.RED));
        ImageAdder img = new ImageAdder();
        img.setImage("assets/logotagliato.png");

        // Load the Components in a parent with GridBagConstraints

        setComponentSize(img, 0, 0, GridBagConstraints.PAGE_START, GridBagConstraints.NONE);
        setComponentSize(userContainer, 1, 0.6, GridBagConstraints.CENTER);
        setComponentSize(passwordContainer, 3, 0.6, GridBagConstraints.PAGE_START);
        setComponentSize(loginButton, 4, 0.6, GridBagConstraints.CENTER);

        // Modify Component Style
        RoundedButton.customizeButton(loginButton);
    }


    @NotNull
    private JButton getLoginButton(JTextField userField, JTextField passwordField) {
        JButton loginButton = new RoundedButton("Login");
        loginButton.setActionCommand("login");
        loginButton.addActionListener(this);

        System.out.println(loginButton.getAction());
        return loginButton;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == loginButton) {
            onButtonPress();
        }
    }

    private void onButtonPress() {
        UserDAO user = new UserDAO();
        if (user.findUser(userField.getText(), new String(passwordField.getPassword()))) {
            JFrame.getFrames()[0].setVisible(false);

            if(!(new StudentDAO().findByEmail(userField.getText()).isEmpty())){
                StudentSide ss = new StudentSide(userField.getText());
            } else if(!(new TeacherDAO().findByEmail(userField.getText()).isEmpty())) {
                TeacherSide ts = new TeacherSide(userField.getText());
            }
        } else {
            userNotFound();
        }
    }

    private void userNotFound() {
        JOptionPane.showMessageDialog(this,
                "Check if the fields are correctly compiled.\nCheck if the username or password are correct.",
                "User not found",
                JOptionPane.ERROR_MESSAGE);
    }

    private void setComponentSize(Component c, int y, double weight, int anchor) {
        GridBagConstraints gb = new GridBagConstraints();
        gb.gridx = 0; // One column
        gb.gridy = y;
        gb.weightx = weight;
        gb.weighty = 0.5;
        gb.insets = new Insets(10, 25, 10, 25);
        gb.fill = GridBagConstraints.BOTH;
        gb.anchor = anchor;
        gb.ipadx = 50;
        gb.ipady = 50;

        add(c, gb);
    }

    private void setComponentSize(Component c, int y, double weight, int anchor, int fill) {
        GridBagConstraints gb = new GridBagConstraints();
        gb.gridx = 0; // One column
        gb.gridy = y;
        gb.weightx = weight;
        gb.weighty = weight;
        gb.insets = new Insets(0, 0, 20, 0);
        gb.fill = fill;
        gb.anchor = anchor;
        //gb.ipadx = 40;
        //gb.ipady = 40;
        add(c, gb);
    }


    public void addLoginFrameToParent(JPanel parent) {
        parent.add(this);
    }

    public void addLoginFrameToParent(JFrame parent) {
        parent.add(this);
    }

    public void addLoginFrameToParent(JFrame parent, String position) {
        parent.add(this, position);
    }

    public void addLoginFrameToParent(JPanel parent, String position) {
        parent.add(this, position);
    }

    public void addLoginFrameToParent(JFrame parent, GridBagConstraints layout) {
        parent.add(this, layout);
    }

    public void addLoginFrameToParent(JPanel parent, GridBagConstraints layout) {
        parent.add(this, layout);
    }

}
