import javax.swing.*;

public class System_ErrorHandler
{
    
    public static void handleException(Exception e, boolean fatalError)
    {
        String errorToPrint = "Exception inattendue :\n" + e;
        if ( e.toString().contains("ClassNotFoundException") ) { errorToPrint += "\n\nMinecraft.jar introuvable ! Veuillez reinstaller Minecraft."; }
        e.printStackTrace();
        openErrorWindow(errorToPrint, true);
        if ( fatalError ) { System.exit(0); }
    }
    
    public static void handleError(String text, boolean fatalError, boolean reportBug)
    {
        openErrorWindow(text, reportBug);
        if ( fatalError ) { System.exit(0); }
    }

    public static void openErrorWindow(String errorText, boolean reportBogue)
    {
        System.out.println("Nick0's Launcher - Erreur : " + errorText);
        if ( reportBogue ) { errorText += "\n\nMerci de reporter tout bogue à cette addresse :\nnicnl25@gmail.com"; }
        JOptionPane.showMessageDialog(new JInternalFrame(), errorText, "Nick0's Launcher - Erreur", JOptionPane.ERROR_MESSAGE);
    }
    
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Nicnl - nicnl25@gmail.com
}
