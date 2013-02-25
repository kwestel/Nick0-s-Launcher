import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class System_RegexHelper
{

    public static String extractForceUsername(String originalString)
    {
        // Initialize ForceUsername Block Pattern
        Pattern pattern = Pattern.compile("(\\[)(ForceUsername=)((.){1,128})(\\])");
        Matcher matcher = pattern.matcher(originalString);

        // Extract Blocks From Regex Results
        String[] results = extractRegexResults(matcher);

        // Return Magic Result
        return ((results.length == 1) ? results[0] : null);
    }

    public static String extractForceSessionID(String originalString)
    {
        // Initialize ForceUsername Block Pattern
        Pattern pattern = Pattern.compile("(\\[)(SessionID)(\\])");
        Matcher matcher = pattern.matcher(originalString);

        // Extract Blocks From Regex Results
        String[] results = extractRegexResults(matcher);

        // Return Magic Result
        return ((results.length == 1) ? results[0] : null);
    }

    public static String[] extractRegexResults(Matcher regexMatcher)
    {
        // Define Results Container
        ArrayList<String> extractedResults = new ArrayList<String>();

        // Add Results To Container
        while ( regexMatcher.find() ) { extractedResults.add(regexMatcher.group()); }

        // Return Results Container
        return extractedResults.toArray(new String[extractedResults.size()]);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Nicnl - nicnl25@gmail.com

}
