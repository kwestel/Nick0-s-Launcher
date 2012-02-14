import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.net.URL;
import java.security.PublicKey;
import java.security.cert.Certificate;
import java.util.Arrays;

public class Web_ClientServerProtocol
{

    public static String connectToServer(String string_serverURL, String serverArguments)
    {
        HttpsURLConnection connectionToServer = null;
        try
        {
            URL serverURL = new URL(string_serverURL);
            connectionToServer = (HttpsURLConnection)serverURL.openConnection();

            connectionToServer.setUseCaches(false);
            connectionToServer.setDoInput(true);
            connectionToServer.setDoOutput(true);

            connectionToServer.connect();

            byte[] serverPublicKey = connectionToServer.getServerCertificates()[0].getPublicKey().getEncoded();
            
            if ( !Arrays.equals(serverPublicKey, DATA_MinecraftPublicKey.getPublicKey()) )
            {
                System_ErrorHandler.handleError("La clef permettant une connexion sécurisée aux serveurs de Minecraft n'est pas valide.", true, true);
            }

            DataOutputStream serverOutputWriter = new DataOutputStream(connectionToServer.getOutputStream());
            serverOutputWriter.writeBytes(serverArguments);
            serverOutputWriter.flush();
            serverOutputWriter.close();

            InputStream serverInputStream = connectionToServer.getInputStream();
            BufferedReader serverBufferedReader = new BufferedReader(new InputStreamReader(serverInputStream));

            String response = serverBufferedReader.readLine();

            serverBufferedReader.close();
            serverInputStream.close();

            return response;
        }
        catch ( Exception e )
        {
            System_ErrorHandler.handleException(e, true);
            return null;
        }
        finally { if ( connectionToServer != null ) { connectionToServer.disconnect(); } }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Nicnl - nicnl25@gmail.com
}
