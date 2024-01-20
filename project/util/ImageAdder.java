package project.util;//Java Program to Add Image in Jframe
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class ImageAdder extends JLabel{
    private String imageFilename;
    public void setImage(String imagePath) {
        try {
            ClassLoader loader = Thread.currentThread().getContextClassLoader();
            this.setIcon(new ImageIcon(ImageIO.read(loader.getResourceAsStream(imagePath))));
        }catch (IOException e) {
            e.printStackTrace();
        }finally {
            this.imageFilename = imagePath;
        }
    }

    private void defaultSetting() {
        this.setSize(getPreferredSize()); //Sets the location of the image
    }
    public void showImage(JFrame frame) {
        frame.add(this);
        defaultSetting();
        this.setVisible(true);
        frame.setVisible(true);
    }

    public void showImage(JFrame frame, String position) {
        frame.add(this, position);
        defaultSetting();
        this.setVisible(true);
        frame.setVisible(true);
    }

    public void showImage(JPanel pan, String position) {
        pan.add(this, position);
        defaultSetting();
        this.setVisible(true);
        pan.setVisible(true);
    }

    public void showImage(JPanel pan) {
        pan.add(this);
        defaultSetting();
        this.setVisible(true);
        pan.setVisible(true);
    }
    public ImageAdder() {
        //this.this = new JLabel(); //JLabel Creation
        //setBorder(new LineBorder(Color.RED));

    }
    
    public ImageAdder(String imageFilename) {
        setImage(imageFilename);
    }

    public String getImageFilename() {
        return imageFilename;
    }

}