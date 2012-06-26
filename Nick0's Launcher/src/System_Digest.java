import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class System_Digest
{

    public static String generateMD5Digest(byte[] originalByteArray)
    {
        MessageDigest messageDigest;
        try { messageDigest = MessageDigest.getInstance("MD5"); }
        catch ( NoSuchAlgorithmException e ) { return "MD5_DIGEST_ERROR"; }

        byte[] rawDigest = messageDigest.digest(originalByteArray);

        String finalDigest = "";
        for ( byte anAd : rawDigest ) { finalDigest += Integer.toString((anAd & 0xff) + 0x100, 16).substring(1); }

        return finalDigest;
    }

    public static String generateSHA512Digest(byte[] originalByteArray)
    {
        MessageDigest messageDigest;
        try { messageDigest = MessageDigest.getInstance("SHA-512"); }
        catch ( NoSuchAlgorithmException e ) { return "SHA-512_DIGEST_ERROR"; }

        messageDigest.update(originalByteArray);
        byte[] bytesDigest = messageDigest.digest();

        String finalDigest = "";
        for ( byte actualByte : bytesDigest )
        {
            int n;
            if ( (n=0xFF&actualByte) < 16 ) { finalDigest += "0"; }
            finalDigest += Integer.toHexString(n);
        }

        return finalDigest;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Nicnl - nicnl25@gmail.com
}
