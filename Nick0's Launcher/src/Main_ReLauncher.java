import java.io.File;

public class Main_ReLauncher
{
    private static final int defaultMemory = 1024;
    private static final int minimalAllocatedMemory = 512;

    public static void main(String[] args)
    {
        float allocatedMemory = Runtime.getRuntime().maxMemory() / 1024L / 1024L;
        
        int memoryToApply;
        File configFile = new File(Main_RealLauncher.configFileDir + Main_RealLauncher.configFileName);
        String[] loadedConfigFile = new String[8];
        if ( configFile.exists()  )
        {
            loadedConfigFile = System_ConfigFileWriter.loadConfigFile();
            memoryToApply = Integer.parseInt(loadedConfigFile[8].split("=")[1]);
        }
        else { memoryToApply = defaultMemory; }

        if ( allocatedMemory >= minimalAllocatedMemory && ( loadedConfigFile[6] != null && loadedConfigFile[6].contains("FALSE") ) ) { Main_RealLauncher.main(args); }
        else
        {
            try
            {
                String pathToJar = Main_ReLauncher.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath();
                
                String[] newParameters = new String[]
                {
                    "java" + ( System_UserHomeDefiner.SystemOS.equals("macosx") ? "" : "w"),
                    "-Xmx" + memoryToApply + "m",
                    "-Dsun.java2d.noddraw=true",
                    "-Dsun.java2d.d3d=false",
                    "-Dsun.java2d.opengl=false",
                    "-Dsun.java2d.pmoffscreen=false",
                    "-classpath",
                    pathToJar,
                    "Main_RealLauncher"
                };

                ProcessBuilder launcherProcessBuilder = new ProcessBuilder(newParameters);
                Process newProcess = launcherProcessBuilder.start();
                if ( newProcess == null ) { throw new Exception("75261350 !"); }

                System.exit(0);
            }
            catch ( Exception e )
            {
                System_ErrorHandler.handleError("Une erreur est survenue lors de la définition de la ram !", false);
                Main_RealLauncher.main(args);
            }
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Nicnl - nicnl25@gmail.com
}
