import javax.swing.*;

public class Gui_JarSelector extends JComboBox
{
    
    public Gui_JarSelector()
    {
        super();
        
        setOpaque(false);
        setBorder(null);
        
        String[] jarList = System_JarSelectorFunctions.getJarList();
        setModel(new DefaultComboBoxModel(jarList));
        
        if ( jarList.length == 1 ) { setEnabled(false); }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Nicnl - nicnl25@gmail.com
}
