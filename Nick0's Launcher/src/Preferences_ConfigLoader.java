public class Preferences_ConfigLoader
{

    public static boolean MinecraftReinstallForcer = false;

    public static boolean CONFIG_updatesDisabled = false;

    public static boolean CONFIG_modsButtonChecked = false;
    public static boolean CONFIG_NicnlModsButtonChecked = false;
    
    public static boolean CONFIG_jarSelector = false;
    public static boolean CONFIG_SaveLastJar = false;
    public static String CONFIG_LastJarSaved = "";
    
    public static boolean CONFIG_ramSelector = false;
    public static int CONFIG_selectedRam = 1024;

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
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Nicnl - nicnl25@gmail.com
}
