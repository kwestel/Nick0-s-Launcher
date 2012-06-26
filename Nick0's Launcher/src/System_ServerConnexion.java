import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

public class System_ServerConnexion
{

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Fields

    private System_DataInputStream dataInputStream;
    private System_DataOutputStream dataOutputStream;
    private Socket connexionSocket;

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Constructor & Main Functions

    public System_ServerConnexion(String serverIP, int serverPort) throws IOException
    {
        connexionSocket = new Socket();
        connexionSocket.setSoTimeout(4000);
        connexionSocket.setTcpNoDelay(true);
        connexionSocket.setTrafficClass(18);

        // 2] Connecting to the server...
        connexionSocket.connect(new InetSocketAddress(serverIP, serverPort), 4000);

        // 3] Define input & output streams...
        dataInputStream = new System_DataInputStream(connexionSocket);
        dataOutputStream = new System_DataOutputStream(connexionSocket);
    }

    private int nextPacket() throws IOException { return dataInputStream.read(); }
    private void nextPacket(int nextPacket) throws IOException { dataOutputStream.write(nextPacket); }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Packet Functions

    public void sendLauncherRecognition() throws IOException { dataOutputStream.write(100); }

    public String getRevision(String index) throws IOException
    {
        nextPacket(1);
        dataOutputStream.writeString(index);
        int nextPacket = nextPacket();

        if ( nextPacket == 125 ) { return null; }
        else if ( nextPacket != 2 ) { throw new IOException("Protocol Error ! [" + nextPacket + "]"); }

        return dataInputStream.readString();
    }

    public byte[] downloadFile(String fileIndex) throws IOException
    {
        nextPacket(3);
        dataOutputStream.writeString(fileIndex);
        int nextPacket = nextPacket();

        if ( nextPacket == 125 ) { throw new IOException("Index Not In Database !"); }
        else if ( nextPacket != 4 ) { throw new IOException("Protocol Error ! [" + nextPacket + "]"); }

        return dataInputStream.readByteArray();
    }

    public void closeConnexions()
    {
        try { dataInputStream.close(); }
        catch ( IOException e ) { }

        try { dataOutputStream.close(); }
        catch ( IOException e ) { }

        try { connexionSocket.close(); }
        catch ( IOException e ) { }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Nicnl - nicnl25@gmail.com
}
