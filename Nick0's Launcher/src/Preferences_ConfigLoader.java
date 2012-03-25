public class Preferences_ConfigLoader
{

    public static boolean MinecraftReinstallForcer = false;

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

    public static boolean CONFIG_erreurSonore = false;

    public static void SYSTEM_LoadPreferences()
    {
        System_LogWriter.write("Chargement des préférences...");

        CONFIG_updatesDisabled = Preferences_ConfigFileWriter.getParameter("DisableUpdates").equals("true");
        CONFIG_jarSelector = Preferences_ConfigFileWriter.getParameter("JarSelector").equals("true");
        CONFIG_ramSelector = Preferences_ConfigFileWriter.getParameter("RamSelector").equals("true");

        CONFIG_selectedRam = Integer.parseInt(Preferences_ConfigFileWriter.getParameter("RAM"));

        CONFIG_SaveLastJar = Preferences_ConfigFileWriter.getParameter("SaveLastJar").equals("true");
        CONFIG_LastJarSaved = Preferences_ConfigFileWriter.getParameter("LastJarSaved");

        CONFIG_modsButtonChecked = Preferences_ConfigFileWriter.getParameter("ModsButtonChecked").equals("true");
        CONFIG_NicnlModsButtonChecked = Preferences_ConfigFileWriter.getParameter("NicnlModsButtonChecked").equals("true");

        CONFIG_LWJGLSelector = Preferences_ConfigFileWriter.getParameter("LWJGLSelector").equals("true");
        CONFIG_LWJGLAddress = Preferences_ConfigFileWriter.getParameter("LWJGLAddress");

        CONFIG_erreurSonore = Preferences_ConfigFileWriter.getParameter("ErreurSonore").equals("true");

        CONFIG_ModifyWindowSize = Preferences_ConfigFileWriter.getParameter("GameSizeEnabled").equals("true");
        try
        {
            if ( !CONFIG_ModifyWindowSize ) { throw new NumberFormatException(); }

            CONFIG_WindowSize = Preferences_ConfigFileWriter.getParameter("GameSizeXY");
            String[] splitSize = CONFIG_WindowSize.split(",");
            
            System.out.println("test : " + splitSize[0] + Integer.parseInt(splitSize[0]));

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

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Nicnl - nicnl25@gmail.com
}
