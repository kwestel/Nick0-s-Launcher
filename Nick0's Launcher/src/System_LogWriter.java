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

    private static boolean consoleBypassed = false;
    private static PrintStream consolePrintStream;
    private static PrintStream consoleErrorPrintStream;

    public static void initializeMinecraftLogs()
    {
        if ( consoleBypassed ) { return; }
        consoleBypassed = true;

        try
        {
            consolePrintStream = System.out;
            consoleErrorPrintStream = System.err;

            PrintStream consoleBypass = new System_PrintStream(consolePrintStream, false);
            PrintStream consoleErrorBypass = new System_PrintStream(consoleErrorPrintStream, true);

            System_Reflection.changeFinalStaticField("out", System.class, consoleBypass);
            System_Reflection.changeFinalStaticField("err", System.class, consoleErrorBypass);

            if ( System.out != consoleBypass ) { throw new Exception("Default Console Is Not Set !"); }
            if ( System.err != consoleErrorBypass ) { throw new Exception("Error Console Is Not Set !"); }

            if ( Preferences_ConfigLoader.CONFIG_ShowTrayIcon ) { startExceptionThread(); }
        }
        catch ( Exception e ) { System_ErrorHandler.handleExceptionWithText(e, "Impossible d'activer le système de log pour Minecraft !", false, true); }

        System_StringEncrypter.C();
    }
    
    public static void write(String text)
    {
        firstVerification();
        
        String textToPrint = "Nick0's Launcher - ";
        try { throw new Exception("LOL !"); }
        catch ( Exception e ) { textToPrint += e.getStackTrace()[1].getClassName() + "/" +  e.getStackTrace()[1].getMethodName() + " - "; }
        
        textToPrint += text;

        consolePrintStream.println(textToPrint);

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

    public static void minecraftLog(String text, boolean newLine, boolean errorMode)
    {
        firstVerification();
        if ( Preferences_ConfigLoader.CONFIG_ShowErrorNotifications && Preferences_ConfigLoader.CONFIG_ShowTrayIcon && errorMode ) { addExceptionToList(text); }
        (errorMode ? consoleErrorPrintStream : consolePrintStream).println(text);
        GuiForm_MinecraftConsole.updateTextPane(text);

        if ( LogsEnabled )
        {
            try
            {
                BW.write(text);
                if ( newLine ) { BW.newLine(); }
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

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Notification Exception Handler

    private static long lastUpdateTime = 0L;
    private static String restructuredException = "";

    private static void addExceptionToList(String exceptionLine)
    {
        restructuredException += exceptionLine + "\n";
        lastUpdateTime = System.currentTimeMillis();
    }

    private static void startExceptionThread() { new Thread() { public void run() { while ( Preferences_ConfigLoader.CONFIG_ShowTrayIcon )
    {
        if ( lastUpdateTime != 0L )
        {
            if ( (System.currentTimeMillis() - lastUpdateTime ) > 250 )
            {
                String finalException = "";
                for ( String actualLine : restructuredException.split("\n") ) { finalException += new StringBuffer(new StringBuffer(actualLine).reverse().toString().trim()).reverse().toString().trim() + "\n"; }
                if ( restructuredException.toLowerCase().trim().endsWith("\n") && restructuredException.length() > 1 ) { restructuredException.substring(0, restructuredException.length()-1); }

                SystemTray_MinecraftIcon.displayExceptionMessage(finalException);

                restructuredException = "";
                lastUpdateTime = 0;
            }
        }

        try { Thread.currentThread().sleep(250); }
        catch ( InterruptedException e ) { }
    } } }.start(); }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Nicnl - nicnl25@gmail.com
}
