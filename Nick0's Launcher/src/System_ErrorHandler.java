import javax.swing.*;

public class System_ErrorHandler
{
    
    public static void handleException(Exception e, boolean fatalError)
    {
        String errorToPrint = "Exception inattendue :\n" + e;
        if ( e.toString().contains("ClassNotFoundException") ) { errorToPrint += "\n\nMinecraft.jar introuvable ! Veuillez reinstaller Minecraft."; }
        e.printStackTrace();
        openErrorWindow(errorToPrint);
        if ( fatalError ) { System.exit(0); }
    }
    
    public static void handleError(String text) { openErrorWindow(text); }

    public static void openErrorWindow(String errorText)
    {
        System.out.println("Nick0's Launcher - Erreur : " + errorText);
        JOptionPane.showMessageDialog(new JInternalFrame(), errorText, "Nick0's Launcher - Erreur", JOptionPane.ERROR_MESSAGE);
    }
    
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Nicnl - nicnl25@gmail.com
}
