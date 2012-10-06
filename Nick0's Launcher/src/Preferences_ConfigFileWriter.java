import javax.swing.text.JTextComponent;
import java.io.*;
import java.util.ArrayList;
import java.util.Collections;

public final class Preferences_ConfigFileWriter
{
    
    private static ArrayList<String> loadedFile = new ArrayList<String>();
    private static boolean reloadFile = true;
    private static final String[] configurationParameters = new String[]
    {
        "Username",
        "UsernameList",
        "Version=0",

        "DisableUpdates=false",
        "JarSelector=false",
        "RamSelector=false",
        "HomeDir",
        "RAM=1024",
        "SaveLastJar=false",
        "LastJarSaved",
        "ModsButtonChecked=false",
        "NicnlModsButtonChecked=false",

        "LWJGLSelector=false",
        "LWJGLAddress",

        "GameSizeEnabled=false",
        "GameSizeXY=" + Main_RealLauncher.getDefaultWindowSize(),

        "ErreurSonore=true",

        "RemoveMETAINF=true",

        "AutomaticRenameJar=true",
        "DisabledMods",

        "ShowConsoleOnStartup=false",
        "ShowTrayIcon=true",
        "AutoLogin=false",
        "AutoUpdate=false",
        "ShowErrorNotifications=true"
    };

    public static String d;

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Basic Write Functions
    
    public static final String getParameter(String index)
    {
        loadConfigFile();

        for ( String actualLine : loadedFile )
        {
            String parameterIndex = actualLine.contains("=") ? actualLine.substring(0, actualLine.indexOf("=")) : actualLine;

            try
            {
                if ( parameterIndex.toLowerCase().trim().equals(index.toLowerCase().trim()) )
                {
                    if ( actualLine.length() - (index.length()+1) > 0 ) { return actualLine.substring(actualLine.indexOf("=") + 1, actualLine.length()); }
                    else { return ""; }
                }
            }
            catch ( Exception e ) { return ""; }
        }

        addDefaultParameterToIndex(index);
        loadConfigFile();

        for ( String actualLine : loadedFile )
        {
            // String parameterIndex = actualLine.substring(0, actualLine.indexOf("="));
            String parameterIndex = actualLine.contains("=") ? actualLine.substring(0, actualLine.indexOf("=")) : actualLine;

            try
            {
                if ( parameterIndex.toLowerCase().trim().equals(index.toLowerCase().trim()) )
                {
                    if ( actualLine.length() - (index.length()+1) > 0 ) { return actualLine.substring(actualLine.indexOf("=") + 1, actualLine.length()); }
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

    public static final void eraseParameter(String parameterIndex)
    {
        loadConfigFile();

        for ( int i=0; i<loadedFile.size(); i++ )
        {
            String actualIndex = loadedFile.get(i).contains("=") ? loadedFile.get(i).substring(0, loadedFile.get(i).indexOf("=")) : loadedFile.get(i);

            if ( parameterIndex.toLowerCase().trim().equals(actualIndex.toLowerCase().trim()) )
            {
                loadedFile.remove(i);
                writeFile();
                return;
            }
        }
    }
    
    public static final void setParameter(String parameterIndex, String data)
    {
        if ( data.toLowerCase().trim().equals("") ) { eraseParameter(parameterIndex); return; }

        loadConfigFile();

        for ( int i=0; i<loadedFile.size(); i++ )
        {
            // String[] splitString = loadedFile.get(i).split("=");
            String actualIndex = loadedFile.get(i).contains("=") ? loadedFile.get(i).substring(0, loadedFile.get(i).indexOf("=")) : loadedFile.get(i);

            if ( parameterIndex.toLowerCase().trim().equals(actualIndex.toLowerCase().trim()) )
            {
                loadedFile.set(i, parameterIndex + "=" + data);
                writeFile();
                return;
            }
        }

        addDefaultParameterToIndex(parameterIndex);
        loadConfigFile();

        for ( int i=0; i<loadedFile.size(); i++ )
        {
            // String actualIndex = loadedFile.get(i).substring(0, loadedFile.get(i).indexOf("="));
            String actualIndex = loadedFile.get(i).contains("=") ? loadedFile.get(i).substring(0, loadedFile.get(i).indexOf("=")) : loadedFile.get(i);

            if ( parameterIndex.toLowerCase().trim().equals(actualIndex.toLowerCase().trim()) )
            {
                loadedFile.set(i, parameterIndex + "=" + data);
                writeFile();
                return;
            }
        }

        System_ErrorHandler.handleError("Erreur lors de la définition de : " + parameterIndex, false, true);
    }

    public static void writeConfigFile(String encodedPassword, boolean writeLogin, boolean erasePassword, boolean offlineMode)
    {
        if ( writeLogin ) { setParameter("Username", System_DataStub.static_getParameter("loginusr")); System_MultiAccountHelper.saveUsername(System_DataStub.static_getParameter("loginusr"), offlineMode); }
        if ( !Preferences_ConfigLoader.CONFIG_updatesDisabled && !offlineMode ) { setParameter("Version", System_DataStub.static_getParameter("latestVersion")); }

        if ( !encodedPassword.equals("") && !erasePassword && !System_DataStub.static_getParameter("loginusr").equals("null") && System_DataStub.static_getParameter("loginusr") != null )
        {
            boolean saveLogin = ( GuiForm_MainFrame.mainFrame != null ) && GuiForm_MainFrame.mainFrame.Check_SaveLogin.isSelected();

            if ( System_DataStub.static_getParameter("loginusr") != null && !System_DataStub.static_getParameter("loginusr").toLowerCase().trim().equals("") )
            {
                if ( saveLogin )
                {
                    setParameter("EncP-" + System_DataStub.static_getParameter("loginusr"), encodedPassword);
                    setParameter("EncH-" + System_DataStub.static_getParameter("loginusr"), d);
                }
                else
                {
                    eraseParameter("EncP-" + System_DataStub.static_getParameter("loginusr"));
                    eraseParameter("EncH-" + System_DataStub.static_getParameter("loginusr"));
                }
            }

            // System.out.println("AAAAAAAAAAAAAAAAA : " + System_DataStub.static_getParameter("loginusr"));
        }

        if ( erasePassword && !((JTextComponent)GuiForm_MainFrame.mainFrame.ComboBox_UserName.getEditor().getEditorComponent()).getText().equals("") && ((JTextComponent)GuiForm_MainFrame.mainFrame.ComboBox_UserName.getEditor().getEditorComponent()).getText() != null )
        {
            eraseParameter("EncP-" + ((JTextComponent)GuiForm_MainFrame.mainFrame.ComboBox_UserName.getEditor().getEditorComponent()).getText());
            eraseParameter("EncH-" + ((JTextComponent) GuiForm_MainFrame.mainFrame.ComboBox_UserName.getEditor().getEditorComponent()).getText());

            // System.out.println("BBBBBBBBBBBBBBBBBBBB : " + System_DataStub.static_getParameter("loginusr"));
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

        try { loadedFile = loadFileRaw(Main_RealLauncher.getConfigFilePath()); }
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
        Collections.addAll(loadedFile, configurationParameters);

        writeFile();
    }
    
    private static final void addDefaultParameterToIndex(String parameterIndex)
    {
        for ( String configurationParameter : configurationParameters )
        {
            String parameterName = configurationParameter.contains("=") ? configurationParameter.substring(0, configurationParameter.indexOf("=")) : configurationParameter;
            if ( parameterName.toLowerCase().trim().equals(parameterIndex.toLowerCase().trim()) ) { loadedFile.add(configurationParameter + (configurationParameter.contains("=") ? "" : "=")); writeFile(); return; }
        }

        loadedFile.add(parameterIndex + "=");
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
