import javazoom.jl.player.Player;

import java.io.InputStream;

public class System_ErrorPlayer
{
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Error Message Player

    public static void playErrorMessage()
    {
        if ( !Preferences_ConfigLoader.CONFIG_erreurSonore ) { return; }
        new Thread() { public void run() { System_ErrorPlayer.rawPlayer(); } }.start();
    }
    
    private static void rawPlayer()
    {
        try
        {
            InputStream musicInput = System_ErrorPlayer.class.getResourceAsStream("autre/72.mp3");
            Player musicPlayer = new Player(musicInput);

            musicPlayer.play();
        }
        catch ( Exception e ) { e.printStackTrace(); }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Nicnl - nicnl25@gmail.com
}
