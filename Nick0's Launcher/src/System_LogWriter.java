import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class System_LogWriter
{

    private static boolean FirstVerificationDone = false;

    private static BufferedWriter BW;
    private static final String logsFilePath = Main_RealLauncher.configFileDir + File.separator + "Nick0sLauncher_LOGS.txt";
    private static final File logsFile = new File(logsFilePath);
    
    private static final boolean LogsEnabled = true;
    
    public static void write(String text)
    {
        firstVerification();
        
        String textToPrint = "Nick0's Launcher - ";
        try { throw new Exception("LOL !"); }
        catch ( Exception e ) { textToPrint += e.getStackTrace()[1].getClassName() + "/" +  e.getStackTrace()[1].getMethodName() + " - "; }
        
        textToPrint += text;
        
        System.out.println(textToPrint);
        if ( LogsEnabled )
        {
            try
            {
                BW.write(textToPrint);
                BW.newLine();
                BW.flush();
            }
            catch ( Exception e ) { e.printStackTrace(); }
        }
    }

    private static void firstVerification()
    {
        if ( !FirstVerificationDone && LogsEnabled )
        {
            if ( logsFile.exists() ) { logsFile.delete(); }

            System_FileManager.createFolder(Main_RealLauncher.configFileDir);
            try { logsFile.createNewFile(); }
            catch ( IOException e ) { e.printStackTrace(); }

            try
            {
                BW = new BufferedWriter(new FileWriter(logsFile, true));

                BW.write("[DEMARRAGE DU LAUNCHER : " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime()) + "]");
                BW.newLine();
                BW.flush();
            }
            catch ( Exception e ) { e.printStackTrace(); }
        }

        FirstVerificationDone = true;
    }
}
