import sun.security.action.GetPropertyAction;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.security.AccessController;
import java.util.ArrayList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class System_ResourceHelper
{

    public static String[] getAllResourceFiles()
    {
        ArrayList<String> finalResourceList = new ArrayList<String>();

        String originalLauncherJarPath = null;

        try { originalLauncherJarPath = URLDecoder.decode(Main_RealLauncher.class.getProtectionDomain().getCodeSource().getLocation().getPath(), "UTF8"); }
        catch ( UnsupportedEncodingException e ) { System_ErrorHandler.handleException(e, false); }

        File classContainer = new File(originalLauncherJarPath);
        if ( classContainer.isFile() )
        {
            try
            {
                ZipInputStream zipInputStream = new ZipInputStream(new FileInputStream(classContainer));

                ZipEntry actualEntry;
                while ( (actualEntry=zipInputStream.getNextEntry()) != null ) { if ( !actualEntry.isDirectory() ) { finalResourceList.add(actualEntry.getName()); } }
            }
            catch ( IOException e ) { System_ErrorHandler.handleException(e, false); }
        }
        else if ( classContainer.isDirectory() ) { for ( File actualFile : classContainer.listFiles() ) { if ( actualFile.isFile() ) { finalResourceList.add(actualFile.getName()); } } }
        else { System_ErrorHandler.handleError("WhatTheFuck Error !\n\nJava Class Container is not Directory & is not File !\n\nPath : \"" + originalLauncherJarPath + "\"", false, true); }

        return finalResourceList.toArray(new String[finalResourceList.size()]);
    }

    public static String[] getAllClassNames()
    {
        ArrayList<String> classFiles = new ArrayList<String>();
        for ( String actualResourceFile : getAllResourceFiles() ) { if ( actualResourceFile.toLowerCase().endsWith(".class") && !actualResourceFile.toLowerCase().contains("$") && !actualResourceFile.toLowerCase().contains("/") ) { classFiles.add(actualResourceFile.replace(".class", "")); } }
        return classFiles.toArray(new String[classFiles.size()]);
    }

    public static Class[] getAllClasses()
    {
        ArrayList<Class> classes = new ArrayList<Class>();
        for ( String actualClassName : getAllClassNames() )
        {
            try { classes.add(Class.forName(actualClassName)); }
            catch ( ClassNotFoundException e ) { e.printStackTrace(); }
        }
        return classes.toArray(new Class[classes.size()]);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Nicnl - nicnl25@gmail.com
}
