import javax.imageio.ImageIO;
import javax.swing.*;
import java.io.IOException;

public class GuiExtend_JFrame extends JFrame
{

    public boolean setSizeModified = true;

    /*
    GuiExtend_JFrame(String windowTitle)
    {
        this();
        setTitle(windowTitle);
    }
    */

    GuiExtend_JFrame()
    {
        super();
        
        new Thread()
        {
            public void run()
            {
                try { setIconImage(ImageIO.read(GuiExtend_JFrame.class.getResource("icons/Icon_Original.png"))); }
                catch ( IOException e ) { System_ErrorHandler.handleException(e, false); }
            }
        }.start();
    }

    public void setSize(int width, int height)
    {
        // if ( setSizeModified && System_UserHomeDefiner.SystemOS.equals("macosx") ) { super.setSize((int)(width * 1.25D), (int)(height * 1.25D)); }
        // else { super.setSize(width, height); }
        super.setSize(width, height);
    }

    public void setSize(double width, double height) { setSize((int)width, (int)height); }

    protected static ImageIcon createImageIcon(String path)
    {
        java.net.URL imgURL = GuiExtend_JFrame.class.getResource(path);
        return (imgURL != null) ? new ImageIcon(imgURL) : null;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Nicnl - nicnl25@gmail.com
}
