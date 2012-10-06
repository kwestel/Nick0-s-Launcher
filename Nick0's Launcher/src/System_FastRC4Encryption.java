import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;

public class System_FastRC4Encryption
{
    private static Cipher encryptCipher;
    private static Cipher decryptCipher;
    
    private static boolean cipherInitialized = false;

    private static void initializeCiphers()
    {
        SecretKey secretKey = generateSecretKey();
        
        try
        {
            encryptCipher = Cipher.getInstance("DES");
            decryptCipher = Cipher.getInstance("DES");
            encryptCipher.init(Cipher.ENCRYPT_MODE, secretKey);
            decryptCipher.init(Cipher.DECRYPT_MODE, secretKey);
        }
        catch ( javax.crypto.NoSuchPaddingException e ) { e.printStackTrace(); }
        catch ( java.security.NoSuchAlgorithmException e ) { e.printStackTrace(); }
        catch ( java.security.InvalidKeyException e ) { e.printStackTrace(); }
        
        cipherInitialized = true;
    }
    
    private static SecretKey generateSecretKey() { return new SecretKeySpec((System_Digest.generateMD5Digest((System.getProperty("os.name")+System.getProperty("user.home")).getBytes())).substring(0,8).getBytes(), "DES"); }

    public static String encrypt(String str)
    {
        if ( !cipherInitialized ) { initializeCiphers(); }
        
        try
        {
            byte[] utf8 = str.getBytes("UTF8");
            byte[] enc = encryptCipher.doFinal(utf8);
            return new sun.misc.BASE64Encoder().encode(enc);
        }
        catch ( javax.crypto.BadPaddingException e ) { }
        catch ( IllegalBlockSizeException e ) { }
        catch ( UnsupportedEncodingException e ) { }
        catch ( java.io.IOException e ) { }

        return null;
    }

    public static String decrypt(String str)
    {
        if ( !cipherInitialized ) { initializeCiphers(); }
        
        try
        {
            byte[] dec = new sun.misc.BASE64Decoder().decodeBuffer(str);
            byte[] utf8 = decryptCipher.doFinal(dec);
            return new String(utf8, "UTF8");
        }
        catch ( javax.crypto.BadPaddingException e ) { }
        catch ( IllegalBlockSizeException e ) { }
        catch ( UnsupportedEncodingException e ) { }
        catch ( java.io.IOException e ) { }

        return null;
    }

}
