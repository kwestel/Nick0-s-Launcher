import java.io.File;

public class System_UserHomeDefiner
{
    
    public static String SystemOS = returnSystemOS();
    
    public static String returnConfigDirectory()
    {
        String configPath = System.getProperty("user.home");
        
        if ( SystemOS.equals("windows") )
        {
            String applicationData = System.getenv("APPDATA");
            if ( applicationData != null ) { configPath = applicationData; }
        }
        else if ( SystemOS.equals("macosx") ) { configPath += File.separator + "Library" + File.separator + "Application Support"; }
        
        configPath += File.separator + ".minecraft";
        
        return configPath;
    }
    
    private static String returnSystemOS()
    {
        String OS = System.getProperty("os.name").toLowerCase();
        String OSReturned = null;
        
        if ( OS.contains("win") ) { OSReturned = "windows"; }
        else if ( OS.contains("linux") ) { OSReturned = "linux"; }
        else if ( OS.contains("mac") ) { OSReturned = "macosx"; }
        else if ( OS.contains("solaris") || OS.contains("sunos") ) { OSReturned = "solaris"; }
        else { System_ErrorHandler.handleError("Votre OS n'est pas supporté par ce launcher." +
        "Veuillez vous renseigner afin de savoir si une version compatible avec votre OS existe.\n\n" +
        "Cordialement, Nicnl.\nnicnl25@gmail.com", true, true); }
        
        return OSReturned;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Nicnl - nicnl25@gmail.com
}
