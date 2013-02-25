public class Preferences_ConfigLoader
{

    public static boolean CONFIG_updatesDisabled = false;
    public static boolean CONFIG_modsButtonChecked = false;
    public static boolean CONFIG_NicnlModsButtonChecked = false;
    public static boolean CONFIG_jarSelector = false;
    public static boolean CONFIG_SaveLastJar = false;
    public static String CONFIG_LastJarSaved = "";
    public static boolean CONFIG_LWJGLSelector = false;
    public static String CONFIG_LWJGLAddress = "";
    public static boolean CONFIG_ramSelector = false;
    public static int CONFIG_selectedRam = 1024;
    public static boolean CONFIG_ModifyWindowSize = false;
    public static String CONFIG_WindowSize = Main_RealLauncher.getDefaultWindowSize();
    public static int CONFIG_WindowSizeX = Main_RealLauncher.defaultWindowSizeX;
    public static int CONFIG_WindowSizeY = Main_RealLauncher.defaultWindowSizeY;
    public static String CONFIG_ModsDisabled = "";
    public static boolean CONFIG_erreurSonore = false;
    public static boolean CONFIG_RemoveMETAINF = false;
    public static boolean CONFIG_AutomaticJarRename = false;
    public static boolean CONFIG_ShowConsoleOnStartup = false;
    public static boolean CONFIG_ShowTrayIcon = true;
    public static boolean CONFIG_AutoLogin = false;
    public static boolean CONFIG_AutoUpdate = false;
    public static boolean CONFIG_ShowErrorNotifications = false;
    public static boolean CONFIG_AlternativeMinecraftUpdateServer = false;


    public static void SYSTEM_LoadPreferences()
    {
        System_LogWriter.write("Chargement des préférences...");

        CONFIG_updatesDisabled = Preferences_ConfigFileWriter.getParameter("DisableUpdates").toLowerCase().equals("true");
        CONFIG_jarSelector = Preferences_ConfigFileWriter.getParameter("JarSelector").toLowerCase().equals("true");
        CONFIG_ramSelector = Preferences_ConfigFileWriter.getParameter("RamSelector").toLowerCase().equals("true");
        CONFIG_selectedRam = Integer.parseInt(Preferences_ConfigFileWriter.getParameter("RAM"));
        CONFIG_SaveLastJar = Preferences_ConfigFileWriter.getParameter("SaveLastJar").toLowerCase().equals("true");
        CONFIG_LastJarSaved = Preferences_ConfigFileWriter.getParameter("LastJarSaved");
        CONFIG_modsButtonChecked = Preferences_ConfigFileWriter.getParameter("ModsButtonChecked").toLowerCase().equals("true");
        CONFIG_NicnlModsButtonChecked = Preferences_ConfigFileWriter.getParameter("NicnlModsButtonChecked").toLowerCase().equals("true");
        CONFIG_LWJGLSelector = Preferences_ConfigFileWriter.getParameter("LWJGLSelector").toLowerCase().equals("true");
        CONFIG_LWJGLAddress = Preferences_ConfigFileWriter.getParameter("LWJGLAddress");
        CONFIG_erreurSonore = Preferences_ConfigFileWriter.getParameter("ErreurSonore").toLowerCase().equals("true");
        CONFIG_RemoveMETAINF = Preferences_ConfigFileWriter.getParameter("RemoveMETAINF").toLowerCase().equals("true");
        CONFIG_AutomaticJarRename = Preferences_ConfigFileWriter.getParameter("AutomaticRenameJar").toLowerCase().equals("true");
        CONFIG_ModsDisabled = Preferences_ConfigFileWriter.getParameter("DisabledMods");
        CONFIG_ModifyWindowSize = Preferences_ConfigFileWriter.getParameter("GameSizeEnabled").toLowerCase().equals("true");
        CONFIG_ShowConsoleOnStartup = Preferences_ConfigFileWriter.getParameter("ShowConsoleOnStartup").toLowerCase().equals("true");
        CONFIG_ShowTrayIcon = Preferences_ConfigFileWriter.getParameter("ShowTrayIcon").toLowerCase().equals("true");
        CONFIG_AutoLogin = Preferences_ConfigFileWriter.getParameter("AutoLogin").toLowerCase().equals("true");
        CONFIG_AutoUpdate = Preferences_ConfigFileWriter.getParameter("AutoUpdate").toLowerCase().equals("true");
        CONFIG_ShowErrorNotifications = Preferences_ConfigFileWriter.getParameter("ShowErrorNotifications").toLowerCase().equals("true");
        CONFIG_AlternativeMinecraftUpdateServer = Preferences_ConfigFileWriter.getParameter("AlternativeMinecraftUpdateServer").toLowerCase().equals("true");

        try
        {
            if ( !CONFIG_ModifyWindowSize ) { throw new NumberFormatException(); }

            CONFIG_WindowSize = Preferences_ConfigFileWriter.getParameter("GameSizeXY");
            String[] splitSize = CONFIG_WindowSize.split(",");

            CONFIG_WindowSizeX = Integer.parseInt(splitSize[0]);
            CONFIG_WindowSizeY = Integer.parseInt(splitSize[1]);
        }
        catch ( NumberFormatException e )
        {
            CONFIG_WindowSize = Main_RealLauncher.getDefaultWindowSize();

            CONFIG_WindowSizeX = Main_RealLauncher.defaultWindowSizeX;
            CONFIG_WindowSizeY = Main_RealLauncher.defaultWindowSizeY;
        }
    }

    public static void SYSTEM_SavePreferences()
    {
        Preferences_ConfigFileWriter.setParameter("DisableUpdates", CONFIG_updatesDisabled);
        Preferences_ConfigFileWriter.setParameter("JarSelector", CONFIG_jarSelector);
        Preferences_ConfigFileWriter.setParameter("RamSelector", CONFIG_ramSelector);
        Preferences_ConfigFileWriter.setParameter("SaveLastJar", CONFIG_SaveLastJar);
        Preferences_ConfigFileWriter.setParameter("LWJGLSelector", CONFIG_LWJGLSelector);
        Preferences_ConfigFileWriter.setParameter("RAM", CONFIG_selectedRam);
        Preferences_ConfigFileWriter.setParameter("ErreurSonore", CONFIG_erreurSonore);
        Preferences_ConfigFileWriter.setParameter("RemoveMETAINF", CONFIG_RemoveMETAINF);
        Preferences_ConfigFileWriter.setParameter("AutomaticRenameJar", CONFIG_AutomaticJarRename);
        Preferences_ConfigFileWriter.setParameter("ShowConsoleOnStartup", CONFIG_ShowConsoleOnStartup);
        Preferences_ConfigFileWriter.setParameter("ShowTrayIcon", CONFIG_ShowTrayIcon);
        Preferences_ConfigFileWriter.setParameter("AutoLogin", CONFIG_AutoLogin);
        Preferences_ConfigFileWriter.setParameter("AutoUpdate", CONFIG_AutoUpdate);
        Preferences_ConfigFileWriter.setParameter("ShowErrorNotifications", CONFIG_ShowErrorNotifications);
        Preferences_ConfigFileWriter.setParameter("AlternativeMinecraftUpdateServer", CONFIG_AlternativeMinecraftUpdateServer);

        if ( !CONFIG_LWJGLSelector ) { Preferences_ConfigFileWriter.setParameter("LWJGLAddress", ""); }

        Preferences_ConfigFileWriter.setParameter("GameSizeEnabled", CONFIG_ModifyWindowSize);

        Preferences_ConfigFileWriter.setParameter("GameSizeXY", CONFIG_WindowSize);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Nicnl - nicnl25@gmail.com
}
