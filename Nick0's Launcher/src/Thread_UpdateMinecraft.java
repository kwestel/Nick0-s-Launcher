import java.io.IOException;

public class Thread_UpdateMinecraft extends Thread
{

    private boolean forceDownload;
    private boolean startGame;
    
    private String downloadURL;
    
    private GuiForm_UpdaterForm formToUpdate;
    private GuiForm_AlternativeJar formToOpen;

    private boolean updateAllJars;

    public Thread_UpdateMinecraft(boolean arg_forceDownload, boolean arg_updateAllJars, boolean arg_StartGame, GuiForm_UpdaterForm arg_formToUpdate)
    {
        forceDownload = arg_forceDownload;
        formToUpdate = arg_formToUpdate;

        updateAllJars = arg_updateAllJars;
        startGame = arg_StartGame;

        downloadURL = "";
    }

    public Thread_UpdateMinecraft(String arg_downloadURL, GuiForm_UpdaterForm arg_formToUpdate, GuiForm_AlternativeJar arg_formToOpen)
    {
        downloadURL = arg_downloadURL;

        formToUpdate = arg_formToUpdate;
        formToOpen = arg_formToOpen;
    }
    
    public void run()
    {
        try
        {
            if ( downloadURL.equals("") )
            {
                if ( updateAllJars ) { Updater_SystemFunctions.updateGame(forceDownload, formToUpdate, startGame); }
                else  { Updater_SystemFunctions.updateMinecraftJar(formToUpdate, forceDownload, startGame); }
            }
            else { Updater_SystemFunctions.updateAlternativeJar(formToUpdate, formToOpen, downloadURL); }
        }
        catch ( IOException e ) { System_ErrorHandler.handleException(e, true); }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Nicnl - nicnl25@gmail.com
}
