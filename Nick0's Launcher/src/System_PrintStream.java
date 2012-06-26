import java.io.PrintStream;

public class System_PrintStream extends PrintStream
{

    // Error Mode
    private boolean errorPrintStream = false;

    // Constructor
    public System_PrintStream(PrintStream originalPrintStream, boolean errorPrintStream)
    {
        super(originalPrintStream);
        this.errorPrintStream = errorPrintStream;
    }

    // String
    public void println(String textToPrint) { System_LogWriter.minecraftLog(textToPrint, true, errorPrintStream); }
    public void print(String textToPrint) { System_LogWriter.minecraftLog(textToPrint, false, errorPrintStream); }

    // Object
    public void println(Object object) { System_LogWriter.minecraftLog(String.valueOf(object), true, errorPrintStream); }
    public void print(Object object) { System_LogWriter.minecraftLog(String.valueOf(object), false, errorPrintStream); }

    // Char
    public void println(char c) { System_LogWriter.minecraftLog(String.valueOf(c), true, errorPrintStream); }
    public void print(char c) { System_LogWriter.minecraftLog(String.valueOf(c), false, errorPrintStream); }

    // Double
    public void println(double d) { System_LogWriter.minecraftLog(String.valueOf(d), true, errorPrintStream); }
    public void print(double d) { System_LogWriter.minecraftLog(String.valueOf(d), false, errorPrintStream); }

    // Float
    public void println(float f) { System_LogWriter.minecraftLog(String.valueOf(f), true, errorPrintStream); }
    public void print(float f) { System_LogWriter.minecraftLog(String.valueOf(f), false, errorPrintStream); }

    // Long
    public void println(long l) { System_LogWriter.minecraftLog(String.valueOf(l), true, errorPrintStream); }
    public void print(long l) { System_LogWriter.minecraftLog(String.valueOf(l), false, errorPrintStream); }

    // Int
    public void println(int i) { System_LogWriter.minecraftLog(String.valueOf(i), true, errorPrintStream); }
    public void print(int i) { System_LogWriter.minecraftLog(String.valueOf(i), false, errorPrintStream); }

    // Boolean
    public void println(boolean b) { System_LogWriter.minecraftLog(String.valueOf(b), true, errorPrintStream); }
    public void print(boolean b) { System_LogWriter.minecraftLog(String.valueOf(b), false, errorPrintStream); }

    // Boolean
    public void println() { System_LogWriter.minecraftLog("", true, errorPrintStream); }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Nicnl - nicnl25@gmail.com
}
