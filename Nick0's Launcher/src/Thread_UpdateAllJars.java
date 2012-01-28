import java.io.IOException;

public class Thread_UpdateAllJars extends Thread
{
    
    private String destinationPath;
    private String nativesFile;
    private boolean forceDownload;
    private Gui_UpdaterForm formToUpdate;
    
    public Thread_UpdateAllJars(String arg_destinationPath, String arg_nativesFile, boolean arg_forceDownload, Gui_UpdaterForm form)
    {
        destinationPath = arg_destinationPath;
        nativesFile = arg_nativesFile;
        forceDownload = arg_forceDownload;
        formToUpdate = form;
    }
    
    public void run()
    {
        try { Updater_SystemFunctions.updateAllJars(destinationPath, nativesFile, forceDownload, formToUpdate); }
        catch ( IOException e ) { System_ErrorHandler.handleException(e, true); }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Nicnl - nicnl25@gmail.com
}
