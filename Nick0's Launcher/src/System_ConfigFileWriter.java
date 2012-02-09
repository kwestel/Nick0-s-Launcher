import java.io.*;
import java.util.ArrayList;

public class System_ConfigFileWriter
{
    
    private static ArrayList<String> dataCache;
    
    public static void clear() { dataCache = new ArrayList<String>(); }
    public static void addLine(String line) { dataCache.add(line); }
    
    public static void writeDataToFile(String outputFileName) throws IOException
    {
        File configDir = new File(Main_RealLauncher.configFileDir);
        if ( !configDir.exists() ) { configDir.mkdir(); }
        
        FileWriter outputFileWriter = new FileWriter(new File(outputFileName));
        PrintWriter printFileWriter = new PrintWriter(outputFileWriter);
        for ( String dataLine : dataCache ) { printFileWriter.println(dataLine); }
        printFileWriter.close();
        outputFileWriter.close();
    }
    
    public static String[] loadFile(String filePathInput) throws IOException
    {
        BufferedReader Load_BufferedReader = new BufferedReader(new FileReader(filePathInput));
        ArrayList<String> LoadedFile = new ArrayList<String>();
        String actualLine;
        while ( (actualLine = Load_BufferedReader.readLine()) != null ) { LoadedFile.add(actualLine); }
        return LoadedFile.toArray(new String[LoadedFile.size()]);
    }
    
    public static void writeConfigFile(String encodedPassword)
    {
        clear();
        /* Username */ addLine(System_DataStub.static_getParameter("username"));
        /* LVersion */ addLine(Preferences_ConfigLoader.CONFIG_updatesDisabled ? loadConfigFile()[1] : System_DataStub.static_getParameter("latestVersion") );
        /* Password */ addLine(Main_RealLauncher.MainFrame.Check_SaveLogin.isSelected() ? encodedPassword : "");
        /* PassHash */ addLine(Main_RealLauncher.MainFrame.Check_SaveLogin.isSelected() ? ""+encodedPassword.hashCode() : "");
        /* UpdatOff */ addLine("DisableUpdates=" + (Preferences_ConfigLoader.CONFIG_updatesDisabled ? "TRUE" : "FALSE"));
        /* JarSelec */ addLine("JarSelector=" + (Preferences_ConfigLoader.CONFIG_jarSelector ? "TRUE" : "FALSE"));
        /* RamSelec */ addLine("RamSelector=" + (Preferences_ConfigLoader.CONFIG_ramSelector ? "TRUE" : "FALSE"));
        /* Home Dir */ addLine("HomeDir=" + (Main_RealLauncher.homeDir.equals(Main_RealLauncher.configFileDir) ? "" : Main_RealLauncher.homeDir));
        /* Ram: Int */ addLine("RAM=" + Preferences_ConfigLoader.CONFIG_selectedRam);
        /* SaveLJar */ addLine("SaveLastJar=" + (Preferences_ConfigLoader.CONFIG_SaveLastJar ? "TRUE" : "FALSE"));
        /* JarSaved */ addLine("LastJarSaved=" + (Preferences_ConfigLoader.CONFIG_SaveLastJar ? Preferences_ConfigLoader.CONFIG_LastJarSaved : ""));
        /* ModButt */ addLine("ModsButtonChecked=" + (Preferences_ConfigLoader.CONFIG_modsButtonChecked ? "TRUE" : "FALSE"));

        try { System_ConfigFileWriter.writeDataToFile(Main_RealLauncher.configFileDir + Main_RealLauncher.configFileName); }
        catch ( IOException e ) { System_ErrorHandler.handleException(e, false); }
    }

    public static String[] writeEmptyFile()
    {
        clear();
        /* Username */ addLine("");
        /* LVersion */ addLine("0");
        /* Password */ addLine("");
        /* PassHash */ addLine("");
        /* UpdatOff */ addLine("DisableUpdates=FALSE");
        /* JarSelec */ addLine("JarSelector=FALSE");
        /* RamSelec */ addLine("RamSelector=FALSE");
        /* Home Dir */ addLine("HomeDir=");
        /* RamSelec */ addLine("RAM=1024");
        /* SaveLJar */ addLine("SaveLastJar=FALSE");
        /* JarSaved */ addLine("LastJarSaved=");
        /* ModButt */ addLine("ModsButtonChecked=FALSE");

        try { System_ConfigFileWriter.writeDataToFile(Main_RealLauncher.configFileDir + Main_RealLauncher.configFileName); }
        catch ( IOException e ) { System_ErrorHandler.handleException(e, false); }
        
        return dataCache.toArray(new String[dataCache.size()]);
    }
    
    public static void updateConfigFile()
    {
        String[] oldConfigFile = loadConfigFile();
        
        clear();
        /* Username */ addLine(oldConfigFile[0]);
        /* LVersion */ addLine(oldConfigFile[1]);
        /* Password */ addLine(oldConfigFile[2]);
        /* PassHash */ addLine(oldConfigFile[3]);
        /* UpdatOff */ addLine("DisableUpdates=" + (Preferences_ConfigLoader.CONFIG_updatesDisabled ? "TRUE" : "FALSE"));
        /* JarSelec */ addLine("JarSelector=" + (Preferences_ConfigLoader.CONFIG_jarSelector ? "TRUE" : "FALSE"));
        /* RamSelec */ addLine("RamSelector=" + (Preferences_ConfigLoader.CONFIG_ramSelector ? "TRUE" : "FALSE"));
        /* Home Dir */ addLine("HomeDir=" + (Main_RealLauncher.homeDir.equals(Main_RealLauncher.configFileDir) ? "" : Main_RealLauncher.homeDir));
        /* Ram: Int */ addLine("RAM=" + Preferences_ConfigLoader.CONFIG_selectedRam);
        /* SaveLJar */ addLine("SaveLastJar=" + (Preferences_ConfigLoader.CONFIG_SaveLastJar ? "TRUE" : "FALSE"));
        /* JarSaved */ addLine("LastJarSaved=" + (Preferences_ConfigLoader.CONFIG_SaveLastJar ? Preferences_ConfigLoader.CONFIG_LastJarSaved : ""));
        /* ModButt */ addLine("ModsButtonChecked=" + (Preferences_ConfigLoader.CONFIG_modsButtonChecked ? "TRUE" : "FALSE"));

        try { System_ConfigFileWriter.writeDataToFile(Main_RealLauncher.configFileDir + Main_RealLauncher.configFileName); }
        catch ( IOException e ) { System_ErrorHandler.handleException(e, false); }
    }
    
    public static String[] loadConfigFile()
    {
        String[] loadedFile;
        try
        {
            //File NicnlConfigFile = new File(Main_RealLauncher.configFileDir + Main_RealLauncher.configFileName);
            loadedFile = System_ConfigFileWriter.loadFile(Main_RealLauncher.configFileDir + Main_RealLauncher.configFileName);
        }
        catch ( IOException e )
        {
            loadedFile = writeEmptyFile();
            System.out.println("Nick0's Launcher - Saved Configuration Error !");
        }
        return loadedFile;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Nicnl - nicnl25@gmail.com
}
