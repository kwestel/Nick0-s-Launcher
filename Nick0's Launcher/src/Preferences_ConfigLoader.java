public class Preferences_ConfigLoader
{

    public static boolean MinecraftReinstallForcer = false;

    public static boolean CONFIG_updatesDisabled = false;
    public static boolean CONFIG_jarSelector = false;
    public static boolean CONFIG_ramSelector = false;
    public static int CONFIG_selectedRam = 1024;

    public static void SYSTEM_LoadPreferences()
    {
        System.out.println("Nick0's Launcher - Chargement des préférences...");

        String[] loadedPreferences = System_ConfigFileWriter.loadConfigFile();

        if ( loadedPreferences == null ) { return; }

        Preferences_ConfigLoader.CONFIG_updatesDisabled = loadedPreferences[4].split("=")[1].equals("TRUE");
        Preferences_ConfigLoader.CONFIG_jarSelector = loadedPreferences[5].split("=")[1].equals("TRUE");
        Preferences_ConfigLoader.CONFIG_ramSelector = loadedPreferences[6].split("=")[1].equals("TRUE");

        Preferences_ConfigLoader.CONFIG_selectedRam = Integer.parseInt(loadedPreferences[8].split("=")[1]);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Nicnl - nicnl25@gmail.com
}
