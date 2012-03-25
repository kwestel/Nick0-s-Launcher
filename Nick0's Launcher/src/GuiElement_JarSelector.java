import javax.swing.*;

public class GuiElement_JarSelector extends GuiElement_BaseComboBox
{
    
    public GuiElement_JarSelector()
    {
        super();
        System_LogWriter.write("Creation du selectionneur de jar...");
        updateJars();
    }

    public void updateJars()
    {
        System_LogWriter.write("Recuperation de la liste des jars");
        String[] jarList = System_JarSelectorFunctions.getJarList();
        setModel(new DefaultComboBoxModel(System_JarSelectorFunctions.getJarList()));

        if ( jarList.length < 2 ) { setEnabled(false); }
        else if ( Preferences_ConfigLoader.CONFIG_SaveLastJar ) { selectStringEntry(Preferences_ConfigLoader.CONFIG_LastJarSaved); }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Nicnl - nicnl25@gmail.com
}
