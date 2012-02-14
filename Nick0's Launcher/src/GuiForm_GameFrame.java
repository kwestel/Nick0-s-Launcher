import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class GuiForm_GameFrame extends Frame
{

    public GuiForm_GameFrame(String username)
    {
        super("Nick0's Launcher V1 - " + username + " - Minecraft");

        setSize(950, 550);
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
