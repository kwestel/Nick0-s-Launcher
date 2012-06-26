import javax.swing.*;
import java.io.*;
import java.lang.reflect.Method;
import java.net.URISyntaxException;

public class Main_ReLauncher
{
    private static final int minimalAllocatedMemory = 512;

    public static void main(String[] args)
    {
        System_LogWriter.initializeMinecraftLogs();

        try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); }
        catch ( Exception e ) { System_ErrorHandler.handleException(e, false); }

        float allocatedMemory = Runtime.getRuntime().maxMemory() / 1024L / 1024L;

        System_FileManager.createFolder(Main_RealLauncher.configFileDir);
        File errorFile = new File(Main_RealLauncher.configFileDir + File.separator + "Nick0sLauncher.error");
        if ( errorFile.exists() )
        {
            System_ErrorHandler.handleError("Lors de sa dernière éxécution,\nle launcher n'd pas démarré correctement.\n\nLes réglages RAM vont êtres ignorés.", false, true);
            loadLauncher(false);
            return;
        }
        else
        {
            try { if ( !errorFile.createNewFile() ) { System_ErrorHandler.handleError("Impossible de créer le fichier de vérification d'erreur !", false, true); } }
            catch ( IOException e ) { System_ErrorHandler.handleExceptionWithText(e, "Impossible de créer le fichier de vérification d'erreur !", false, true); }
        }
        
        int memoryToApply;
        File configFile = new File(Main_RealLauncher.getConfigFilePath());
        
        if ( configFile.exists()  )
        {
            try { memoryToApply = Integer.parseInt(Preferences_ConfigFileWriter.getParameter("RAM")); }
            catch ( Exception e ) { memoryToApply = 1024; }
        }
        else
        {
            loadLauncher(false);
            return;
        }

        if ( allocatedMemory >= minimalAllocatedMemory && !Preferences_ConfigFileWriter.getParameter("RamSelector").equals("true") ) { loadLauncher(false); }
        else { loadLauncher(memoryToApply); }
    }

    public static void loadLauncher(int memory)
    {
        String pathToJar = null;
        try { pathToJar = Main_ReLauncher.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath(); }
        catch ( URISyntaxException e ) { System_ErrorHandler.handleException(e, true); }

        String[] newParameters = new String[]
        {
            "java" + ( System_UserHomeDefiner.SystemOS.equals("windows") ? "w" : ""),
            "-Xmx" + memory + "m",
            "-Dsun.java2d.noddraw=true",
            "-Dsun.java2d.d3d=false",
            "-Dsun.java2d.opengl=false",
            "-Dsun.java2d.pmoffscreen=false",
            "-classpath",
            pathToJar,
            "Main_RealLauncher"
        };

        loadLauncher(newParameters);
    }
    
    public static void loadLauncher(String[] launcherParameters)
    {
        try
        {
            ProcessBuilder launcherProcessBuilder = new ProcessBuilder(launcherParameters);
            Process newProcess = launcherProcessBuilder.start();

            if ( newProcess == null ) { throw new Exception("75261350 !"); }

            System.exit(0);
        }
        catch ( Exception e )
        {
            System_ErrorHandler.handleExceptionWithText(e, "Une erreur est survenue lors de la définition de la ram !", false, true);
            loadLauncher(false);
        }
    }
    
    public static void loadLauncher(boolean recreateProcess)
    {
        if ( !recreateProcess ) { Main_RealLauncher.main(null); }
        else
        {
            String pathToJar = null;
            try { pathToJar = Main_ReLauncher.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath(); }
            catch ( URISyntaxException e ) { System_ErrorHandler.handleException(e, true); }

            String[] newParameters = new String[]
            {
                "java" + ( System_UserHomeDefiner.SystemOS.equals("windows") ? "w" : ""),
                "-Dsun.java2d.noddraw=true",
                "-Dsun.java2d.d3d=false",
                "-Dsun.java2d.opengl=false",
                "-Dsun.java2d.pmoffscreen=false",
                "-classpath",
                pathToJar,
                "Main_RealLauncher"
            };

            loadLauncher(newParameters);
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Nicnl - nicnl25@gmail.com
}
