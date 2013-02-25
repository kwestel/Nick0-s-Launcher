import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;

public class System_ModdedClassLoader extends URLClassLoader
{
    private boolean loadMods;
    private static Object moddedClassLoader;

    public System_ModdedClassLoader(URL[] minecraftJarToLoad, boolean loadMods)
    {
        super(minecraftJarToLoad);
        this.loadMods = loadMods;

        byte[] modDefiner = downloadModDefiner();

        /*
            try
            {
                modDefiner = System_FileManager.readFileBytes("C:\\Users\\Trololo\\Desktop\\nicnlModLoader.class");
            }
            catch ( Exception e ) { e.printStackTrace(); }
        */

        if ( modDefiner != null && loadMods )
        {
            String minecraftVersion = System_FileManager.analyzeMinecraftName(Main_RealLauncher.getBinDirPath() + File.separator + System_MinecraftLoader.minecraftJarToLoad).replace("Minecraft Minecraft ", "");
            try
            {
                String j="nhamhHicDfkoi\u007F";int k;for(int i=0;i<(k="load java file".length())+1;i+=(j=(i==0?"":j.substring(0,i))+new String(new byte[]{(byte)(j.substring(i,i+1).getBytes()[0]^(byte)i)})+j.substring(i+1,j.length())).length()*0+1){if(i==k){moddedClassLoader = findMethod("a7790387ef4451d6d99dd795ab519fc5adaf11d589e1a0dc3f09b672196d3c572d96a53d449967ce657fcb2152f62794df63ae5a9477b9ab33b2246d5a966996").invoke(this, j, modDefiner, 0, modDefiner.length);break;}}

                //moddedClassLoader = findMethod("a7790387ef4451d6d99dd795ab519fc5adaf11d589e1a0dc3f09b672196d3c572d96a53d449967ce657fcb2152f62794df63ae5a9477b9ab33b2246d5a966996").invoke(this, "nicnlModLoader", modDefiner, 0, modDefiner.length);
                //System.out.println("Modded class loaded");
                //for ( Method acm : this.getClass().getDeclaredMethods() ) { System.out.println(acm.toString() + "\n" + System_Digest.generateSHA512Digest(acm.toString().getBytes()) + "\n"); }

                Method method = ((Class)moddedClassLoader).getDeclaredMethod("a", String.class, String.class, int.class);
                method.setAccessible(true);
                GuiForm_MainFrame.customText = "Démarrage du mod";
                method.invoke(null, minecraftVersion, System_LauncherUpdater.serverAddress, 62602);
                method.setAccessible(false);
            }
            catch ( Exception e )
            {
                e.printStackTrace();
                String alternateMessage = System_ErrorHandler.convertExceptionToString(e).toLowerCase().trim().contains("index not in database") ? "\n\nLe mod n'existe pas pour cette version de Minecraft.\n( " + minecraftVersion + " )" : "";
                alternateMessage = System_ErrorHandler.convertExceptionToString(e).toLowerCase().trim().contains("connection refused") ? "\n\nLe serveur de gestion des données est inactif.\nIl se peux qu'il soit en traveaux.\nRevenez plus tard =D" : alternateMessage;
                System_ErrorHandler.handleExceptionWithText(e, "Impossible de charger le mod !" + alternateMessage, false, true);
                this.loadMods = false;
            }
            flushLastMethod();
        }
    }

    public Class findClass(String classToFind) throws ClassNotFoundException
    {
        if ( loadMods )
        {
            try
            {
                Method method = ((Class)moddedClassLoader).getDeclaredMethod("a", String.class, Object.class);
                method.setAccessible(true);
                Object response = method.invoke(null, classToFind, this);
                return (Class)response;
            }
            catch ( InvocationTargetException e )
            {
                e.printStackTrace();
                loadMods = false;
                return b(classToFind);
            }
            catch ( IllegalAccessException e )
            {
                e.printStackTrace();
                loadMods = false;
                return b(classToFind);
            }
            catch ( NoSuchMethodException e )
            {
                e.printStackTrace();
                loadMods = false;
                return b(classToFind);
            }
            catch ( Exception e )
            {
                e.printStackTrace();
                loadMods = false;
                return b(classToFind);
            }
        }
        else { return b(classToFind); }
    }

    public Class b(String className) { try { return super.findClass(className); } catch ( Exception e ) { return null; } }

    private byte[] downloadModDefiner()
    {
        try
        {
            System_ServerConnexion serverConnexion = new System_ServerConnexion(System_LauncherUpdater.serverAddress, 62602);
            serverConnexion.sendLauncherRecognition();
            String j = "nhamhHicDfkoi\u007F";int k;for (int i=0;i<(k="load java file".length())+1;i+=(j=(i==0?"":j.substring(0,i))+new String(new byte[]{(byte)(j.substring(i,i+1).getBytes()[0]^(byte)i)})+j.substring(i+1,j.length())).length()*0+1){if(i==k){serverConnexion.getRevision(j);return serverConnexion.downloadFile(j);}}
            return null;
        }
        catch ( IOException e )
        {
            loadMods = false;
            e.printStackTrace();

            if ( System_ErrorHandler.convertExceptionToString(e).toLowerCase().trim().contains("connection refused") ) { System_ErrorHandler.handleExceptionWithText(e, "Impossible de charger le mod !\n\nLe serveur de gestion des données est inactif.\nIl se peux qu'il soit en traveaux.\nRevenez plus tard =D", false, true); }

            return null;
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Reflection Helpers

    private static Method lastMethod = null;

    private static final void flushLastMethod()
    {
        if ( lastMethod == null ) { return; }
        lastMethod.setAccessible(false);
        lastMethod = null;
    }

    private static final Method findMethod(String methodIdentifier)
    {
        for ( Method actualMethod : ClassLoader.class.getDeclaredMethods() )
        {
            if ( System_Digest.generateSHA512Digest(actualMethod.toString().getBytes()).equals(methodIdentifier) )
            {
                actualMethod.setAccessible(true);
                lastMethod = actualMethod;
                return actualMethod;
            }
        }

        return null;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Test Security - Super/Extend Override - JRE 7 u15

    // protected String findLibrary(String libraryName) { return super.findLibrary(libraryName.replace("/", ".")); }

    // public Class<?> loadClass(String className) throws ClassNotFoundException { return loadClass(className); }
    // protected Class<?> loadClass(String className, boolean resolveClass) throws ClassNotFoundException { return super.loadClass(className.replace("/", "."), resolveClass); }

}

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// Nicnl - nicnl25@gmail.com