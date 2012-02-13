import java.io.*;
import java.util.ArrayList;

public class System_ConfigFileWriter
{
    
    private static ArrayList<String> dataCache;
    
    public static void clear() { dataCache = new ArrayList<String>(); }
    public static void addLine(String line) { dataCache.add(line); }
    
    public static void writeDataToFile(String outputFileName) throws IOException
    {
        System_FileManager.createFolder(Main_RealLauncher.configFileDir);
        
        FileWriter outputFileWriter = new FileWriter(new File(outputFileName));
        PrintWriter printFileWriter = new PrintWriter(outputFileWriter);
        for ( String dataLine : dataCache ) { printFileWriter.println(dataLine); }
        printFileWriter.close();
        outputFileWriter.close();
    }
    
    public static String[] loadFile(String filePathInput) throws IOException
    {
        FileReader Load_FileReader = new FileReader(filePathInput);
        BufferedReader Load_BufferedReader = new BufferedReader(Load_FileReader);
        
        ArrayList<String> LoadedFile = new ArrayList<String>();
        
        String actualLine;
        while ( (actualLine = Load_BufferedReader.readLine()) != null ) { LoadedFile.add(actualLine); }
        
        Load_BufferedReader.close();
        Load_FileReader.close();

        return LoadedFile.toArray(new String[LoadedFile.size()]);
    }
    
    public static void writeConfigFile(String encodedPassword)
    {
        clear();
        /* Username */ addLine(System_DataStub.static_getParameter("username"));
        /* LVersion */ addLine(Preferences_ConfigLoader.CONFIG_updatesDisabled ? loadConfigFile()[1] : System_DataStub.static_getParameter("latestVersion") );
        /* Password */ addLine(Main_RealLauncher.MainFrame.Check_SaveLogin.isSelected() ? encodedPassword : "");
        /* PassHash */ addLine(Main_RealLauncher.MainFrame.Check_SaveLogin.isSelected() ? (""+encodedPassword.hashCode()) : "");
        /* UpdatOff */ addLine("DisableUpdates=" + (Preferences_ConfigLoader.CONFIG_updatesDisabled ? "TRUE" : "FALSE"));
        /* JarSelec */ addLine("JarSelector=" + (Preferences_ConfigLoader.CONFIG_jarSelector ? "TRUE" : "FALSE"));
        /* RamSelec */ addLine("RamSelector=" + (Preferences_ConfigLoader.CONFIG_ramSelector ? "TRUE" : "FALSE"));
        /* Home Dir */ addLine("HomeDir=" + (Main_RealLauncher.homeDir.equals(Main_RealLauncher.configFileDir) ? "" : Main_RealLauncher.homeDir));
        /* Ram: Int */ addLine("RAM=" + Preferences_ConfigLoader.CONFIG_selectedRam);
        /* SaveLJar */ addLine("SaveLastJar=" + (Preferences_ConfigLoader.CONFIG_SaveLastJar ? "TRUE" : "FALSE"));
        /* JarSaved */ addLine("LastJarSaved=" + (Preferences_ConfigLoader.CONFIG_SaveLastJar ? Preferences_ConfigLoader.CONFIG_LastJarSaved : ""));
        /* Mod Butt */ addLine("ModsButtonChecked=" + (Preferences_ConfigLoader.CONFIG_modsButtonChecked ? "TRUE" : "FALSE"));

        try { System_ConfigFileWriter.writeDataToFile(Main_RealLauncher.getConfigFilePath()); }
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
        /* Mod Butt */ addLine("ModsButtonChecked=FALSE");

        try { System_ConfigFileWriter.writeDataToFile(Main_RealLauncher.getConfigFilePath()); }
        catch ( IOException e ) { System_ErrorHandler.handleException(e, false); }
        
        return dataCache.toArray(new String[dataCache.size()]);
    }
    
    public static void updateConfigFile(boolean forceSave)
    {
        String[] oldConfigFile = loadConfigFile();
        
        clear();
        /* Username */ addLine(oldConfigFile[0]);
        /* LVersion */ addLine(oldConfigFile[1]);
        /* Password */ addLine((Main_RealLauncher.MainFrame.Check_SaveLogin.isSelected() || forceSave) ? oldConfigFile[2] : "");
        /* PassHash */ addLine((Main_RealLauncher.MainFrame.Check_SaveLogin.isSelected() || forceSave) ? oldConfigFile[3] : "");
        /* UpdatOff */ addLine("DisableUpdates=" + (Preferences_ConfigLoader.CONFIG_updatesDisabled ? "TRUE" : "FALSE"));
        /* JarSelec */ addLine("JarSelector=" + (Preferences_ConfigLoader.CONFIG_jarSelector ? "TRUE" : "FALSE"));
        /* RamSelec */ addLine("RamSelector=" + (Preferences_ConfigLoader.CONFIG_ramSelector ? "TRUE" : "FALSE"));
        /* Home Dir */ addLine("HomeDir=" + (Main_RealLauncher.homeDir.equals(Main_RealLauncher.configFileDir) ? "" : Main_RealLauncher.homeDir));
        /* Ram: Int */ addLine("RAM=" + Preferences_ConfigLoader.CONFIG_selectedRam);
        /* SaveLJar */ addLine("SaveLastJar=" + (Preferences_ConfigLoader.CONFIG_SaveLastJar ? "TRUE" : "FALSE"));
        /* JarSaved */ addLine("LastJarSaved=" + (Preferences_ConfigLoader.CONFIG_SaveLastJar ? Preferences_ConfigLoader.CONFIG_LastJarSaved : ""));
        /* Mod Butt */ addLine("ModsButtonChecked=" + (Preferences_ConfigLoader.CONFIG_modsButtonChecked ? "TRUE" : "FALSE"));

        try { System_ConfigFileWriter.writeDataToFile(Main_RealLauncher.getConfigFilePath()); }
        catch ( IOException e ) { System_ErrorHandler.handleException(e, false); }
    }
    
    public static String[] loadConfigFile()
    {
        String[] loadedFile;
        try { loadedFile = System_ConfigFileWriter.loadFile(Main_RealLauncher.getConfigFilePath()); }
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
