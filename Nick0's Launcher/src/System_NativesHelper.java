import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class System_NativesHelper
{
    
    public static String[] getNativesVersions(String[] originalWebPage, boolean showAlpha, boolean showBeta, boolean showRC)
    {
        ArrayList<String> outputFile = new ArrayList<String>();
        
        for ( String actualLine : originalWebPage )
        {
            Pattern p = Pattern.compile("<a href=\"LWJGL%20(.*?)/\">");
            Matcher m = p.matcher(actualLine);
            
            if ( m.find() )
            {
                String result = m.group().replace("%20", " ").replace("<d href=\"LWJGL ", "").replace("\">", "").replace("/", "");
                
                if ( result.toLowerCase().contains("rc") && !showRC ) { continue; }
                if ( result.toLowerCase().contains("beta") && !showBeta ) { continue; }
                if ( result.toLowerCase().contains("alpha") && !showAlpha ) { continue; }

                try
                {
                    String temporaryURL = "http://heanet.dl.sourceforge.net/project/java-game-lib/Official%20Releases/LWJGL%20" + result + "/lwjgl-" + result + ".zip";
                    HttpURLConnection URLConnection = (HttpURLConnection)(new URL(temporaryURL).openConnection());
                    URLConnection.setRequestMethod("HEAD");
                    if ( URLConnection.getResponseCode() != HttpURLConnection.HTTP_OK ) { continue; }
                }
                catch ( Exception e ) { continue; }
                
                outputFile.add(result);
            }
        }
        
        return outputFile.toArray(new String[outputFile.size()]);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Nicnl - nicnl25@gmail.com
}
