import java.io.IOException;

public class Thread_UpdateAllJars extends Thread
{
    
    private String destinationPath;
    private String nativesFile;
    private boolean forceDownload;
    private Gui_UpdaterForm formToUpdate;

    private boolean updateAllJars;
    
    public Thread_UpdateAllJars(String arg_destinationPath, String arg_nativesFile, boolean arg_forceDownload, Gui_UpdaterForm form)
    {
        destinationPath = arg_destinationPath;
        nativesFile = arg_nativesFile;
        forceDownload = arg_forceDownload;
        formToUpdate = form;

        updateAllJars = true;
    }

    public Thread_UpdateAllJars(String arg_destinationPath, Gui_UpdaterForm form)
    {
        destinationPath = arg_destinationPath;
        formToUpdate = form;

        updateAllJars = false;
    }
    
    public void run()
    {
        try
        {
            if ( updateAllJars ) { Updater_SystemFunctions.updateAllJars(destinationPath, nativesFile, forceDownload, formToUpdate); }
            else  { Updater_SystemFunctions.updateMinecraftJar(destinationPath, formToUpdate); }
        }
        catch ( IOException e ) { System_ErrorHandler.handleException(e, true); }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Nicnl - nicnl25@gmail.com
}
