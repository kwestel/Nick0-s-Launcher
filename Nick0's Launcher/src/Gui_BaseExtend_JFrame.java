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

}
