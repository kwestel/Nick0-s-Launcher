import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class Updater_SystemFunctions
{

    public static String minecraftDownloadServer = "http://s3.amazonaws.com/MinecraftDownload/";

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Download Main Functions

    public static void updateAllJars(boolean forceDownload, GuiForm_UpdaterForm formToUpdate) throws IOException
    {
        String nativesFile = System_UserHomeDefiner.SystemOS + "_natives.jar";
        String binDirPath = Main_RealLauncher.getBinDirPath();
        
        formToUpdate.updateStatus(0, "Démarrage...");

        for ( String actualFile : System_MinecraftLoader.jarList )
        {
            formToUpdate.updateStatus(0, actualFile);

            File verification = new File(binDirPath + File.separator + actualFile);
            if ( verification.exists() && !forceDownload ) { continue; }

            byte[] temp_data = downloadSingleJar(actualFile, formToUpdate);
            writeByteArrayToFile(temp_data, binDirPath + File.separator + actualFile);
        }
        formToUpdate.updateStatus(0, nativesFile);

        updateNatives(binDirPath, nativesFile, formToUpdate);

        formToUpdate.downloadFinished();
    }

    public static void updateMinecraftJar(GuiForm_UpdaterForm formToUpdate) throws IOException
    {
        String binDirPath = Main_RealLauncher.getBinDirPath();
        String actualFile = "minecraft.jar";

        formToUpdate.updateStatus(0, actualFile);

        byte[] temp_data = downloadSingleJar(actualFile, formToUpdate);
        writeByteArrayToFile(temp_data, binDirPath + File.separator + actualFile);

        formToUpdate.downloadFinished();
    }

    public static void updateNatives(String destinationPath, String nativesFile, GuiForm_UpdaterForm formToUpdate) throws IOException
    {
        byte[] temp_natives = downloadSingleJar(nativesFile, formToUpdate);
        writeByteArrayToFile(temp_natives, destinationPath + File.separator + nativesFile);
        extractOSNatives(destinationPath, nativesFile);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Download System Functions

    private static byte[] downloadSingleJar(String fileToDownload, GuiForm_UpdaterForm formToUpdate) throws IOException
    {
        
        String DownloadTicket = System_DataStub.static_getParameter("downloadTicket");
        String Username = System_DataStub.static_getParameter("username");
        String DownloadArguments = ( DownloadTicket.equals("0") ) ? ( "" ) : ( "?user=" + Username + "&ticket=" + DownloadTicket );

        URL fileUrl = new URL(minecraftDownloadServer + fileToDownload + DownloadArguments);
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
        String nativesJarPath = outputPath + File.separator + nativesFile;
        File nativeJar = new File(nativesJarPath);

        System_FileManager.createFolder(Main_RealLauncher.getNativesDirPath());

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

            System_FileManager.removeFile(Main_RealLauncher.getNativesDirPath() + File.separator + thisFile.getName(), false);

            InputStream jarInputStream = jarFile.getInputStream(jarFile.getEntry(thisFile.getName()));
            OutputStream extractedFileOutput = new FileOutputStream(Main_RealLauncher.getNativesDirPath() + File.separator + thisFile.getName());

            byte[] buffer = new byte[65536];
            int bufferSize;
            while ( (bufferSize = jarInputStream.read(buffer, 0, buffer.length)) != -1 ) { extractedFileOutput.write(buffer, 0, bufferSize); }

            jarInputStream.close();
            extractedFileOutput.close();
        }

        jarFile.close();

        System_FileManager.removeFile(nativesJarPath, true);
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
