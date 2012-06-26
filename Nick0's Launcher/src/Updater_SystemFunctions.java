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

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Public Functions - Main Download

    public static void updateGame(final boolean forceDownload, final boolean startGame)
    {
        final GuiForm_UpdaterForm formToUpdate = new GuiForm_UpdaterForm();

        new Thread() { public void run() {
            try { SYSTEM_updateGame(formToUpdate, forceDownload, startGame); }
            catch ( IOException e ) { System_ErrorHandler.handleException(e, true); }
        } }.start();
    }

    public static void reinstallGameFromPreferences()
    {
        GuiForm_PreferenceFrame.newForm(false);
        final GuiForm_UpdaterForm formToUpdate = new GuiForm_UpdaterForm();

        new Thread() { public void run() {
            try { SYSTEM_updateGame(formToUpdate, true, false); }
            catch ( IOException e ) { System_ErrorHandler.handleException(e, true); }
            finally { GuiForm_PreferenceFrame.newForm(true); }
        } }.start();
    }

    public static void updateMinecraftJar(final boolean forceDownload, final boolean startGame)
    {
        final GuiForm_UpdaterForm formToUpdate = new GuiForm_UpdaterForm();

        new Thread() { public void run() {
            try { SYSTEM_updateMinecraftJar(formToUpdate, forceDownload, startGame); }
            catch ( IOException e ) { System_ErrorHandler.handleException(e, true); }
        } }.start();
    }
    public static void updateAlternativeJar(final String downloadURL, final String jarFileName, final boolean startGame)
    {
        final GuiForm_UpdaterForm formToUpdate = new GuiForm_UpdaterForm();

        new Thread() { public void run() {
            try { SYSTEM_updateAlternativeJar(formToUpdate, downloadURL, jarFileName, startGame); }
            catch ( IOException e ) { System_ErrorHandler.handleException(e, true); }
        } }.start();
    }
    public static void updateOnlyNatives()
    {
        final GuiForm_UpdaterForm formToUpdate = new GuiForm_UpdaterForm();

        new Thread() { public void run() {
            try { SYSTEM_updateOnlyNatives(formToUpdate); }
            catch ( IOException e ) { System_ErrorHandler.handleException(e, true); }
        } }.start();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Private System Functions

    private static void  SYSTEM_updateGame(GuiForm_UpdaterForm formToUpdate, boolean forceDownload, boolean startGame) throws IOException
    {
        System_LogWriter.write("UPDATER - Démarrage d'une mise à jour des JARs / ForceDownload = " + forceDownload);
        formToUpdate.updateStatus(0, "Démarrage...");
        
        String binDirPath = Main_RealLauncher.getBinDirPath();

        if ( Preferences_ConfigLoader.CONFIG_LWJGLSelector && !Preferences_ConfigLoader.CONFIG_LWJGLAddress.equals("") ) { installLWJGLfromOfficialServer(formToUpdate, binDirPath, forceDownload); }
        else { installLWJGLfromMojang(formToUpdate, binDirPath, forceDownload); }

        SYSTEM_updateMinecraftJar(formToUpdate, forceDownload, false);

        if ( startGame ) { formToUpdate.downloadFinished(); }
        else { formToUpdate.destroyWindow(); }
    }

    private static void SYSTEM_updateMinecraftJar(GuiForm_UpdaterForm formToUpdate, boolean forceDownload, boolean startGame) throws IOException
    {
        if ( !forceDownload && System_JarSelectorFunctions.getJarList().length > 0 )
        {
            System.out.println("UPDATER - Downloaded not forced && Valid Minecraft Jar Already Exist");
            if ( startGame ) { formToUpdate.downloadFinished(); }
            else { formToUpdate.destroyWindow(); }

            return;
        }

        System_LogWriter.write("UPDATER - Démarrage d'une mise à jour du Minecraft.jar");

        String binDirPath = Main_RealLauncher.getBinDirPath();
        String actualFile = "minecraft.jar";

        formToUpdate.updateStatus(0, actualFile);

        byte[] downloadedData = downloadFile(actualFile, formToUpdate, true, "");

        if ( Preferences_ConfigLoader.CONFIG_AutomaticJarRename )
        {
            byte[] minecraftClass = System_FileManager.extractMinecraftClassFromBytes(downloadedData);
            String rawMinecraftVersion = System_FileManager.analyzeMinecraftName(minecraftClass);

            if ( rawMinecraftVersion != null && !rawMinecraftVersion.equals("") )
            {
                String newJarName = rawMinecraftVersion.replace("Minecraft Minecraft ", "").replace(" ", "_").toLowerCase();
                newJarName = "minecraft_" + newJarName + ".jar";

                System_FileManager.writeByteArrayToFile(downloadedData, binDirPath + File.separator + actualFile);

                actualFile = newJarName;
                if ( Preferences_ConfigLoader.CONFIG_jarSelector ) { System_MinecraftLoader.minecraftJarToLoad = newJarName; }
            }
        }

        System_FileManager.writeByteArrayToFile(downloadedData, binDirPath + File.separator + actualFile);

        if ( Preferences_ConfigLoader.CONFIG_RemoveMETAINF ) { System_FileManager.rewriteJar(binDirPath + File.separator + actualFile); }

        if ( startGame ) { formToUpdate.downloadFinished(); }
        else { formToUpdate.destroyWindow(); }
    }

    private static void SYSTEM_updateAlternativeJar(GuiForm_UpdaterForm formToUpdate, String downloadURL, String jarFileName, boolean startGame) throws IOException
    {
        System_LogWriter.write("UPDATER - Démarrage du téléchargement d'un Minecraft alternatif : " + jarFileName);

        String binDirPath = Main_RealLauncher.getBinDirPath();
        String alternativeJarDestination = binDirPath + File.separator + jarFileName;

        System_FileManager.removeFile(alternativeJarDestination, false);

        formToUpdate.updateStatus(0, jarFileName);

        byte[] downloadedData = downloadFile(downloadURL, formToUpdate, false, jarFileName);
        System_FileManager.writeByteArrayToFile(downloadedData, alternativeJarDestination);

        System_LogWriter.write("UPDATER - Fin du téléchargement de : " + jarFileName);

        if ( startGame ) { formToUpdate.downloadFinished(); }
        else
        {
            formToUpdate.destroyWindow();
            GuiForm_AlternativeJar.newForm(true);
        }

    }

    private static void SYSTEM_updateOnlyNatives(GuiForm_UpdaterForm formToUpdate) throws IOException
    {
        String binDirPath = Main_RealLauncher.getBinDirPath();

        if ( Preferences_ConfigLoader.CONFIG_LWJGLSelector && !Preferences_ConfigLoader.CONFIG_LWJGLAddress.equals("") ) { installLWJGLfromOfficialServer(formToUpdate, binDirPath, true); }
        else { installLWJGLfromMojang(formToUpdate, binDirPath, true); }

        formToUpdate.setVisible(false);
        formToUpdate.dispose();
        GuiForm_PreferenceFrame.newForm(true);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // System Functions
    
    private static void downloadSpecificMojangLWJGL(String destinationPath, String nativesFile, boolean forceDownload, GuiForm_UpdaterForm formToUpdate) throws IOException
    {
        File nativesDir = new File(destinationPath + File.separator + "natives");
        if ( !forceDownload && ( nativesDir.exists() && nativesDir.isFile() && nativesDir.list().length <= 0 ) ) { return; }
        
        System_LogWriter.write("UPDATER - Téléchargement des natives / " + nativesFile);
        byte[] downloadedData = downloadFile(nativesFile, formToUpdate, true, "");

        System_FileManager.writeByteArrayToFile(downloadedData, destinationPath + File.separator + nativesFile);

        System_LogWriter.write("UPDATER - Extraction des natives...");
        extractOSNatives(destinationPath, nativesFile);
    }
    
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // LWJGL Downloader
    
    private static void installLWJGLfromMojang(GuiForm_UpdaterForm formToUpdate, String binDirPath, boolean forceDownload) throws IOException
    {
        for ( String actualFile : System_MinecraftLoader.LWJGLJars )
        {
            System_LogWriter.write("UPDATER - Début du téléchargement de : " + actualFile);

            formToUpdate.updateStatus(0, actualFile);

            File verification = new File(binDirPath + File.separator + actualFile);
            if ( verification.exists() && !forceDownload ) { continue; }

            byte[] downloadedData = downloadFile(actualFile, formToUpdate, true, "");
            System_FileManager.writeByteArrayToFile(downloadedData, binDirPath + File.separator + actualFile);

            System_LogWriter.write("UPDATER - Fin du téléchargement du fichier : " + actualFile);
        }

        File nativesDir = new File(Main_RealLauncher.getNativesDirPath());
        if ( forceDownload || !nativesDir.exists() || nativesDir.list().length != System_UserHomeDefiner.getNumberOfDefaultNativesFiles() )
        {
            System_LogWriter.write("UPDATER - Téléchargement des natives...");

            String nativesFile = System_UserHomeDefiner.SystemOS + "_natives.jar";
            formToUpdate.updateStatus(0, nativesFile);
            downloadSpecificMojangLWJGL(binDirPath, nativesFile, forceDownload, formToUpdate);
        } else { System_LogWriter.write("UPDATER - Skipping Natives Installation..."); }
    }
    
    private static void installLWJGLfromOfficialServer(GuiForm_UpdaterForm formToUpdate, String binDirPath, boolean forceDownload) throws IOException
    {
        if ( !forceDownload )
        {
            boolean fileMissing = false;

            File nativesDir = new File(Main_RealLauncher.getNativesDirPath());
            if ( !nativesDir.exists() || nativesDir.list().length != System_UserHomeDefiner.getNumberOfDefaultNativesFiles() ) { fileMissing = true; }

            for ( String actualFile : System_MinecraftLoader.LWJGLJars )
            {
                File verification = new File(binDirPath + File.separator + actualFile);
                if ( !verification.exists() ) { fileMissing = true; }
            }

            if ( !fileMissing ) { return; }
        }

        String LWJGLFileAddress = Preferences_ConfigLoader.CONFIG_LWJGLAddress;
        String LWJGLFileName = LWJGLFileAddress.substring(LWJGLFileAddress.lastIndexOf("/")+1, LWJGLFileAddress.length());
        
        System_LogWriter.write("UPDATER - Téléchargement des libraries LWJGL : " + LWJGLFileName);

        formToUpdate.updateStatus(0, LWJGLFileName);

        byte[] downloadedData = downloadFile(LWJGLFileAddress, formToUpdate, false, "");
        System_FileManager.writeByteArrayToFile(downloadedData, binDirPath + File.separator + LWJGLFileName);

        System_LogWriter.write("UPDATER - Fin du téléchargement des libraries LWJGL : " + LWJGLFileName);
        formToUpdate.updateStatus(0, "Extraction...");
        
        extractLWJGLFromArchive(binDirPath, LWJGLFileName);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Download System Functions

    private static byte[] downloadFile(String fileToDownload, GuiForm_UpdaterForm formToUpdate, boolean useDefaultServer, String alternativeName) throws IOException
    {
        
        // String DownloadTicket = System_DataStub.static_getParameter("downloadTicket");
        // String Username = System_DataStub.static_getParameter("username");
        
        //String finalUrlAddress = useDefaultServer ? minecraftDownloadServer + fileToDownload + (( DownloadTicket == null || DownloadTicket.equals("0") ) ? ( "" ) : ( "?user=" + Username + "&ticket=" + DownloadTicket )) : fileToDownload;
        String finalUrlAddress = (useDefaultServer ? minecraftDownloadServer : "") + fileToDownload;


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
            String finalFileName = (alternativeName.equals("")) ? (useDefaultServer ? fileToDownload : fileToDownload.substring(fileToDownload.lastIndexOf("/")+1, fileToDownload.length())) : alternativeName;
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

            // System_FileManager.removeFile(Main_RealLauncher.getNativesDirPath() + File.separator + thisFile.getName(), false);

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
        System_LogWriter.write("UPDATER - Préparation de la décompression des libraries LWJGL...");
        
        // Basic Preparation
        System_FileManager.createFolder(Main_RealLauncher.getNativesDirPath());
        
        String LWJGLFilePath = outputPath + File.separator + nativesFile;
        ZipFile LWJGLZipFile = new ZipFile(LWJGLFilePath);
        
        String basicDir = nativesFile.substring(0, nativesFile.length()-4);
        
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
        
        System_LogWriter.write("UPDATER - Début de la décompression des libraries LWJGL...");

        // Final Extract :
        
        // Jar Files Extract
        for ( String actualJarFile : System_MinecraftLoader.LWJGLJars )
        {
            String zipEntryName = jarDir + "/" + actualJarFile;
            System_LogWriter.write("UPDATER - LWJGL Décompression de : " + zipEntryName);
            
            ZipEntry actualZipEntry = LWJGLZipFile.getEntry(zipEntryName);
            
            ByteArrayOutputStream temporaryByteArray = new ByteArrayOutputStream();
            InputStream temporaryInputStream = LWJGLZipFile.getInputStream(actualZipEntry);
            
            int tempByte;
            while ( (tempByte=temporaryInputStream.read()) != -1 ) { temporaryByteArray.write(tempByte); }
            
            System_FileManager.writeByteArrayToFile(temporaryByteArray.toByteArray(), outputPath + "/" + actualJarFile);
            
            temporaryInputStream.close();
        }
        
        // Natives Extract
        for ( ZipEntry actualZipEntry : nativesFiles )
        {
            System_LogWriter.write("UPDATER - LWJGL Décompression de : " + actualZipEntry.getName());
            
            ByteArrayOutputStream temporaryByteArray = new ByteArrayOutputStream();
            InputStream temporaryInputStream = LWJGLZipFile.getInputStream(actualZipEntry);
            
            int tempByte;
            while ( (tempByte=temporaryInputStream.read()) != -1 ) { temporaryByteArray.write(tempByte); }
            
            String actualZipEntryName = actualZipEntry.toString();
            actualZipEntryName = actualZipEntryName.substring(actualZipEntryName.lastIndexOf("/")+1, actualZipEntryName.length());
            System_FileManager.writeByteArrayToFile(temporaryByteArray.toByteArray(), outputPath + "/natives/" + actualZipEntryName);
            
            temporaryInputStream.close();
        }
        
        // End Of Extraction - Delete LWJGL Archive
        LWJGLZipFile.close();
        System_FileManager.removeFile(LWJGLFilePath, true);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Nicnl - nicnl25@gmail.com
}
