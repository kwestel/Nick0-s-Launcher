import javax.swing.*;
import java.io.*;

public class Web_MinecraftUpdater
{
    
    public static void mainMinecraftUpdater(String[] loadedConfFile)
    {
        Updater_FileVerifications();
        
        if ( loadedConfFile != null ) { Updater_ConfigFileExists(loadedConfFile); }
        else { Updater_NoInstallation(false); }
    }
    
    public static void mainOfflineUpdater()
    {
        Updater_FileVerifications();

        if ( Preferences_ConfigLoader.MinecraftReinstallForcer ) { OfflineUpdater_ReinstallForced(); }
        else { Main_RealLauncher.startMinecraft(); }
    }
    
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Updates Functions
    
    private static void Updater_ConfigFileExists(String[] loadedConfFile)
    {
        boolean needToUpdate = !loadedConfFile[1].equals(System_DataStub.static_getParameter("latestVersion")) && !Preferences_ConfigLoader.CONFIG_updatesDisabled;

        System.out.println("Nick0's Launcher - Updater - " + ( needToUpdate ? "Mise a jour de Minecraft disponible !" : "Minecraft est a jour." ));

        if ( loadedConfFile[1].equals("0") || Preferences_ConfigLoader.MinecraftReinstallForcer )
        {
            Updater_NoInstallation(Preferences_ConfigLoader.MinecraftReinstallForcer);
        }
        else if ( checkCorruptedMinecraft() )
        {
            int userResponse = JOptionPane.showConfirmDialog(new JInternalFrame(), "Votre installation Minecraft est corrompue.\nVotre jeu risque de ne pas démarrer.\n\nVoulez vous réparer Minecraft ?", "Installation corrompue", JOptionPane.YES_NO_OPTION);
            GuiForm_MainFrame.mainFrame.setVisible(false);

            if ( userResponse == 0 )
            {
                System_MinecraftLoader.jarList[3] = "minecraft.jar";
                new GuiForm_UpdaterForm(false, true);
            }
            else { Main_RealLauncher.startMinecraft(); }
        }
        else if ( needToUpdate )
        {
            int userResponse = JOptionPane.showConfirmDialog(new JInternalFrame(), "Une mise à jour de Minecraft est dispnonible.\nVoulez-vous la téléchager maintenant ?", "Mise à jour disponible", JOptionPane.YES_NO_OPTION);
            GuiForm_MainFrame.mainFrame.setVisible(false);

            if ( userResponse == 0 ) { new GuiForm_UpdaterForm(true, false); }
            else
            {
                System_DataStub.setParameter("latestVersion",loadedConfFile[1]);
                Main_RealLauncher.startMinecraft();
            }
        }
        else { Main_RealLauncher.startMinecraft(); }
    }
    
    private static void Updater_NoInstallation(boolean forceUpdate)
    {
        GuiForm_MainFrame.mainFrame.setVisible(false);
        new GuiForm_UpdaterForm(forceUpdate, true);
    }
    
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Offline Updater Function
    
    private static void OfflineUpdater_ReinstallForced()
    {
        String MSGText = "Vous voulez forcer une mise à jour du jeu,\n" +
        "et ce sans vous connecter à Minecraft.net\n\n" +
        "Aucun support technique ne sera disponible,\n" +
        "car le launcher risque de devenir instable.\n\n" +
        "Voulez-vous continuer ?";
        int userResponse = JOptionPane.showConfirmDialog(new JInternalFrame(), MSGText, "Mise à jour risquée", JOptionPane.YES_NO_OPTION);

        if ( userResponse == 0 )
        {
            GuiForm_MainFrame.mainFrame.setVisible(false);
            new GuiForm_UpdaterForm(false, true);
        }
    }
    
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // System Function
    
    public static boolean checkCorruptedMinecraft()
    {
        for ( String actualJar : System_MinecraftLoader.jarList )
        {
            actualJar = ( actualJar.equals("") ) ? "minecraft.jar" : actualJar;
            
            if ( Preferences_ConfigLoader.CONFIG_jarSelector && actualJar.equals("minecraft.jar") )
            {
                String[] jarList = System_JarSelectorFunctions.getJarList();

                if ( jarList.length > 0 ) { actualJar = jarList[0]; }
                else { return true; }
            }

            File actualJarFile = new File(Main_RealLauncher.getBinDirPath() + File.separator + actualJar);
            if ( !(actualJarFile.exists() && actualJarFile.isFile()) ) { return true; }
        }

        return false;
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
