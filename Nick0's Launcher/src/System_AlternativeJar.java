import java.util.ArrayList;

public class System_AlternativeJar
{
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Jar List

    public static final String[] alternativeJarList = new String[]
    {
        "Minefield", "http://www.minefield.fr/java/minefield.jar", "Minefield.jar", "http://www.minefield.fr/java/version_minefield.txt" ,
        "Caldera", "http://caldera-minecraft.fr/launcher/caldera.jar", "Caldera.jar", "http://caldera-minecraft.fr/launcher/version_caldera.txt",
        "NoyalKub", "http://launcher.noyalkub.com/java/noyalkub.jar", "NoyalKub.jar", "http://launcher.noyalkub.com/java/version.txt",
        "BomberCraft - coldknife2", "http://dl.dropbox.com/u/62495291/minecraft.jar", "BomberCraft_coldknife2.jar", "http://dl.dropbox.com/u/62495291/version.txt"
    };

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Data Handlers
    
    public static String[] getComboBoxList()
    {
        ArrayList<String> jarList = new ArrayList<String>();

        for ( int i=0; i<alternativeJarList.length/4; i++ )
        {
            String actualJar = alternativeJarList[i*4];
            jarList.add(actualJar);
        }
        
        return jarList.toArray(new String[jarList.size()]);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Verifications

    public static boolean isAlternativeMinecraft(String jarFileName)
    {
        jarFileName = jarFileName.toLowerCase().trim();
        for ( int i=0; i<alternativeJarList.length/4; i++ )
        {
            String actualJar = alternativeJarList[i*4+2].toLowerCase().trim();
            if ( actualJar.equals(jarFileName) ) { return true; }
        }

        return false;
    }

    public static int getAltMinID_FromGameName(String gameName)
    {
        for ( int i=0; i<alternativeJarList.length/4; i++ )
        {
            String actualJar = alternativeJarList[i*4].toLowerCase().trim();
            if ( actualJar.equals(gameName.toLowerCase().trim()) ) { return i*4; }
        }

        System_ErrorHandler.handleError("ERREUR INTERNE DU SYSTEME !\nL'ID du Minecraft Alternatif est introuvable dans la base de donnée !", true, true);

        return -1;
    }

    public static int getAltMinID_FromJarFileName(String jarFileName)
    {
        for ( int i=0; i<alternativeJarList.length/4; i++ )
        {
            String actualJar = alternativeJarList[i*4+2].toLowerCase().trim();
            if ( actualJar.equals(jarFileName.toLowerCase().trim()) ) { return i*4; }
        }

        System_ErrorHandler.handleError("ERREUR INTERNE DU SYSTEME !\nL'ID du Minecraft Alternatif est introuvable dans la base de donnée !", true, true);

        return -1;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Alternative Minecraft Downloader/Updater

    public static void downloadAlternativeGame(String givenName, boolean startGame, boolean useJarName)
    {
        int alternativeID = useJarName ? getAltMinID_FromJarFileName(givenName) : getAltMinID_FromGameName(givenName);
        if ( alternativeID == -1 )
        {
            System_ErrorHandler.handleError("ERREUR INTERNE DU SYSTEME !\nL'ID du Minecraft Alternatif est introuvable dans la base de donnée !", true, true);
            return;
        }

        byte[] downloadedVersionFile = Web_ClientServerProtocol.downloadFile(alternativeJarList[alternativeID+3]);
        String MD5Digest = Encrypter_StringEncrypter.MD5Digest(downloadedVersionFile);
        Preferences_ConfigFileWriter.setParameter("AlternativeMinecraftMD5-" + alternativeJarList[alternativeID], MD5Digest);

        System_LogWriter.write("Ajout d'un Minecraft Alternatif dans la base de données : " + alternativeJarList[alternativeID] + " / " + MD5Digest);

        new GuiForm_UpdaterForm(alternativeJarList[alternativeID+1], alternativeJarList[alternativeID+2], startGame);
    }
    
    public static boolean alternativeGameNeedUpdate(String jarFileName)
    {
        int alternativeID = getAltMinID_FromJarFileName(jarFileName);
        if ( alternativeID == -1 )
        {
            System_ErrorHandler.handleError("ERREUR INTERNE DU SYSTEME !\nL'ID du Minecraft Alternatif est introuvable dans la base de donnée !", true, true);
            return false;
        }
        
        byte[] downloadedVersionFile = Web_ClientServerProtocol.downloadFile(alternativeJarList[alternativeID+3]);
        String latestMD5Digest = Encrypter_StringEncrypter.MD5Digest(downloadedVersionFile);
        String savedMD5Digest = Preferences_ConfigFileWriter.getParameter("AlternativeMinecraftMD5-" + alternativeJarList[alternativeID]);
        
        return !latestMD5Digest.toLowerCase().trim().equals(savedMD5Digest.toLowerCase().trim());
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Nicnl - nicnl25@gmail.com
}
