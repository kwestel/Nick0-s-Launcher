import javax.swing.*;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class Web_MinecraftUpdater
{
    
    public static void mainMinecraftUpdater(String[] loadedConfFile)
    {
        Updater_FileVerifications();
        
        if ( loadedConfFile != null ) { Updater_ConfigFileExists(loadedConfFile); }
        else { Updater_NoInstallation(); }
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
            Updater_NoInstallation();
        }
        else if ( checkCorruptedMinecraft() )
        {
            int userResponse = JOptionPane.showConfirmDialog(new JInternalFrame(), "Votre installation Minecraft est corrompue.\nVotre jeu risque de ne pas démarrer.\n\nVoulez vous réparer Minecraft ?", "Installation corrompue", JOptionPane.YES_NO_OPTION);
            Main_RealLauncher.MainFrame.setVisible(false);

            if ( userResponse == 0 )
            {
                System_MinecraftLoader.jarList[3] = "minecraft.jar";
                new Gui_UpdaterForm(false, true);
            }
            else { Main_RealLauncher.startMinecraft(); }
        }
        else if ( needToUpdate )
        {
            int userResponse = JOptionPane.showConfirmDialog(new JInternalFrame(), "Une mise à jour de Minecraft est dispnonible.\nVoulez-vous la téléchager maintenant ?", "Mise à jour disponible", JOptionPane.YES_NO_OPTION);
            Main_RealLauncher.MainFrame.setVisible(false);

            if ( userResponse == 0 ) { new Gui_UpdaterForm(true, false); }
            else
            {
                System_DataStub.setParameter("latestVersion",loadedConfFile[1]);
                Main_RealLauncher.startMinecraft();
            }
        }
        else { Main_RealLauncher.startMinecraft(); }
    }
    
    private static void Updater_NoInstallation()
    {
        Main_RealLauncher.MainFrame.setVisible(false);
        new Gui_UpdaterForm(false, true);
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
            Main_RealLauncher.MainFrame.setVisible(false);
            new Gui_UpdaterForm(false, true);
        }
        else { return; }
    }
    
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // System Function
    
    public static boolean checkCorruptedMinecraft()
    {
        // Verifier si un jar est inexistant

        for ( String actualJar : System_MinecraftLoader.jarList )
        {
            File actualJarFile = new File(Main_RealLauncher.homeDir + File.separator + "bin" + File.separator + ( ( actualJar.equals("") ) ? "minecraft.jar" : actualJar ));
            if ( !actualJarFile.exists() ) { return true; }
        }

        return false;
    }
    
    private static void Updater_FileVerifications()
    {
        File basicMC_DIR_SysFile = new File(Main_RealLauncher.configFileDir);
        if ( !basicMC_DIR_SysFile.exists() ) { basicMC_DIR_SysFile.mkdir(); }

        File basicMC_DIR_GAME = new File(Main_RealLauncher.homeDir);
        if ( !basicMC_DIR_GAME.exists() ) { basicMC_DIR_GAME.mkdir(); }
        
        File basicBIN_DIR = new File(Main_RealLauncher.homeDir + File.separator + "bin");
        if ( !basicBIN_DIR.exists() ) { basicBIN_DIR.mkdir(); }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Nicnl - nicnl25@gmail.com
}
