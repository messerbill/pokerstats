package pokerstats.client.gui;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class ImagePanel extends JPanel{

	private static final long serialVersionUID = 1L;
	
	private BufferedImage image;

    public ImagePanel(String fileName) {
       try {                
          image = ImageIO.read(Match.class.getResource("/resources/images/cards/smallCards/"+fileName));
       } catch (IOException ex) {
            ex.printStackTrace();
       }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(image, 0, 0, null); // see javadoc for more info on the parameters            
    }

}