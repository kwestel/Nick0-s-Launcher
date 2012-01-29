import javax.imageio.ImageIO;
import javax.swing.*;
import java.io.IOException;

public class Gui_BaseExtend_JFrame extends JFrame
{

    Gui_BaseExtend_JFrame()
    {
        super();
        try { setIconImage(ImageIO.read(Gui_BaseExtend_JFrame.class.getResource("icons/Icon_Original.png"))); }
        catch ( IOException e ) { System_ErrorHandler.handleException(e, false); }
    }

    public void setSize(int width, int height)
    {
        if ( System_UserHomeDefiner.SystemOS.equals("macosx") ) { super.setSize((int)(width * 1.15D), (int)(height * 1.15D)); }
        else { super.setSize(width, height); }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Nicnl - nicnl25@gmail.com
}
