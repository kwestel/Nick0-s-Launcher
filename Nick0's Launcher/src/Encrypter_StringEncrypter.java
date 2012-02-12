import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.io.ByteArrayOutputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.security.Key;
import java.util.Random;

public class Encrypter_StringEncrypter
{
    
    private static String lastEncodedPassword;
    private final static Key secretKey = new SecretKeySpec(getEncodeKey().getBytes(), "RC4");

    public static void encodeAndSavePassword(String pass) { lastEncodedPassword = encodeString(pass); }
    public static String getLastPassword() { return lastEncodedPassword; }
    
    public static String encodeString(String originalString)
    {
        String finishedFile = null;

        try
        {
            Cipher cipherInstance = Cipher.getInstance("RC4");
            cipherInstance.init(Cipher.ENCRYPT_MODE, secretKey);

            byte[] encryptedBytes = cipherInstance.doFinal(originalString.getBytes());

            finishedFile = "";
            for ( byte actualByte : encryptedBytes ) { finishedFile += actualByte + " "; }
            finishedFile = finishedFile.trim();
        }
        catch ( Exception e ) { System_ErrorHandler.handleException(e, false); }

        return finishedFile;
    }

    public static String decodeString(String encodedString)
    {
        byte[] decodedBytes = null;

        try
        {
            Cipher cipher = Cipher.getInstance("RC4");
            cipher.init(Cipher.DECRYPT_MODE, secretKey);

            ByteArrayOutputStream BAOS = new ByteArrayOutputStream();
            for ( String actualChar : encodedString.split(" ") ) { BAOS.write(Integer.parseInt(actualChar)); }
            byte[] encodedArray = BAOS.toByteArray();

            decodedBytes = cipher.doFinal(encodedArray);
        }
        catch ( Exception e ) { System_ErrorHandler.handleException(e, false); }

        return (new String(decodedBytes));
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
    
    public static String stringRandomizer(String originalText)
    {
        String newText = "";
        Random randomizer = new Random();

        for ( String actualChar : originalText.split("") )
        {
            char randomChar = (char)(randomizer.nextInt(26) + 'a');
            newText += randomChar;
        }

        return newText.substring(0,newText.length()-1);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Nicnl - nicnl25@gmail.com
}
