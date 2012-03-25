import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class GuiForm_GameFrame extends Frame
{

    public GuiForm_GameFrame(String username)
    {
        super("Nick0's Launcher - Minecraft - " + username + " - Revision " + Main_RealLauncher.getLauncherRevision());

        setSize(Preferences_ConfigLoader.CONFIG_WindowSizeX, Preferences_ConfigLoader.CONFIG_WindowSizeY);
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
