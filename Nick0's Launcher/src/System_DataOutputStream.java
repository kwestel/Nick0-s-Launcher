import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class System_DataOutputStream extends DataOutputStream
{

    public System_DataOutputStream(Socket socket) throws IOException { super(socket.getOutputStream()); }

    public void writeString(String stringToWrite) throws IOException
    {
        byte[] byteToWrite = stringToWrite.trim().getBytes();
        writeLong(byteToWrite.length);
        for ( byte actualByte : byteToWrite ) { writeByte(actualByte); }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Nicnl - nicnl25@gmail.com

}