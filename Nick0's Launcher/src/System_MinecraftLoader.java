import java.applet.Applet;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Collections;

public class System_MinecraftLoader
{

    public static URLClassLoader MC_ClassLoader;

    public static String minecraftJarToLoad = "minecraft.jar";
    public static final String[] LWJGLJars = new String[] { "lwjgl.jar", "jinput.jar", "lwjgl_util.jar" };
    public static boolean LoadMods = false;
    
    private static void updateClassPath(boolean LoadNicnlMod) throws MalformedURLException
    {
        String minecraftVersion;

        try { minecraftVersion = System_FileManager.analyzeMinecraftName(Main_RealLauncher.getBinDirPath() + File.separator + minecraftJarToLoad).replace("Minecraft Minecraft ", "").replace(" ", "_").toLowerCase(); }
        catch ( Exception e ) { minecraftVersion = null; }
        String[] finalFileList = mergeFilesToLoad(minecraftVersion == null ? new String[0] : getModsFiles(minecraftVersion));

        URL[] urlList = transformPathsToURLs(Main_RealLauncher.getBinDirPath(), finalFileList);
        MC_ClassLoader = new System_ModdedClassLoader(urlList, LoadNicnlMod);

        System.setProperty("org.lwjgl.librarypath", Main_RealLauncher.getNativesDirPath());
        //System.setProperty("java.library.path", Main_RealLauncher.getNativesDirPath());
        System.setProperty("net.java.games.input.librarypath", Main_RealLauncher.getNativesDirPath());
    }

    public static Applet LoadMinecraft() throws MalformedURLException, ClassNotFoundException, InstantiationException, IllegalAccessException
    {
        boolean EnableNicnlMods = ( GuiForm_MainFrame.mainFrame != null && GuiForm_MainFrame.mainFrame.Check_EnableNicnlMods != null ) && GuiForm_MainFrame.mainFrame.Check_EnableNicnlMods.isSelected();
        updateClassPath(EnableNicnlMods);

        Applet loadedApplet = null;
        try
        {
            Class loadedAppletClass = MC_ClassLoader.loadClass("net.minecraft.client.MinecraftApplet");
            loadedApplet = (Applet)loadedAppletClass.newInstance();
        }
        catch ( NoClassDefFoundError e ) { System_ErrorHandler.handleError("Erreur fatale lors du chargement de Minecraft !\nDes classes systèmes ne sont pas chargées.\nVérifiez votre dossier META-INF.", true, true); }

        return loadedApplet;
    }
    
    
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // URL<-->FilePath | Convert

    private static String[] getModsFiles(String minecraftVersion)
    {
        ArrayList<String> finalModsList = new ArrayList<String>();

        if ( !LoadMods ) { return new String[] { }; }

        System_Mods.initializeData();

        File actualModsFile = System_FileManager.createFolder(Main_RealLauncher.getModsDirPath() + File.separator + minecraftVersion);
        for ( String actualFile : actualModsFile.list() )
        {
            if ( !actualFile.toLowerCase().endsWith(".zip") ) { continue; }
            if ( !System_Mods.getModState(minecraftVersion, actualFile.substring(0, actualFile.length()-4)) ) { continue; }

            finalModsList.add("mods" + File.separator + minecraftVersion + File.separator + actualFile);
        }

        return finalModsList.toArray(new String[finalModsList.size()]);
    }

    private static String[] mergeFilesToLoad(String[] newFiles)
    {
        ArrayList<String> finalArray = new ArrayList<String>();

        Collections.addAll(finalArray, LWJGLJars);
        Collections.addAll(finalArray, newFiles);
        finalArray.add(minecraftJarToLoad);

        return finalArray.toArray(new String[finalArray.size()]);
    }

    public static String[] getBasicMinecraftFiles()
    {
        ArrayList<String> finalArray = new ArrayList<String>();

        Collections.addAll(finalArray, LWJGLJars);
        finalArray.add(minecraftJarToLoad);

        return finalArray.toArray(new String[finalArray.size()]);
    }

    private static URL[] transformPathsToURLs(String path, String[] files) throws MalformedURLException
    {
        ArrayList<URL> URLList = new ArrayList<URL>();
        
        File modsFolder = System_FileManager.createFolder(Main_RealLauncher.getModsDirPath());
        if ( LoadMods ) { URLList.add(modsFolder.toURI().toURL()); }
        
        for ( String actualJar : files ) { URLList.add(transformPathToURL(path, actualJar)); }

        return URLList.toArray(new URL[URLList.size()]);
    }

    private static URL transformPathToURL(String path, String file) throws MalformedURLException
    {
        return new File(path, file).toURI().toURL();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Nicnl - nicnl25@gmail.com
}
