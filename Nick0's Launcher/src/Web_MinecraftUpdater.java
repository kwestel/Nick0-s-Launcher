import javax.swing.*;
import java.io.*;

public class Web_MinecraftUpdater
{
    
    public static void mainMinecraftUpdater()
    {
        System_UpdaterHelper.Updater_FileVerifications();
        
        boolean haveInstallation = new File(Main_RealLauncher.getConfigFilePath()).exists();
        
        if ( !haveInstallation ) { System_UpdaterHelper.Updater_NoInstallation(); }
        else { Updater_ConfigFileExists(); }
    }

    public static void mainOfflineUpdater()
    {
        System_UpdaterHelper.Updater_FileVerifications();
        Main_RealLauncher.startMinecraft();
    }
    
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Updates Functions
    
    private static void Updater_ConfigFileExists()
    {
        String localVersion = Preferences_ConfigFileWriter.getParameter("Version");
        String latestVersion = System_DataStub.static_getParameter("latestVersion");
        boolean needToUpdate = !localVersion.equals(latestVersion) && !Preferences_ConfigLoader.CONFIG_updatesDisabled;

        System_LogWriter.write(needToUpdate ? "Une mise à jour de Minecraft est disponible ! ( ID : " + localVersion + " => " + latestVersion + " )" : "Minecraft est à jour. ( ID : " + localVersion + " )");

        if ( localVersion.equals("0") || localVersion.equals("") ) { System_UpdaterHelper.Updater_NoInstallation(); }
        else if ( System_UpdaterHelper.checkCorruptedMinecraft() ) { System_UpdaterHelper.corruptedMinecraftProcedure(); }
        else if ( Preferences_ConfigLoader.CONFIG_jarSelector && GuiForm_MainFrame.mainFrame.ComboBox_JarSelector != null && System_AlternativeJar.isAlternativeMinecraft(GuiForm_MainFrame.mainFrame.ComboBox_JarSelector.getSelection()) ) { System_UpdaterHelper.alternativeJarProdecure(); }
        else if ( needToUpdate )
        {
            int userResponse = JOptionPane.showConfirmDialog(new JInternalFrame(), "Une mise à jour de Minecraft est disponible.\nVoulez-vous la téléchager maintenant ?", "Mise à jour disponible", JOptionPane.YES_NO_OPTION);
            GuiForm_MainFrame.mainFrame.setVisible(false);

            if ( userResponse == 0 ) { Updater_SystemFunctions.updateMinecraftJar(true, true); }
            else
            {
                System_DataStub.setParameter("latestVersion", localVersion);
                Main_RealLauncher.startMinecraft();
            }
        }
        else { Main_RealLauncher.startMinecraft(); }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Nicnl - nicnl25@gmail.com
}
