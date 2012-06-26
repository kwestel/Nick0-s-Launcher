import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;

public class System_DataInputStream extends DataInputStream
{

    public System_DataInputStream(Socket socket) throws IOException { super(socket.getInputStream()); }

    public String readString() throws IOException
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int stringLength = (int)readLong();
        for ( int i=0; i<stringLength; i++ ) { baos.write(readByte()); }
        return new String(baos.toByteArray()).trim();
    }

    public byte[] readByteArray() throws IOException
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        int byteArrayLength = (int)readLong();
        for ( int i=0; i<byteArrayLength; i++ ) { baos.write(readByte()); }

        byte[] byteArrayReceived = baos.toByteArray();
        String newArrayDigest = System_Digest.generateMD5Digest(byteArrayReceived).toLowerCase().trim();
        String receivedDigest = readString();

        if ( !receivedDigest.equals(newArrayDigest) ) { throw new IOException("Byte Array Receiving Error !\n" + newArrayDigest + " / " + receivedDigest); }

        return byteArrayReceived;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Nicnl - nicnl25@gmail.com

}