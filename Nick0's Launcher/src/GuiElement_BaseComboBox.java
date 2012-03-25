import javax.swing.*;

public class GuiElement_BaseComboBox extends JComboBox
{

    public GuiElement_BaseComboBox()
    {
        super();

        setOpaque(false);
        if ( !System_UserHomeDefiner.SystemOS.equals("macosx") ) { setBorder(null); }
    }

    public String getSelection() { return (String)getSelectedItem(); }

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

}
