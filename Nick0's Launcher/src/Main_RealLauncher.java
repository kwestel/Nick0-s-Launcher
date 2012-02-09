import javax.swing.*;
import java.applet.Applet;
import java.io.File;
import java.io.IOException;

public class Main_RealLauncher
{

    public static Gui_MainFrame MainFrame = null;
    public static Applet minecraftInstance = null;
    public static String officialAddress = "nicnl25@gmail.com";

    public static String configFileDir = System_UserHomeDefiner.returnConfigDirectory();
    public static String homeDir = configFileDir;
    public static String configFileName = File.separator + "Nick0's_Launcher.mconf";

    public static boolean PasswordNotDisplayed = false;
    private static String StoredPassword;

    public static void main(String[] args)
    {
        // Forcer le theme de l'OS hôte
        try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); }
        catch ( Exception e ) { System_ErrorHandler.handleException(e, false); }
        
        File modsFolder = new File(homeDir + File.separator + "bin" + File.separator + "mods");
        if ( !modsFolder.exists() ) { modsFolder.mkdir(); }

        System.out.println("Nick0's Launcher - Initialisation de l'interface en cours...");

        // Définition des variables systèmes importantes
        homeDir = getHomeDir();
        Preferences_ConfigLoader.SYSTEM_LoadPreferences();

        // Création de la frame principale
        MainFrame = new Gui_MainFrame();

        // Chargement des identifiants de connexion
        String[] loadedTextFile = System_ConfigFileWriter.loadConfigFile();
        if ( loadedTextFile != null )
        {
            if ( !loadedTextFile[0].equals("") )
            {
                String loadedUsername = loadedTextFile[0];
                MainFrame.Field_UserName.setText(loadedUsername);
                MainFrame.Field_UserName.setCaretPosition(loadedUsername.length());
            }

            if ( !loadedTextFile[2].equals("") && !loadedTextFile[3].equals("") )
            {
                String decodedPassword = Encrypter_StringEncrypter.decodeString(loadedTextFile[2]);
                int recodedHashCode = Encrypter_StringEncrypter.encodeString(decodedPassword).hashCode();

                // Si pas d'erreur de décodage :
                // le hashcode du mdp encrypté ET celui décrypté PUIS rencrypté sont égaux

                if ( recodedHashCode == Integer.parseInt(loadedTextFile[3]) )
                {
                    // Systeme de protection V2 :
                    // Le MDP n'est plus foutu en clair dans le JPasswordField

                    PasswordNotDisplayed = true;
                    StoredPassword = decodedPassword;

                    MainFrame.Field_Password.setText(Encrypter_StringEncrypter.stringRandomizer(StoredPassword));

                    MainFrame.Check_SaveLogin.setSelected(true);
                }
                else { System.out.println("Nick0's Launcher - Password decrypting fail !"); }
            }
        }

        System.out.println("Nick0's Launcher - Launcher fonctionnel !");
    }

    public static void startLogin(String username, String password)
    {
        if ( MainFrame.Field_UserName.getText().equals("") ) { return; }
        else if ( MainFrame.Field_Password.getText().equals("") && !MainFrame.Check_Offline.isSelected() ) { return; }
        else if ( MainFrame.Field_Password.getText().equals("") && !PasswordNotDisplayed ) { return; }

        if ( !MainFrame.Check_Offline.isSelected() )
        {
            try { System_MainTransaction.Main_ClientTransactions(username, password); }
            catch ( IOException e ) { System_ErrorHandler.handleException(e, true); }
        }
        else { System_MainTransaction.Main_OfflineLogin(username); }
    }
    
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // GetVariables
    
    public static String getHomeDir()
    {
        String[] loadedPreferences = System_ConfigFileWriter.loadConfigFile();
        return loadedPreferences[7].split("=").length == 2 ? loadedPreferences[7].split("=")[1] : Main_RealLauncher.configFileDir;
    }
    
    public static final String getStoredPassword()
    {
        String tempPass = StoredPassword;
        StoredPassword = null;
        return tempPass;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // System functions - DO NOT USE

    public static void startMinecraft()
    {
        if ( !MainFrame.Check_Offline.isSelected() )
        {
            String TempSelectedItem = Preferences_ConfigLoader.CONFIG_SaveLastJar ? (String)MainFrame.ComboBox_JarSelector.getSelectedItem() : null;
            Preferences_ConfigLoader.CONFIG_LastJarSaved = (TempSelectedItem == null) ? "" : TempSelectedItem;
            System_ConfigFileWriter.writeConfigFile(Encrypter_StringEncrypter.getLastPassword());
        }

        System.out.println("Initialisation de minecraft !\n\n_____________________________________\n");

        MainFrame = MainFrame.closeWindow(); // Le return est null = vide la variable MainFrame

        try { minecraftInstance = System_MinecraftLoader.LoadMinecraft(homeDir + File.separator + "bin"); }
        catch ( SecurityException e ) { System_ErrorHandler.handleError("Impossible d'initialiser les mods que vous avez installé.\n\nVeuillez supprimer le dossier META-INF de votre jeu.", true, false); }
        catch ( Exception e ) { System_ErrorHandler.handleException(e, true); }

        System_GameFrame baseFrame = new System_GameFrame(System_DataStub.MCParameters_Values[0]);
        baseFrame.add(minecraftInstance);

        baseFrame.setVisible(true);

        // Fait apparaitre le bouton "Quit Minecraft"
        System_DataStub.setParameter("stand-alone", "true");

        // Definition du stub de données
        minecraftInstance.setStub(new System_DataStub());
        minecraftInstance.setSize(baseFrame.getSize());

        // Démarrage de Minecraft
        minecraftInstance.init();
        minecraftInstance.start();
        minecraftInstance.validate();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Nicnl - nicnl25@gmail.com
}
