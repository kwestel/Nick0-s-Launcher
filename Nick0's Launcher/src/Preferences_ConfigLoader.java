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

        String[] loadedPreferences = Preferences_ConfigFileWriter.loadConfigFile();

        if ( loadedPreferences == null ) { return; }

        try
        {
            /* UpdatOff */ CONFIG_updatesDisabled = loadedPreferences[4].split("=")[1].equals("TRUE");
            /* JarSelec */ CONFIG_jarSelector = loadedPreferences[5].split("=")[1].equals("TRUE");
            /* RamSelec */ CONFIG_ramSelector = loadedPreferences[6].split("=")[1].equals("TRUE");

            /* Ram: Int */ CONFIG_selectedRam = Integer.parseInt(loadedPreferences[8].split("=")[1]);

            /* SaveLJar */ CONFIG_SaveLastJar = loadedPreferences[9].split("=")[1].equals("TRUE");
            /* JarSaved */ CONFIG_LastJarSaved = ( loadedPreferences[10].split("=").length == 2 ) ? ( loadedPreferences[10].split("=")[1] ) : "";

            /* Mod Butt */ CONFIG_modsButtonChecked = loadedPreferences[11].split("=")[1].equals("TRUE");
            /* NicnlMod */ CONFIG_NicnlModsButtonChecked = loadedPreferences[12].split("=")[1].equals("TRUE");
        }
        catch ( ArrayIndexOutOfBoundsException e )
        {
            System_LogWriter.write("Erreur lors du chargement des préférences ! Update simple du fichier...");
            e.printStackTrace();
            Preferences_ConfigFileWriter.updateConfigFile(false);
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Nicnl - nicnl25@gmail.com
}
