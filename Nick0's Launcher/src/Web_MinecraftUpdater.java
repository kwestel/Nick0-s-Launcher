import javax.swing.*;
import java.io.*;

public class Web_MinecraftUpdater
{
    
    public static void mainMinecraftUpdater()
    {
        Updater_FileVerifications();
        
        boolean haveInstallation = new File(Main_RealLauncher.getConfigFilePath()).exists();
        
        if ( !haveInstallation ) { Updater_NoInstallation(); }
        else { Updater_ConfigFileExists(); }
    }
    
    public static void mainOfflineUpdater()
    {
        Updater_FileVerifications();
        // TODO : Offline Updater Functions
        Main_RealLauncher.startMinecraft();
    }
    
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Updates Functions
    
    private static void Updater_ConfigFileExists()
    {
        String latestVersion = Preferences_ConfigFileWriter.getParameter("Version");
        boolean needToUpdate = !latestVersion.equals(System_DataStub.static_getParameter("latestVersion")) && !Preferences_ConfigLoader.CONFIG_updatesDisabled;

        System_LogWriter.write(needToUpdate ? "Mise d jour de Minecraft disponible !" : "Minecraft est d jour.");

        if ( ( latestVersion.equals("0") || latestVersion.equals("") ) )
        {
            Updater_NoInstallation();
        }
        else if ( checkCorruptedMinecraft() )
        {
            int userResponse = JOptionPane.showConfirmDialog(new JInternalFrame(), "Votre installation Minecraft est corrompue.\nVotre jeu risque de ne pas démarrer.\n\nVoulez vous réparer Minecraft ?", "Installation corrompue", JOptionPane.YES_NO_OPTION);
            GuiForm_MainFrame.mainFrame.setVisible(false);

            if ( userResponse == 0 )
            {
                if ( !Preferences_ConfigLoader.CONFIG_jarSelector ) { System_MinecraftLoader.minecraftJarToLoad = "minecraft.jar"; }
                Updater_SystemFunctions.updateGame(false, true);
            }
            else { Main_RealLauncher.startMinecraft(); }
        }
        else if ( Preferences_ConfigLoader.CONFIG_jarSelector && GuiForm_MainFrame.mainFrame.ComboBox_JarSelector != null && System_AlternativeJar.isAlternativeMinecraft(GuiForm_MainFrame.mainFrame.ComboBox_JarSelector.getSelection()) )
        {
            String alternativeGameFileName = GuiForm_MainFrame.mainFrame.ComboBox_JarSelector.getSelection();
            String alternativeGameName = System_AlternativeJar.alternativeJarList[System_AlternativeJar.getAltMinID_FromJarFileName(alternativeGameFileName)];

            System_LogWriter.write("Chargement d'un Minecraft Alternatif : " + alternativeGameName);

            if ( System_AlternativeJar.alternativeGameNoNeedUpdate(alternativeGameFileName) )
            {
                System_LogWriter.write("Pas de mise à jour disponible : " + alternativeGameName);
                Main_RealLauncher.startMinecraft();
                return;
            }

            System_LogWriter.write("Mise à jour disponible : " + alternativeGameName);

            int userResponse = JOptionPane.showConfirmDialog(new JInternalFrame(), "Une mise à jour de " + alternativeGameName + " est disponible.\nVoulez-vous la téléchager maintenant ?", "Mise à jour disponible", JOptionPane.YES_NO_OPTION);
            GuiForm_MainFrame.mainFrame.setVisible(false);

            if ( userResponse == 0 ) { System_AlternativeJar.downloadAlternativeGame(alternativeGameFileName, true, true); }
            else
            {
                System_DataStub.setParameter("latestVersion", latestVersion);
                Main_RealLauncher.startMinecraft();
            }
        }
        else if ( needToUpdate )
        {
            int userResponse = JOptionPane.showConfirmDialog(new JInternalFrame(), "Une mise à jour de Minecraft est disponible.\nVoulez-vous la téléchager maintenant ?", "Mise à jour disponible", JOptionPane.YES_NO_OPTION);
            GuiForm_MainFrame.mainFrame.setVisible(false);

            if ( userResponse == 0 ) { Updater_SystemFunctions.updateMinecraftJar(true, true); }
            else
            {
                System_DataStub.setParameter("latestVersion", latestVersion);
                Main_RealLauncher.startMinecraft();
            }
        }
        else { Main_RealLauncher.startMinecraft(); }
    }
    
    private static void Updater_NoInstallation()
    {
        GuiForm_MainFrame.mainFrame.setVisible(false);
        Updater_SystemFunctions.updateGame(false, true);
    }
    
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // System Function
    
    public static boolean checkCorruptedMinecraft()
    {
        for ( String actualLWJGLJar : System_MinecraftLoader.LWJGLJars )
        {
            File actualJarFile = new File(Main_RealLauncher.getBinDirPath() + File.separator + actualLWJGLJar);
            if ( !actualJarFile.exists() || !actualJarFile.isFile() || actualJarFile.isDirectory() ) { return true; }
        }

        if ( Preferences_ConfigLoader.CONFIG_jarSelector )
        {
            String[] jarList = System_JarSelectorFunctions.getJarList();
            if ( jarList.length == 0 ) { return true; }
        }
        else
        {
            File minecraftJarFile = new File(Main_RealLauncher.getBinDirPath() + File.separator + "minecraft.jar");
            if ( !minecraftJarFile.exists() || !minecraftJarFile.isFile() || minecraftJarFile.isDirectory() ) { return true; }
        }

        File nativesDir = new File(Main_RealLauncher.getNativesDirPath());
        return nativesDir.list().length != System_UserHomeDefiner.getNumberOfDefaultNativesFiles();
    }
    
    private static void Updater_FileVerifications()
    {
        System_FileManager.createFolder(Main_RealLauncher.configFileDir);
        System_FileManager.createFolder(Main_RealLauncher.homeDir);
        System_FileManager.createFolder(Main_RealLauncher.getBinDirPath());
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Nicnl - nicnl25@gmail.com
}
