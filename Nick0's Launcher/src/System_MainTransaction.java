import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class System_MainTransaction
{
    
    public static void Main_ClientTransactions(String username, String password) throws IOException
    {
        String serverArgs = "user=" + URLEncoder.encode(username, "UTF-8") + "&password=" + URLEncoder.encode(password, "UTF-8") + "&version=" + 1337; // For you, notch ;)
        String result = Web_ClientServerProtocol.executePost("https://login.minecraft.net/", serverArgs);

        if ( result == null )
        {
            System_ErrorHandler.handleError("Impossible de se connecter à Minecraft.net !");
            return;
        }
        
        if ( !result.contains(":") )
        {
            if ( result.trim().equals("Bad login") ) { System_ErrorHandler.handleError("Nom d'utilisateur et/ou mot de passe incorrect !"); }
            else if ( result.trim().equals("Old version") ) { System_ErrorHandler.handleError("Le launcher est périmé.\nContactez votre service client ;D\n\n" + Main_RealLauncher.officialAddress); }
            else { System_ErrorHandler.handleError("Une erreur inconnue s'est produite.\nVeuillez me contacter à cette adresse :\n" + Main_RealLauncher.officialAddress + "\n\"" + result + "\""); }
            return;
        }
        
        String[] serverResults = result.split(":");

        System_DataStub.setParameter("latestVersion", serverResults[0].trim());
        System_DataStub.setParameter("downloadTicket", serverResults[1].trim());
        System_DataStub.setParameter("username", serverResults[2].trim());
        System_DataStub.setParameter("sessionid", serverResults[3].trim());

        File NicnlConfigFile = new File(Main_RealLauncher.configFileDir + Main_RealLauncher.configFileName);
        String[] loadedTextFile = NicnlConfigFile.exists() ? System_ConfigFileWriter.loadFile(Main_RealLauncher.configFileDir + Main_RealLauncher.configFileName) : null;

        Encrypter_StringEncrypter.encodeAndSavePassword(password);

        Web_MinecraftUpdater.mainMinecraftUpdater(Main_RealLauncher.homeDir, "windows_natives.jar", loadedTextFile); // Grosse fonction de mise a jour du jeu
    }
    
    public static void Main_OfflineLogin(String username)
    {
        System_DataStub.setParameter("username", username);
        System_DataStub.setParameter("sessionid", "0");

        Main_RealLauncher.startMinecraft();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Nicnl - nicnl25@gmail.com
}
