import javax.swing.*;

public class GuiElement_JTable extends JTable
{

    public GuiElement_JTable(String[][] data, String[] rows)
    {
        super(data, rows);
        setOpaque(false);
        if ( !System_UserHomeDefiner.SystemOS.equals("macosx") ) { setBorder(null); }
    }

    public boolean isCellEditable(int row, int col)
    {
        if ( col == 2 )
        {
            System_Mods.reverseModState((String)getValueAt(row, 1), (String)getValueAt(row, 0));
            setValueAt(System_Mods.getModState((String)getValueAt(row, 1), (String)getValueAt(row, 0)) ? "Actif" : "Inactif", row, col);
            System_Mods.saveData();
        }
        return false;//col != 2;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Nicnl - nicnl25@gmail.com
}
