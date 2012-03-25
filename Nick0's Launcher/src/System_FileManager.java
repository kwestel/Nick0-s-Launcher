import java.io.*;
import java.util.zip.ZipEntry;
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
    
    public static boolean removeFile(String filePath, boolean showError)
    {
        File fileToRemove = new File(filePath);
        String fileName = fileToRemove.getName();

        if ( !fileToRemove.delete() || fileToRemove.exists() )
        {
            if ( showError ) { System_ErrorHandler.handleError("Error lors de la suppression du fichier \"" + fileName + "\"", false, true); }
            return false;
        }

        return true;
    }
    
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // 
    
    public static void rewriteJar(String zipFilePath) throws IOException
    {
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
            if ( entryName.contains("META-INF") ) { continue; }
    
            zipOutputStream.putNextEntry(new ZipEntry(entryName));
            int len;
            while ( (len=zipInputStream.read(buf)) > 0 ) { zipOutputStream.write(buf, 0, len); }
        }
    
        zipInputStream.close();
        zipOutputStream.close();
        temporaryJar.delete();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Nicnl - nicnl25@gmail.com
}
