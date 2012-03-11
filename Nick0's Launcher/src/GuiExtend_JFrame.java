import javax.imageio.ImageIO;
import javax.swing.*;
import java.io.IOException;

public class GuiExtend_JFrame extends JFrame
{

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
        if ( System_UserHomeDefiner.SystemOS.equals("macosx") ) { super.setSize((int)(width * 1.15D), (int)(height * 1.15D)); }
        else { super.setSize(width, height); }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Nicnl - nicnl25@gmail.com
}
