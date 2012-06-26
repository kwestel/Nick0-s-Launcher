import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

public class System_JarSelectorFunctions
{

    public static boolean isFileValidMinecraftJar(String fileName)
    {
        ZipFile jarFile = null;

        try
        {
            File actualFile = new File(Main_RealLauncher.getBinDirPath() + File.separator + fileName);
            if ( !actualFile.exists() || !actualFile.isFile() || actualFile.isDirectory() ) { return false; }

            jarFile = new ZipFile(actualFile);
            ZipEntry minecraftClass = jarFile.getEntry("net/minecraft/client/Minecraft.class");

            return minecraftClass != null;
        }
        catch ( Exception e ) { }
        finally { if ( jarFile != null ) { try { jarFile.close(); } catch ( IOException e ) { e.printStackTrace(); } } }

        return false;
    }

    public static String[] getJarList()
    {
        ArrayList<String> finalJarList = new ArrayList<String>();

        File jarDir = new File(Main_RealLauncher.getBinDirPath());
        String[] rawFileList = jarDir.list();

        if ( rawFileList != null && rawFileList.length > 0 ) { for ( String actualFile : rawFileList ) { if ( isNotForbidden(actualFile) && isFileValidMinecraftJar(actualFile) ) { finalJarList.add(actualFile); } } }
        
        return finalJarList.toArray(new String[finalJarList.size()]);
    }
    
    private static boolean isNotForbidden(String fileName)
    {
        for ( String actualFile : System_MinecraftLoader.LWJGLJars ) { if ( fileName.equals(actualFile) ) { return false; } }
        return true;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Nicnl - nicnl25@gmail.com
}
