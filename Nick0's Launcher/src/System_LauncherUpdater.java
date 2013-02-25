import javax.swing.*;
import java.io.File;
import java.io.IOException;

public class System_LauncherUpdater
{
    public static final String clientUpdaterTemporaryName = "Nick0sLauncher_UpdateSystem.jar";
    public static final String serverAddress = "nicnl.com";

    public static String latestLauncherRevision;
    private static System_ServerConnexion serverConnexion;

    public static boolean updateAvaivable = false;


    public static void startLauncherUpdater() { new Thread() { public void run() { mainLauncherUpdater(); } }.start(); }

    private static void mainLauncherUpdater()
    {
        serverConnexion = null;

        try
        {
            serverConnexion = new System_ServerConnexion(serverAddress, 62602);
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

            serverConnexion.closeConnexions();
        }
        catch ( IOException e )
        {
            if ( serverConnexion != null ) { serverConnexion.closeConnexions(); }

            if ( SystemTray_MinecraftIcon.trayIconInitialized )
            {
                SystemTray_MinecraftIcon.displayErrorMessage("Erreur !", "Le serveur de mise à jour n'a pas pu être contacté.\nIl se peux qu'il soit en maintenance, ou que le projet de ce launcher soit mort.\n\nDésolé du dérangement.");
            }
            else { System_ErrorHandler.handleExceptionWithText(e, "Le serveur de mise à jour n'a pas pu être contacté.\nIl se peux qu'il soit en maintenance, ou que le projet de ce launcher soit mort.\n\n Désolé du dérangement.", false, true); }
        }
    }

    public static void displayUpdateConfirmationWindow() throws IOException
    {
        serverConnexion = new System_ServerConnexion(serverAddress, 62602);
        serverConnexion.sendLauncherRecognition();

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

        serverConnexion.closeConnexions();
    }

    private static File downloadClientUpdater(System_ServerConnexion serverConnexion) throws IOException
    {
        serverConnexion = new System_ServerConnexion(serverAddress, 62602);
        serverConnexion.sendLauncherRecognition();

        byte[] updateSystemData = serverConnexion.downloadFile("Update System");

        File updateSystemFile = new File(Main_RealLauncher.configFileDir + File.separator + clientUpdaterTemporaryName);
        System_FileManager.writeByteArrayToFile(updateSystemData, updateSystemFile);

        serverConnexion.closeConnexions();

        return updateSystemFile;
    }

    public static String getAMUS()
    {
        try
        {
            serverConnexion = new System_ServerConnexion(serverAddress, 62602);
            serverConnexion.sendLauncherRecognition();

            String AMUS = serverConnexion.getRevision("LatestMinecraftVersion");

            serverConnexion.closeConnexions();

            if ( AMUS.equals("[ERROR]") ) { throw new Exception("Unknown Server Error"); }

            return AMUS.replace("Minecraft Minecraft ", "").trim();
        }
        catch ( Exception e ) { return null; }
    }

    private static void launchClientUpdater(File clientUpdaterFile) throws IOException
    {
        String[] processParameters = new String[]
        {
            "java",
            "-jar",
            clientUpdaterFile.getAbsolutePath(),
            Main_ReLauncher.reLauncherPath == null ? Main_RealLauncher.getLauncherJarPath() : Main_ReLauncher.reLauncherPath,
            serverAddress
        };

        ProcessBuilder launcherProcessBuilder = new ProcessBuilder(processParameters);
        Process newProcess = launcherProcessBuilder.start();

        System.exit(0);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Nicnl - nicnl25@gmail.com
}
