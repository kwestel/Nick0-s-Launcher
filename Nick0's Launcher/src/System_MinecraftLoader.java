import java.applet.Applet;
import java.io.File;
import java.io.FilePermission;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.SocketPermission;
import java.net.URL;
import java.net.URLClassLoader;
import java.security.CodeSource;
import java.security.PermissionCollection;
import java.security.SecureClassLoader;
import java.util.Vector;

public class System_MinecraftLoader extends ClassLoader
{

    private static URLClassLoader MC_ClassLoader;
    public static String[] jarList = new String[] { "lwjgl.jar", "jinput.jar", "lwjgl_util.jar", "minecraft.jar" };
    
    public static void updateClassPath(String jarPath) throws Exception
    {
        URL[] urlList = transformPathFileToUrl(jarPath, jarList);
        MC_ClassLoader = new URLClassLoader(urlList);

        if ( !jarPath.endsWith(File.separator) ) { jarPath += File.separator; }
  
        System.setProperty("org.lwjgl.librarypath", jarPath + "natives");
        System.setProperty("net.java.games.input.librarypath", jarPath + "natives");
    }

    public static Applet LoadMinecraft(String path) throws Exception
    {
        updateClassPath(path);
        Class loadedAppletClass = MC_ClassLoader.loadClass("net.minecraft.client.MinecraftApplet");
        return (Applet)loadedAppletClass.newInstance();
    }

    public static URL[] transformPathFileToUrl(String path, String[] files) throws MalformedURLException
    {
        URL[] result = new URL[jarList.length];
        for (int i=0;i<jarList.length;i++) { result[i] = new File(path, files[i]).toURI().toURL(); }
        return result;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Nicnl - nicnl25@gmail.com
}
