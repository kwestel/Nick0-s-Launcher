import java.io.*;
import java.util.ArrayList;

public final class Preferences_ConfigFileWriter
{
    
    private static ArrayList<String> loadedFile = new ArrayList<String>();
    private static boolean reloadFile = true;
    private static final String[] parameterList = new String[]
    {
        "Username",
        "Version",
        "EncP",
        "EncH",

        "DisableUpdates",
        "JarSelector",
        "RamSelector",
        "HomeDir",
        "RAM",
        "SaveLastJar",
        "LastJarSaved",
        "ModsButtonChecked",
        "NicnlModsButtonChecked",

        "LWJGLSelector",
        "LWJGLAddress",

        "GameSizeEnabled",
        "GameSizeXY",

        "ErreurSonore",

        "RemoveMETAINF"
    };
    private static final String[] parameterData = new String[]
    {
        "",
        "0",
        "",
        "",

        "false",
        "false",
        "RamSelector",
        "",
        "1024",
        "false",
        "",
        "false",
        "false",

        "false",
        "",

        "false",
        Main_RealLauncher.getDefaultWindowSize(),

        "true",

        "true"
    };

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Basic Write Functions
    
    public static final String getParameter(String index)
    {
        loadConfigFile();

        for ( String actualIndex : loadedFile )
        {
            String[] splitString = actualIndex.split("=");
            try
            {
                if ( splitString[0].toLowerCase().equals(index.toLowerCase()) )
                {
                    if ( splitString.length > 1 ) { return splitString[1]; }
                    else { return ""; }
                }
            }
            catch ( Exception e ) { return ""; }
        }

        addParameterToCurrentFile(index);
        loadConfigFile();

        for ( String actualIndex : loadedFile )
        {
            String[] splitString = actualIndex.split("=");
            try
            {
                if ( splitString[0].toLowerCase().equals(index.toLowerCase()) )
                {
                    if ( splitString.length > 1 ) { return splitString[1]; }
                    else { return ""; }
                }
            }
            catch ( Exception e ) { return ""; }
        }

        System_ErrorHandler.handleError("Erreur lors de la récupération de : " + index, false, true);
        return null;
    }
    
    public static final void setParameter(String index, boolean data) { setParameter(index, data+""); }
    public static final void setParameter(String index, int data) { setParameter(index, data+""); }
    
    public static final void setParameter(String index, String data)
    {
        loadConfigFile();

        for ( int i=0; i<loadedFile.size(); i++ )
        {
            String[] splitString = loadedFile.get(i).split("=");
            if ( splitString[0].toLowerCase().equals(index.toLowerCase()) )
            {
                loadedFile.set(i, index + "=" + data);
                writeFile();
                return;
            }
        }

        addParameterToCurrentFile(index);
        loadConfigFile();

        for ( int i=0; i<loadedFile.size(); i++ )
        {
            String[] splitString = loadedFile.get(i).split("=");
            if ( splitString[0].toLowerCase().equals(index.toLowerCase()) )
            {
                loadedFile.set(i, index + "=" + data);
                writeFile();
                return;
            }
        }

        System_ErrorHandler.handleError("Erreur lors de la définition de : " + index, false, true);
    }

    public static void writeConfigFile(String encodedPassword, boolean writeLogin, boolean erasePassword)
    {
        if ( writeLogin ) { setParameter("Username", System_DataStub.static_getParameter("username")); }
        
        if ( !encodedPassword.equals("") && !erasePassword )
        {
            boolean SaveLogin = ( GuiForm_MainFrame.mainFrame != null ) && GuiForm_MainFrame.mainFrame.Check_SaveLogin.isSelected();

            if ( !Preferences_ConfigLoader.CONFIG_updatesDisabled ) { setParameter("Version", System_DataStub.static_getParameter("latestVersion")); }
            setParameter("EncP", SaveLogin ? encodedPassword : "");
            setParameter("EncH", SaveLogin ? encodedPassword.hashCode()+"" : "");
        }

        if ( erasePassword )
        {
            if ( !Preferences_ConfigLoader.CONFIG_updatesDisabled ) { setParameter("Version", System_DataStub.static_getParameter("latestVersion")); }
            setParameter("EncP", "");
            setParameter("EncH", "");
        }

        setParameter("DisableUpdates", Preferences_ConfigLoader.CONFIG_updatesDisabled);
        setParameter("JarSelector", Preferences_ConfigLoader.CONFIG_jarSelector);
        setParameter("RamSelector", Preferences_ConfigLoader.CONFIG_ramSelector);
        if ( !Main_RealLauncher.homeDir.equals(Main_RealLauncher.configFileDir) ) { setParameter("HomeDir", Main_RealLauncher.homeDir); }
        setParameter("RAM", Preferences_ConfigLoader.CONFIG_selectedRam);
        setParameter("SaveLastJar", Preferences_ConfigLoader.CONFIG_SaveLastJar);
        if ( Preferences_ConfigLoader.CONFIG_SaveLastJar ) { setParameter("LastJarSaved", Preferences_ConfigLoader.CONFIG_LastJarSaved); }
        setParameter("ModsButtonChecked", Preferences_ConfigLoader.CONFIG_modsButtonChecked);
        setParameter("NicnlModsButtonChecked", Preferences_ConfigLoader.CONFIG_NicnlModsButtonChecked);
    }
    
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Raw Loading File

    private static final ArrayList<String> loadFileRaw(String filePathInput) throws IOException
    {
        FileReader Load_FileReader = new FileReader(filePathInput);
        BufferedReader Load_BufferedReader = new BufferedReader(Load_FileReader);

        ArrayList<String> newLoadedFile = new ArrayList<String>();

        String actualLine;
        while ( (actualLine = Load_BufferedReader.readLine()) != null ) { newLoadedFile.add(actualLine); }

        Load_BufferedReader.close();
        Load_FileReader.close();

        return newLoadedFile;
    }

    private static final void loadConfigFile()
    {
        if ( !reloadFile ) { return; }
        else { reloadFile = false; }

        try { loadedFile = Preferences_ConfigFileWriter.loadFileRaw(Main_RealLauncher.getConfigFilePath()); }
        catch ( IOException e )
        {
            writeEmptyFile();
            System_LogWriter.write("Saved Configuration Error !");
        }
    }
    
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Raw Writing Functions

    private static final void writeEmptyFile()
    {
        loadedFile = new ArrayList<String>();
        for ( int i=0; i<parameterList.length; i++ ) { loadedFile.add(parameterList[i] + "=" + parameterData[i]); }

        writeFile();
    }
    
    private static final void addParameterToCurrentFile(String newParameter)
    {
        String newParameterData = "";
        for ( int i=0; i<parameterList.length; i++ )
        {
            if ( parameterList[i].toLowerCase().trim().equals(newParameter.toLowerCase().trim())) { newParameterData = parameterData[i]; }
        }
        
        loadedFile.add(newParameter + "=" + newParameterData);
        writeFile();
    }

    private static final void writeFile()
    {
        try
        {
            System_FileManager.createFolder(Main_RealLauncher.configFileDir);

            FileWriter outputFileWriter = new FileWriter(new File(Main_RealLauncher.getConfigFilePath()));
            PrintWriter printFileWriter = new PrintWriter(outputFileWriter);

            for ( String dataLine : loadedFile ) { printFileWriter.println(dataLine); }

            printFileWriter.close();
            outputFileWriter.close();
        }
        catch ( Exception e ) { System_ErrorHandler.handleExceptionWithText(e, "Erreur lors de l'écriture du fichier.", false, true); }

        reloadFile = true;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Nicnl - nicnl25@gmail.com
}
