import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class GuiElement_Panel extends JPanel
{

    public Image background_image;
    public String background_path;

    public GuiElement_Panel(String background)
    {
        background_path = background;

        try
        {
            Image temp_loaded = ImageIO.read(GuiForm_MainFrame.class.getResource("backgrounds/" + background_path));
            background_image = temp_loaded.getScaledInstance(temp_loaded.getWidth(null), temp_loaded.getHeight(null), 16);
        }
        catch ( IOException e ) { System_ErrorHandler.handleException(e, false); }
    }

    public void paintComponent(Graphics g)
    {
        int SizeX = getWidth();
        int SizeY = getHeight();
        Image img = createImage(SizeX, SizeY);
        Graphics g2 = img.getGraphics();
        g2.drawImage(background_image, 0, 0, SizeX, SizeY, null);
        g.drawImage(img, 0, 0, SizeX, SizeY, null);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Nicnl - nicnl25@gmail.com
}
