import javax.swing.*;
import java.util.ArrayList;

public class GuiElement_AlternativeJarSelector extends GuiElement_BaseComboBox
{
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Constructor

    public GuiElement_AlternativeJarSelector()
    {
        super();
        
        setModel(new DefaultComboBoxModel(System_AlternativeJar.getComboBoxList()));
    }
    
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Nicnl - nicnl25@gmail.com
}
