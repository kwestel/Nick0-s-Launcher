import javax.swing.*;
import java.io.PrintWriter;
import java.io.StringWriter;

public class System_ErrorHandler
{
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Error Handlers

    public static final String officialAddress = "nicnl25@gmail.com";
    
    public static void handleException(Exception e, boolean fatalError)
    {
        System_ErrorPlayer.playErrorMessage();

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
        if ( e instanceof ClassNotFoundException ) { System_ErrorHandler.handleExceptionWithText(e, "Erreur lors du chargement de \"" + System_MinecraftLoader.minecraftJarToLoad + "\"\nLa classe Java demandée n'est pas chargée.\nVotre jar est surement corrompu.\n\nUne réinstallation de Minecraft vous est conseillée.", true, false); }
        else if ( e instanceof SecurityException ) { System_ErrorHandler.handleExceptionWithText(e, "Impossible d'initialiser les mods que vous avez installé.\n\nVeuillez supprimer le dossier META-INF de votre jeu.", true, false); }
        else if ( e instanceof NullPointerException ) { System_ErrorHandler.handleExceptionWithText(e, "Erreur lors du chargement de \"" + System_MinecraftLoader.minecraftJarToLoad + "\"\nLe jar sélectionné est introuvable.\n\nUne réinstallation de Minecraft vous est conseillée.", true, false); }
        else { System_ErrorHandler.handleExceptionWithText(e, "Une erreur inconnue est survenue lors du lancement du jeu.\n\nUne reinstallation de Minecraft vous est conseillée.", true, true); }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Private System Function

    private static void openErrorWindow(String errorText, boolean reportBogue)
    {
        System_ErrorPlayer.playErrorMessage();

        System_LogWriter.write("Erreur : " + errorText);
        if ( reportBogue ) { errorText += "\n\nMerci de reporter tout bogue à cette addresse :\n" + officialAddress; }
        JOptionPane.showMessageDialog(new JInternalFrame(), errorText, "Erreur", JOptionPane.ERROR_MESSAGE);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Exception Manager

    public static String convertExceptionToString(Exception e)
    {
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        e.printStackTrace(printWriter);
        return stringWriter.toString();
    }
    
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Nicnl - nicnl25@gmail.com
}
