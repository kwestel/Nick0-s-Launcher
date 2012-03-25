import javax.swing.*;
import java.applet.Applet;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public final class Main_RealLauncher
{
    public static Applet minecraftInstance = null;

    public static String configFileDir = System_UserHomeDefiner.returnConfigDirectory();
    public static String homeDir = configFileDir;

    public static boolean PasswordNotDisplayed = false;
    protected static String StoredPassword;

    public static void main(String[] args)
    {
        System_LogWriter.write("Initialisation du launcher...");

        System_LogWriter.write("Chargement du thème du système d'exploitation hôte...");
        try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); }
        catch ( Exception e ) { System_ErrorHandler.handleException(e, false); }

        System_LogWriter.write("Mise en place des variables système...");
        homeDir = getHomeDir();
        System_DataStub.setParameter("latestVersion","0");

        System_LogWriter.write("Vérification des dossiers d'installation...");
        System_FileManager.createFolder(configFileDir);
        if ( !homeDir.equals(configFileDir) ) { System_FileManager.createFolder(homeDir); }
        System_FileManager.createFolder(getBinDirPath());

        System_LogWriter.write("Chargement des préférences...");
        Preferences_ConfigLoader.SYSTEM_LoadPreferences();

        System_LogWriter.write("Initialisation de la fenêtre principale...");
        GuiForm_MainFrame.newForm(true);
        new Thread() { public void run() { loadPassword(); } }.start();

        System_LogWriter.write("Launcher fonctionnel !");
    }

    public static void startLogin(String username, String password)
    {
        if ( GuiForm_MainFrame.mainFrame.Field_UserName.getText().equals("") ) { return; }
        else if ( GuiForm_MainFrame.mainFrame.Field_Password.getPassword().length == 0 && !GuiForm_MainFrame.mainFrame.Check_Offline.isSelected() ) { return; }

        if ( !GuiForm_MainFrame.mainFrame.Check_Offline.isSelected() )
        {
            try { Web_MainTransaction.Main_ClientTransactions(username, password); }
            catch ( IOException e ) { System_ErrorHandler.handleException(e, true); }
        }
        else { Web_MainTransaction.Main_OfflineLogin(username); }
    }
    
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // GetVariables
    
    private static String getHomeDir()
    {
        String loadedHomeDir = Preferences_ConfigFileWriter.getParameter("HomeDir");
        return loadedHomeDir.equals("") ? configFileDir : loadedHomeDir;
    }
    
    public static String getStoredPassword()
    {
        String tempPass = StoredPassword;
        StoredPassword = null;
        return tempPass;
    }
    
    public static String getConfigFilePath() { return configFileDir + File.separator + "Nick0's_Launcher.mconf"; }
    public static String getBinDirPath() { return homeDir + File.separator + "bin"; }
    public static String getNativesDirPath() { return getBinDirPath() + File.separator + "natives"; }
    public static String getModsDirPath() { return getBinDirPath() + File.separator + "mods"; }
    
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Load Password
    
    private static final void loadPassword()
    {
        if ( new File(Main_RealLauncher.getConfigFilePath()).exists() )
        {
            System_LogWriter.write("Chargement des données de connexion...");
            
            String UserName = Preferences_ConfigFileWriter.getParameter("Username");
            String EncP = Preferences_ConfigFileWriter.getParameter("EncP");
            String EncH = Preferences_ConfigFileWriter.getParameter("EncH");

            if ( !UserName.equals("") )
            {
                System_LogWriter.write("Chargement de l'UserName...");

                GuiForm_MainFrame.mainFrame.Field_UserName.setText(UserName);
                GuiForm_MainFrame.mainFrame.Field_UserName.setCaretPosition(UserName.length());
            }

            if ( !EncP.equals("") && !EncH.equals("") )
            {
                System_LogWriter.write("Décodage du mot de passe...");
                String decodedPassword = Encrypter_StringEncrypter.decodeString(EncP);
                int recodedHashCode = Encrypter_StringEncrypter.encodeString(decodedPassword).hashCode();

                if ( recodedHashCode == Integer.parseInt(EncH) )
                {
                    PasswordNotDisplayed = true;
                    StoredPassword = decodedPassword;

                    GuiForm_MainFrame.mainFrame.Field_Password.setText(Encrypter_StringEncrypter.stringRandomizer(StoredPassword));

                    GuiForm_MainFrame.mainFrame.Check_SaveLogin.setSelected(true);
                }
                else { System_LogWriter.write("Password decrypting fail !"); }
            }
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // System functions - DO NOT USE

    public static void startMinecraft()
    {
        String TempSelectedItem = Preferences_ConfigLoader.CONFIG_SaveLastJar ? GuiForm_MainFrame.mainFrame.ComboBox_JarSelector.getSelection() : null;
        Preferences_ConfigLoader.CONFIG_LastJarSaved = ( TempSelectedItem == null ) ? "" : TempSelectedItem;

        if ( GuiForm_MainFrame.mainFrame != null && GuiForm_MainFrame.mainFrame.Check_Offline != null && GuiForm_MainFrame.mainFrame.Check_Offline.isSelected() ) { Preferences_ConfigFileWriter.writeConfigFile("", true, false); }
        else { Preferences_ConfigFileWriter.writeConfigFile(Encrypter_StringEncrypter.getLastPassword(), true, false); }

        System_LogWriter.write("Initialisation de minecraft !\n\n_____________________________________\n");

        try { minecraftInstance = System_MinecraftLoader.LoadMinecraft(getBinDirPath()); }
        catch ( Exception e ) { System_ErrorHandler.handleMinecraftLoadingException(e); }

        GuiForm_MainFrame.destroyWindow();

        GuiForm_GameFrame baseFrame = new GuiForm_GameFrame(System_DataStub.static_getParameter("username"));
        baseFrame.add(minecraftInstance);

        baseFrame.setVisible(true);

        // Fait apparaitre le bouton "Quit Minecraft"
        System_DataStub.setParameter("stand-alone", "true");

        // Definition du stub de données
        minecraftInstance.setStub(new System_DataStub());
        minecraftInstance.setSize(baseFrame.getSize());

        // Démarrage de Minecraft
        try
        {
            minecraftInstance.init();
            minecraftInstance.start();
            minecraftInstance.validate();
        }
        catch ( SecurityException e ) { System_ErrorHandler.handleMinecraftLoadingException(e); }
        catch ( Exception e ) { System_ErrorHandler.handleMinecraftLoadingException(e); }
    }
    
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Master Values
    
    private static final int LauncherRevision = 23;
    
    public static final int getLauncherRevision() { return LauncherRevision; }
    
    public static final int defaultWindowSizeX = 950;
    public static final int defaultWindowSizeY = 550;
    
    public static final String getDefaultWindowSize() { return defaultWindowSizeX + "," + defaultWindowSizeY; }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Nicnl - nicnl25@gmail.com
}
