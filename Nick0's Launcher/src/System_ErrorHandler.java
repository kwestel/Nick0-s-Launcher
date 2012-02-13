import javax.swing.*;

public class System_ErrorHandler
{
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Error Handlers

    public static String officialAddress = "nicnl25@gmail.com";
    
    public static void handleException(Exception e, boolean fatalError)
    {
        String errorToPrint = "Exception inattendue :\n" + e;
        e.printStackTrace();
        openErrorWindow(errorToPrint, true);
        if ( fatalError ) { System.exit(0); }
    }
    
    public static void handleExceptionWithText(Exception e, String text, boolean fatalError, boolean reportBug)
    {
        e.printStackTrace();
        handleError(text, fatalError, reportBug);
    }
    
    public static void handleError(String text, boolean fatalError, boolean reportBug)
    {
        openErrorWindow(text, reportBug);
        if ( fatalError ) { System.exit(0); }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Specific Error

    public static void handleMinecraftLoadingException(Exception e)
    {
        if ( e instanceof SecurityException ) { System_ErrorHandler.handleExceptionWithText(e, "Impossible d'initialiser les mods que vous avez installé.\n\nVeuillez supprimer le dossier META-INF de votre jeu.", true, false); }
        if ( e instanceof NullPointerException || e instanceof ClassNotFoundException ) { System_ErrorHandler.handleExceptionWithText(e, "Le jar sélectionné est introuvable.\n\nUne réinstallation de Minecraft vous est conseillée.", true, false); }
        else { System_ErrorHandler.handleExceptionWithText(e, "Une erreur inconnue est survenue lors du lancement du jeu.\n\nUne reinstallation de Minecraft vous est conseillée.", true, true); }
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Private System Function

    private static void openErrorWindow(String errorText, boolean reportBogue)
    {
        System.out.println("Nick0's Launcher - Erreur : " + errorText);
        if ( reportBogue ) { errorText += "\n\nMerci de reporter tout bogue à cette addresse :\n" + officialAddress; }
        JOptionPane.showMessageDialog(new JInternalFrame(), errorText, "Nick0's Launcher - Erreur", JOptionPane.ERROR_MESSAGE);
    }
    
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Nicnl - nicnl25@gmail.com
}
