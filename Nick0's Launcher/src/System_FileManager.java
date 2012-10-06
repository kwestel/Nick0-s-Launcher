import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.jar.JarFile;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class System_FileManager
{
    
    public static File createFolder(String folderPath)
    {
        File directory = new File(folderPath);
        String dirName = directory.getName();
        
        if ( !directory.exists() && !directory.mkdir() )
        {
            System_ErrorHandler.handleError("Error lors de la création du dossier \"" + dirName + "\"", true, true);
        }
        else if ( directory.isFile() && (!removeFile(folderPath, true) || !directory.mkdir()) )
        {
            System_ErrorHandler.handleError("Error lors de la création du dossier \"" + dirName + "\"\nUn fichier du même nom existe déjà.", false, true);
        }
        
        return directory;
    }
    
    public static boolean removeFile(File fileToRemove, boolean showError)
    {
        String fileName = fileToRemove.getName();

        if ( !fileToRemove.delete() || fileToRemove.exists() )
        {
            if ( showError ) { System_ErrorHandler.handleError("Error lors de la suppression du fichier \"" + fileName + "\"", false, true); }
            return false;
        }

        return true;
    }

    public static boolean removeFile(String filePath, boolean showError)
    {
        File fileToRemove = new File(filePath);
        return removeFile(fileToRemove, showError);
    }
    
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Rewrite Jar Function ( Erase META-INF Folder )
    
    public static void rewriteJar(String zipFilePath) throws IOException
    {
        if ( !jarContainsMetaInf(zipFilePath) ) { return; }

        File zipFile = new File(zipFilePath);
        File temporaryJar = File.createTempFile(zipFile.getName(), null);
    
        if ( !temporaryJar.delete() ) { throw new IOException("Impossible de renommer le jar. " + zipFile.getPath()); }
        temporaryJar.deleteOnExit();
    
        if ( !zipFile.renameTo(temporaryJar) ) { throw new IOException("Impossible de renommer le jar. " + zipFile.getPath()); }
    
        byte[] buf = new byte[1024];
    
        ZipInputStream zipInputStream = new ZipInputStream(new FileInputStream(temporaryJar));
        ZipOutputStream zipOutputStream = new ZipOutputStream(new FileOutputStream(zipFile));
    
        ZipEntry actualEntry;
        while ( (actualEntry=zipInputStream.getNextEntry()) != null )
        {
            String entryName = actualEntry.getName();
            if ( entryName.toLowerCase().contains("meta-inf") ) { continue; }
    
            zipOutputStream.putNextEntry(new ZipEntry(entryName));
            int len;
            while ( (len=zipInputStream.read(buf)) > 0 ) { zipOutputStream.write(buf, 0, len); }
        }
    
        zipInputStream.close();
        zipOutputStream.close();
        temporaryJar.delete();
    }

    private static boolean jarContainsMetaInf(String jarFilePath)
    {
        ZipFile jarFile = null;

        try
        {
            File actualFile = new File(jarFilePath);
            if ( !actualFile.exists() || !actualFile.isFile() || actualFile.isDirectory() ) { return false; }

            jarFile = new ZipFile(actualFile);

            ZipEntry actualEntry = null;
            try { actualEntry = jarFile.getEntry("META-INF/CODESIGN.RSA"); }
            catch ( Exception e ) { e.printStackTrace(); }
            if ( actualEntry != null ) { return true; }

            actualEntry = null;
            try { actualEntry = jarFile.getEntry("META-INF/CODESIGN.SF"); }
            catch ( Exception e ) { e.printStackTrace();  }
            if ( actualEntry != null ) { return true; }

            actualEntry = null;
            try { actualEntry = jarFile.getEntry("META-INF/MANIFEST.MF"); }
            catch ( Exception e ) { e.printStackTrace();  }

            jarFile.close();

            return actualEntry != null;

        }
        catch ( Exception e ) { return false; }
        finally { if ( jarFile != null ) { try { jarFile.close(); } catch ( Exception e ) { e.printStackTrace(); } } }
    }
    
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Rewrite Jar Function ( Erase META-INF Folder )
    
    public static String analyzeMinecraftName(String jarFilePath)
    {
        try
        {
            JarFile minecraftJarFile = new JarFile(jarFilePath);
            ZipEntry mainClassEntry = minecraftJarFile.getEntry("net/minecraft/client/Minecraft.class");

            InputStream mainClassInputStream = minecraftJarFile.getInputStream(mainClassEntry);
            ByteArrayOutputStream classBytes = new ByteArrayOutputStream();

            int readByte;
            while ( (readByte=mainClassInputStream.read()) != - 1 ) { classBytes.write(readByte); }

            return analyzeMinecraftName(classBytes.toByteArray());
        }
        catch ( NullPointerException e ) { return null; }
        catch ( Exception e ) { return null; }
    }

    public static String analyzeMinecraftName(byte[] minecraftBytes)
    {
        String minecraftClassAsString = new String(minecraftBytes);

        Pattern pattern = Pattern.compile("(Minecraft Minecraft )(.* {0,2})(\\d\\.{0,5})(\\d)");
        Matcher matcher = pattern.matcher(minecraftClassAsString);

        return ( matcher.find() ) ? matcher.group() : null;
    }

    public static byte[] extractMinecraftClassFromBytes(byte[] originalJar) throws IOException
    {
        ByteArrayInputStream minecraftJarBytes = new ByteArrayInputStream(originalJar);
        ZipInputStream minecraftJarStream = new ZipInputStream(minecraftJarBytes);

        ZipEntry actualEntry;
        while ( (actualEntry=minecraftJarStream.getNextEntry()) != null && !actualEntry.getName().equals("net/minecraft/client/Minecraft.class") ) { }

        ByteArrayOutputStream minecraftClass = new ByteArrayOutputStream();
        int temporaryByte;
        while ( (temporaryByte=minecraftJarStream.read()) != -1 ) { minecraftClass.write(temporaryByte); }

        return minecraftClass.toByteArray();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Write Byte Array To File


    public static void writeByteArrayToFile(byte[] byteArray_File, String outputPath) throws IOException { writeByteArrayToFile(byteArray_File, new File(outputPath)); }
    public static void writeByteArrayToFile(byte[] byteArray_File, File outputFile) throws IOException
    {
        removeFile(outputFile, false);
        OutputStream OutputStream_File = new FileOutputStream(outputFile);
        OutputStream_File.write(byteArray_File);

        OutputStream_File.close();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Load File To Byte Array

    public static byte[] readFileBytes(String filePath) throws IOException { return readFileBytes(new File(filePath)); }
    public static byte[] readFileBytes(File fileRaw) throws IOException
    {
        FileInputStream fileInputStream = null;
        try
        {
            fileInputStream = new FileInputStream(fileRaw);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            int actualByte;
            while ( (actualByte=fileInputStream.read()) != -1 ) { baos.write(actualByte); }

            return baos.toByteArray();
        }
        finally { if ( fileInputStream != null ) { fileInputStream.close(); } }
    }

    public static byte[] readInputStreamBytes(InputStream inputStream) throws IOException
    {
        try
        {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            int actualByte;
            while ( (actualByte=inputStream.read()) != -1 ) { baos.write(actualByte); }

            return baos.toByteArray();
        }
        finally { if ( inputStream != null ) { inputStream.close(); } }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Minecraft Version Scan

    public static String[] getAllMinecraftVersions()
    {
        File binDir = new File(Main_RealLauncher.getBinDirPath());
        String[] fileList = binDir.list();

        ArrayList<String> minecraftVersions = new ArrayList<String>();
        for ( String actualPath : fileList )
        {
            File temporaryFile = new File(Main_RealLauncher.getBinDirPath() + File.separator + actualPath);
            if ( temporaryFile.isFile() && actualPath.toLowerCase().endsWith(".jar") )
            {
                String actualVersion = analyzeMinecraftName(Main_RealLauncher.getBinDirPath() + File.separator + actualPath);
                if ( actualVersion != null )
                {
                    actualVersion = actualVersion.replace("Minecraft Minecraft ", "");
                    if ( !minecraftVersions.contains(actualVersion) ) { minecraftVersions.add(actualVersion); }
                }
            }
        }

        if ( minecraftVersions.size() == 0 )
        {
            minecraftVersions.add("1.0.0");
            minecraftVersions.add("1.0.1");
            minecraftVersions.add("1.1");
            minecraftVersions.add("1.2.2");
            minecraftVersions.add("1.2.3");
            minecraftVersions.add("1.2.4");
            minecraftVersions.add("1.2.5");
        }

        return minecraftVersions.toArray(new String[minecraftVersions.size()]);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // File Helpers

    public static File getAbsoluteFile(String fileName) { return new File(fileName).getAbsoluteFile(); }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Nicnl - nicnl25@gmail.com
}
