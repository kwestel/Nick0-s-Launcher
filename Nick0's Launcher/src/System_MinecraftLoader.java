import java.applet.Applet;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;

public class System_MinecraftLoader extends ClassLoader
{

    private static URLClassLoader MC_ClassLoader;
    
    public static String[] jarList = new String[] { "lwjgl.jar", "jinput.jar", "lwjgl_util.jar", "minecraft.jar" };
    public static boolean LoadMods = false;
    
    public static void updateClassPath(String jarPath, boolean LoadNicnlMod) throws MalformedURLException
    {
        URL[] urlList = transformPathFileToUrl(jarPath, jarList);
        MC_ClassLoader = LoadNicnlMod ? new System_ModdedClassLoader(urlList, true) : new URLClassLoader(urlList);
        
        if ( !jarPath.endsWith(File.separator) ) { jarPath += File.separator; }
  
        System.setProperty("org.lwjgl.librarypath", jarPath + "natives");
        System.setProperty("net.java.games.input.librarypath", jarPath + "natives");
    }

    public static Applet LoadMinecraft(String path) throws MalformedURLException, ClassNotFoundException, InstantiationException, IllegalAccessException
    {
        boolean EnableNicnlMods = ( GuiForm_MainFrame.mainFrame != null && GuiForm_MainFrame.mainFrame.Check_EnableNicnlMods != null ) && GuiForm_MainFrame.mainFrame.Check_EnableNicnlMods.isSelected();
        updateClassPath(path, EnableNicnlMods);
        
        Applet loadedApplet = null;

        try
        {
            Class loadedAppletClass = MC_ClassLoader.loadClass("net.minecraft.client.MinecraftApplet");
            loadedApplet = (Applet)loadedAppletClass.newInstance();
        }
        catch ( NoClassDefFoundError e ) { System_ErrorHandler.handleError("Erreur fatale lors du chargement de Minecraft !\nDes classes systèmes ne sont pas chargées.\nVerifiez votre dossier META-INF.", true, true); }

        return loadedApplet;
    }

    public static URL[] transformPathFileToUrl(String path, String[] files) throws MalformedURLException
    {
        ArrayList<URL> URLList = new ArrayList<URL>();
        
        File modsFolder = System_FileManager.createFolder(Main_RealLauncher.getModsDirPath());
        if ( LoadMods ) { URLList.add(modsFolder.toURI().toURL()); }
        
        for ( String actualJar : files ) { URLList.add(new File(path, actualJar).toURI().toURL()); }
        
        return URLList.toArray(new URL[URLList.size()]);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Nicnl - nicnl25@gmail.com
}
