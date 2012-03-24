import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class Updater_SystemFunctions
{

    private static final String minecraftDownloadServer = "http://s3.amazonaws.com/MinecraftDownload/";
    private static final String[] fileToDownload = new String[] { "lwjgl.jar", "jinput.jar", "lwjgl_util.jar" };

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Download Main Functions

    public static void updateAllJars(boolean forceDownload, GuiForm_UpdaterForm formToUpdate) throws IOException
    {
        System_LogWriter.write("UPDATER - Démarrage d'une mise à jour des JARs / ForceDownload = " + forceDownload);
        formToUpdate.updateStatus(0, "Démarrage...");
        
        String binDirPath = Main_RealLauncher.getBinDirPath();
        
        if ( Preferences_ConfigLoader.CONFIG_LWJGLSelector && !Preferences_ConfigLoader.CONFIG_LWJGLAddress.equals("") ) { downloadLWJGLfromServer(formToUpdate, binDirPath); }
        else { downloadLWJGLfromMojang(formToUpdate, binDirPath, forceDownload); }

        updateMinecraftJar(formToUpdate, forceDownload);

        formToUpdate.downloadFinished();
    }

    public static void updateMinecraftJar(GuiForm_UpdaterForm formToUpdate, boolean forceDownload) throws IOException
    {
        System_LogWriter.write("UPDATER - Démarrage d'une mise à jour du Minecraft.jar");

        String binDirPath = Main_RealLauncher.getBinDirPath();
        String actualFile = "minecraft.jar";
        
        File minecraftJarFile = new File(binDirPath + File.separator + actualFile);
        if ( !forceDownload && minecraftJarFile.exists() ) { return; }

        formToUpdate.updateStatus(0, actualFile);

        byte[] temp_data = downloadFile(actualFile, formToUpdate, true);
        writeByteArrayToFile(temp_data, binDirPath + File.separator + actualFile);
    }

    private static void updateNatives(String destinationPath, String nativesFile, GuiForm_UpdaterForm formToUpdate) throws IOException
    {
        System_LogWriter.write("UPDATER - Téléchargement des natives / " + nativesFile);
        byte[] temp_natives = downloadFile(nativesFile, formToUpdate, true);
        writeByteArrayToFile(temp_natives, destinationPath + File.separator + nativesFile);
        System_LogWriter.write("UPDATER - Extraction des natives...");
        extractOSNatives(destinationPath, nativesFile);
    }
    
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // LWJGL Downloader
    
    private static void downloadLWJGLfromMojang(GuiForm_UpdaterForm formToUpdate, String binDirPath, boolean forceDownload) throws IOException
    {
        String nativesFile = System_UserHomeDefiner.SystemOS + "_natives.jar";
        
        for ( String actualFile : fileToDownload )
        {
            System_LogWriter.write("UPDATER - Début du téléchargement de : " + actualFile);

            formToUpdate.updateStatus(0, actualFile);

            File verification = new File(binDirPath + File.separator + actualFile);
            if ( verification.exists() && !forceDownload ) { continue; }

            byte[] temp_data = downloadFile(actualFile, formToUpdate, true);
            writeByteArrayToFile(temp_data, binDirPath + File.separator + actualFile);

            System_LogWriter.write("UPDATER - Fin du téléchargement du fichier : " + actualFile);
        }
        formToUpdate.updateStatus(0, nativesFile);

        System_LogWriter.write("UPDATER - Téléchargement des natives...");
        updateNatives(binDirPath, nativesFile, formToUpdate);
    }
    
    private static void downloadLWJGLfromServer(GuiForm_UpdaterForm formToUpdate, String binDirPath) throws IOException
    {
        String LWJGLFileAddress = Preferences_ConfigLoader.CONFIG_LWJGLAddress;
        String LWJGLFileName = LWJGLFileAddress.substring(LWJGLFileAddress.lastIndexOf("/")+1, LWJGLFileAddress.length());
        
        System_LogWriter.write("UPDATER - Téléchargement des libraries LWJGL : " + LWJGLFileName);

        formToUpdate.updateStatus(0, LWJGLFileName);

        byte[] temp_data = downloadFile(LWJGLFileAddress, formToUpdate, false);
        writeByteArrayToFile(temp_data, binDirPath + File.separator + LWJGLFileName);

        System_LogWriter.write("UPDATER - Fin du téléchargement des libraries LWJGL : " + LWJGLFileName);
        formToUpdate.updateStatus(0, "Extraction...");

        System_LogWriter.write("UPDATER - Décompression des libraries LWJGL...");
        extractLWJGLFromArchive(binDirPath, LWJGLFileName);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Download System Functions

    private static byte[] downloadFile(String fileToDownload, GuiForm_UpdaterForm formToUpdate, boolean useDefaultServer) throws IOException
    {
        
        String DownloadTicket = System_DataStub.static_getParameter("downloadTicket");
        String Username = System_DataStub.static_getParameter("username");
        String DownloadArguments = ( DownloadTicket.equals("0") ) ? ( "" ) : ( "?user=" + Username + "&ticket=" + DownloadTicket );
        
        String finalUrlAddress = useDefaultServer ? minecraftDownloadServer + fileToDownload + DownloadArguments : fileToDownload; 

        URL fileUrl = new URL(finalUrlAddress);
        URLConnection fileConnection = fileUrl.openConnection();

        if ( fileConnection instanceof HttpURLConnection )
        {
            fileConnection.setRequestProperty("Cache-Control", "no-cache");
            fileConnection.connect();
        }

        InputStream serverInputStream = fileConnection.getInputStream();
        int fileLength = fileConnection.getContentLength();
        
        byte[] outputData = new byte[fileLength];

        for ( int i=0; i<fileLength; i++ )
        {
            outputData[i] = (byte)serverInputStream.read();
            String finalFileName = useDefaultServer ? fileToDownload : fileToDownload.substring(fileToDownload.lastIndexOf("/")+1, fileToDownload.length());
            formToUpdate.updateStatus(i * 100 / fileLength, finalFileName);
        }

        return outputData;
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
    // Official LWJGL Extractor

    private static void extractLWJGLFromArchive(String outputPath, String nativesFile) throws IOException
    {
        // Basic Preparation
        System_FileManager.createFolder(Main_RealLauncher.getNativesDirPath());
        
        String LWJGLFilePath = outputPath + File.separator + nativesFile;
        ZipFile LWJGLZipFile = new ZipFile(LWJGLFilePath);
        
        String basicDir = nativesFile.replace(".zip", "");
        
        String nativesDir = basicDir + "/native/" + System_UserHomeDefiner.SystemOS;
        String jarDir = basicDir + "/jar";

        ArrayList<ZipEntry> nativesFiles = new ArrayList<ZipEntry>();

        // Usable Jar Entries
        Enumeration<? extends ZipEntry> LWJGLZipEntries = LWJGLZipFile.entries();
        while ( LWJGLZipEntries.hasMoreElements() )
        {
            ZipEntry actualZipEntry = LWJGLZipEntries.nextElement();
            if ( actualZipEntry.toString().contains(nativesDir) && !actualZipEntry.isDirectory() ) { nativesFiles.add(actualZipEntry); }
        }

        // Final Extract :
        
        // Jar Files Extract
        for ( String actualJarFile : fileToDownload )
        {
            ZipEntry actualZipEntry = LWJGLZipFile.getEntry(jarDir + "/" + actualJarFile);
            
            ByteArrayOutputStream temporaryByteArray = new ByteArrayOutputStream();
            InputStream temporaryInputStream = LWJGLZipFile.getInputStream(actualZipEntry);
            
            int tempByte;
            while ( (tempByte=temporaryInputStream.read()) != -1 ) { temporaryByteArray.write(tempByte); }
            
            writeByteArrayToFile(temporaryByteArray.toByteArray(), outputPath + "/" + actualJarFile);
            
            temporaryInputStream.close();
        }
        
        // Natives Extract
        for ( ZipEntry actualZipEntry : nativesFiles )
        {
            ByteArrayOutputStream temporaryByteArray = new ByteArrayOutputStream();
            InputStream temporaryInputStream = LWJGLZipFile.getInputStream(actualZipEntry);
            
            int tempByte;
            while ( (tempByte=temporaryInputStream.read()) != -1 ) { temporaryByteArray.write(tempByte); }
            
            String actualZipEntryName = actualZipEntry.toString();
            actualZipEntryName = actualZipEntryName.substring(actualZipEntryName.lastIndexOf("/")+1, actualZipEntryName.length());
            writeByteArrayToFile(temporaryByteArray.toByteArray(), outputPath + "/natives/" + actualZipEntryName);
            
            temporaryInputStream.close();
        }
        
        // End Of Extraction - Delete LWJGL Archive
        LWJGLZipFile.close();
        System_FileManager.removeFile(LWJGLFilePath, true);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Writers

    private static void writeByteArrayToFile(byte[] byteArray_File, String outputPath) throws IOException
    {
        System_FileManager.removeFile(outputPath, false);

        File FileOutput = new File(outputPath);
        OutputStream OutputStream_File = new FileOutputStream(FileOutput);
        OutputStream_File.write(byteArray_File);

        OutputStream_File.close();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Nicnl - nicnl25@gmail.com
}
