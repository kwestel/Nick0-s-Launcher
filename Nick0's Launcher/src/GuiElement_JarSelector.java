import javax.swing.*;

public class GuiElement_JarSelector extends JComboBox
{
    
    public GuiElement_JarSelector()
    {
        super();
        
        setOpaque(false);
        if ( !System_UserHomeDefiner.SystemOS.equals("macosx") ) { setBorder(null); }

        System_LogWriter.write("Recuperation de la liste des jars");
        String[] jarList = System_JarSelectorFunctions.getJarList();
        setModel(new DefaultComboBoxModel(System_JarSelectorFunctions.getJarList()));
        
        if ( jarList.length < 2 ) { setEnabled(false); }
    }
    
    public void SelectStringEntry(String entry)
    {
        for (int i=0; i<getItemCount(); i++)
        {
            if ( getItemAt(i).equals(entry) )
            {
                setSelectedIndex(i);
                break;
            }
        }
    }
    
    public String getSelection() { return (String)getSelectedItem(); }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Nicnl - nicnl25@gmail.com
}
