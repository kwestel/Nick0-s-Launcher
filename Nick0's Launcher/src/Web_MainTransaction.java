import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;

public class Web_MainTransaction
{

    public static void Main_ClientTransactions(String username, String password) throws IOException
    {
        String serverArgs = "user=" + URLEncoder.encode(username, "UTF-8") + "&password=" + URLEncoder.encode(password, "UTF-8") + "&version=" + 1337; // For you, notch ;)
        String result = Web_ClientServerProtocol.connectToServer("https://login.minecraft.net/", serverArgs);

        if ( result == null || !result.contains(":") )
        {
            if ( result == null ) { System_ErrorHandler.handleError("Impossible de se connecter à Minecraft.net !", false, false); }
            else if ( result.toLowerCase().trim().equals("bad login") )
            {
                System_ErrorHandler.handleError("Nom d'utilisateur et/ou mot de passe incorrect !", false, false);
                GuiForm_MainFrame.mainFrame.disableAntiDisplaying();
            }
            else if ( result.toLowerCase().trim().equals("old version") ) { System_ErrorHandler.handleError("Le launcher est périmé.", false, true); }
            else { System_ErrorHandler.handleError("Une erreur inconnue s'est produite lors de la connexion : \"" + result + "\"", false, true); }
            return;
        }

        String[] serverResults = result.split(":");

        System_DataStub.setParameter("latestVersion", serverResults[0].trim());
        System_DataStub.setParameter("downloadTicket", serverResults[1].trim());
        System_DataStub.setParameter("username", serverResults[2].trim());
        System_DataStub.setParameter("sessionid", serverResults[3].trim());

        Encrypter_StringEncrypter.encodeAndSavePassword(password);

        Web_MinecraftUpdater.mainMinecraftUpdater();
    }

    public static void Main_OfflineLogin(String username)
    {
        System_DataStub.setParameter("username", username);
        System_DataStub.setParameter("downloadTicket", "0");
        System_DataStub.setParameter("sessionid", "0");

        Web_MinecraftUpdater.mainOfflineUpdater();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Nicnl - nicnl25@gmail.com
}
