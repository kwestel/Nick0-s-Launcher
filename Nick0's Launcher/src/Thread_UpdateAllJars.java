import java.io.IOException;

public class Thread_UpdateAllJars extends Thread
{

    private boolean forceDownload;
    private Gui_UpdaterForm formToUpdate;

    private boolean updateAllJars;
    
    public Thread_UpdateAllJars(boolean arg_forceDownload, Gui_UpdaterForm form)
    {
        forceDownload = arg_forceDownload;
        formToUpdate = form;

        updateAllJars = true;
    }

    public Thread_UpdateAllJars(Gui_UpdaterForm form)
    {
        formToUpdate = form;

        updateAllJars = false;
    }
    
    public void run()
    {
        try
        {
            if ( updateAllJars ) { Updater_SystemFunctions.updateAllJars(forceDownload, formToUpdate); }
            else  { Updater_SystemFunctions.updateMinecraftJar(formToUpdate); }
        }
        catch ( IOException e ) { System_ErrorHandler.handleException(e, true); }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Nicnl - nicnl25@gmail.com
}
