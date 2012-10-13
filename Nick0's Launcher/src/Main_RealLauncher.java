import javax.swing.*;
import javax.swing.text.JTextComponent;
import java.applet.Applet;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.net.URLDecoder;

public final class Main_RealLauncher
{
    public static Applet minecraftInstance = null;

    public static String configFileDir = System_UserHomeDefiner.returnConfigDirectory();
    public static String homeDir = configFileDir;

    public static boolean a = false;
    protected static String b;
    public static boolean c = false;
    public static char d;

    public static void main(String[] args)
    {
        Main_ReLauncher.loadedArgs = args;
        if ( args != null && args.length > 0 && !args[0].trim().equals("") ) { Main_ReLauncher.reLauncherPath = args[0]; }
        else { Main_ReLauncher.reLauncherPath = null; }

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
        loadUsername();

        System_LogWriter.write("Launcher fonctionnel !");
    }

    public static void startLogin(String u, String p)
    {
        if ( ((JTextComponent)GuiForm_MainFrame.mainFrame.ComboBox_UserName.getEditor().getEditorComponent()).getText().equals("") ) { return; }
        else if ( GuiForm_MainFrame.mainFrame.Field_Password.getPassword().length == 0 && !GuiForm_MainFrame.mainFrame.Check_Offline.isSelected() ) { return; }

        GuiForm_MainFrame.disableLoginWindow(GuiForm_MainFrame.mainFrame.Check_Offline.isSelected());

        if ( !GuiForm_MainFrame.mainFrame.Check_Offline.isSelected() )
        {
            try { Web_MainTransaction.Main_OnlineLogin(u, p); }
            catch ( IOException e ) { GuiForm_MainFrame.enableLoginWindow(); System_ErrorHandler.handleException(e, false); }
        }
        else { Web_MainTransaction.Main_OfflineLogin(u); }
    }

    private static String getHomeDir()
    {
        String loadedHomeDir = Preferences_ConfigFileWriter.getParameter("HomeDir");
        return loadedHomeDir.equals("") ? configFileDir : loadedHomeDir;
    }

    public static String gB(){String tP=(b==null?null:b);b=null;return tP==null?null:System_StringEncrypter.E(tP.substring(1, tP.length()));}

    public static String getConfigFilePath() { return configFileDir + File.separator + "Nick0's_Launcher.mconf"; }
    public static String getBinDirPath() { return homeDir + File.separator + "bin"; }
    public static String getNativesDirPath() { return getBinDirPath() + File.separator + "natives"; }
    public static String getModsDirPath() { return getBinDirPath() + File.separator + "mods"; }

    public static String getLauncherJarPath()
    {
        String pathToJar = null;
        try { pathToJar = Main_ReLauncher.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath(); }
        catch ( URISyntaxException e ) { System_ErrorHandler.handleException(e, true); }

        try { return pathToJar == null ? null : URLDecoder.decode(pathToJar, "UTF8"); }
        catch ( UnsupportedEncodingException e ) { return pathToJar; }
    }

    public static void loadUsername()
    {
        if ( new File(Main_RealLauncher.getConfigFilePath()).exists() )
        {
            System_LogWriter.write("Chargement de l'username...");

            Preferences_ConfigFileWriter.eraseParameter("EncP");
            Preferences_ConfigFileWriter.eraseParameter("EncH");

            String username = Preferences_ConfigFileWriter.getParameter("username");

            if ( !username.equals("") )
            {
                String usernameList = Preferences_ConfigFileWriter.getParameter("UsernameList");

                if ( usernameList != null && !usernameList.equals("") && usernameList.contains(System_StringEncrypter.uk) ) { GuiForm_MainFrame.setUsername(username, usernameList.split(System_StringEncrypter.uk)); }
                else { GuiForm_MainFrame.setUsername(username); }

                String offlineList = Preferences_ConfigFileWriter.getParameter("OfflineList");
                if ( usernameList != null && !usernameList.equals("") && offlineList != null && !offlineList.equals("") )
                {
                    String[] splitUsernameList = usernameList.split(System_StringEncrypter.uk);
                    if ( usernameList.contains(System_StringEncrypter.uk) && offlineList.contains(System_StringEncrypter.uk) )
                    {
                        int indexToReach = -1;
                        for ( int i=0; i<splitUsernameList.length; i++ ) { if ( splitUsernameList[i].toLowerCase().trim().equals(username.toLowerCase().trim()) ) { indexToReach = i; } }
                        if ( indexToReach != -1 )
                        {
                            final boolean offlineMode = offlineList.split(System_StringEncrypter.uk)[indexToReach].toLowerCase().trim().equals("true");
                            SwingUtilities.invokeLater(new Runnable() { public void run() {
                                GuiForm_MainFrame.mainFrame.Check_Offline.setSelected(offlineMode);
                                GuiForm_MainFrame.mainFrame.Check_SaveLogin.setSelected(!offlineMode);
                            } });
                        }
                    }
                    else
                    {
                        if ( usernameList.toLowerCase().trim().equals(username.toLowerCase().trim()) )
                        {
                            boolean offlineMode = offlineList.toLowerCase().trim().equals("true");
                            GuiForm_MainFrame.mainFrame.Check_Offline.setSelected(offlineMode);
                            GuiForm_MainFrame.mainFrame.Check_SaveLogin.setSelected(!offlineMode);
                        }
                    }
                }

            }
        }
    }

    public static void loadPassword(String username)
    {
        disablePassword();

        if ( new File(Main_RealLauncher.getConfigFilePath()).exists() )
        {
            if ( username.toLowerCase().trim().equals("") ) { return; }

            System_LogWriter.write("Chargement des données de connexion...");

            String Pe = Preferences_ConfigFileWriter.getParameter("EncP-" + username.replace("=", ""));
            String He = Preferences_ConfigFileWriter.getParameter("EncH-" + username.replace("=", ""));

            String usernameList = Preferences_ConfigFileWriter.getParameter("UsernameList");
            String offlineList = Preferences_ConfigFileWriter.getParameter("OfflineList");

            if ( usernameList != null && !usernameList.equals("") && offlineList != null && !offlineList.equals("") )
            {
                String[] splitUsernameList = usernameList.split(System_StringEncrypter.uk);
                if ( usernameList.contains(System_StringEncrypter.uk) && offlineList.contains(System_StringEncrypter.uk) )
                {
                    int indexToReach = -1;
                    for ( int i=0; i<splitUsernameList.length; i++ ) { if ( splitUsernameList[i].toLowerCase().trim().equals(username.toLowerCase().trim()) ) { indexToReach = i; } }
                    if ( indexToReach != -1 )
                    {
                        final boolean offlineMode = offlineList.split(System_StringEncrypter.uk)[indexToReach].toLowerCase().trim().equals("true");
                        SwingUtilities.invokeLater(new Runnable() { public void run() {
                            GuiForm_MainFrame.mainFrame.Check_Offline.setSelected(offlineMode);
                            GuiForm_MainFrame.mainFrame.Check_SaveLogin.setSelected(!offlineMode);
                        } });
                    }
                }
                else
                {
                    if ( usernameList.toLowerCase().trim().equals(username.toLowerCase().trim()) )
                    {
                        final boolean offlineMode = offlineList.toLowerCase().trim().equals("true");
                        SwingUtilities.invokeLater(new Runnable() { public void run() {
                            GuiForm_MainFrame.mainFrame.Check_Offline.setSelected(offlineMode);
                            GuiForm_MainFrame.mainFrame.Check_SaveLogin.setSelected(!offlineMode);
                        } });
                    }
                }
            }

            if ( !Pe.equals("") && !He.equals("") )
            {
                System_LogWriter.write("Décodage du mot de passe...");

                String dD;
                String stringToHash;
                try
                {
                    stringToHash = System_Digest.generateSHA512Digest((System_Digest.generateSHA512Digest(Pe.substring(1, Pe.length()).toLowerCase().getBytes()) + System_Digest.generateSHA512Digest(System_StringEncrypter.E(Pe.substring(1, Pe.length())).getBytes()).hashCode()).getBytes());
                    dD = "";
                }
                catch ( Exception e ) { dD = null; stringToHash = null; }

                if ( dD != null && stringToHash != null && stringToHash.toLowerCase().trim().equals(He.split("-")[0].toLowerCase().trim()) )
                {
                    System_LogWriter.write("Décodage validé !");

                    a = true;
                    b = Pe;

                    while ( !GuiForm_MainFrame.formInitialized  ) { }

                    try { GuiForm_MainFrame.setRandomPasswordString(System_StringEncrypter.J(Integer.parseInt(System_FastRC4Encryption.decrypt(He.split("-")[1])))); }
                    catch ( NumberFormatException e ) { System_ErrorHandler.handleException(e, false); }

                    System_LogWriter.write("Décodage terminé !");
                }
                else
                {
                    SystemTray_MinecraftIcon.displayErrorMessage("Erreur !", "Impossible de decrypter le mot de passe du compte \"" + username.trim() + "\" !");

                    Preferences_ConfigFileWriter.eraseParameter("EncP-" + username.replace("=", ""));
                    Preferences_ConfigFileWriter.eraseParameter("EncH-" + username.replace("=", ""));

                    SwingUtilities.invokeLater(new Runnable() { public void run() { GuiForm_MainFrame.mainFrame.Check_SaveLogin.setSelected(false); } });

                    System_LogWriter.write("Password decrypting fail !");
                }
            }
            else
            {
                Preferences_ConfigFileWriter.eraseParameter("EncP-" + username.replace("=", ""));
                Preferences_ConfigFileWriter.eraseParameter("EncH-" + username.replace("=", ""));

                SwingUtilities.invokeLater(new Runnable() { public void run() { GuiForm_MainFrame.mainFrame.Check_SaveLogin.setSelected(false); } });
            }
        }

        c = true;
    }

    public static void disablePassword()
    {
        gB();
        a = false;
        b = null;
        GuiForm_MainFrame.mainFrame.Check_SaveLogin.setSelected(false);
        GuiForm_MainFrame.mainFrame.ignoreNextPasswordEntry = true;
        GuiForm_MainFrame.mainFrame.Field_Password.setText("");
        GuiForm_MainFrame.mainFrame.ignoreNextPasswordEntry = false;
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
        else { Preferences_ConfigFileWriter.writeConfigFile("@" + System_StringEncrypter.B(), true, false, false); }

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

    private static final String LauncherRevision = "30";

    public static final String getLauncherRevision() { return LauncherRevision; }

    public static final int defaultWindowSizeX = 950;
    public static final int defaultWindowSizeY = 550;

    public static final String getDefaultWindowSize() { return defaultWindowSizeX + "," + defaultWindowSizeY; }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Nicnl - nicnl25@gmail.com
}
