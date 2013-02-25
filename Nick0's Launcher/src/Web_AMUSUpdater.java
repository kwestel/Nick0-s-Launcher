import javax.swing.*;
import java.io.File;

public class Web_AMUSUpdater
{
    public static void mainMinecraftUpdater()
    {
        System_UpdaterHelper.Updater_FileVerifications();

        boolean haveInstallation = new File(Main_RealLauncher.getConfigFilePath()).exists();

        if ( !haveInstallation ) { System_UpdaterHelper.Updater_NoInstallation(); }
        else { Updater_ConfigFileExists(); }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Updates Functions

    private static void Updater_ConfigFileExists()
    {
        String localVersion = Preferences_ConfigFileWriter.getParameter("AMUSVersion");
        String AMUSVersion = System_DataStub.static_getParameter("AMUSVersion");
        boolean needToUpdate = !localVersion.equals(AMUSVersion) && !Preferences_ConfigLoader.CONFIG_updatesDisabled;

        System_LogWriter.write(needToUpdate ? "A.M.U.S - Une mise à jour de Minecraft disponible ! ( Version : " + AMUSVersion + " )" : "A.M.U.S - Minecraft est à jour. ( Version : " + AMUSVersion + " )");

        if ( localVersion.equals("0") || localVersion.equals("") ) { System_UpdaterHelper.Updater_NoInstallation(); }
        else if ( System_UpdaterHelper.checkCorruptedMinecraft() ) { System_UpdaterHelper.corruptedMinecraftProcedure(); }
        else if ( Preferences_ConfigLoader.CONFIG_jarSelector && GuiForm_MainFrame.mainFrame.ComboBox_JarSelector != null && System_AlternativeJar.isAlternativeMinecraft(GuiForm_MainFrame.mainFrame.ComboBox_JarSelector.getSelection()) ) { System_UpdaterHelper.alternativeJarProdecure(); }
        else if ( needToUpdate )
        {
            int userResponse = JOptionPane.showConfirmDialog(new JInternalFrame(), "-<[A.M.U.S]>-\n\nUne mise à jour de Minecraft est disponible.\n( Version : " + localVersion + " => " + AMUSVersion + " )\nVoulez-vous la téléchager maintenant ?", "AMUS - Mise à jour disponible", JOptionPane.YES_NO_OPTION);
            GuiForm_MainFrame.mainFrame.setVisible(false);

            if ( userResponse == 0 ) { Updater_SystemFunctions.updateMinecraftJar(true, true); }
            else
            {
                System_DataStub.setParameter("AMUSVersion", localVersion);
                Main_RealLauncher.startMinecraft();
            }
        }
        else { Main_RealLauncher.startMinecraft(); }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Nicnl - nicnl25@gmail.com
}
