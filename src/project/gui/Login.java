package project.gui;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.Objects;

import project.util.ImageAdder;
import project.gui.components.LoginForm;

public class Login extends JFrame{
    private static final String logoImage = "assets/logotagliato.png";
    public Login() {
        super("Login");

        // Define the termination of program on close
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


        setResizable(false);
        setMinimumSize(new Dimension(1440, 800));
        //loginFrame.setSize(new Dimension(1440, 1000));

        setLayout(new GridBagLayout());
        setBackground(Color.white);
        pack();
        ((JComponent)getContentPane()).setBorder(BorderFactory.createEmptyBorder(-5, 0, -1, 0));


        // Try to load the icon from assets
        try {
            ClassLoader loader = Thread.currentThread().getContextClassLoader();
            setIconImage(ImageIO.read(Objects.requireNonNull(loader.getResourceAsStream(logoImage))));
        }catch (IOException e) {
            e.printStackTrace();
        }


        // set the position of JPanel
        JPanel topPanel = new JPanel();
        JPanel bottomPanel = new JPanel();


        GridBagConstraints gb = new GridBagConstraints();
        gb.gridy = 0;
        gb.weighty = 0.01;
        gb.anchor = GridBagConstraints.PAGE_START;

        add(topPanel, gb);
        topPanel.setSize(topPanel.getParent().getWidth(), 320);


        gb.gridy = 2;
        gb.weighty = 0.01;
        gb.anchor = GridBagConstraints.PAGE_END;
        add(bottomPanel, gb);
        bottomPanel.setSize(bottomPanel.getParent().getWidth(), 260);

        loadImage(topPanel, "assets/sopra_onda.png");
        loadImage(bottomPanel, "assets/Sotto_onda_scura.png");

        LoginForm form = new LoginForm(new JPanel());


        gb.gridy = 1;
        gb.weighty = 0.03;
        gb.anchor = GridBagConstraints.PAGE_START;
        form.addLoginFrameToParent(this, gb);


        setVisible(true);

        requestFocusInWindow();
    }




    public void loadImage(Component parent, String file) {
        ImageAdder img = new ImageAdder();

        img.setImage(file);
        img.showImage((JPanel) parent);
    }

}
