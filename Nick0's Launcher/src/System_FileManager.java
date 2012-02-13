import java.io.File;

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
        else if ( directory.isFile() && (!removeFile(folderPath) || !directory.mkdir()) )
        {
            System_ErrorHandler.handleError("Error lors de la création du dossier \"" + dirName + "\"\nUn fichier du même nom existe déjà.", true, true);
        }
        
        return directory;
    }
    
    public static boolean removeFile(String filePath)
    {
        File fileToRemove = new File(filePath);
        String fileName = fileToRemove.getName();
        
        boolean result = fileToRemove.delete() && !fileToRemove.exists();

        if ( !result )
        {
            System_ErrorHandler.handleError("Error lors de la suppression du fichier \"" + fileName + "\"", true, true);
        }

        return result;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Nicnl - nicnl25@gmail.com
}
