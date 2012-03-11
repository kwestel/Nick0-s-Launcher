import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

public class System_ModdedClassLoader extends URLClassLoader
{
    private boolean a;
    private final static Key b = new SecretKeySpec("#-Nicnl-#".getBytes(), "RC4");
    private Class c;

    public System_ModdedClassLoader(URL[] d, boolean e)
    {
        super(d);
        a = e;
        if ( a )
        {
            try { f(); }
            catch ( Exception ex )
            {
                a = false;
                System_ErrorHandler.handleExceptionWithText(ex, "Erreur lors du chargement de : Nicnl's Mods V2.\n\nLe mod ne sera pas chargé.", false, true);
            }
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Magic Functions

    private final void f() throws Exception
    {
        c = g();

        Method h = c.getDeclaredMethod("b", String.class);
        h.setAccessible(true);
        h.invoke(null, Main_RealLauncher.getBinDirPath());
        h.setAccessible(false);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Basic Surcharge

    public Class findClass(String i)
    {
        if ( a )
        {
            try { return (Class)j(i); }
            catch ( Exception e ) { return k(i); }
        }
        else { return k(i); }
    }
    
    public Object j(String n) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException
    {
        Method l = c.getDeclaredMethod("k", String.class, Object.class);
        l.setAccessible(true);
        Class m = (Class)l.invoke(null, n, this);
        l.setAccessible(false);
        return m;
    }
    
    public Class k(String o)
    {
        try { return super.findClass(o); }
        catch ( Exception e ) { return null; }
    }

    private Class g() throws Exception
    {
        String[] v = l(Main_RealLauncher.configFileDir);
        byte[] p = n(o(v[1].substring(1, v[1].length())));
        if ( !q(p).equals((new String(o(v[2].substring(1, v[2].length()))))) ) { throw new Exception(); }
        return defineClass(new String(o(v[0].substring(1, v[0].length()))), p, 0, p.length);
    }

    public static String[] l(String r) throws IOException
    {
        FileReader s = new FileReader(Main_RealLauncher.getBinDirPath() + File.separator + "Nicnl's Mods V2.launcher");
        BufferedReader t = new BufferedReader(s);

        ArrayList<String> u = new ArrayList<String>();

        u.add(t.readLine());
        u.add(t.readLine());
        u.add(t.readLine());

        t.close();
        s.close();

        return u.toArray(new String[u.size()]);
    }

    public static byte[] o(String v) throws Exception
    {
        Cipher w = Cipher.getInstance("RC4");
        w.init(Cipher.DECRYPT_MODE, b);

        int x = 0;
        ByteArrayOutputStream y = new ByteArrayOutputStream();
        for ( String z : v.split("_") ) { y.write(Integer.parseInt(z) / (x=x+1)); }

        return w.doFinal(y.toByteArray());
    }
    
    public static String q(byte[] ab) throws NoSuchAlgorithmException
    {
        byte[] ad = MessageDigest.getInstance("MD5").digest(ab);

        String ae = "";
        for ( int i=0; i<ad.length; i++ ) { ae += Integer.toString( ( ad[i] & 0xff ) + 0x100, 16).substring(1); }

        return ae;
    }
    
    public static byte[] n(byte[] af)
    {
        ByteArrayOutputStream ai = new ByteArrayOutputStream();
        for ( String ah : (new String(af)).split("_") ) { ai.write(Integer.parseInt(ah)); }
        return ai.toByteArray();
    }

}
