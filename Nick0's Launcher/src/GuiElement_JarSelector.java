import javax.swing.*;

public class GuiElement_JarSelector extends GuiElement_BaseComboBox
{
    
    public GuiElement_JarSelector()
    {
        super();
        updateJars();
    }

    public void updateJars()
    {
        System_LogWriter.write("Recuperation de la liste des jars");
        String[] jarList = System_JarSelectorFunctions.getJarList();
        setModel(new DefaultComboBoxModel(System_JarSelectorFunctions.getJarList()));

        if ( jarList.length < 2 ) { setEnabled(false); }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Nicnl - nicnl25@gmail.com
}
