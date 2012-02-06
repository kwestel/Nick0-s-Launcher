import java.io.File;
import java.util.ArrayList;

public class System_JarSelectorFunctions
{
    
    private static String[] forbiddenFiles = new String[] { "lwjgl.jar", "jinput.jar", "lwjgl_util.jar" };
    
    public static String[] getJarList()
    {
        File jarDir = new File(Main_RealLauncher.homeDir + File.separator + "bin");
        ArrayList<String> arrayFiles = new ArrayList<String>();
        
        String[] jarFileList = jarDir.list();
        for ( String actualFile : jarFileList )
        {
            if ( isNotForbidden(actualFile) && actualFile.contains(".jar") ) { arrayFiles.add(actualFile); }
        }
        
        return arrayFiles.toArray(new String[arrayFiles.size()]);
    }
    
    private static boolean isNotForbidden(String fileName)
    {
        for ( String actualFile : forbiddenFiles ) { if ( fileName.equals(actualFile) ) { return false; } }
        return true;
    }
    
}
