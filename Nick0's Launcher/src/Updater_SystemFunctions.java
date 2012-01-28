import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class Updater_SystemFunctions
{

    private static String minecraftDownloadServer = "http://s3.amazonaws.com/MinecraftDownload/";

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Download Main Functions

    public static void updateAllJars(String destinationPath, String nativesFile, boolean forceDownload, Gui_UpdaterForm formToUpdate) throws IOException
    {
        formToUpdate.updateStatus(0, "Démarrage...");
        for ( String actualFile : System_MinecraftLoader.jarList )
        {
            formToUpdate.updateStatus(0, actualFile);
            File verification = new File(destinationPath + "\\bin\\" + actualFile);
            if ( verification.exists() && !forceDownload ) { continue; }
            byte[] temp_data = downloadSingleJar(actualFile, formToUpdate);
            writeByteArrayToFile(temp_data, destinationPath + "\\bin\\" + actualFile);
        }
        formToUpdate.updateStatus(0, nativesFile);
        updateNatives(destinationPath, nativesFile, formToUpdate);
        formToUpdate.downloadFinished();
    }

    public static void updateNatives(String destinationPath, String nativesFile, Gui_UpdaterForm formToUpdate) throws IOException
    {
        byte[] temp_natives = downloadSingleJar(nativesFile, formToUpdate);
        writeByteArrayToFile(temp_natives, destinationPath + "\\bin\\" + nativesFile);
        extractOSNatives(destinationPath, nativesFile);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Download System Functions

    private static byte[] downloadSingleJar(String fileToDownload, Gui_UpdaterForm formToUpdate) throws IOException
    {
        URL fileUrl = new URL(minecraftDownloadServer + fileToDownload);
        URLConnection fileConnection = fileUrl.openConnection();

        if ( fileConnection instanceof HttpURLConnection)
        {
            fileConnection.setRequestProperty("Cache-Control", "no-cache");
            fileConnection.connect();
        }

        InputStream serverInputStream = fileConnection.getInputStream();
        ByteArrayOutputStream BAOS = new ByteArrayOutputStream();

        int fileLength = fileConnection.getContentLength();

        int readByte;
        while ( (readByte = serverInputStream.read()) != -1 )
        {
            BAOS.write(readByte);
            formToUpdate.updateStatus(BAOS.size() * 100 / fileLength, fileToDownload);
        }

        return BAOS.toByteArray();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Natives extraction

    private static void extractOSNatives(String outputPath, String nativesFile) throws IOException
    {
        String nativesJarPath = outputPath + "\\bin\\" + nativesFile;
        File nativeJar = new File(nativesJarPath);

        File nativesFolder = new File(outputPath + "\\bin\\natives");
        if ( !nativesFolder.exists() ) { nativesFolder.mkdir(); }

        JarFile jarFile = new JarFile(nativeJar, true);
        Enumeration contentFiles = jarFile.entries();

        while ( contentFiles.hasMoreElements() )
        {
            JarEntry thisFile = (JarEntry)contentFiles.nextElement();
            if ( thisFile.isDirectory() || thisFile.getName().indexOf('/') != -1 ) { continue; }
        }

        contentFiles = jarFile.entries();
        while ( contentFiles.hasMoreElements() )
        {
            JarEntry thisFile = (JarEntry)contentFiles.nextElement();
            if ( thisFile.isDirectory() || thisFile.getName().indexOf('/') != -1 ) { continue; }

            File newFile = new File(outputPath + "\\bin\\natives\\" + thisFile.getName());
            if ( newFile.exists() ) { newFile.delete(); }

            InputStream jarInputStream = jarFile.getInputStream(jarFile.getEntry(thisFile.getName()));
            OutputStream extractedFileOutput = new FileOutputStream(outputPath + "\\bin\\natives\\" + thisFile.getName());

            byte[] buffer = new byte[65536];
            int bufferSize;
            while ( (bufferSize = jarInputStream.read(buffer, 0, buffer.length)) != -1 ) { extractedFileOutput.write(buffer, 0, bufferSize); }

            jarInputStream.close();
            extractedFileOutput.close();
        }

        jarFile.close();

        nativeJar = new File(nativesJarPath);
        nativeJar.delete();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Writers

    public static void writeByteArrayToFile(byte[] byteArray_File, String outputPath) throws IOException
    {
        File FileOutput = new File(outputPath);
        OutputStream OutputStream_File = new FileOutputStream(FileOutput);
        OutputStream_File.write(byteArray_File);
        OutputStream_File.close();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Nicnl - nicnl25@gmail.com
}
