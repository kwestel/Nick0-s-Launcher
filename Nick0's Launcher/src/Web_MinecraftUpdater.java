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
    
    public static void mainMinecraftUpdater(String basePath, String[] loadedConfFile)
    {
        String nativesFile = System_UserHomeDefiner.SystemOS + "_natives.jar";

        File basicMC_DIR_SysFile = new File(Main_RealLauncher.configFileDir);
        if ( !basicMC_DIR_SysFile.exists() ) { basicMC_DIR_SysFile.mkdir(); }

        File basicMC_DIR_GAME = new File(basePath);
        if ( !basicMC_DIR_GAME.exists() ) { basicMC_DIR_GAME.mkdir(); }
        
        File basicBIN_DIR = new File(basePath + File.separator + "bin");
        if ( !basicBIN_DIR.exists() ) { basicBIN_DIR.mkdir(); }
        
        if ( loadedConfFile != null )
        {
            boolean needToUpdate = !loadedConfFile[1].equals(System_DataStub.MCParameters_Values[7]) && !Preferences_ConfigLoader.CONFIG_updatesDisabled;

            System.out.println("Nick0's Launcher - Updater - " + ( needToUpdate ? "Mise a jour de Minecraft disponible !" : "Minecraft est a jour." ));

            if ( loadedConfFile[1].equals("0") || Preferences_ConfigLoader.MinecraftReinstallForcer )
            {
                Main_RealLauncher.MainFrame.setVisible(false);
                new Gui_UpdaterForm(basePath, nativesFile, true, true);
            }
            else if ( checkCorruptedMinecraft() )
            {
                int userResponse = JOptionPane.showConfirmDialog(new JInternalFrame(), "Votre installation Minecraft est corrompue.\n\nVoulez vous reinstaller Minecraft ?", "Installation corrompue", JOptionPane.YES_NO_OPTION);
                Main_RealLauncher.MainFrame.setVisible(false);

                if ( userResponse == 0 )
                {
                    System_MinecraftLoader.jarList[3] = "minecraft.jar";
                    new Gui_UpdaterForm(basePath, nativesFile, false, true);
                }
                else { Main_RealLauncher.startMinecraft(); }
            }
            else if ( needToUpdate )
            {
                int userResponse = JOptionPane.showConfirmDialog(new JInternalFrame(), "Une mise à jour de Minecraft est dispnonible.\nVoulez-vous la téléchager maintenant ?", "Mise à jour disponible", JOptionPane.YES_NO_OPTION);
                Main_RealLauncher.MainFrame.setVisible(false);

                if ( userResponse == 0 ) { new Gui_UpdaterForm(basePath, nativesFile, true, false); }
                else
                {
                    System_DataStub.MCParameters_Values[7] = loadedConfFile[1];
                    Main_RealLauncher.startMinecraft();
                }
            }
            else { Main_RealLauncher.startMinecraft(); }
        }
        else
        {
            Main_RealLauncher.MainFrame.setVisible(false);
            new Gui_UpdaterForm(basePath, nativesFile, true, true);
        }
    }
    
    private static boolean checkCorruptedMinecraft()
    {
        // Verifier si un jar est inexistant

        for ( String actualJar : System_MinecraftLoader.jarList )
        {
            File actualJarFile = new File(Main_RealLauncher.homeDir + File.separator + "bin" + File.separator + ( ( actualJar.equals("") ) ? "minecraft.jar" : actualJar ));
            if ( !actualJarFile.exists() ) { return true; }
        }

        return false;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Nicnl - nicnl25@gmail.com
}
