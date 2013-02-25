import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class System_Mods
{
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Basic Vars

    private static boolean dataInitialized = false;

    public static ArrayList<String> modsVersions = new ArrayList<String>();
    public static ArrayList<String> modsName = new ArrayList<String>();
    public static ArrayList<Boolean> modsState = new ArrayList<Boolean>();

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Change Mod States

    public static boolean getModState(String modVersion, String modName)
    {
        for ( int i=0; i<modsVersions.size(); i++ )
        {
            if ( modsVersions.get(i).equals(modVersion) && modsName.get(i).equals(modName) )
            {
                return modsState.get(i);
            }
        }

        setModState(modVersion,  modName,  true);
        return true;
    }

    public static void setModState(String modVersion, String modName, boolean newState)
    {
        for ( int i=0; i<modsVersions.size(); i++ )
        {
            if ( modsVersions.get(i).equals(modVersion) && modsName.get(i).equals(modName) )
            {
                modsState.set(i, newState);
                return;
            }
        }

        modsVersions.add(modVersion);
        modsName.add(modName);
        modsState.add(true);
    }

    public static void reverseModState(String modVersion, String modName)
    {
        for ( int i=0; i<modsVersions.size(); i++ )
        {
            if ( modsVersions.get(i).equals(modVersion) && modsName.get(i).equals(modName) )
            {
                modsState.set(i, !modsState.get(i));
                return;
            }
        }

        modsVersions.add(modVersion);
        modsName.add(modName);
        modsState.add(false);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Data Initialization

    public static void initializeData()
    {
        if ( dataInitialized ) { return; }
        dataInitialized = true;

        try
        {
            if ( !Preferences_ConfigLoader.CONFIG_ModsDisabled.contains("<") ) {  }

            for ( String actualVersion : Preferences_ConfigLoader.CONFIG_ModsDisabled.split("<") )
            {
                if ( !actualVersion.contains(":") ) { continue; }

                String[] tempSplit = actualVersion.split(":");

                String finalVersion = tempSplit[0];
                String[] allMods = tempSplit[1].split("/");

                for ( String actualMod : allMods )
                {
                    String[] actualModSplit = actualMod.split(">");

                    modsName.add(actualModSplit[0]);
                    modsVersions.add(finalVersion);
                    modsState.add(actualModSplit[1].equals("true"));
                }
            }
        }
        catch ( Exception e ) { e.printStackTrace(); }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // System Functions

    public static String[][] getData(String versionToRefresh)
    {
        initializeData();

        ArrayList<String[]> finalData_Array = new ArrayList<String[]>();

        File modsDir = new File(Main_RealLauncher.getModsDirPath());

        String[] fileList = modsDir.list();

        if ( fileList == null ) { return new String[0][0]; }

        for ( String actualModsDir : fileList )
        {
            Pattern p = Pattern.compile("((\\d.{0,5})(\\d))");
            Matcher m = p.matcher(actualModsDir);
            if ( m.find() )
            {
                File versionModDir = new File(Main_RealLauncher.getModsDirPath() + File.separator + actualModsDir);
                for ( String actualMod : versionModDir.list() )
                {
                    if ( !actualMod.toLowerCase().endsWith(".zip") ) { continue; }
                    if ( !versionToRefresh.equals("Toutes") && !versionToRefresh.equals(actualModsDir) ) { continue; }

                    String actualModName = actualMod.substring(0, actualMod.length()-4);
                    String actualModState = getModState(actualModsDir, actualModName) ? "Actif" : "Inactif";

                    String[] finalData = new String[] { actualModName, actualModsDir, actualModState };
                    finalData_Array.add(finalData);
                }
            }
        }

        return finalData_Array.toArray(new String[finalData_Array.size()][]);
    }


    public static void saveData()
    {
        String finalString = "";

        ArrayList<String> differentVersions = new ArrayList<String>();
        for ( String actualVersion : modsVersions ) { if ( !differentVersions.contains(actualVersion) ) { differentVersions.add(actualVersion); } }

        for ( String actualVersion : differentVersions )
        {
            boolean haveFalse = false;
            for ( int i=0; i<modsVersions.size(); i++ ) { if ( modsVersions.get(i).equals(actualVersion) && !modsState.get(i) ) { haveFalse = true; } }
            if ( !haveFalse ) { continue; }

            String actualVersionTemp = actualVersion + ":";

            for ( int i=0; i<modsVersions.size(); i++ ) { if ( modsVersions.get(i).equals(actualVersion) && !modsState.get(i) ) { actualVersionTemp += modsName.get(i) + ">" + modsState.get(i) + "/"; } }

            finalString += actualVersionTemp.substring(0, actualVersionTemp.length()-1) + "<";
        }

        finalString = finalString.length() > 1 ? finalString.substring(0, finalString.length()-1) : "";
        Preferences_ConfigLoader.CONFIG_ModsDisabled = finalString;
        Preferences_ConfigFileWriter.setParameter("DisabledMods", finalString);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Install Mod

    public static void installMod(String modVersion, String modName, String modPath)
    {
        System_FileManager.createFolder(Main_RealLauncher.getBinDirPath());
        System_FileManager.createFolder(Main_RealLauncher.getModsDirPath());
        System_FileManager.createFolder(Main_RealLauncher.getModsDirPath() + File.separator + modVersion);

        try
        {
            byte[] readFile = System_FileManager.readFileBytes(modPath);
            System_FileManager.writeByteArrayToFile(readFile, Main_RealLauncher.getModsDirPath() + File.separator + modVersion + File.separator + modName + ".zip");
        }
        catch ( IOException e )
        {
            System_ErrorHandler.handleExceptionWithText(e, "Erreur lors de l'installation du mod : " + modName, false, true);
            e.printStackTrace();
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Install Mod

    public static String[] getAllDifferentModVersions()
    {
        ArrayList<String> differentVersions = new ArrayList<String>();
        for ( String actualVersion : modsVersions ) { if ( !differentVersions.contains(actualVersion) ) { differentVersions.add(actualVersion); } }

        return differentVersions.toArray(new String[differentVersions.size()]);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Nicnl - nicnl25@gmail.com
}
