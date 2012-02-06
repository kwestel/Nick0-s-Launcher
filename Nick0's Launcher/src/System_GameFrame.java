import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class System_GameFrame extends Frame
{

    public System_GameFrame(String username)
    {
        super("Nick0's Launcher - " + username + " - Minecraft");

        setSize(900, 525);
        setLocationRelativeTo(null);
        
        addWindowListener(new WindowAdapter() { public void windowClosing(WindowEvent arg0)
        {
            Main_RealLauncher.minecraftInstance.stop();
            Main_RealLauncher.minecraftInstance.destroy();
            System.exit(0);
        } });
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Nicnl - nicnl25@gmail.com
}
