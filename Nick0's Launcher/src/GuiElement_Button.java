import javax.swing.*;

public class GuiElement_Button extends JButton
{

    public GuiElement_Button(String text)
    {
        super(text);
        setOpaque(false);

        if ( !System_UserHomeDefiner.SystemOS.equals("macosx") ) { setBorderPainted(false); }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Nicnl - nicnl25@gmail.com
}
