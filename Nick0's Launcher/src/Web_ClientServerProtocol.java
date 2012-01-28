import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.net.URL;
import java.security.PublicKey;
import java.security.cert.Certificate;

public class Web_ClientServerProtocol
{

    public static String executePost(String string_serverURL, String serverArguments)
    {
        HttpsURLConnection connectionToServer = null;
        try
        {
            URL serverURL = new URL(string_serverURL);
            connectionToServer = (HttpsURLConnection)serverURL.openConnection();

            connectionToServer.setRequestMethod("POST");
            connectionToServer.setRequestProperty("Content-Length", Integer.toString(serverArguments.getBytes().length));

            connectionToServer.setUseCaches(false);
            connectionToServer.setDoInput(true);
            connectionToServer.setDoOutput(true);

            connectionToServer.connect();
            Certificate[] cerfificatsSSL = connectionToServer.getServerCertificates();

            Certificate serverCertificate = cerfificatsSSL[0];
            PublicKey PK_serverPublicKey = serverCertificate.getPublicKey();
            byte[] serverPublicKey = PK_serverPublicKey.getEncoded();

            for (int i=0;i<serverPublicKey.length;i++)
            {
                if ( serverPublicKey[i] != DATA_MinecraftPublicKey.minecraftKey[i] )
                {
                    throw new RuntimeException("Erreur de la clef publique !");
                }
            }

            DataOutputStream serverOutputWriter = new DataOutputStream(connectionToServer.getOutputStream());
            serverOutputWriter.writeBytes(serverArguments);
            serverOutputWriter.flush();
            serverOutputWriter.close();

            InputStream serverInputStream = connectionToServer.getInputStream();
            BufferedReader serverBufferedReader = new BufferedReader(new InputStreamReader(serverInputStream));

            String response = "";
            String line;
            while ( (line = serverBufferedReader.readLine()) != null ) { response += line + "\r"; }
            serverBufferedReader.close();

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
