import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.io.ByteArrayOutputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;

public class Encrypter_StringEncrypter
{
    
    private static String lastEncodedPassword;
    private final static Key secretKey = new SecretKeySpec(getEncodeKey().getBytes(), "RC4");

    public static void encodeAndSavePassword(String pass) { lastEncodedPassword = encodeString(pass); }
    public static String getLastPassword() { return lastEncodedPassword; }
    
    public static String encodeString(String originalString)
    {
        Cipher cipherInstance = null;
        try { cipherInstance = Cipher.getInstance("RC4"); }
        catch ( NoSuchAlgorithmException e ) { System_ErrorHandler.handleException(e, false); }
        catch ( NoSuchPaddingException e ) { System_ErrorHandler.handleException(e, false); }

        try { cipherInstance.init(Cipher.ENCRYPT_MODE, secretKey); }
        catch ( InvalidKeyException e ) { System_ErrorHandler.handleException(e, false); }

        byte[] encryptedBytes = null;
        try { encryptedBytes = cipherInstance.doFinal(originalString.getBytes()); }
        catch ( IllegalBlockSizeException e ) { System_ErrorHandler.handleException(e, false); }
        catch ( BadPaddingException e ) { System_ErrorHandler.handleException(e, false); }
        
        String finishedFile = "";
        for ( byte actualByte : encryptedBytes ) { finishedFile += actualByte + " "; }

        return finishedFile.trim();
    }

    public static String decodeString(String encodedString)
    {
        Cipher cipher = null;
        try { cipher = Cipher.getInstance("RC4"); }
        catch ( NoSuchAlgorithmException e ) { System_ErrorHandler.handleException(e, false); }
        catch ( NoSuchPaddingException e ) { System_ErrorHandler.handleException(e, false); }

        try { cipher.init(Cipher.DECRYPT_MODE, secretKey); }
        catch ( InvalidKeyException e ) { System_ErrorHandler.handleException(e, false); }

        ByteArrayOutputStream BAOS = new ByteArrayOutputStream();
        for ( String actualChar : encodedString.split(" ") ) { BAOS.write(Integer.parseInt(actualChar)); }
        byte[] encodedArray = BAOS.toByteArray();

        byte[] decodedBytes = null;
        try { decodedBytes = cipher.doFinal(encodedArray); }
        catch (IllegalBlockSizeException e) { System_ErrorHandler.handleException(e, false); }
        catch (BadPaddingException e) { System_ErrorHandler.handleException(e, false); }

        return new String(decodedBytes);
    }
    
    private static String getEncodeKey()
    {
        try
        {
            String a = InetAddress.getLocalHost().getHostName().substring(0,1);
            String b = System.getProperty("user.name").substring(0,1);
            String c = System.getProperty("user.home").substring(0,1);
            String d = System.getProperty("os.name").substring(0,1);
            String e = System.getProperty("os.arch").substring(0,1);
            String f = System.getProperty("os.version").substring(0,1);

            String g = (""+f.hashCode()).substring(0,1);
            String h = (""+e.hashCode()).substring(0,1);
            String i = (""+d.hashCode()).substring(0,1);
            String j = (""+c.hashCode()).substring(0,1);
            String k = (""+b.hashCode()).substring(0,1);
            String l = (""+a.hashCode()).substring(0,1);

            return a + l + b + k + c + j + d + i + e + h + f + g;
        }
        catch ( UnknownHostException e ) { }
        return null;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Nicnl - nicnl25@gmail.com
}
