import javax.swing.filechooser.FileFilter;
import javax.swing.*;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.awt.image.BufferedImage;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class ImagePreviewer extends JPanel implements ActionListener{
    private BufferedImage image = null;
    private JFileChooser fileChooser = null;
    
    public void actionPerformed(ActionEvent e) {
        int choice = fileChooser.showOpenDialog(this);
        if(choice == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            image = loadImage(file);
            layoutImage();
            repaint();
        }
    }
    
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if(image!=null) g.drawImage(image, 0, 0, this);
    }
    
    private void layoutImage() {
        int x = image.getWidth();
        int y = image.getHeight();
        setPreferredSize(new Dimension(x , y));
        revalidate();
    }
    
    private BufferedImage loadImage(File file) {
        BufferedImage image = null;
        try {
            image = ImageIO.read(file);
            // CROP IT!
            image = image.getSubimage(100, 100, 170, 170);
        } catch(IOException e) {
            System.out.println("read error: " + e.getMessage());
        }
        return image;
    }
    
    public JPanel getUIPanel(){
        fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new ImageFilter());
        fileChooser.setAcceptAllFileFilterUsed(false);
        fileChooser.setMultiSelectionEnabled(false);
        JButton button = new JButton("open");
        button.addActionListener(this);
        JPanel panel = new JPanel();
        panel.add(button);
        return panel;
    }
    
    public void setWindowProperty(JFrame frame){
        frame.setTitle("Simple Image Previewer");
        frame.setSize(new Dimension(600,450));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setLocation((int)(dim.getWidth()-frame.getWidth())/2,(int)(dim.getHeight()-frame.getHeight())/2);
    }
    
    public static void main(String args[]){
        ImagePreviewer imagePane = new ImagePreviewer();
        JFrame frame = new JFrame();
        frame.getContentPane().add(new JScrollPane(imagePane));
        frame.getContentPane().add(imagePane.getUIPanel(),"Last");
        imagePane.setWindowProperty(frame);
    }
    
}


class ImageFilter extends FileFilter {
    String GIF = "gif";
    String PNG = "png";
    String JPG = "jpg";
    String BMP = "bmp";
    String JPEG = "jpeg";
    
    public boolean accept(File file) {
        if(file != null) {
            if(file.isDirectory())
                return true;
            String extension = getExtension(file);
            if(extension != null && isSupported(extension))
                return true;
        }
        return false;
    }
    
    public String getDescription() {
        return GIF + ", " + PNG + ", " + JPG + ", " + BMP + " images";
    }
    
    private String getExtension(File file) {
        if(file != null) {
            String filename = file.getName();
            int dot = filename.lastIndexOf('.');
            if(dot > 0 && dot < filename.length()-1)
                return filename.substring(dot+1).toLowerCase();
        }
        return null;
    }
    
    private boolean isSupported(String ext) {
        return ext.equalsIgnoreCase(GIF) || ext.equalsIgnoreCase(PNG) ||
            ext.equalsIgnoreCase(JPG) || ext.equalsIgnoreCase(BMP) ||
            ext.equalsIgnoreCase(JPEG);
    }
}