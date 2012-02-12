import java.io.File;
import java.net.URISyntaxException;

public class Main_ReLauncher
{
    private static final int minimalAllocatedMemory = 512;

    public static void main(String[] args)
    {
        float allocatedMemory = Runtime.getRuntime().maxMemory() / 1024L / 1024L;
        
        int memoryToApply;
        File configFile = new File(Main_RealLauncher.getConfigFilePath());
        String[] loadedConfigFile;
        if ( configFile.exists()  )
        {
            loadedConfigFile = System_ConfigFileWriter.loadConfigFile();
            memoryToApply = Integer.parseInt(loadedConfigFile[8].split("=")[1]);
        }
        else
        {
            loadLauncher(false);
            return;
        } // Premier démarrage

        if ( allocatedMemory >= minimalAllocatedMemory && ( loadedConfigFile[6] != null && loadedConfigFile[6].contains("FALSE") ) ) { loadLauncher(false); }
        else { loadLauncher(memoryToApply); }
    }

    public static void loadLauncher(int memory)
    {
        String pathToJar = null;
        try { pathToJar = Main_ReLauncher.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath(); }
        catch ( URISyntaxException e ) { System_ErrorHandler.handleException(e, true); }

        String[] newParameters = new String[]
        {
            "java" + ( System_UserHomeDefiner.SystemOS.equals("macosx") ? "" : "w"),
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
            System_ErrorHandler.handleError("Une erreur est survenue lors de la définition de la ram !", false, true);
            loadLauncher(false);
        }
    }
    
    public static void loadLauncher(boolean hyperMode)
    {
        if ( !hyperMode ) { Main_RealLauncher.main(null); }
        else
        {
            String pathToJar = null;
            try { pathToJar = Main_ReLauncher.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath(); }
            catch ( URISyntaxException e ) { System_ErrorHandler.handleException(e, true); }

            String[] newParameters = new String[]
            {
                "java" + ( System_UserHomeDefiner.SystemOS.equals("macosx") ? "" : "w"),
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
