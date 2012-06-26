import javax.swing.*;
import java.applet.Applet;
import java.io.File;
import java.io.IOException;
import java.net.URLClassLoader;

public final class Main_RealLauncher
{
    public static Applet minecraftInstance = null;

    public static String configFileDir = System_UserHomeDefiner.returnConfigDirectory();
    public static String homeDir = configFileDir;

    public static boolean a = false;
    protected static String b;
    public static boolean c = false;

    public static void main(String[] args)
    {
        System_LogWriter.initializeMinecraftLogs();

        System_LogWriter.write("Initialisation du launcher...");

        System_FileManager.removeFile(Main_RealLauncher.configFileDir + File.separator + "Nick0sLauncher.error", false);

        System_LogWriter.write("Chargement du thème du système d'exploitation hôte...");
        try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); }
        catch ( Exception e ) { System_ErrorHandler.handleException(e, false); }

        System_LogWriter.write("Mise en place des variables système...");
        homeDir = getHomeDir();
        System_DataStub.setParameter("latestVersion", "0");

        System_LogWriter.write("Vérification des dossiers d'installation...");
        System_FileManager.createFolder(configFileDir);
        if ( !homeDir.equals(configFileDir) ) { System_FileManager.createFolder(homeDir); }
        System_FileManager.createFolder(getBinDirPath());

        System_LogWriter.write("Chargement des préférences...");
        Preferences_ConfigLoader.SYSTEM_LoadPreferences();

        System_LogWriter.write("Initialisation de la TrayIcon...");
        SystemTray_MinecraftIcon.enableTrayIcon();

        System_LogWriter.write("Initialisation de la fenêtre principale...");
        GuiForm_MainFrame.newForm(true);

        System_LogWriter.write(Preferences_ConfigLoader.CONFIG_updatesDisabled ? "Mise à jour désactivées ! Thread skippé !" : "Démarrage du Thread de mise à jour du Launcher");
        if ( !Preferences_ConfigLoader.CONFIG_updatesDisabled ) { System_LauncherUpdater.startLauncherUpdater(); }

        while ( !GuiForm_MainFrame.formInitialized ) { }
        System_LogWriter.write("Décodage du mot de passe...");
        loadPassword();

        System_LogWriter.write("Launcher fonctionnel !");
    }

    public static void startLogin(String username, String password)
    {
        if ( GuiForm_MainFrame.mainFrame.Field_UserName.getText().equals("") ) { return; }
        else if ( GuiForm_MainFrame.mainFrame.Field_Password.getPassword().length == 0 && !GuiForm_MainFrame.mainFrame.Check_Offline.isSelected() ) { return; }

        GuiForm_MainFrame.disableLoginWindow(GuiForm_MainFrame.mainFrame.Check_Offline.isSelected());

        if ( !GuiForm_MainFrame.mainFrame.Check_Offline.isSelected() )
        {
            try { Web_MainTransaction.Main_OnlineLogin(username, password); }
            catch ( IOException e ) { GuiForm_MainFrame.enableLoginWindow(); System_ErrorHandler.handleException(e, false); }
        }
        else { Web_MainTransaction.Main_OfflineLogin(username); }
    }

    private static String getHomeDir()
    {
        String loadedHomeDir = Preferences_ConfigFileWriter.getParameter("HomeDir");
        return loadedHomeDir.equals("") ? configFileDir : loadedHomeDir;
    }

    public static String getB()
    {
        String tempPass = b;
        b = null;
        return tempPass;
    }

    public static String getConfigFilePath() { return configFileDir + File.separator + "Nick0's_Launcher.mconf"; }
    public static String getBinDirPath() { return homeDir + File.separator + "bin"; }
    public static String getNativesDirPath() { return getBinDirPath() + File.separator + "natives"; }
    public static String getModsDirPath() { return getBinDirPath() + File.separator + "mods"; }

    private static void loadPassword()
    {
        if ( new File(Main_RealLauncher.getConfigFilePath()).exists() )
        {
            System_LogWriter.write("Chargement des données de connexion...");

            String UserName = Preferences_ConfigFileWriter.getParameter("username");
            String EncP = Preferences_ConfigFileWriter.getParameter("EncP");
            String EncH = Preferences_ConfigFileWriter.getParameter("EncH");

            if ( !UserName.equals("") )
            {
                System_LogWriter.write("Chargement de l'UserName...");

                GuiForm_MainFrame.setUsername(UserName);
            }

            if ( !EncP.equals("") && !EncH.equals("") )
            {
                if ( Preferences_ConfigLoader.CONFIG_OfflineSelected ) { return; }

                System_LogWriter.write("Décodage du mot de passe...");

                String decodedPassword;
                String stringToHash;
                try
                {
                    decodedPassword = System_StringEncrypter.E(EncP);
                    stringToHash = System_Digest.generateSHA512Digest((System_Digest.generateSHA512Digest(EncP.toLowerCase().getBytes()) + System_Digest.generateSHA512Digest(decodedPassword.getBytes()).hashCode()).getBytes());
                }
                catch ( Exception e ) { decodedPassword = null; stringToHash = null; }

                if ( decodedPassword != null && stringToHash != null && stringToHash.toLowerCase().trim().equals(EncH.toLowerCase().trim()) )
                {
                    System_LogWriter.write("Décodage validé !");

                    a = true;
                    b = decodedPassword;

                    while ( !GuiForm_MainFrame.formInitialized  ) { }

                    GuiForm_MainFrame.setRandomPasswordString(System_StringEncrypter.J(b));

                    System_LogWriter.write("Décodage terminé !");
                }
                else
                {
                    if ( Preferences_ConfigLoader.CONFIG_ShowErrorNotifications && Preferences_ConfigLoader.CONFIG_ShowTrayIcon && SystemTray_MinecraftIcon.trayIconInitialized ) { SystemTray_MinecraftIcon.displayErrorMessage("Erreur !", "Impossible de decrypter le mot de passe !"); }
                    System_LogWriter.write("Password decrypting fail !");
                }
            }
        }

        c = true;
    }

    public static void startMinecraft()
    {
        if ( Preferences_ConfigLoader.CONFIG_RemoveMETAINF )
        {
            try { System_FileManager.rewriteJar(getBinDirPath() + File.separator + System_MinecraftLoader.minecraftJarToLoad); }
            catch ( IOException e ) { System_ErrorHandler.handleExceptionWithText(e, "Impossible de supprimer le META-INF de votre jeu.", false, true); }
        }

        String TempSelectedItem = Preferences_ConfigLoader.CONFIG_SaveLastJar ? GuiForm_MainFrame.mainFrame.ComboBox_JarSelector.getSelection() : null;
        Preferences_ConfigLoader.CONFIG_LastJarSaved = ( TempSelectedItem == null ) ? "" : TempSelectedItem;

        if ( GuiForm_MainFrame.mainFrame != null && GuiForm_MainFrame.mainFrame.Check_Offline != null && GuiForm_MainFrame.mainFrame.Check_Offline.isSelected() ) { Preferences_ConfigFileWriter.writeConfigFile("", true, false, true); }
        else { Preferences_ConfigFileWriter.writeConfigFile(System_StringEncrypter.B(), true, false, false); }

        System_LogWriter.write("Initialisation de minecraft !\n\n_____________________________________\n");

        try { minecraftInstance = System_MinecraftLoader.LoadMinecraft(); }
        catch ( Exception e ) { System_ErrorHandler.handleMinecraftLoadingException(e); }

        GuiForm_MainFrame.destroyWindow();

        GuiForm_GameFrame.newForm(true, System_DataStub.static_getParameter("username")).addMinecraftToFrame(minecraftInstance);
        GuiForm_GameFrame.gameFrame.setVisible(true);


        System_DataStub.setParameter("stand-alone", "true");


        minecraftInstance.setStub(new System_DataStub());
        minecraftInstance.setSize(GuiForm_GameFrame.gameFrame.getSize());

        try
        {
            minecraftInstance.init();
            minecraftInstance.start();
            minecraftInstance.validate();
        }
        catch ( SecurityException e ) { System_ErrorHandler.handleMinecraftLoadingException(e); }
        catch ( Exception e ) { System_ErrorHandler.handleMinecraftLoadingException(e); }
    }

    private static final String LauncherRevision = "27-B38";

    public static final String getLauncherRevision() { return LauncherRevision; }

    public static final int defaultWindowSizeX = 950;
    public static final int defaultWindowSizeY = 550;

    public static final String getDefaultWindowSize() { return defaultWindowSizeX + "," + defaultWindowSizeY; }
}
