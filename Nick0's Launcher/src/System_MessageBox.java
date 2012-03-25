import javax.swing.*;

public class System_MessageBox
{
    
    public static void openMessageBox(String messageTitle, String... rawText)
    {
        String textToEcho = "";
        for ( String actualLine : rawText ) { textToEcho += actualLine + "\n"; }
        textToEcho = textToEcho.substring(0, textToEcho.length()-1);
        
        JOptionPane.showMessageDialog(new JInternalFrame(), textToEcho, messageTitle, JOptionPane.INFORMATION_MESSAGE);
    }
    
}
