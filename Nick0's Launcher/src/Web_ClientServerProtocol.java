import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Arrays;

public class Web_ClientServerProtocol
{
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Connect to Minecraft Login Server

    public static String connectToServerWithKey(String stringServerURL, String serverArguments, byte[] storedPublicKey)
    {
        HttpsURLConnection connectionToServer = null;
        try
        {
            URL serverURL = new URL(stringServerURL);
            connectionToServer = (HttpsURLConnection)serverURL.openConnection();

            connectionToServer.setUseCaches(false);
            connectionToServer.setDoInput(true);
            connectionToServer.setDoOutput(true);

            connectionToServer.connect();

            byte[] serverPublicKey = connectionToServer.getServerCertificates()[0].getPublicKey().getEncoded();
            
            if ( !Arrays.equals(serverPublicKey, storedPublicKey) )
            {
                System_ErrorHandler.handleError("La clef permettant une connexion sécurisée aux serveurs n'est pas valide.", true, true);
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
    // Lire une page web ( LWJGL Update )

    public static String[] readServerWebPage(String stringServerURL)
    {
        URLConnection connectionToServer = null;
        try
        {
            URL serverURL = new URL(stringServerURL);
            connectionToServer = serverURL.openConnection();
            connectionToServer.connect();

            BufferedReader inputReader = new BufferedReader(new InputStreamReader(connectionToServer.getInputStream()));

            ArrayList<String> outputFile = new ArrayList<String>();
            String inputLine;
            while ( (inputLine = inputReader.readLine()) != null ) { outputFile.add(inputLine); }
            inputReader.close();

            return outputFile.toArray(new String[outputFile.size()]);
        }
        catch ( Exception e )
        {
            System_ErrorHandler.handleException(e, true);
            return null;
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Download File

    public static byte[] downloadFile(String fileToDownload)
    {
        try
        {
            URL fileUrl = new URL(fileToDownload);
            URLConnection fileConnection = fileUrl.openConnection();

            if ( fileConnection instanceof HttpURLConnection )
            {
                fileConnection.setRequestProperty("Cache-Control", "no-cache");
                fileConnection.connect();
            }

            InputStream serverInputStream = fileConnection.getInputStream();
            int fileLength = fileConnection.getContentLength();

            byte[] outputData = new byte[fileLength];

            for ( int i=0; i<fileLength; i++ ) { outputData[i] = (byte)serverInputStream.read(); }

            return outputData;
        }
        catch ( Exception e ) { return null; }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Nicnl - nicnl25@gmail.com
}
