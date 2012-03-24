import java.io.*;

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

    /*public static byte[] loadFileByte(String filePathInput) throws IOException
    {
        File fileToLoad = new File(filePathInput);
        byte[] loadedFile = new byte[(int)fileToLoad.length()];
        FileInputStream fileLoader = new FileInputStream(fileToLoad);
        fileLoader.read(loadedFile);
        return loadedFile;
    }*/

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Nicnl - nicnl25@gmail.com
}
