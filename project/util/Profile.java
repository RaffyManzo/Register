package project.util;

import project.database.UserDAO;
import project.gui.components.rounded.*;
import project.gui.components.teacher.TeacherProfile;


import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;

public interface Profile {
    GridBagConstraints gb = new GridBagConstraints();

    default void addHead(String text, JPanel master) {
        JPanel head = new RoundedPanel(new BorderLayout());
        head.setBackground(new Color(155,81,224));
        //head.setBorder(new LineBorder(Color.RED));

        JLabel label = new JLabel("<HTML><BODY><u>%s</u></BODY></HTML>".formatted(text));

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
    }

     default void addInfo(Container pan) {
        gb.anchor = GridBagConstraints.CENTER;
        gb.gridx = 0;
        gb.insets = new Insets(5,5,5,5);
        gb.fill = GridBagConstraints.BOTH;
        gb.weightx = 1;
        gb.weighty = 0.01;

        gb.gridy = 1;
        pan.add(createLayerComponent("ID", getMatricola()), gb);
        gb.gridy = 2;
        pan.add(createLayerComponent("Name", getNome()), gb);
        gb.gridy = 3;
        pan.add(createLayerComponent("Surname", getCognome()), gb);
        gb.gridy = 4;
        pan.add(createLayerComponent("Birth date", getData().toString()), gb);
        gb.gridy = 5;
        pan.add(createLayerComponent("Adress", getResidenza()), gb);
        gb.gridy = 6;
        pan.add(createLayerComponent("Email adress", getEmail()), gb);
        gb.gridy = 7;

        JSeparator sep = new JSeparator(SwingConstants.HORIZONTAL);
        sep.setForeground(new Color(51,51,51));
        pan.add(sep, gb);

        addPasswordModifySection((JPanel) pan);

        gb.weighty = 1;
        gb.gridy = 10;
        pan.add(new JLabel(), gb);
     }

     private void addPasswordModifySection(JPanel master) {
         JLabel label = new RoundedLabel("<HTML><BODY><u>%s</u></BODY></HTML>".formatted("Reset here your password:"));

         label.setForeground(new Color(242,242,242));
         label.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 16));
         label.setAlignmentX(JLabel.WEST);
         label.setBackground(new Color(155,81,224));
         gb.gridy = 8;
         master.add(label, gb);


         JPanel pan = new RoundedPanel(new BorderLayout(30,20));
         JLabel left = new JLabel("Reset password:");
         JButton right = new RoundedButton("Apply");
         JPanel passwordElements = new JPanel(new BorderLayout(5, 20));
         passwordElements.setBackground(Color.WHITE);
         RoundedPasswordField center = new RoundedPasswordField("Insert here");
         ImageIcon img = null;
         ImageIcon replace = null;

         try {
             ClassLoader loader = Thread.currentThread().getContextClassLoader();
             img = new ImageIcon(
                     ImageIO.read(
                             Objects.requireNonNull(loader.getResourceAsStream(
                                     "assets/eye.png"
                             ))
                     )
             );

             replace = new ImageIcon(
                     ImageIO.read(
                             Objects.requireNonNull(loader.getResourceAsStream(
                                     "assets/eye-off.png"
                             ))
                     )
             );
         } catch (IOException e) {
             throw new RuntimeException(e);
         }


         RoundedButton showHide = new RoundedButton(img);
         showHide.setBackground(Color.WHITE);
         showHide.setBorderPainted(false);
         showHide.setFocusPainted(false);
         showHide.setMaximumSize(new Dimension(50, 50));

         ImageIcon finalReplace = replace;
         ImageIcon finalImg = img;
         showHide.addActionListener(new ActionListener() {
             @Override
             public void actionPerformed(ActionEvent e) {
                 center.changePasswordVisibility();
                 if(center.getVisibility()) {
                     showHide.setIcon(finalReplace);
                 } else
                     showHide.setIcon(finalImg);
             }
         });

         passwordElements.add(center, BorderLayout.CENTER);
         passwordElements.add(showHide, BorderLayout.EAST);

         adjustModifySection(master, pan, left, right, passwordElements);

         right.addActionListener(new ActionListener() {
             @Override
             public void actionPerformed(ActionEvent e) {
                 String password = new String((center.getPassword()));

                 if(!password.isEmpty() && !password.contentEquals("Insert here")) {
                     new UserDAO().updatePassword(password, getEmail());
                     center.resetPasswordField();

                     JOptionPane.showMessageDialog(master,
                             "Password successfully changed",
                             "Action on password",
                             JOptionPane.INFORMATION_MESSAGE);
                 } else JOptionPane.showMessageDialog(master,
                         "Password doesn't changed",
                         "Action on password",
                         JOptionPane.ERROR_MESSAGE);
             }
         });
     }

    private void adjustModifySection(JPanel master, JPanel pan, JLabel left, JButton right, JPanel center) {

        pan.setBackground(Color.WHITE);
        adjustInfoLabel(left, new Color(51,51, 51));

        right.setFocusPainted(false);
        right.setBorderPainted(false);
        right.setBackground(new Color(155,81,224));
        right.setForeground(Color.WHITE);
        right.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 14));



        center.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 14));
        center.setAlignmentX(JTextField.WEST);

        pan.add(left, BorderLayout.WEST);
        pan.add(center, BorderLayout.CENTER);
        pan.add(right, BorderLayout.EAST);

        gb.anchor = GridBagConstraints.CENTER;
        gb.gridx = 0;
        gb.gridy = 9;
        gb.insets = new Insets(15,5,15,5);
        gb.fill = GridBagConstraints.BOTH;
        gb.weightx = 1;
        gb.weighty = 0.01;

        master.add(pan, gb);
    }

    private JPanel createLayerComponent(String leftString, String rightString) {
        JPanel pan = new RoundedPanel(new BorderLayout(0,20));
        JLabel left = new JLabel(leftString);
        JLabel right = new JLabel(rightString);
        pan.setBackground(Color.WHITE);
        adjustInfoLabel(left, new Color(51,51, 51));
        adjustInfoLabel(right, Color.GRAY);

        pan.add(left, BorderLayout.WEST);
        pan.add(right, BorderLayout.EAST);

        return pan;
     }

     private void adjustInfoLabel(JLabel label, Color c) {
         label.setForeground(c);
         label.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 14));

     }

    String getNome();

    String getCognome();

    public String getMatricola();

    public java.sql.Date getData();
    public String getResidenza();

    public String getEmail() ;
}
