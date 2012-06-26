import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class System_LauncherUpdater
{
    public static final String clientUpdaterTemporaryName = "Nick0sLauncher_UpdateSystem.jar";
    public static final String serverUpdate = "nicnl.com";

    public static String latestLauncherRevision;
    private static System_ServerConnexion serverConnexion;

    public static boolean updateAvaivable = false;


    public static void startLauncherUpdater() { new Thread() { public void run() { mainLauncherUpdater(); } }.start(); }

    private static void mainLauncherUpdater()
    {
        serverConnexion = null;

        try
        {
            serverConnexion = new System_ServerConnexion(serverUpdate, 62602);
            serverConnexion.sendLauncherRecognition();

            latestLauncherRevision = serverConnexion.getRevision("Nick0's Launcher");

            if ( latestLauncherRevision.toLowerCase().contains("error") )
            {
                System_ErrorHandler.handleError("Attention !\nUne erreur inconnue est survenue dans le serveur de mise à jour !\n\n[" + latestLauncherRevision + "]", false, true);
                return;
            }

            if ( !latestLauncherRevision.toLowerCase().trim().equals(Main_RealLauncher.getLauncherRevision().toLowerCase().trim()) )
            {
                System_LogWriter.write("New Launcher Revision : " + Main_RealLauncher.getLauncherRevision() + "==>" + latestLauncherRevision);

                if ( Preferences_ConfigLoader.CONFIG_AutoUpdate )
                {
                    File clientUpdaterFile = downloadClientUpdater(serverConnexion);
                    launchClientUpdater(clientUpdaterFile);
                    return;
                }

                updateAvaivable = true;
                if ( SystemTray_MinecraftIcon.trayIconInitialized ) { SystemTray_MinecraftIcon.displaySpamUpdateMessage(); }
                else { displayUpdateConfirmationWindow(); }
            }
            else { System_LogWriter.write("Launcher Is Up To Date ! [" + latestLauncherRevision + "]"); }
        }
        catch ( IOException e ) { System_ErrorHandler.handleExceptionWithText(e, "Une erreur est survenue dans le thread de mise à jour !", false, true); }
    }

    public static void displayUpdateConfirmationWindow() throws IOException
    {
        String tempText = "Une mise à jour du launcher est disponible !\n" +
        "[Revision : " + latestLauncherRevision + "]\n\n" +
        "Voulez vous mettre à jour le launcher maintenant ?";
        int userResponse = JOptionPane.showConfirmDialog(new JInternalFrame(), tempText, "Mise à jour disponible", JOptionPane.YES_NO_OPTION);

        if ( userResponse != 0 ) { System_LogWriter.write("Client Abort Update !"); }
        else
        {
            File clientUpdaterFile = downloadClientUpdater(serverConnexion);
            launchClientUpdater(clientUpdaterFile);
        }

        if ( serverConnexion != null ) { serverConnexion.closeConnexions(); }
    }

    private static File downloadClientUpdater(System_ServerConnexion serverConnexion) throws IOException
    {
        byte[] updateSystemData = serverConnexion.downloadFile("Update System");

        File updateSystemFile = new File(Main_RealLauncher.configFileDir + File.separator + clientUpdaterTemporaryName);
        System_FileManager.writeByteArrayToFile(updateSystemData, updateSystemFile);

        return updateSystemFile;
    }

    private static void launchClientUpdater(File clientUpdaterFile) throws IOException
    {
        String actualLauncherPath = Main_RealLauncher.class.getProtectionDomain().getCodeSource().getLocation().getPath();

        String[] processParameters = new String[]
        {
            "java",
            "-jar",
            clientUpdaterFile.getAbsolutePath(),
            actualLauncherPath,
            serverUpdate
        };

        ProcessBuilder launcherProcessBuilder = new ProcessBuilder(processParameters);
        Process newProcess = launcherProcessBuilder.start();

        System.exit(0);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Nicnl - nicnl25@gmail.com
}
